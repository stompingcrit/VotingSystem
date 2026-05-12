package ru.dom.votingsystem.action;

import org.bukkit.Bukkit;
import ru.dom.votingsystem.util.ColorUtil;

public final class MessageActionHandler implements ActionHandler {
    @Override
    public void execute(final String payload, final ActionContext context) {
        if (!payload.isBlank()) {
            Bukkit.getServer().broadcast(ColorUtil.colorize(payload));
        }
    }
}
