package eu.builderscoffee.hub.configuration;

import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import lombok.Data;

@Data
@Configuration("permissions")
public class PermissionsConfiguration {

    String modifyHub = "builderscoffee.hub.modify";
}
