package eu.builderscoffee.hub.listeners;

import eu.builderscoffee.api.bukkit.board.FastBoard;
import eu.builderscoffee.api.bukkit.utils.HeaderAndFooter;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.api.bukkit.utils.LocationsUtil;
import eu.builderscoffee.api.bukkit.utils.Title;
import eu.builderscoffee.commons.bukkit.utils.SkullCreator;
import eu.builderscoffee.hub.Main;
import eu.builderscoffee.hub.board.BBBoard;
import eu.builderscoffee.hub.utils.MessageUtils;
import lombok.val;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {

    private ItemStack navigationItem(Player player){
        return new ItemBuilder(Material.COMPASS)
                .setName(MessageUtils.getMessageConfig(player).getItems().getNavigation())
                .build();
    }

    private ItemStack profileItem(Player player){
        return new ItemBuilder(SkullCreator.itemFromUuid(player.getUniqueId()))
                .setName(MessageUtils.getMessageConfig(player).getItems().getProfile())
                .build();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        val player = event.getPlayer();

        // Scoreboard Updater
        val board = new FastBoard(player);
        board.updateTitle(MessageUtils.getMessageConfig(player).getScoreboard().getTitle().replace("&", "§")); // Même titre pour tout
        BBBoard.boards.put(player.getUniqueId(), board);

        // Player initialisation
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);

        Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
            player.setFlying(false);
            player.setAllowFlight(true);
        }, 2L);

        player.getInventory().clear();
        player.getInventory().setHeldItemSlot(4);
        player.getInventory().setItem(4, navigationItem(player));
        player.getInventory().setItem(8, profileItem(player));
        player.teleport(LocationsUtil.getLocationFromString(Main.getInstance().getHubConfig().getSpawnLocation()));

        new Title(MessageUtils.getMessageConfig(player).getTitle().getTitle().replace("&", "§"), MessageUtils.getMessageConfig(player).getTitle().getSubTitle().replace("&", "§"), 20, 100, 20).send(player);
        new HeaderAndFooter(MessageUtils.getMessageConfig(player).getTablist().getHeader().replace("&", "§"), MessageUtils.getMessageConfig(player).getTablist().getFooter().replace("&", "§")).send(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Scoreboard clean
        val board = BBBoard.boards.remove(event.getPlayer().getUniqueId());
        if (board != null)
            board.delete();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onOpenInventory(PlayerInteractEvent event) {
        val player = event.getPlayer();

        if (event.getItem() != null) {
            if(event.getItem().isSimilar(navigationItem(player)))
                player.performCommand("network");
            else if(event.getItem().isSimilar(profileItem(player)))
                player.performCommand("profil");
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        val player = event.getPlayer();
        val message = event.getMessage();

        if(message.equalsIgnoreCase("/hub") || message.equalsIgnoreCase("/lobby")) {
            event.setCancelled(true);
            player.teleport(LocationsUtil.getLocationFromString(Main.getInstance().getHubConfig().getSpawnLocation()));
        }
    }

    @EventHandler
    public void onToggleFly(PlayerToggleFlightEvent event) {
        val player = event.getPlayer();

        if(player.getGameMode().equals(GameMode.ADVENTURE) || player.getGameMode().equals(GameMode.SURVIVAL)) {
            Vector vector = player.getLocation().getDirection().multiply(1);
            vector.setY(1);
            player.setVelocity(vector);
            player.setAllowFlight(false);

            event.setCancelled(true);

            Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> player.setAllowFlight(true), 40L);
        }
    }

    @EventHandler
    public void onMoveInventoryItems(InventoryClickEvent event) {
        val player = (Player) event.getWhoClicked();

        if (!canModifyHub(player, Main.getInstance().getPermissionsConfig().getModifyHub())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Location hubLocation = LocationsUtil.getLocationFromString(Main.getInstance().getHubConfig().getSpawnLocation());

        if (player.getLocation().getWorld().equals(hubLocation.getWorld()) && player.getLocation().getY() < 0) {
            player.teleport(hubLocation);
        }
    }

    @EventHandler
    public void onOpenInventory(InventoryOpenEvent event) {
        final Player player = (Player) event.getPlayer();

        if(!event.getView().equals(player.getOpenInventory()))
            player.playSound(player.getLocation(), Sound.ENTITY_SNOWMAN_SHOOT, 1f, 1f);
    }

    @EventHandler
    public void onOpenInventory(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();

        if(!event.getView().equals(player.getOpenInventory()))
            player.playSound(player.getLocation(), Sound.ENTITY_SNOWMAN_SHOOT, 1f, 1f);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        final Player player = event.getPlayer();

        if (!canModifyHub(player, "builderscoffee.temp")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if (!canModifyHub(player, "builderscoffee.temp")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(ExplosionPrimeEvent event) {
        final Location location = event.getEntity().getLocation();
        if (location.getWorld().equals(LocationsUtil.getLocationFromString(Main.getInstance().getHubConfig().getSpawnLocation()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        final Location location = event.getEntity().getLocation();
        if (location.getWorld().equals(LocationsUtil.getLocationFromString(Main.getInstance().getHubConfig().getSpawnLocation()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void OnLeaveDecay(LeavesDecayEvent event) {
        final Location location = event.getBlock().getLocation();
        if (location.getWorld().equals(LocationsUtil.getLocationFromString(Main.getInstance().getHubConfig().getSpawnLocation()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();

        if (!canModifyHub(player, "builderscoffee.temp")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();

        if (!canModifyHub(player, "builderscoffee.temp")) {
            event.setCancelled(true);
        }
    }

    //Interaction avec les itemsframes
    @EventHandler
    public void onRightClickEntity(PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();

        if (event.getRightClicked() instanceof ItemFrame && !canModifyHub(player, "builderscoffee.temp")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onRightClickAtEntity(PlayerInteractAtEntityEvent event) {
        final Player player = event.getPlayer();

        if (event.getRightClicked() instanceof ArmorStand && !canModifyHub(player, "builderscoffee.temp")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeftClickEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            final Player player = (Player) event.getDamager();
            final Entity entity = event.getEntity();

            if ((entity instanceof ItemFrame || entity instanceof ArmorStand) && !canModifyHub(player, "builderscoffee.temp")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPaintingBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) {
            final Player player = (Player) event.getRemover();

            if (!canModifyHub(player, "builderscoffee.temp")) {
                event.setCancelled(true);
            }
        }
    }

    private boolean canModifyHub(Player player, String permission) {
        return player.hasPermission(permission) && player.getGameMode().equals(GameMode.CREATIVE);
    }
}
