package com.linda.simplehardcore;

import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import com.linda.simplehardcore.entity.SHPlayer;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PlayerManager {

    private static final Set<SHPlayer> players = new HashSet<>();
    private static final Integer MAX_LIVES = 3;
    private static final String PLAYER_DATA_FILE_DIR = ".\\world\\playerstats";

    public static SHPlayer findByPlayer(Player player){
        for (SHPlayer shPLayer: players) {
            if(shPLayer.equals(player)){
                return shPLayer;
            }
        }
        return null;
    }
    public static SHPlayer findByName(String playerName){
        for (SHPlayer shPlayer: players){
            if(shPlayer.getName().equals(playerName)){
                return shPlayer;
            }
        }
        return null;
    }
    public static Integer getLives(Player player) {
        SHPlayer shPlayer = findByPlayer(player);
        if(shPlayer == null) {
            System.out.println("An error has occurred. Getting player lives before registration");
            return null;
        }
        return shPlayer.getLives();
    }
    public static boolean setLives(Player player, int lives) {
        SHPlayer shPlayer = findByPlayer(player);
        if(shPlayer == null) return false;
        shPlayer.setLives(lives);
        return false;
    }
    public static boolean containsPlayer(Player player) {
        return findByPlayer(player) != null;
    }

    public static boolean registerPlayer(Player player) {
        if(findByPlayer(player) != null) return true;
        players.add(new SHPlayer(player, MAX_LIVES));
        return true;
    }


    public static void revokeLive(Player player) {
        findByPlayer(player).revokeLive();
    }

    public static void savePlayerData() {
        try {
            File dataDir = new File(PLAYER_DATA_FILE_DIR);
            if (!dataDir.exists() && !dataDir.mkdirs()) {
                throw new IOException("Failed to create player data dirs!");
            }
            for(SHPlayer shPlayer: players){
                File dataFile = new File(dataDir, shPlayer.getUuid() + ".json");
                if (!dataFile.exists()){
                    Files.createFile(Paths.get(dataFile.toURI()));
                }

                FileWriter writer = new FileWriter(dataFile);
                JsonObject rootJson = new JsonObject();

                rootJson.addProperty("PlayerName", shPlayer.getName());
                rootJson.addProperty("PlayerUUID", shPlayer.getUuid().toString());
                rootJson.addProperty("PlayerLives", shPlayer.getLives());

                writer.write(rootJson.toString());
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("Failed to create player data: " + e);
        }
    }

    public static void savePlayerData(Player player) {
        try {
            SHPlayer shPlayer = findByPlayer(player);
            if(shPlayer == null){
                throw new NullPointerException("Tried to save unregistered player");
            }

            File dataDir = new File(PLAYER_DATA_FILE_DIR);
            if (!dataDir.exists() && !dataDir.mkdirs()) {
                throw new IOException("Failed to create player data dirs!");
            }

            File dataFile = new File(dataDir, shPlayer.getUuid().toString() + ".json");
            if (!dataFile.exists()){
                Files.createFile(Paths.get(dataFile.toURI()));
            }

            FileWriter writer = new FileWriter(dataFile);
            JsonObject rootJson = new JsonObject();

            rootJson.addProperty("PlayerName", shPlayer.getName());
            rootJson.addProperty("PlayerUUID", shPlayer.getUuid().toString());
            rootJson.addProperty("PlayerLives", shPlayer.getLives());
            writer.write(rootJson.toString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }


    }
    public static void loadPlayerData(Player player) {
        try{
            if(findByPlayer(player) != null) throw new Exception("Player already loaded");

            File dataFile = new File(PLAYER_DATA_FILE_DIR, player.getUniqueId().toString() + ".json");
            if (!dataFile.exists()) return;

            try (FileReader reader = new FileReader(dataFile)) {
                JsonObject rootJson = new JsonStreamParser(reader).next().getAsJsonObject();
                int lives = rootJson.get("PlayerLives").getAsInt();
                players.add(new SHPlayer(player, lives));
            } catch (IOException e) {
                throw new IOException("Failed to read JSON structure, during player data loading");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void unregister(Player player) {
        players.remove(findByPlayer(player));
    }
}
