package ru.dom.votingsystem.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ru.dom.votingsystem.action.ActionContext;
import ru.dom.votingsystem.action.ActionExecutor;
import ru.dom.votingsystem.config.ConfigManager;
import ru.dom.votingsystem.config.MessageManager;
import ru.dom.votingsystem.config.SoundManager;
import ru.dom.votingsystem.model.NoVotesAction;
import ru.dom.votingsystem.model.VotingDefinition;
import ru.dom.votingsystem.model.VotingOption;

public final class VotingManager {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final MessageManager messageManager;
    private final SoundManager soundManager;
    private final ActionExecutor actionExecutor;

    private VotingSession activeSession;
    private BukkitTask endTask;
    private long lastVotingEndMillis = 0L;

    public VotingManager(
            final JavaPlugin plugin,
            final ConfigManager configManager,
            final MessageManager messageManager,
            final SoundManager soundManager,
            final ActionExecutor actionExecutor
    ) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.messageManager = messageManager;
        this.soundManager = soundManager;
        this.actionExecutor = actionExecutor;
    }

    public synchronized boolean startVoting(final String votingId) {
        if (activeSession != null) {
            return false;
        }
        VotingDefinition definition = configManager.getVotings().get(votingId);
        if (definition == null) {
            return false;
        }
        Map<String, Integer> votes = new HashMap<>();
        for (String optionId : definition.options().keySet()) {
            votes.put(optionId, 0);
        }
        activeSession = new VotingSession(definition, votes, new HashMap<>());

        Bukkit.broadcast(messageManager.get("start_of_voting"));
        soundManager.playToAll(SoundManager.Key.START_OF_VOTING);

        endTask = Bukkit.getScheduler().runTaskLater(plugin, this::finishVoting, configManager.getVotingDurationSeconds() * 20L);
        return true;
    }

    public synchronized boolean forceEnd() {
        if (activeSession == null) {
            return false;
        }
        finishVoting();
        return true;
    }

    private synchronized void finishVoting() {
        if (activeSession == null) {
            return;
        }
        VotingSession session = activeSession;
        VotingOption winner = selectWinner(session);
        if (winner != null) {
            actionExecutor.execute(winner.actionsOnWin(), new ActionContext(winner));
        }
        soundManager.playToAll(SoundManager.Key.END_OF_VOTING);
        activeSession = null;
        if (endTask != null) {
            endTask.cancel();
            endTask = null;
        }
        lastVotingEndMillis = System.currentTimeMillis();
    }

    private VotingOption selectWinner(final VotingSession session) {
        if (session.votedByPlayer().isEmpty()) {
            if (soundManager.getNoVotesAction() == NoVotesAction.NOTHING) {
                Bukkit.broadcast(messageManager.get("no_one_vote"));
                soundManager.playToAll(SoundManager.Key.NO_ONE_VOTE);
                return null;
            }
            Bukkit.broadcast(messageManager.get("no_one_vote_random_option"));
            soundManager.playToAll(SoundManager.Key.NO_ONE_VOTE);
            List<VotingOption> options = new ArrayList<>(session.definition().options().values());
            return options.get(ThreadLocalRandom.current().nextInt(options.size()));
        }

        int maxVotes = session.votesByOption().values().stream().max(Integer::compareTo).orElse(0);
        List<VotingOption> winners = session.votesByOption().entrySet().stream()
                .filter(entry -> entry.getValue() == maxVotes)
                .map(entry -> session.definition().options().get(entry.getKey()))
                .toList();

        if (winners.size() > 1) {
            Bukkit.broadcast(messageManager.get("same_number_of_votes"));
            soundManager.playToAll(SoundManager.Key.SAME_NUMBER_OF_VOTES);
        }
        return winners.get(ThreadLocalRandom.current().nextInt(winners.size()));
    }

    public synchronized boolean registerVote(final Player player, final String optionId) {
        if (activeSession == null) {
            return false;
        }
        UUID playerId = player.getUniqueId();
        if (activeSession.votedByPlayer().containsKey(playerId)) {
            return false;
        }
        if (!activeSession.definition().options().containsKey(optionId)) {
            return false;
        }
        activeSession.votedByPlayer().put(playerId, optionId);
        activeSession.votesByOption().merge(optionId, 1, Integer::sum);

        VotingOption option = activeSession.definition().options().get(optionId);
        player.sendMessage(messageManager.get("selection_of_voting_option", Map.of(
                "select_voting_option", option.title()
        )));
        soundManager.playToPlayer(player, SoundManager.Key.SELECTION_OF_VOTING_OPTION);
        return true;
    }

    public synchronized Optional<VotingDefinition> getActiveVoting() {
        return Optional.ofNullable(activeSession).map(VotingSession::definition);
    }

    public synchronized boolean isVotingActive() {
        return activeSession != null;
    }

    public synchronized boolean hasVoted(final UUID playerId) {
        return activeSession != null && activeSession.votedByPlayer().containsKey(playerId);
    }

    public synchronized int getVotes(final String optionId) {
        if (activeSession == null) {
            return 0;
        }
        return activeSession.votesByOption().getOrDefault(optionId, 0);
    }

    public synchronized long secondsUntilNextVoting() {
        if (lastVotingEndMillis == 0L) {
            return 0L;
        }
        long delay = configManager.getDelayBetweenVotingsSeconds() * 1000L;
        long elapsed = System.currentTimeMillis() - lastVotingEndMillis;
        return Math.max(0L, (delay - elapsed) / 1000L);
    }

    public synchronized void shutdown() {
        if (endTask != null) {
            endTask.cancel();
            endTask = null;
        }
        activeSession = null;
    }

    private record VotingSession(
            VotingDefinition definition,
            Map<String, Integer> votesByOption,
            Map<UUID, String> votedByPlayer
    ) {
    }
}
