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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class EntityExplodeListener implements Listener {

    private static final ConfigurationManager CONFIGURATION_MANAGER = BreakerPlugin.getInstance().getConfigurationManager();
    private final BreakerManager BREAKER_MANAGER = BreakerPlugin.getInstance().getBreakerManager();
    private static final BreakerPlugin BREAKER_PLUGIN = BreakerPlugin.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent event) {

        EntityType entityType = event.getEntityType();

        Integer damage = CONFIGURATION_MANAGER.retrieve(entityType);

        if (damage != null) {

            event.blockList().removeIf(block -> Objects.nonNull(CONFIGURATION_MANAGER.retrieve(block.getTypeId())));

            Location location = event.getLocation();

            int radius = 2;

            for (int x = -radius; x <= radius; x++)
                for (int y = -radius; y <= radius; y++)
                    for (int z = -radius; z <= radius; z++) {

                        Location targetLoc = new Location(location.getWorld(), location.getX() + x, location.getY() + y, location.getZ() + z);

                        if (location.distance(targetLoc) <= 2)
                            handleExplosion(targetLoc, damage);
                    }

        }

    }

    public void handleExplosion(Location location, int damage) {

        Block block = location.getWorld().getBlockAt(location);

        if (block.getType() == Material.AIR)
                return;

        if (location.getY() <= 2)
                return;

        BlockConfigurator blockConfigurator = CONFIGURATION_MANAGER.retrieve(block.getTypeId());

        if (blockConfigurator == null)
                return;

        BlockBreaker blockBreaker = BREAKER_MANAGER.retrieve(block.getX(), block.getY(), block.getZ());

        if (blockBreaker != null) {

            int durability = (blockBreaker.getDurability()-damage);

            blockBreaker.setDurability(durability);

            if (durability <= 0) {
                block.setType(Material.AIR);

                BREAKER_MANAGER.remove(blockBreaker);

            }

            return;

        }

        blockBreaker = BlockBreaker
                .builder()
                .x(block.getX())
                .y(block.getY())
                .z(block.getZ())
                .durability(blockConfigurator.getDurability()-damage)
                .time(System.currentTimeMillis()+ TimeUnit.MINUTES.toMillis(blockConfigurator.getMinutes()))
                .build();

        BREAKER_MANAGER.insert(blockBreaker);

    }

}
