package eu.builderscoffee.hub.configuration.messages;

import lombok.Data;

@Data
public class RankingConfigurationPart {

    String generalTitle = "&7&nClassement Générale";
    String generalFormat = "&6%player% &8- &f%score%";
    String lastBuildbattleTitle = "&7&nClassement du dernier expresso";
    String lastBuildbattleFormat = "&6%player% &8- &f%score%";
}
