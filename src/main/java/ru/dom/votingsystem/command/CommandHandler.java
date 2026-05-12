package ru.dom.votingsystem.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dom.votingsystem.config.ConfigManager;
import ru.dom.votingsystem.config.MessageManager;
import ru.dom.votingsystem.manager.GUIManager;
import ru.dom.votingsystem.manager.VotingManager;

public final class CommandHandler implements CommandExecutor, TabCompleter {
    private final ConfigManager configManager;
    private final MessageManager messageManager;
    private final VotingManager votingManager;
    private final GUIManager guiManager;
    private final Runnable reloadAction;

    public CommandHandler(
            final ConfigManager configManager,
            final MessageManager messageManager,
            final VotingManager votingManager,
            final GUIManager guiManager,
            final Runnable reloadAction
    ) {
        this.configManager = configManager;
        this.messageManager = messageManager;
        this.votingManager = votingManager;
        this.guiManager = guiManager;
        this.reloadAction = reloadAction;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return openVotingMenu(sender);
        }
        return switch (args[0].toLowerCase()) {
            case "forcestart" -> forceStart(sender, args);
            case "forceend" -> forceEnd(sender);
            case "reload" -> reload(sender);
            default -> openVotingMenu(sender);
        };
    }

    private boolean openVotingMenu(final CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by player.");
            return true;
        }
        if (!player.hasPermission("votingsystem.use")) {
            player.sendMessage(messageManager.get("no_permission"));
            return true;
        }
        if (!votingManager.isVotingActive()) {
            long seconds = votingManager.secondsUntilNextVoting();
            player.sendMessage(messageManager.get("time_until_voting", Map.of(
                    "min", String.valueOf(seconds / 60),
                    "sec", String.valueOf(seconds % 60)
            )));
            return true;
        }
        guiManager.createInventory().ifPresent(player::openInventory);
        return true;
    }

    private boolean forceStart(final CommandSender sender, final String[] args) {
        if (!sender.hasPermission("votingsystem.admin.forcestart")) {
            sender.sendMessage(messageManager.get("no_permission"));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage("/vote forcestart <название>");
            return true;
        }
        if (votingManager.isVotingActive()) {
            sender.sendMessage(messageManager.get("voting_active"));
            return true;
        }
        if (!configManager.getVotings().containsKey(args[1])) {
            sender.sendMessage(messageManager.get("voting_not_found"));
            return true;
        }
        votingManager.startVoting(args[1]);
        sender.sendMessage(messageManager.get("forcestart_of_voting.successfully"));
        return true;
    }

    private boolean forceEnd(final CommandSender sender) {
        if (!sender.hasPermission("votingsystem.admin.forceend")) {
            sender.sendMessage(messageManager.get("no_permission"));
            return true;
        }
        if (!votingManager.forceEnd()) {
            sender.sendMessage(messageManager.get("no_active_voting"));
        }
        return true;
    }

    private boolean reload(final CommandSender sender) {
        if (!sender.hasPermission("votingsystem.admin.reload")) {
            sender.sendMessage(messageManager.get("no_permission"));
            return true;
        }
        reloadAction.run();
        sender.sendMessage(messageManager.get("reload"));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("forcestart", "forceend", "reload");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("forcestart")) {
            return new ArrayList<>(configManager.getVotings().keySet());
        }
        return List.of();
    }
}
