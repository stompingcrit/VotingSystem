package ru.dom.votingsystem.action;

public interface ActionHandler {
    void execute(String payload, ActionContext context);
}
