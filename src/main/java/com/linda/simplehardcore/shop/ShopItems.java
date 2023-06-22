package com.linda.simplehardcore.shop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopItems {

    public static ItemStack extraHealth() {
        ItemStack item = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&d&lExtra Health"));
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.AQUA + "Increases your base");
        loreList.add(ChatColor.AQUA + "health by " + ChatColor.GREEN + "10HP");
        loreList.add("");
        loreList.add(ChatColor.GOLD + "Cost: " + ChatColor.YELLOW + "55 LVL EXP.");
        meta.setLore(loreList);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack purchasedExtraHealth() {
        ItemStack item = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&d&lExtra Health"));
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.AQUA + "Increases your base");
        loreList.add(ChatColor.AQUA + "health by " + ChatColor.GREEN + "10HP");
        meta.setLore(loreList);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack speedBoost() {
        ItemStack item = new ItemStack(Material.SUGAR, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&d&lSpeed Up"));
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.AQUA + "Increases your base");
        loreList.add(ChatColor.AQUA + "movement speed by " + ChatColor.GREEN + "20%");
        loreList.add("");
        loreList.add(ChatColor.GOLD + "Cost: " + ChatColor.YELLOW + "35 LVL EXP.");
        meta.setLore(loreList);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack purchasedSpeedBoost() {
        ItemStack item = new ItemStack(Material.SUGAR, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&d&lSpeed Up"));
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.AQUA + "Increases your base");
        loreList.add(ChatColor.AQUA + "movement speed by " + ChatColor.GREEN + "20%");
        meta.setLore(loreList);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack strengthBoost() {
        ItemStack item = new ItemStack(Material.BLAZE_POWDER, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&d&lStrength Buff"));
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.AQUA + "Increases your base");
        loreList.add(ChatColor.AQUA + "damage dealt by " + ChatColor.GREEN + "250%");
        loreList.add("");
        loreList.add(ChatColor.GOLD + "Cost: " + ChatColor.YELLOW + "45 LVL EXP.");
        meta.setLore(loreList);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack purchasedStrengthBoost() {
        ItemStack item = new ItemStack(Material.BLAZE_POWDER, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&d&lStrength Buff"));
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.AQUA + "Increases your base");
        loreList.add(ChatColor.AQUA + "damage dealt by " + ChatColor.GREEN + "250%");
        meta.setLore(loreList);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack attackSpeed() {
        ItemStack item = new ItemStack(Material.REDSTONE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&d&lAttack speed Up"));
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.AQUA + "Increases your attack");
        loreList.add(ChatColor.AQUA + "speed by " + ChatColor.GREEN + "100%");
        loreList.add("");
        loreList.add(ChatColor.GOLD + "Cost: " + ChatColor.YELLOW + "45 LVL EXP.");
        meta.setLore(loreList);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack purchasedAttackSpeed() {
        ItemStack item = new ItemStack(Material.REDSTONE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&d&lAttack speed Up"));
        List<String> loreList = new ArrayList<>();
        loreList.add(ChatColor.AQUA + "Increases your attack");
        loreList.add(ChatColor.AQUA + "speed by " + ChatColor.GREEN + "100%");
        meta.setLore(loreList);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getRedGlass() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lNot Purchased"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getGreenGlass() {
        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lPurchased"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getBlackGlass() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "Simple Hardcore Shop");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getGrayGlass() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "Simple Hardcore Shop");
        item.setItemMeta(meta);
        return item;
    }

}