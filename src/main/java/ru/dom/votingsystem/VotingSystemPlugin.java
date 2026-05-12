package ru.dom.votingsystem;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import ru.dom.votingsystem.action.ActionExecutor;
import ru.dom.votingsystem.command.CommandHandler;
import ru.dom.votingsystem.config.ConfigManager;
import ru.dom.votingsystem.config.MessageManager;
import ru.dom.votingsystem.config.SoundManager;
import ru.dom.votingsystem.listener.VotingListener;
import ru.dom.votingsystem.manager.GUIManager;
import ru.dom.votingsystem.manager.VotingManager;

public final class VotingSystemPlugin extends JavaPlugin {
    private ConfigManager configManager;
    private MessageManager messageManager;
    private SoundManager soundManager;
    private VotingManager votingManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        messageManager = new MessageManager(this);
        soundManager = new SoundManager(this);
        reloadPluginConfigs();

        ActionExecutor actionExecutor = new ActionExecutor(this);
        votingManager = new VotingManager(this, configManager, messageManager, soundManager, actionExecutor);
        GUIManager guiManager = new GUIManager(configManager, votingManager);

        CommandHandler commandHandler = new CommandHandler(
                configManager,
                messageManager,
                votingManager,
                guiManager,
                this::reloadPluginConfigs
        );

        PluginCommand vote = getCommand("vote");
        if (vote == null) {
            throw new IllegalStateException("Command /vote is not present in plugin.yml");
        }
        vote.setExecutor(commandHandler);
        vote.setTabCompleter(commandHandler);

        getServer().getPluginManager().registerEvents(new VotingListener(guiManager, votingManager), this);
        getLogger().info("VotingSystem enabled");
    }

    @Override
    public void onDisable() {
        if (votingManager != null) {
            votingManager.shutdown();
        }
    }

    private void reloadPluginConfigs() {
        configManager.reload();
        messageManager.reload();
        soundManager.reload();
    }
}
