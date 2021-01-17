package eu.builderscoffee.hub.configuration;

import eu.builderscoffee.api.configuration.annotation.Configuration;
import lombok.Data;

@Data
@Configuration("messages")
public class MessageConfiguration {

    private String test = "test";
}
