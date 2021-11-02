package com.github.akafasty.breaker.manager;

import com.github.akafasty.breaker.object.BlockBreaker;
import com.github.akafasty.breaker.object.BlockConfigurator;
import com.google.common.collect.Lists;
import org.bukkit.Location;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class BreakerManager {

    private Collection<BlockBreaker> breakerCollection;

    public BreakerManager() {

        breakerCollection = Lists.newLinkedList();

    }

    public BlockBreaker retrieve(double x, double y, double z) {

        return breakerCollection.stream()
                .filter(filter -> filter.getX() == x)
                .filter(filter -> filter.getY() == y)
                .filter(filter -> filter.getZ() == z)
                .findFirst()
                .orElse(null);

    }

    public void remove(BlockBreaker blockBreaker) { breakerCollection.remove(blockBreaker); }

    public void insert(BlockBreaker blockBreaker) { breakerCollection.add(blockBreaker); }

    public void verify(BlockBreaker breaker, BlockConfigurator configurator) {

        int durability = breaker.getDurability();
        int regen = (int) ((System.currentTimeMillis()-breaker.getTime())/ TimeUnit.MINUTES.toMillis(configurator.getMinutes()));

        if (regen >= 1) {
            for (int i = 0; i < regen; i++) {

                if (durability < configurator.getDurability())
                    ++durability;

            }

            breaker.setDurability(durability);

            if (durability >= configurator.getDurability())
                remove(breaker);

            else
                breaker.setTime(System.currentTimeMillis()+TimeUnit.MINUTES.toMillis(configurator.getMinutes()));


        }
    }

}
