package com.linda.simplehardcore.commands;

import com.google.gson.*;
import com.linda.simplehardcore.SimpleHardcore;
import com.linda.simplehardcore.PlayerManager;
import com.linda.simplehardcore.entity.SHPlayer;
import com.linda.simplehardcore.shop.EventGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.Arrays;

public class SHCommands implements CommandExecutor {

    SimpleHardcore plugin;

    public SHCommands(SimpleHardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!command.getName().equals("sh")) return true;

        if (!(sender instanceof Player || sender instanceof ConsoleCommandSender)) return true;

        if (args.length == 0) {
            emptyCommand(sender);
            return true;
        }
        if (args[0].equals("help")) {
            return helpCommand(sender, getArraysSlice(args, 1, args.length));
        }

        if (args[0].equals("perks")) {
            return perksCommand(sender, getArraysSlice(args, 1, args.length));
        }

        if (args[0].equals("getl")) {
            return getLivesCommand(sender, args[0], getArraysSlice(args, 1, args.length));
        }

        if(args[0].equals("setl")){
            return setLivesCommand(sender, args[0], getArraysSlice(args, 1, args.length));
        }
        sender.sendMessage(ChatColor.RED + "Wrong arguments!");
        sender.sendMessage(ChatColor.RED + "Use /sh help");
        return true;
    }

    private String[] getArraysSlice(String[] array, int from, int to) {
        if (from > array.length - 1) return new String[0];
        return Arrays.copyOfRange(array, from, to);
    }

    private void emptyCommand(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "You didn't provide any arguments when running the command!");
        sender.sendMessage(ChatColor.RED + "Use /sh help");
    }

    private boolean helpCommand(CommandSender sender, final String[] args) {
        if (args.length != 0) {
            sender.sendMessage(ChatColor.RED + "Invalid arguments!");
        } else {
            //DON'T TOUCH IT
            sender.sendMessage(ChatColor.DARK_GREEN + "Simple Hardcore commands:");
            sender.sendMessage(ChatColor.GOLD + "/sh help" + ChatColor.WHITE + " - " + ChatColor.AQUA + "Show all plugin commands");
            sender.sendMessage(ChatColor.GOLD + "/sh perks" + ChatColor.WHITE + " - " + ChatColor.AQUA + "Open perks shop");
            sender.sendMessage(ChatColor.GOLD + "/sh getl <playerName>" + ChatColor.WHITE + " - " + ChatColor.AQUA + "Get player lives");
            sender.sendMessage(ChatColor.GOLD + "/sh setl <playerName> <livesAmount>" + ChatColor.WHITE + " - " + ChatColor.AQUA + "Set player lives");
        }
        return true;
    }

    private boolean perksCommand(CommandSender sender, final String[] args) {
        if (args.length != 0) {
            sender.sendMessage(ChatColor.RED + "Invalid arguments!");
            return true;
        }

        if (sender instanceof Player) {
            Bukkit.getServer().getPluginManager().callEvent(new EventGUI((Player) sender));
        } else if (sender instanceof ConsoleCommandSender) {
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "You must be a player to execute this command!");
        }
        return true;
    }

    private boolean getLivesCommand(CommandSender sender, final String command, final String[] args) {
        String message = null;
        Integer lives = null;
        class Helper{
            static Integer getLivesOnlinePlayer(Player player){
                return PlayerManager.getLives(player);
            }
            static Integer getLivesOfflinePlayer(String playerName){
                File playerListFile = new File("usercache.json");
                try{
                    FileReader reader = new FileReader(playerListFile);
                    JsonArray playersJsonArr = new JsonStreamParser(reader).next().getAsJsonArray();
                    String playerUuid = null;
                    for(JsonElement jsonPlayer: playersJsonArr){
                        String jsonPlayerName = jsonPlayer.getAsJsonObject().get("name").getAsString();
                        if(jsonPlayerName.equals(playerName)){
                            playerUuid = jsonPlayer.getAsJsonObject().get("uuid").getAsString();
                            break;
                        }
                    }
                    if (playerUuid == null) return null;
                    File playerFile = new File(SimpleHardcore.playerDataDir, playerUuid + ".json");
                    if(!playerFile.exists())return null;
                    reader = new FileReader(playerFile);
                    JsonObject rootJson = new JsonStreamParser(reader).next().getAsJsonObject();
                    return rootJson.getAsJsonObject().get("PlayerLives").getAsInt();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Invalid arguments!");
            return true;
        }
        String playerName = args[0];
        SHPlayer shPlayer = PlayerManager.findByName(playerName);
        if(shPlayer != null){
            lives = Helper.getLivesOnlinePlayer(shPlayer.getPlayer());
        }else{
            lives = Helper.getLivesOfflinePlayer(playerName);
        }

        if(lives == null){
            message = ChatColor.RED + "Error. This player is not registered on the server!";
        }else {
            message = ChatColor.GOLD + playerName + ChatColor.YELLOW + " has ";
            switch (lives) {
                case 0 -> message += ChatColor.RED + "no more " + ChatColor.YELLOW + "lives.";
                case 1 -> message += ChatColor.RED + lives.toString() + ChatColor.YELLOW + " live.";
                default -> message += ChatColor.RED + lives.toString() + ChatColor.YELLOW + " lives.";
            }
        }
        sender.sendMessage(message);
        return true;
    }

    private boolean checkOPPermissions(CommandSender sender){
        return ((sender instanceof Player) && ((Player) sender).isOp()) || (sender instanceof ConsoleCommandSender);
    }

    private boolean setLivesCommand(CommandSender sender, final String command, final String[] args) {
        String playerName;
        Integer lives;
        if(!checkOPPermissions(sender)){
            sender.sendMessage(ChatColor.RED + "You must be the server operator to execute this command!");
            return true;
        }

        if(args.length < 2){
            sender.sendMessage(ChatColor.RED + "Invalid arguments!");
            sender.sendMessage(ChatColor.RED + "Use /sh help");
            return true;
        }

        class Helper {
            static boolean setLivesOnlinePlayerCommand(Player player, Integer lives, CommandSender sender){
                PlayerManager.findByPlayer(player).setLives(lives);
                switch (lives) {
                    case 1 -> {
                        if (sender != player) sender.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.GREEN + " has " + ChatColor.RED + "1" + ChatColor.GREEN + " live now.");
                        player.sendMessage(ChatColor.GREEN + "You have " + ChatColor.RED + "1" + ChatColor.GREEN + " live now.");
                    }
                    case 0 -> {
                        if (sender != player) sender.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.GREEN + " has " + ChatColor.RED + "no mere" + ChatColor.GREEN + " lives now.");
                        player.sendMessage(ChatColor.GREEN + "You have " + ChatColor.RED + "no more" + ChatColor.GREEN + " lives now.");
                    }
                    default -> {
                        if (sender != player) sender.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.GREEN + " has " + ChatColor.RED + lives + ChatColor.GREEN + " lives now.");
                        player.sendMessage(ChatColor.GREEN + "You have " + ChatColor.RED + lives + ChatColor.GREEN + " lives now.");
                    }
                }
                return true;
            }
            static boolean setLivesOfflinePlayerCommand(String playerName, Integer lives, CommandSender sender){
                File userCacheFile = new File("usercache.json");
                String playerUUID = null;
                try{
                    FileReader reader = new FileReader(userCacheFile);
                    JsonArray userJsonArr = new JsonStreamParser(reader).next().getAsJsonArray();
                    for(JsonElement userJson : userJsonArr){
                        if(userJson.getAsJsonObject().get("name").getAsString().equals(playerName)){
                            playerUUID = userJson.getAsJsonObject().get("uuid").getAsString();
                            break;
                        }
                    }
                    if(playerUUID == null){
                        sender.sendMessage(ChatColor.RED + "This player has not registered on the server!");
                        return true;
                    }
                    File jsonFile = new File("world\\playerstats", playerUUID + ".json");
                    reader = new FileReader(jsonFile);
                    JsonObject playerJson = new JsonStreamParser(reader).next().getAsJsonObject();
                    reader.close();
                    playerJson.addProperty("PlayerLives", lives);
                    FileWriter writer = new FileWriter(jsonFile);
                    writer.write(playerJson.toString());
                    writer.close();
                }catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                sender.sendMessage(ChatColor.GOLD + playerName + ChatColor.GREEN + " has " + ChatColor.RED + lives + ChatColor.GREEN + " lives now.");
                return true;
            }
        }
        
        playerName = args[0];
        try {
            lives = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            sender.sendMessage(ChatColor.RED + "Invalid arguments!");
            sender.sendMessage(ChatColor.RED + "Use /sh help");
            return true;
        }
        if (lives < 0) lives = 0;

        SHPlayer shPlayer = PlayerManager.findByName(playerName);
        Boolean result;
        if(shPlayer != null){
            result = Helper.setLivesOnlinePlayerCommand(shPlayer.getPlayer(), lives, sender);
        } else {
            result = Helper.setLivesOfflinePlayerCommand(playerName, lives, sender);
        }
        return result;

    }
}

