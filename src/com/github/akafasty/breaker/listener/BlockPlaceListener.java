package com.github.akafasty.breaker.listener;

import com.github.akafasty.breaker.main.BreakerPlugin;
import com.github.akafasty.breaker.manager.BreakerManager;
import com.github.akafasty.breaker.manager.ConfigurationManager;
import com.github.akafasty.breaker.object.BlockBreaker;
import com.github.akafasty.breaker.object.BlockConfigurator;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.concurrent.TimeUnit;

public class BlockPlaceListener implements Listener {

    private final ConfigurationManager configurationManager = BreakerPlugin.getInstance().getConfigurationManager();
    private final BreakerManager breakerManager = BreakerPlugin.getInstance().getBreakerManager();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {

        Block block = event.getBlock();

        BlockConfigurator blockConfigurator = configurationManager.retrieve(block.getTypeId());

        Location location = block.getLocation();

        if (blockConfigurator != null && blockConfigurator.isStarts()) {

            BlockBreaker blockBreaker = BlockBreaker
                    .builder()
                    .durability(1)
                    .x(location.getX())
                    .y(location.getY())
                    .z(location.getZ())
                    .time((System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(blockConfigurator.getMinutes())))
                    .build();

            breakerManager.insert(blockBreaker);

        }
    }

}
