package ru.dom.votingsystem.config;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.dom.votingsystem.model.NoVotesAction;

public final class SoundManager {
    public enum Key {
        START_OF_VOTING,
        SELECTION_OF_VOTING_OPTION,
        NO_ONE_VOTE,
        SAME_NUMBER_OF_VOTES,
        END_OF_VOTING
    }

    private final JavaPlugin plugin;
    private final Map<Key, Sound> sounds = new EnumMap<>(Key.class);
    private NoVotesAction noVotesAction = NoVotesAction.NOTHING;

    public SoundManager(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        File file = new File(plugin.getDataFolder(), "sounds.yml");
        if (!file.exists()) {
            plugin.saveResource("sounds.yml", false);
        }

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        sounds.clear();

        map(cfg, "sounds.start_of_voting", Key.START_OF_VOTING, Sound.ENTITY_VILLAGER_YES);
        map(cfg, "sounds.selection_of_voting_option", Key.SELECTION_OF_VOTING_OPTION, Sound.BLOCK_NOTE_BLOCK_PLING);
        map(cfg, "sounds.no_one_vote", Key.NO_ONE_VOTE, Sound.ENTITY_VILLAGER_NO);
        map(cfg, "sounds.same_number_of_votes", Key.SAME_NUMBER_OF_VOTES, Sound.ENTITY_VILLAGER_NO);
        map(cfg, "sounds.end_of_voting", Key.END_OF_VOTING, Sound.ENTITY_WITHER_DEATH);

        noVotesAction = NoVotesAction.fromString(cfg.getString("sounds.no_votes_action", "NOTHING"));
    }

    private void map(final YamlConfiguration cfg, final String path, final Key key, final Sound fallback) {
        String raw = cfg.getString(path, fallback.name());
        try {
            sounds.put(key, Sound.valueOf(raw.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            plugin.getLogger().warning("Invalid sound '" + raw + "' at " + path + ", fallback: " + fallback);
            sounds.put(key, fallback);
        }
    }

    public NoVotesAction getNoVotesAction() {
        return noVotesAction;
    }

    public void playToAll(final Key key) {
        Sound sound = sounds.get(key);
        if (sound == null) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, 1f, 1f);
        }
    }

    public void playToPlayer(final Player player, final Key key) {
        Sound sound = sounds.get(key);
        if (sound != null) {
            player.playSound(player.getLocation(), sound, 1f, 1f);
        }
    }
}
