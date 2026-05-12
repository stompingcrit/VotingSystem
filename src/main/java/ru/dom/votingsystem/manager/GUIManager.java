package ru.dom.votingsystem.manager;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.dom.votingsystem.config.ConfigManager;
import ru.dom.votingsystem.model.VotingDefinition;
import ru.dom.votingsystem.model.VotingOption;
import ru.dom.votingsystem.util.ColorUtil;

public final class GUIManager {
    private final ConfigManager configManager;
    private final VotingManager votingManager;

    public GUIManager(final ConfigManager configManager, final VotingManager votingManager) {
        this.configManager = configManager;
        this.votingManager = votingManager;
    }

    public Optional<Inventory> createInventory() {
        Optional<VotingDefinition> activeVoting = votingManager.getActiveVoting();
        if (activeVoting.isEmpty()) {
            return Optional.empty();
        }
        VotingDefinition voting = activeVoting.get();
        Inventory inventory = Bukkit.createInventory(new VotingHolder(voting.id()), configManager.getGuiSize(), ColorUtil.colorize(voting.title()));

        for (VotingOption option : voting.options().values()) {
            ItemStack stack = new ItemStack(option.material() == null ? Material.BARRIER : option.material());
            ItemMeta meta = stack.getItemMeta();
            meta.displayName(ColorUtil.colorize(option.title()));
            int votes = votingManager.getVotes(option.id());
            List<Component> lore = option.lore().stream()
                    .map(line -> line.replace("{votes}", String.valueOf(votes)))
                    .map(ColorUtil::colorize)
                    .toList();
            meta.lore(lore);
            stack.setItemMeta(meta);
            inventory.setItem(option.slot(), stack);
        }
        return Optional.of(inventory);
    }

    public Optional<String> optionBySlot(final Inventory inventory, final int rawSlot) {
        if (!(inventory.getHolder() instanceof VotingHolder holder)) {
            return Optional.empty();
        }
        VotingDefinition definition = configManager.getVotings().get(holder.votingId());
        if (definition == null) {
            return Optional.empty();
        }
        for (Map.Entry<String, VotingOption> entry : definition.options().entrySet()) {
            if (entry.getValue().slot() == rawSlot) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }

    public record VotingHolder(String votingId) implements InventoryHolder {
        @Override
        public Inventory getInventory() {
            return Bukkit.createInventory(this, 9);
        }
    }
}
