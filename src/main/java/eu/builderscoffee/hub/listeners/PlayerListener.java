package eu.builderscoffee.hub.listeners;

import eu.builderscoffee.api.utils.HeaderAndFooter;
import eu.builderscoffee.api.utils.ItemBuilder;
import eu.builderscoffee.api.utils.LocationsUtil;
import eu.builderscoffee.api.utils.Title;
import eu.builderscoffee.hub.Main;
import eu.builderscoffee.hub.configuration.HubConfiguration;
import eu.builderscoffee.hub.configuration.MessageConfiguration;
import lombok.val;
import org.bukkit.*;
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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class PlayerListener implements Listener {

    private final MessageConfiguration messagesConfig = Main.getInstance().getMessageConfiguration();
    private final HubConfiguration hubConfig = Main.getInstance().getHubConfiguration();
    private final ItemStack hubCompass = new ItemBuilder(Material.COMPASS).setName(messagesConfig.getCompassName()).build();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);

        player.setAllowFlight(true);

        player.getInventory().clear();
        player.getInventory().setHeldItemSlot(4);
        player.getInventory().setItem(4, hubCompass);
        player.teleport(LocationsUtil.getLocationFromString(Main.getInstance().getHubConfiguration().getSpawnLocation()));
        new Title(messagesConfig.getTitle().replace("&", "§"), messagesConfig.getSubTitle().replace("&", "§"), 20, 100, 20).send(player);
        new HeaderAndFooter(messagesConfig.getHeaderMessage().replace("&", "§"), messagesConfig.getFooterMessage().replace("&", "§")).send(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
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
    public void onCommand(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String message = event.getMessage();

        if(message.equalsIgnoreCase("/hub") || message.equalsIgnoreCase("/lobby")) {
            event.setCancelled(true);
            player.teleport(LocationsUtil.getLocationFromString(hubConfig.getSpawnLocation()));
        }
    }

    @EventHandler
    public void onMoveInventoryItems(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        if (!canModifyHub(player, "builderscoffee.temp")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Location hubLocation = LocationsUtil.getLocationFromString(Main.getInstance().getHubConfiguration().getSpawnLocation());

        if (player.getLocation().getWorld().equals(hubLocation.getWorld()) && player.getLocation().getY() < 0) {
            player.teleport(hubLocation);
        }
    }

    @EventHandler
    public void onOpenInventory(InventoryOpenEvent event) {
        final Player player = (Player) event.getPlayer();

        player.playSound(player.getLocation(), Sound.ENTITY_SNOWMAN_SHOOT, 1f, 1f);
    }

    @EventHandler
    public void onOpenInventory(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();

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
