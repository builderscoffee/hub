package eu.builderscoffee.hub.board;

import eu.builderscoffee.api.bukkit.board.FastBoard;
import eu.builderscoffee.commons.common.utils.LuckPermsUtils;
import eu.builderscoffee.hub.Main;
import eu.builderscoffee.hub.configuration.MessageConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class BBBoard {
    private static final MessageConfiguration messages = Main.getInstance().getMessageConfiguration();
    public static Map<UUID, FastBoard> boards = new HashMap<>();

    private static int amountPlayers = 0;

    /***
     * Mettre à jours le scoreboard tout les ticks
     * @param board
     */
    public static void updateBoard(FastBoard board) {
        final Player player = board.getPlayer();
        eu.builderscoffee.api.bukkit.Main.getBungeeChannelApi().getPlayerCount("ALL")
                .whenComplete((result, error) -> {
                    amountPlayers = result;
                });
        List<String> lines = new ArrayList<>();
        final String primaryGroup = LuckPermsUtils.getPrimaryGroup(player.getUniqueId()).substring(0, 1).toUpperCase() + LuckPermsUtils.getPrimaryGroup(player.getUniqueId()).substring(1);
        messages.getScoreBoard().forEach(line -> lines.add(line
                .replace("%player%", player.getName())
                .replace("%online%", "" + amountPlayers)
                .replace("%rank%", primaryGroup)
                .replace("%prefix%", LuckPermsUtils.getPrefix(player.getUniqueId()))
                .replace("%suffix%", LuckPermsUtils.getSuffix(player.getUniqueId()))
                .replace("&", "§")
        ));
        board.updateLines(lines);
    }
}
