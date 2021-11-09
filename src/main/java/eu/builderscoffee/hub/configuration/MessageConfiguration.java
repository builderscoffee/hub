package eu.builderscoffee.hub.configuration;

import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import eu.builderscoffee.hub.configuration.messages.*;
import lombok.Data;

@Data
@Configuration("messages")
public final class MessageConfiguration {

    TitleConfigurationPart title = new TitleConfigurationPart();
    ItemConfigurationPart items = new ItemConfigurationPart();
    RankingConfigurationPart ranking = new RankingConfigurationPart();
    ScoreboardConfigurationPart scoreboard = new ScoreboardConfigurationPart();
    TablistConfigurationPart tablist = new TablistConfigurationPart();
}
