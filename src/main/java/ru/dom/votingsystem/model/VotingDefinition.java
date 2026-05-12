package ru.dom.votingsystem.model;

import java.util.Map;

public record VotingDefinition(
        String id,
        String title,
        Map<String, VotingOption> options
) {
}
