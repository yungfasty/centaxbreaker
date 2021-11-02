package com.github.akafasty.breaker.manager;

import com.github.akafasty.breaker.main.BreakerPlugin;
import com.github.akafasty.breaker.object.BlockBreaker;
import com.github.akafasty.breaker.object.BlockConfigurator;
import com.github.viiictorxd.reference.Cache;
import com.github.viiictorxd.reference.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.concurrent.TimeUnit;

public class ConfigurationManager {

    private final Cache<Integer, BlockConfigurator> breakerCache;
    private final Cache<EntityType, Integer> entitiesCache;

    public ConfigurationManager() {

        breakerCache = new CacheBuilder<Integer, BlockConfigurator>()
                .maximumSize(25)
                .build();

        entitiesCache = new CacheBuilder<EntityType, Integer>()
                .maximumSize(10)
                .build();

    }

    public void insert(EntityType entityType, int damage) {
        entitiesCache.put(entityType, damage);
    }

    public void insert(BlockConfigurator blockConfigurator) { breakerCache.put(blockConfigurator.getId(), blockConfigurator); }

    public BlockConfigurator retrieve(int id) {
        return breakerCache.fetchOrDefault(id, null);
    }

    public Integer retrieve(EntityType entityType) {
        return entitiesCache.fetchOrDefault(entityType, null);
    }

    public void loadAll() {
        FileConfiguration configuration = BreakerPlugin.getInstance().getConfig();

        configuration.getStringList("blocks").forEach(each -> {

            String[] arguments = each.split(":");
            int id = Integer.parseInt(arguments[0]);
            int durability = Integer.parseInt(arguments[1]);
            int minutes = Integer.parseInt(arguments[2]);
            boolean starts = Boolean.parseBoolean(arguments[3]);

            BlockConfigurator blockConfigurator = BlockConfigurator
                    .builder()
                    .id(id)
                    .minutes(minutes)
                    .durability(durability)
                    .starts(starts)
                    .build();

            insert(blockConfigurator);

        });

        configuration.getStringList("entities").forEach(each -> {

            String[] arguments = each.split(":");
            EntityType entityType = EntityType.valueOf(arguments[0].toUpperCase());
            int damage = Integer.parseInt(arguments[1]);

            insert(entityType, damage);

        });

        Bukkit.getConsoleSender().sendMessage(String.format("§a[CentaxBreaker] Total de '%s' entidades carregados.", entitiesCache.size()));
        Bukkit.getConsoleSender().sendMessage(String.format("§a[CentaxBreaker] Total de '%s' blocos carregados.", breakerCache.size()));

    }

}
