package ru.dom.votingsystem.config;

import java.io.File;
import java.util.Map;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.dom.votingsystem.util.ColorUtil;

public final class MessageManager {
    private final JavaPlugin plugin;
    private YamlConfiguration config;

    public MessageManager(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public Component get(final String path) {
        return get(path, Map.of());
    }

    public Component get(final String path, final Map<String, String> placeholders) {
        String value = config.getString("messages." + path, "&cMissing message: " + path);
        for (var entry : placeholders.entrySet()) {
            value = value.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return ColorUtil.colorize(value);
    }
}
