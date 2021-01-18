package eu.builderscoffee.hub.configuration;

import eu.builderscoffee.api.configuration.annotation.Configuration;
import eu.builderscoffee.api.utils.LocationsUtil;
import eu.builderscoffee.hub.Main;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@Data
@Configuration("hub")
public final class HubConfiguration {

    String spawnLocation = "world:0:65:0:0.0:0.0";
}
