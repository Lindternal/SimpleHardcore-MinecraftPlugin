package com.linda.simplehardcore;

import com.linda.simplehardcore.commands.SHCommands;
import com.linda.simplehardcore.lobbymange.LoginLogout;
import com.linda.simplehardcore.shop.ShopGUI;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import com.linda.simplehardcore.deathmanager.DeathMessage;

import java.io.File;

public final class SimpleHardcore extends JavaPlugin {

    public static final File serverDataDir = new File("world");
    public static final File playerDataDir = new File(serverDataDir, "playerstats");

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Simple Hardcore has been enabled!");
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Simple Hardcore for 1.20.2 made by Lindternal and FallenVEye");
        getServer().getPluginManager().registerEvents(new DeathMessage(this), this);
        getServer().getPluginManager().registerEvents(new LoginLogout(this), this);
        getServer().getPluginManager().registerEvents(new ShopGUI(), this);
        getServer().getPluginCommand("sh").setExecutor(new SHCommands(this));
    }

    @Override
    public void onDisable() {
        PlayerManager.savePlayerData();
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "Simple Hardcore has been disabled!");
    }
}