package eu.builderscoffee.hub.configuration;

import eu.builderscoffee.api.configuration.annotation.Configuration;
import lombok.Data;

@Data
@Configuration("messages")
public final class MessageConfiguration {

    String title = "&aBienvenue sur &fBuilders Coffee !";
    String subTitle = "&7Buvez vos idées,&7construisez votre café !";
}
