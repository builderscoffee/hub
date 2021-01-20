package eu.builderscoffee.hub.configuration;

import eu.builderscoffee.api.configuration.annotation.Configuration;
import lombok.Data;

@Data
@Configuration("hub")
public final class HubConfiguration {

    String spawnLocation = "hub:0:100:0:90.0:0.0";
}
