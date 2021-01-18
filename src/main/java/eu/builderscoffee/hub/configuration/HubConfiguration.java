package eu.builderscoffee.hub.configuration;

import eu.builderscoffee.api.configuration.annotation.Configuration;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Location;

@Data
@Configuration("hub")
public class HubConfiguration {

    @Getter
    private Location hubLocation = null;
}
