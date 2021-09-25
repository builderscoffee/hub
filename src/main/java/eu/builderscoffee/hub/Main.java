package eu.builderscoffee.hub;

import eu.builderscoffee.hub.board.BBBoard;
import eu.builderscoffee.hub.configuration.HubConfiguration;
import eu.builderscoffee.hub.configuration.MessageConfiguration;
import eu.builderscoffee.hub.listeners.PlayerListener;
import eu.builderscoffee.hub.tasks.RankingTask;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

import static eu.builderscoffee.api.bukkit.configuration.Configurations.readOrCreateConfiguration;

@Getter
public class Main extends JavaPlugin {

    @Getter
    private static Main instance;
    //Configuration
    private MessageConfiguration messageConfiguration;
    private HubConfiguration hubConfiguration;

    @Override
    public void onEnable() {
        instance = this;

        // Configuration
        instance.getLogger().log(Level.INFO, "Chargement des configurations");
        messageConfiguration = readOrCreateConfiguration(this, MessageConfiguration.class);
        hubConfiguration = readOrCreateConfiguration(this, HubConfiguration.class);

        // Enregistrement des listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        // Update scoreboard
        this.getServer().getScheduler().runTaskTimer(this, () -> BBBoard.boards.values().forEach(BBBoard::updateBoard), 0, 20);

        // Destruction des armorstands de classement
        RankingTask.destroyArmorstands();
        RankingTask.getRankingTask().runTaskTimer(this, 0L, 20 * 60 * 5L);
    }

    @Override
    public void onDisable() {
        // Nothing to do
    }
}
