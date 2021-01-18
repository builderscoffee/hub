package eu.builderscoffee.hub;

import eu.builderscoffee.hub.configuration.HubConfiguration;
import eu.builderscoffee.hub.configuration.MessageConfiguration;
import eu.builderscoffee.hub.listeners.PlayerListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    //Configuration
    @Getter
    private MessageConfiguration messageConfiguration;
    @Getter
    private HubConfiguration hubConfiguration;

    @Getter
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        // Configuration

        //log("Chargement des configurations");
        //messageConfiguration = readOrCreateConfiguration(this, MessageConfiguration.class);
        //hubConfiguration = Configurations.readOrCreateConfiguration(this, HubConfiguration.class);

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
