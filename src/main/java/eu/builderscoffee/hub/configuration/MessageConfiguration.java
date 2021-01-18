package eu.builderscoffee.hub.configuration;

import eu.builderscoffee.api.configuration.annotation.Configuration;
import lombok.Data;

@Data
@Configuration("messages")
public final class MessageConfiguration {

    String test = "test";
}
