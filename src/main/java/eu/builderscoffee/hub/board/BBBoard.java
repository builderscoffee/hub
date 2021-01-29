package eu.builderscoffee.hub.board;

import eu.builderscoffee.api.board.FastBoard;
import eu.builderscoffee.commons.utils.LuckPermsUtils;
import eu.builderscoffee.hub.Main;
import eu.builderscoffee.hub.configuration.MessageConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class BBBoard {
    private static final MessageConfiguration messages = Main.getInstance().getMessageConfiguration();
    public static Map<UUID, FastBoard> boards = new HashMap<>();

    public static void updateBoard(FastBoard board) {
        final Player player = board.getPlayer();
        eu.builderscoffee.api.Main.getBungeeChannelApi().getPlayerCount("ALL")
                .whenComplete((result, error) -> {
                    List<String> lines = new ArrayList<>();
                    final String primaryGroup = LuckPermsUtils.getPrimaryGroup(player).substring(0, 1).toUpperCase() + LuckPermsUtils.getPrimaryGroup(player).substring(1);
                    messages.getScoreBoard().forEach(line -> lines.add(line
                            .replace("%player%", player.getName())
                            .replace("%online%", "" + result)
                            .replace("%rank%", primaryGroup)
                            .replace("%prefix%", LuckPermsUtils.getPrefix(player))
                            .replace("%suffix%", LuckPermsUtils.getSuffix(player))
                            .replace("&", "§")
                    ));
                    board.updateLines(lines);
                });
    }
}
