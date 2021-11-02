package com.github.akafasty.breaker.listener;

import com.github.akafasty.breaker.main.BreakerPlugin;
import com.github.akafasty.breaker.manager.BreakerManager;
import com.github.akafasty.breaker.manager.ConfigurationManager;
import com.github.akafasty.breaker.object.BlockBreaker;
import com.github.akafasty.breaker.object.BlockConfigurator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final ConfigurationManager CONFIGURATION_MANAGER = BreakerPlugin.getInstance().getConfigurationManager();
    private final BreakerManager BREAKER_MANAGER = BreakerPlugin.getInstance().getBreakerManager();
    private static final BreakerPlugin breakerPlugin = BreakerPlugin.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_BLOCK && player.getItemInHand().getType().equals(Material.STICK)) {

            Block block = event.getClickedBlock();

            BlockConfigurator configurator = CONFIGURATION_MANAGER.retrieve(block.getTypeId());

            if (configurator != null) {

                BlockBreaker blockBreaker = BREAKER_MANAGER.retrieve(block.getX(), block.getY(), block.getZ());

                int durability = configurator.getDurability();

                if (blockBreaker != null) {

                    BREAKER_MANAGER.verify(blockBreaker, configurator);

                    durability = blockBreaker.getDurability();

                }

                player.sendMessage(breakerPlugin.getConfig().getString("messages.check-durability").replace("&", "§").replace("{current}", String.valueOf(durability)).replace("{max}", String.valueOf(configurator.getDurability())));

            }


        }


    }


}
