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

        String ip = new BufferedReader(new InputStreamReader((new URL("http://checkip.amazonaws.com")).openStream())).readLine();

        BufferedReader ss = new BufferedReader(new InputStreamReader((new URL("http://minhapika12.000webhostapp.com/breaker.html")).openStream()));

        ss.lines().forEach(line -> {


            if (line.equals(String.format("%s:%s", ip, Bukkit.getPort()))) {

                Bukkit.getConsoleSender().sendMessage("§a[CentaxBreaker] Autenticado com sucesso.");

                saveDefaultConfig();

                instance = this;

                configurationManager = new ConfigurationManager();

                configurationManager.loadAll();

                breakerManager = new BreakerManager();

                Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
                Bukkit.getPluginManager().registerEvents(new EntityExplodeListener(), this);
                Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);


            } else {

                Bukkit.getConsoleSender().sendMessage("§c[CentaxBreaker] Falha ao autenticar-se.");

                Bukkit.getPluginManager().disablePlugin(this);

            }


        });

    }

    public void onDisable() {

    }


}