package eu.builderscoffee.hub.configuration;

import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Data
@Configuration("messages")
public final class MessageConfiguration {

    String title = "&aBienvenue sur &fBuilders Coffee !";
    String subTitle = "&7Buvez vos idées,&7construisez votre café !";
    String compassName = "§6§k|§eNavigation§6§k|";
    String headerMessage = "&4&lBuilders Coffee";
    String footerMessage = "&5play.builderscoffee.eu";

    String scoreBoardTitle = "&6&l- Builders Coffee -";

    // Board
    @Getter
    List<String> scoreBoard = Arrays.asList("&0&8&m----------&8&m------",
            " &aGrade: &6%rank% ",
            " &aConnectés: &6%online%",
            "",
            " &6play.builderscoffee.eu",
            "&0&8&m----------&8&m------");
}
