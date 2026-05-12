package ru.dom.votingsystem.config;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.dom.votingsystem.model.VotingDefinition;
import ru.dom.votingsystem.model.VotingOption;

public final class ConfigManager {
    private final JavaPlugin plugin;
    private int votingDurationSeconds = 60;
    private int delayBetweenVotingsSeconds = 300;
    private int guiSize = 27;
    private Map<String, VotingDefinition> votings = Map.of();

    public ConfigManager(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        votingDurationSeconds = Math.max(1, config.getInt("settings.voting-duration-seconds", 60));
        delayBetweenVotingsSeconds = Math.max(0, config.getInt("settings.delay-between-votings-seconds", 300));
        guiSize = normalizeGuiSize(config.getInt("gui.size", 27));
        votings = Collections.unmodifiableMap(loadVotings(config.getConfigurationSection("votings")));
    }

    private int normalizeGuiSize(final int size) {
        if (size < 9 || size > 54 || size % 9 != 0) {
            plugin.getLogger().warning("Invalid gui.size, fallback to 27");
            return 27;
        }
        return size;
    }

    private Map<String, VotingDefinition> loadVotings(final ConfigurationSection section) {
        if (section == null) {
            plugin.getLogger().warning("votings section is missing");
            return Map.of();
        }
        Map<String, VotingDefinition> loaded = new LinkedHashMap<>();
        for (String votingId : section.getKeys(false)) {
            ConfigurationSection votingSection = section.getConfigurationSection(votingId);
            if (votingSection == null) {
                continue;
            }
            String title = votingSection.getString("title", "&aVoting");
            ConfigurationSection itemsSection = votingSection.getConfigurationSection("items");
            if (itemsSection == null) {
                plugin.getLogger().warning("Voting '" + votingId + "' has no items");
                continue;
            }

            Map<String, VotingOption> options = new LinkedHashMap<>();
            for (String itemId : itemsSection.getKeys(false)) {
                ConfigurationSection item = itemsSection.getConfigurationSection(itemId);
                if (item == null) {
                    continue;
                }
                int slot = item.getInt("slot", -1);
                if (slot < 0 || slot >= guiSize) {
                    plugin.getLogger().warning("Voting item '" + itemId + "' has invalid slot");
                    continue;
                }
                String materialName = item.getString("material", "BARRIER");
                Material material = Material.matchMaterial(materialName);
                if (material == null || !material.isItem()) {
                    plugin.getLogger().warning("Invalid material '" + materialName + "' in " + votingId + ":" + itemId + ", fallback to BARRIER");
                    material = Material.BARRIER;
                }
                options.put(itemId, new VotingOption(
                        itemId,
                        item.getString("title", "&aOption"),
                        slot,
                        material,
                        List.copyOf(item.getStringList("lore")),
                        List.copyOf(item.getStringList("actions_on_win"))
                ));
            }
            if (!options.isEmpty()) {
                loaded.put(votingId, new VotingDefinition(votingId, title, Collections.unmodifiableMap(options)));
            }
        }
        return loaded;
    }

    public int getVotingDurationSeconds() {
        return votingDurationSeconds;
    }

    public int getDelayBetweenVotingsSeconds() {
        return delayBetweenVotingsSeconds;
    }

    public int getGuiSize() {
        return guiSize;
    }

    public Map<String, VotingDefinition> getVotings() {
        return votings;
    }
}
