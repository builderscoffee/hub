package eu.builderscoffee.hub.commands;

import eu.builderscoffee.api.bukkit.utils.LocationsUtil;
import eu.builderscoffee.hub.Main;
import lombok.val;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            // Téléportation du joueur
            val hubLocation = LocationsUtil.getLocationFromString(Main.getInstance().getHubConfiguration().getSpawn_location());
            ((Player) sender).teleport(hubLocation);

            return true;
        }

        sender.sendMessage(Main.getInstance().getMessageConfiguration().getCommandMustBePlayer());
        return true;
    }
}
