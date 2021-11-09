package eu.builderscoffee.hub.utils;

import eu.builderscoffee.api.common.data.tables.Profil;
import eu.builderscoffee.hub.Main;
import eu.builderscoffee.hub.configuration.MessageConfiguration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MessageUtils {

    public static MessageConfiguration getMessageConfig(Player player) {
        return Main.getInstance().getMessages().get(eu.builderscoffee.commons.bukkit.utils.MessageUtils.getLang(player));
    }

    public static MessageConfiguration getMessageConfig(UUID uuid) {
        return Main.getInstance().getMessages().get(eu.builderscoffee.commons.bukkit.utils.MessageUtils.getLang(uuid));
    }

    public static MessageConfiguration getMessageConfig(CommandSender sender) {
        if (sender instanceof Player) {
            return getMessageConfig((Player) sender);
        }
        else{
            return getDefaultMessageConfig();
        }
    }

    public static MessageConfiguration getDefaultMessageConfig() {
        return Main.getInstance().getMessages().get(Profil.Languages.FR);
    }
}
