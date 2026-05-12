package ru.dom.votingsystem.model;

import java.util.List;
import org.bukkit.Material;

public record VotingOption(
        String id,
        String title,
        int slot,
        Material material,
        List<String> lore,
        List<String> actionsOnWin
) {
}
