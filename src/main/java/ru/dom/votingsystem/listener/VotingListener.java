package ru.dom.votingsystem.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import ru.dom.votingsystem.manager.GUIManager;
import ru.dom.votingsystem.manager.VotingManager;

public final class VotingListener implements Listener {
    private final GUIManager guiManager;
    private final VotingManager votingManager;

    public VotingListener(final GUIManager guiManager, final VotingManager votingManager) {
        this.guiManager = guiManager;
        this.votingManager = votingManager;
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        if (!(event.getView().getTopInventory().getHolder() instanceof GUIManager.VotingHolder)) {
            return;
        }
        event.setCancelled(true);
        if (event.getClickedInventory() == null || event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }
        if (votingManager.hasVoted(player.getUniqueId())) {
            return;
        }
        guiManager.optionBySlot(event.getView().getTopInventory(), event.getRawSlot()).ifPresent(optionId -> {
            if (votingManager.registerVote(player, optionId)) {
                guiManager.createInventory().ifPresent(player::openInventory);
            }
        });
    }

    @EventHandler
    public void onDrag(final InventoryDragEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof GUIManager.VotingHolder) {
            event.setCancelled(true);
        }
    }
}
