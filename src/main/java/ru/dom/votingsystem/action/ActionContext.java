package ru.dom.votingsystem.action;

import ru.dom.votingsystem.model.VotingOption;

public record ActionContext(VotingOption winnerOption) {
}
