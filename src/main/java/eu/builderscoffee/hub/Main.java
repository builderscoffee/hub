package eu.builderscoffee.hub;

import eu.builderscoffee.hub.configuration.MessageConfiguration;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import static eu.builderscoffee.api.configuration.Configurations.readOrCreateConfiguration;

public class Main extends JavaPlugin {

    //Configuration
    @Getter
    private MessageConfiguration messageConfiguration;

    @Getter
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        //log("Chargement des configurations");
        //messageConfiguration = readOrCreateConfiguration(this, MessageConfiguration.class);
    }

    @Override
    public void onDisable() {

    }
}
