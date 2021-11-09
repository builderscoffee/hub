package eu.builderscoffee.hub;

import eu.builderscoffee.api.common.data.tables.Profil;
import eu.builderscoffee.hub.board.BBBoard;
import eu.builderscoffee.hub.configuration.HubConfiguration;
import eu.builderscoffee.hub.configuration.MessageConfiguration;
import eu.builderscoffee.hub.configuration.PermissionsConfiguration;
import eu.builderscoffee.hub.listeners.PlayerListener;
import eu.builderscoffee.hub.tasks.RankingTask;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.logging.Level;

import static eu.builderscoffee.api.common.configuration.Configuration.readOrCreateConfiguration;

@Getter
public class Main extends JavaPlugin {

    @Getter
    private static Main instance;
    //Configuration
    private Map<Profil.Languages, MessageConfiguration> messages;
    private HubConfiguration hubConfig;
    private PermissionsConfiguration permissionsConfig;

    @Override
    public void onEnable() {
        instance = this;

        // Configuration
        instance.getLogger().log(Level.INFO, "Chargement des configurations");

        messages = readOrCreateConfiguration(this.getName(), MessageConfiguration.class, Profil.Languages.class);
        hubConfig = readOrCreateConfiguration(this.getName(), HubConfiguration.class);
        permissionsConfig = readOrCreateConfiguration(this.getName(), PermissionsConfiguration.class);

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
