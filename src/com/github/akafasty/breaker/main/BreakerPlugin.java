package com.github.akafasty.breaker.main;

import com.github.akafasty.breaker.listener.BlockPlaceListener;
import com.github.akafasty.breaker.listener.EntityExplodeListener;
import com.github.akafasty.breaker.listener.PlayerInteractListener;
import com.github.akafasty.breaker.manager.BreakerManager;
import com.github.akafasty.breaker.manager.ConfigurationManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class BreakerPlugin extends JavaPlugin {

    @Getter private static BreakerPlugin instance;
    @Getter private ConfigurationManager configurationManager;
    @Getter private BreakerManager breakerManager;

    @SneakyThrows
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        configurationManager = new ConfigurationManager();

        configurationManager.loadAll();

        breakerManager = new BreakerManager();

        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityExplodeListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);


    }

    public void onDisable() {

    }


}