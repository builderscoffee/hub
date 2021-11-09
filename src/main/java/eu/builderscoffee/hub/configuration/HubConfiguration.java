package eu.builderscoffee.hub.configuration;

import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import lombok.Data;

@Data
@Configuration("hub")
public class HubConfiguration {

    String spawnLocation = "world:0.5:100:0.5:90.0:0.0";
    String generalRankingLocation = "world:-11.5:100:8.5:0:0";
    String lastBuildbattleRankingLocation = "world:3.5:102:0.5:0:0";
}
