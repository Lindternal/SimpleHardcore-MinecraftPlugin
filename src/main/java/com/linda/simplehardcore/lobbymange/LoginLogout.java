package com.linda.simplehardcore.lobbymange;

import com.linda.simplehardcore.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class LoginLogout implements Listener {

    Plugin plugin;

    public LoginLogout(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerManager.loadPlayerData(player);
        if(!PlayerManager.containsPlayer(player)){
            PlayerManager.registerPlayer(player);
        }

        respawnOnLogin(player);
        int playerLives = PlayerManager.getLives(player);
        if (playerLives > 0) {
            player.sendTitle(ChatColor.GREEN + "Hello " + ChatColor.GOLD + player.getName() + ChatColor.GREEN + "!",
                    ChatColor.YELLOW + "You have " + ChatColor.RED + playerLives + ChatColor.YELLOW + ((playerLives > 1) ? " lives!" : " live!"),
                    5, 100, 20);
            player.sendMessage(ChatColor.AQUA + "Use " + ChatColor.GOLD + "/sh perks" + ChatColor.AQUA + " command to open shop menu.");
        } else {
            if (player.getGameMode() != GameMode.SPECTATOR)  player.setGameMode(GameMode.SPECTATOR);
            player.sendTitle(ChatColor.RED + "YOU DIED!",
                    ChatColor.RED + "You have no more lives!",
                    5, 100, 20);
        }
        switch (PlayerManager.getLives(player)) {
            case 1 -> event.setJoinMessage(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " has joined!\nThey have " + ChatColor.RED + "1" + ChatColor.YELLOW + " more live.");
            case 0 -> event.setJoinMessage(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " has joined!\nThey are " + ChatColor.RED + "dead" + ChatColor.YELLOW + ".");
            default -> event.setJoinMessage(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " has joined!\nThey have " + ChatColor.RED + PlayerManager.getLives(player) + ChatColor.YELLOW + " more lives.");
        }
    }

    private static void respawnOnLogin(Player player) {
        if (PlayerManager.getLives(player) <= 0 || player.getGameMode() != GameMode.SPECTATOR) return;
        //Get player spawn location
        Location bedLocation = player.getBedSpawnLocation();
        Location spawnLocation = player.getWorld().getSpawnLocation();
        Location respawnLocation = (bedLocation != null)?bedLocation:spawnLocation;

        //Respawn player
        player.teleport(respawnLocation);
        player.setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event) {
        PlayerManager.savePlayerData(event.getPlayer());
        PlayerManager.unregister(event.getPlayer());
        event.setQuitMessage(ChatColor.GOLD + event.getPlayer().getName() + ChatColor.YELLOW + " has quit!");
    }
}
