package eu.builderscoffee.hub.configuration.messages;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class ScoreboardConfigurationPart {

    String title = "&6&l- Builders Coffee -";
    List<String> items = Arrays.asList("&0&8&m----------&8&m------",
            " &aGrade: &6%rank% ",
            " &aConnectés: &6%online%",
            "",
            " &6play.builderscoffee.eu",
            "&0&8&m----------&8&m------");
}
