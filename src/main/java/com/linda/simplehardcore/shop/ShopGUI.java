package com.linda.simplehardcore.shop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopGUI implements Listener {

    private static final double DEFAULT_MAX_HEALTH = 20;
    private static final double DEFAULT_ATTACK_DAMAGE = 1;
    private static final double DEFAULT_ATTACK_SPEED = 4;
    private static final double DEFAULT_SPEED = 0.1;
    private static final double BUFF_MAX_HEALTH = 30; //+10HP
    private static final double BUFF_ATTACK_DAMAGE = 3.5; //+250%
    private static final double BUFF_ATTACK_SPEED = 4.6; //+100%
    private static final double BUFF_SPEED = 0.12; //+20%
    private Inventory gui;

    public void openNewGUI(Player player) {
        gui = Bukkit.createInventory(null, 27, "Perks");
        for (int i = 0; i < 18; i++) {
            gui.setItem(i, ShopItems.getGrayGlass());
        }
        for (int i = 18; i < 27; i++) {
            gui.setItem(i, ShopItems.getBlackGlass());
        }

        if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() == BUFF_MAX_HEALTH) {
            gui.setItem(1, ShopItems.purchasedExtraHealth());
            gui.setItem(10, ShopItems.getGreenGlass());
        } else {
            gui.setItem(1, ShopItems.extraHealth());
            gui.setItem(10, ShopItems.getRedGlass());
        }

        if (player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() == BUFF_SPEED) {
            gui.setItem(3, ShopItems.purchasedSpeedBoost());
            gui.setItem(12, ShopItems.getGreenGlass());
        } else {
            gui.setItem(3, ShopItems.speedBoost());
            gui.setItem(12, ShopItems.getRedGlass());
        }

        if (player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() == BUFF_ATTACK_DAMAGE) {
            gui.setItem(5, ShopItems.purchasedStrengthBoost());
            gui.setItem(14, ShopItems.getGreenGlass());
        } else {
            gui.setItem(5, ShopItems.strengthBoost());
            gui.setItem(14, ShopItems.getRedGlass());
        }

        if (player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() == BUFF_ATTACK_SPEED) {
            gui.setItem(7, ShopItems.purchasedAttackSpeed());
            gui.setItem(16, ShopItems.getGreenGlass());
        } else {
            gui.setItem(7, ShopItems.attackSpeed());
            gui.setItem(16, ShopItems.getRedGlass());
        }

        player.openInventory(gui);
    }

    @EventHandler
    public void guiClickEvent(InventoryClickEvent event) {
        if (!event.getInventory().equals(gui)) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        switch (event.getSlot()) {
            case 1 -> {
                if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() == BUFF_MAX_HEALTH) {
                    player.sendMessage(ChatColor.RED + "You already have this perk!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 0.5F);
                    return;
                }

                if (player.getLevel() < 55) {
                    player.sendMessage(ChatColor.RED + "You don't have enough experience to purchase this perk!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 0.5F);
                    return;
                }

                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(BUFF_MAX_HEALTH);
                player.setHealth(player.getHealth() + 10);
                player.giveExpLevels(-55);
                player.sendMessage(ChatColor.GREEN + "You have purchased " + ChatColor.translateAlternateColorCodes('&', "&d&lExtra Health")
                        + ChatColor.GREEN + " perk!");
                gui.setItem(1, ShopItems.purchasedExtraHealth());
                gui.setItem(10, ShopItems.getGreenGlass());
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);

                for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                    if (otherPlayer.equals(player)) continue;
                    otherPlayer.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.GREEN + " has just purchased "
                            + ChatColor.translateAlternateColorCodes('&', "&d&lExtra Health" + ChatColor.GREEN + " perk!"));
                    otherPlayer.playSound(otherPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                }
            }
            case 3 -> {
                if (player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() == BUFF_SPEED) {
                    player.sendMessage(ChatColor.RED + "You already have this perk!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 0.5F);
                    return;
                }

                if (player.getLevel() < 35) {
                    player.sendMessage(ChatColor.RED + "You don't have enough experience to purchase this perk!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 0.5F);
                    return;
                }

                player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(BUFF_SPEED);
                player.giveExpLevels(-35);
                player.sendMessage(ChatColor.GREEN + "You have purchased " + ChatColor.translateAlternateColorCodes('&', "&d&lSpeed Up")
                        + ChatColor.GREEN + " perk!");
                gui.setItem(3, ShopItems.purchasedSpeedBoost());
                gui.setItem(12, ShopItems.getGreenGlass());
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);

                for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                    if (otherPlayer.equals(player)) continue;
                    otherPlayer.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.GREEN + " has just purchased "
                            + ChatColor.translateAlternateColorCodes('&', "&d&lSpeed Up" + ChatColor.GREEN + " perk!"));
                    otherPlayer.playSound(otherPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                }
            }
            case 5 -> {
                if (player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() == BUFF_ATTACK_DAMAGE) {
                    player.sendMessage(ChatColor.RED + "You already have this perk!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 0.5F);
                    return;
                }

                if (player.getLevel() < 45) {
                    player.sendMessage(ChatColor.RED + "You don't have enough experience to purchase this perk!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 0.5F);
                    return;
                }

                player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(BUFF_ATTACK_DAMAGE);
                player.giveExpLevels(-45);
                player.sendMessage(ChatColor.GREEN + "You have purchased " + ChatColor.translateAlternateColorCodes('&', "&d&lStrength Buff")
                        + ChatColor.GREEN + " perk!");
                gui.setItem(5, ShopItems.purchasedStrengthBoost());
                gui.setItem(14, ShopItems.getGreenGlass());
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);

                for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                    if (otherPlayer.equals(player)) continue;
                    otherPlayer.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.GREEN + " has just purchased "
                            + ChatColor.translateAlternateColorCodes('&', "&d&lStrength Buff" + ChatColor.GREEN + " perk!"));
                    otherPlayer.playSound(otherPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                }
            }
            case 7 -> {
                if (player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() == BUFF_ATTACK_SPEED) {
                    player.sendMessage(ChatColor.RED + "You already have this perk!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 0.5F);
                    return;
                }

                if (player.getLevel() < 45) {
                    player.sendMessage(ChatColor.RED + "You don't have enough experience to purchase this perk!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 0.5F);
                    return;
                }

                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(BUFF_ATTACK_SPEED);
                player.giveExpLevels(-45);
                player.sendMessage(ChatColor.GREEN + "You have purchased " + ChatColor.translateAlternateColorCodes('&', "&d&lAttack speed Up")
                        + ChatColor.GREEN + " perk!");
                gui.setItem(7, ShopItems.purchasedAttackSpeed());
                gui.setItem(16, ShopItems.getGreenGlass());
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);

                for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                    if (otherPlayer.equals(player)) continue;
                    otherPlayer.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.GREEN + " has just purchased "
                            + ChatColor.translateAlternateColorCodes('&', "&d&lAttack speed Up" + ChatColor.GREEN + " perk!"));
                    otherPlayer.playSound(otherPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                }
            }
        }
    }

    public static void onDeath(Player player) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(DEFAULT_MAX_HEALTH);
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(DEFAULT_ATTACK_DAMAGE);
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(DEFAULT_ATTACK_SPEED);
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(DEFAULT_SPEED);
    }

    @EventHandler
    public void openGUI(EventGUI event) {
        openNewGUI(event.getPlayer());
    }

}
