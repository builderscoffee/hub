package eu.builderscoffee.hub.configuration;

import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import lombok.Data;

@Data
@Configuration("hub")
public final class HubConfiguration {

    String spawn_location = "world:0.5:100:0.5:90.0:0.0";
    String general_ranking_location = "world:-11.5:100:8.5:0:0";
    String last_buildbattle_ranking_location = "world:3.5:102:0.5:0:0";
}
