package eu.builderscoffee.hub.commands;

import eu.builderscoffee.api.utils.LocationsUtil;
import eu.builderscoffee.hub.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location hubLocation = LocationsUtil.getLocationFromString(Main.getInstance().getHubConfiguration().getSpawnLocation());

            player.teleport(hubLocation);

            return true;
        }

        //sender.sendMessage(Main.getInstance().getMessages().getCommandMustBePlayer());
        return true;
    }
}
