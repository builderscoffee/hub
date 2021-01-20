package eu.builderscoffee.hub.listeners;

import eu.builderscoffee.api.utils.ItemBuilder;
import eu.builderscoffee.api.utils.LocationsUtil;
import eu.builderscoffee.api.utils.Title;
import eu.builderscoffee.hub.Main;
import eu.builderscoffee.hub.configuration.MessageConfiguration;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private final ItemStack hubCompass = new ItemBuilder(Material.COMPASS).setName("§k§6|§eNavigation§k§6").build();
    private final MessageConfiguration messages = Main.getInstance().getMessageConfiguration();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);

        player.setAllowFlight(true);

        player.getInventory().clear();
        player.getInventory().setHeldItemSlot(4);
        player.getInventory().setItem(4, hubCompass);
        player.teleport(LocationsUtil.getLocationFromString(Main.getInstance().getHubConfiguration().getSpawnLocation()));
        new Title(messages.getTitle().replace("&", "§"), messages.getSubTitle().replace("&", "§"), 20, 100, 20).send(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Nothing to do here
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onOpenNetworkInventory(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() != null && event.getItem().isSimilar(hubCompass)) {
            player.performCommand("network");
        }
    }

    @EventHandler
    public void onMoveInventoryItems(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (!canModifyHub(player, "builderscoffee.temp")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location hubLocation = LocationsUtil.getLocationFromString(Main.getInstance().getHubConfiguration().getSpawnLocation());

        if (player.getLocation().getWorld().equals(hubLocation.getWorld()) && player.getLocation().getY() < 0) {
            player.teleport(hubLocation);
        }
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
        Player player = event.getPlayer();

        if (!canModifyHub(player, "builderscoffee.temp")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!canModifyHub(player, "builderscoffee.temp")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (!canModifyHub(player, "builderscoffee.temp")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (!canModifyHub(player, "builderscoffee.temp")) {
            event.setCancelled(true);
        }
    }

    //Interaction avec les itemsframes
    @EventHandler
    public void onRightClickEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (event.getRightClicked() instanceof ItemFrame && !canModifyHub(player, "builderscoffee.temp")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onRightClickAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        if (event.getRightClicked() instanceof ArmorStand && !canModifyHub(player, "builderscoffee.temp")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeftClickEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Entity entity = event.getEntity();

            if ((entity instanceof ItemFrame || entity instanceof ArmorStand) && !canModifyHub(player, "builderscoffee.temp")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPaintingBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) {
            Player player = (Player) event.getRemover();

            if (!canModifyHub(player, "builderscoffee.temp")) {
                event.setCancelled(true);
            }
        }
    }

    private boolean canModifyHub(Player player, String permission) {
        return player.hasPermission(permission) && player.getGameMode().equals(GameMode.CREATIVE);
    }
}
