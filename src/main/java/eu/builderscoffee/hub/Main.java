package eu.builderscoffee.hub;

import eu.builderscoffee.api.board.FastBoard;
import eu.builderscoffee.hub.board.BBBoard;
import eu.builderscoffee.hub.configuration.HubConfiguration;
import eu.builderscoffee.hub.configuration.MessageConfiguration;
import eu.builderscoffee.hub.listeners.PlayerListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

import static eu.builderscoffee.api.configuration.Configurations.readOrCreateConfiguration;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;
    //Configuration
    @Getter
    private MessageConfiguration messageConfiguration;
    @Getter
    private HubConfiguration hubConfiguration;

    @Override
    public void onEnable() {
        instance = this;

        // Configuration
        instance.getLogger().log(Level.INFO, "Chargement des configurations");
        messageConfiguration = readOrCreateConfiguration(this, MessageConfiguration.class);
        hubConfiguration = readOrCreateConfiguration(this, HubConfiguration.class);

        // Register Events
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        // Update scoreboard
        this.getServer().getScheduler().runTaskTimer(this, () -> {
            for (FastBoard board : BBBoard.boards.values()) {
                BBBoard.updateBoard(board);
            }
        }, 0, 20);
    }

    @Override
    public void onDisable() {
        // Nothing to do here
    }
}
