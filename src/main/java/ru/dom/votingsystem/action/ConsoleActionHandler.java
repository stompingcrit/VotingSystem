package ru.dom.votingsystem.action;

import org.bukkit.Bukkit;

public final class ConsoleActionHandler implements ActionHandler {
    @Override
    public void execute(final String payload, final ActionContext context) {
        if (!payload.isBlank()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), payload);
        }
    }
}
