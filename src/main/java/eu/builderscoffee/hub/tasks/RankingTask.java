package eu.builderscoffee.hub.tasks;

import eu.builderscoffee.api.bukkit.utils.LocationsUtil;
import eu.builderscoffee.commons.bukkit.Main;
import eu.builderscoffee.commons.common.data.Buildbattle;
import eu.builderscoffee.commons.common.data.BuildbattleEntity;
import eu.builderscoffee.commons.common.data.NoteEntity;
import eu.builderscoffee.commons.common.data.ProfilEntity;
import eu.builderscoffee.hub.configuration.HubConfiguration;
import eu.builderscoffee.hub.configuration.MessageConfiguration;
import io.requery.query.Tuple;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class RankingTask extends BukkitRunnable {

    private static final Map<Integer, ArmorStand> GENERAL_RANKING_AMORSTANDS = new HashMap<>();
    private static final Map<Integer, ArmorStand> LAST_RANKING_AMORSTANDS = new HashMap<>();

    private static final HubConfiguration HUB_CONFIGURATION = eu.builderscoffee.hub.Main.getInstance().getHubConfiguration();
    private static final MessageConfiguration MESSAGE_CONFIGURATION = eu.builderscoffee.hub.Main.getInstance().getMessageConfiguration();

    private static RankingTask rankingTask;

    public static RankingTask getRankingTask() {
        if (rankingTask == null)
            rankingTask = new RankingTask();
        return rankingTask;
    }

    @SneakyThrows
    @Override
    public void run() {
        val store = Main.getInstance().getNotesStore();
        /*val result = store.select(ProfilEntity.NAME.as("name"),
                    NoteEntity.AMENAGEMENT.as("amenagement"),
                    NoteEntity.BEAUTE.as("beaute"),
                    NoteEntity.CREATIVITE.as("creativite"),
                    NoteEntity.FOLKLORE.as("folklore"),
                    NoteEntity.FUN.as("fun"))
                .from(NoteEntity.class)
                .leftJoin(ProfilEntity.class).on(NoteEntity.PROFIL_ID.eq(ProfilEntity.ID))
                .groupBy(ProfilEntity.NAME)
                .get()
                .toList();*/
        try (val result = store.raw("SELECT p.name, sum(n." + NoteEntity.AMENAGEMENT.getName() + ") + sum(n." + NoteEntity.BEAUTE.getName() + ") + sum(n." + NoteEntity.CREATIVITE.getName() + ") + sum(n." + NoteEntity.FOLKLORE.getName() + ") + sum(n." + NoteEntity.FUN.getName() + ") as 'score' " +
                "FROM " + NoteEntity.$TYPE.getName() + " n " +
                "JOIN " + ProfilEntity.$TYPE.getName() + " p " +
                "ON (n." + NoteEntity.PROFIL_ID.getName() + " = p." + ProfilEntity.ID.getName() + ") " +
                "GROUP BY p." + ProfilEntity.NAME.getName() + " " +
                "ORDER BY score desc " +
                "LIMIT 10")) {
            val resultList = result.toList();
            val generalRankingLoc = LocationsUtil.getLocationFromString(HUB_CONFIGURATION.getGeneral_ranking_location());

            for (int i = -1; i < resultList.size(); i++) {
                val tempLoc = new Location(generalRankingLoc.getWorld(), generalRankingLoc.getX(), generalRankingLoc.getY() + 1 - ((i == -1? 0.4 : 0.2) * i), generalRankingLoc.getZ());

                ArmorStand armorstand = GENERAL_RANKING_AMORSTANDS.get(i);
                if (armorstand == null) {
                    armorstand = (ArmorStand) generalRankingLoc.getWorld().spawnEntity(tempLoc, EntityType.ARMOR_STAND);
                    GENERAL_RANKING_AMORSTANDS.put(i, armorstand);
                }

                armorstand.setCustomNameVisible(true);
                armorstand.setGravity(false);
                armorstand.setBasePlate(false);
                armorstand.setVisible(false);
                if (i == -1) {
                    armorstand.setCustomName(MESSAGE_CONFIGURATION.getGeneral_ranking_header_message().replace("&", "§"));
                } else {
                    armorstand.setCustomName(MESSAGE_CONFIGURATION.getGeneral_ranking_format_message().replace("&", "§")
                            .replace("%player%", resultList.get(i).get(0))
                            .replace("%score%", resultList.get(i).get(1) + ""));
                }
            }
        }


        try (val result = store.raw("SELECT p.name, sum(n." + NoteEntity.AMENAGEMENT.getName() + ") + sum(n." + NoteEntity.BEAUTE.getName() + ") + sum(n." + NoteEntity.CREATIVITE.getName() + ") + sum(n." + NoteEntity.FOLKLORE.getName() + ") + sum(n." + NoteEntity.FUN.getName() + ") as 'score' " +
                "FROM " + NoteEntity.$TYPE.getName() + " n " +
                "JOIN " + ProfilEntity.$TYPE.getName() + " p " +
                "ON (n." + NoteEntity.PROFIL_ID.getName() + " = p." + ProfilEntity.ID.getName() + ") " +
                "JOIN (" +
                        "SELECT " + BuildbattleEntity.ID.getName() + ", " + BuildbattleEntity.DATE.getName() + " " +
                        "FROM " + BuildbattleEntity.$TYPE.getName() + " " +
                        "WHERE " + BuildbattleEntity.DATE.getName() + " < NOW() " +
                        "ORDER BY date DESC " +
                        "LIMIT 1) b " +
                "ON (b." + BuildbattleEntity.ID.getName() + " = n." + NoteEntity.BUILDBATTLE_ID.getName() + ") " +
                "GROUP BY p." + ProfilEntity.NAME.getName() + " " +
                "ORDER BY score desc " +
                "LIMIT 10")) {
            val resultList = result.toList();
            val lastRankingLoc = LocationsUtil.getLocationFromString(HUB_CONFIGURATION.getLast_buildbattle_ranking_location());

            for (int i = -1; i < resultList.size(); i++) {
                val tempLoc = new Location(lastRankingLoc.getWorld(), lastRankingLoc.getX(), lastRankingLoc.getY() + 1 - ((i == -1? 0.4 : 0.2) * i), lastRankingLoc.getZ());

                ArmorStand armorstand = LAST_RANKING_AMORSTANDS.get(i);
                if (armorstand == null) {
                    armorstand = (ArmorStand) lastRankingLoc.getWorld().spawnEntity(tempLoc, EntityType.ARMOR_STAND);
                    LAST_RANKING_AMORSTANDS.put(i, armorstand);
                }

                armorstand.setCustomNameVisible(true);
                armorstand.setGravity(false);
                armorstand.setBasePlate(false);
                armorstand.setVisible(false);
                if (i == -1) {
                    armorstand.setCustomName(MESSAGE_CONFIGURATION.getLast_buildbattle_ranking_header_message().replace("&", "§"));
                } else {
                    armorstand.setCustomName(MESSAGE_CONFIGURATION.getLast_buildbattle_ranking_format_message().replace("&", "§")
                            .replace("%player%", resultList.get(i).get(0))
                            .replace("%score%", resultList.get(i).get(1) + ""));
                }
            }
        }
    }

    public static void destroyArmorstands() {

        val generalRankingLoc = LocationsUtil.getLocationFromString(HUB_CONFIGURATION.getGeneral_ranking_location());
        val generalRankingEntities = generalRankingLoc.getWorld().getNearbyEntities(generalRankingLoc, 3, 3, 3);
        for (Entity entity : generalRankingEntities) {
            if (entity instanceof ArmorStand)
                entity.remove();
        }

        val lastRankingLoc = LocationsUtil.getLocationFromString(HUB_CONFIGURATION.getLast_buildbattle_ranking_location());
        val lastRankingEntities = lastRankingLoc.getWorld().getNearbyEntities(lastRankingLoc, 3, 3, 3);
        for (Entity entity : lastRankingEntities) {
            if (entity instanceof ArmorStand)
                entity.remove();
        }
    }
}
