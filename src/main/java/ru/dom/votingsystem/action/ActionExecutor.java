package ru.dom.votingsystem.action;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.bukkit.plugin.java.JavaPlugin;

public final class ActionExecutor {
    private final JavaPlugin plugin;
    private final Map<ActionType, ActionHandler> handlers = new EnumMap<>(ActionType.class);

    public ActionExecutor(final JavaPlugin plugin) {
        this.plugin = plugin;
        register(ActionType.CONSOLE, new ConsoleActionHandler());
        register(ActionType.MESSAGE, new MessageActionHandler());
    }

    public void register(final ActionType type, final ActionHandler handler) {
        handlers.put(type, handler);
    }

    public void execute(final List<String> actions, final ActionContext context) {
        for (String line : actions) {
            parseAndExecute(line, context);
        }
    }

    private void parseAndExecute(final String line, final ActionContext context) {
        if (line == null || line.isBlank()) {
            return;
        }
        int closeBracket = line.indexOf(']');
        if (!line.startsWith("[") || closeBracket < 0) {
            plugin.getLogger().warning("Invalid action: " + line);
            return;
        }
        String token = line.substring(1, closeBracket).trim();
        String payload = line.substring(closeBracket + 1).trim();
        ActionType type = ActionType.fromToken(token);
        if (type == null) {
            plugin.getLogger().warning("Unknown action type: " + token);
            return;
        }
        ActionHandler handler = handlers.get(type);
        if (handler == null) {
            plugin.getLogger().warning("No action handler for type: " + type);
            return;
        }
        try {
            handler.execute(payload, context);
        } catch (Exception exception) {
            plugin.getLogger().warning("Action failed '" + line + "': " + exception.getMessage());
        }
    }
}
