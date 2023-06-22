package com.linda.simplehardcore.deathmanager;

import com.linda.simplehardcore.PlayerManager;
import com.linda.simplehardcore.shop.ShopGUI;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DeathMessage implements Listener {

    Plugin plugin;

    public DeathMessage(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerGetDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player)event.getEntity();
        if (player.getGameMode() == GameMode.SPECTATOR) {
            event.setCancelled(true);
            return;
        }

        if (player.getHealth() - event.getFinalDamage() <= 0.0D) {
            ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING);
            if (player.getInventory().getItemInMainHand().equals(item) || player.getInventory().getItemInOffHand().equals(item)) {
                event.setCancelled(true);
                new PlayerDeathManager(player).totemOfUndying();
                return;
            }
            try{
                new PlayerDeathManager(player).onDeath();
            } catch (Exception ignored) {}

            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                try{
                    new PlayerDeathManager(player).onRespawn();
                } catch (Exception ignored) {}
            } , 200L);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerGetExp(PlayerExpChangeEvent event){
        int eventLvl = event.getAmount() / 100;
        float eventExp = (((float)event.getAmount()) % 100) / 100;

        int playerLvl = event.getPlayer().getLevel();
        float playerExp = event.getPlayer().getExp();

        float expToSet = (eventExp + playerExp) % 1;
        int lvlToSet = eventLvl + playerLvl + (int)(eventExp + playerExp);

        event.getPlayer().setLevel(lvlToSet);
        event.getPlayer().setExp(expToSet);

        event.setAmount(0);
    }
}

class PlayerDeathManager {
    private static final int PLAYER_MAX_FOOD_LEVEL = 20;
    private static final float PLAYER_MAX_SATURATION = 5;

    Player player;

    PlayerDeathManager(Player player) {
        this.player = player;
    }

    public void totemOfUndying() {
        ItemStack item  = new ItemStack(Material.TOTEM_OF_UNDYING);
        if (player.getInventory().getItemInMainHand().equals(item)) {
            player.getInventory().setItemInMainHand(null);
        } else if (player.getInventory().getItemInOffHand().equals(item)) {
            player.getInventory().setItemInOffHand(null);
        }
        Set<PotionEffect> effects = new HashSet<>();
        effects.add(new PotionEffect(PotionEffectType.ABSORPTION, 100, 1));
        effects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 0));
        effects.add(new PotionEffect(PotionEffectType.REGENERATION, 900, 1));
        player.setHealth(0.5);
        player.addPotionEffects(effects);
        player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
        awardTotemOfUndyingAdvancement(player);
        player.sendTitle(ChatColor.GREEN + "Saved!", "",0, 20, 10);
    }

    public void onDeath() {
        PlayerManager.revokeLive(player);
        PlayerManager.savePlayerData();

        //Reset perks
        ShopGUI.onDeath(player);

        //Lightning
        player.getWorld().strikeLightningEffect(player.getLocation());

        //Drop items
        ItemStack[] isTemp = getDropItems(player);
        dropItems(player, isTemp);

        //Drop skull on final death
        if (PlayerManager.getLives(player) == 0) {
            player.getWorld().dropItem(player.getLocation(), getPlayerSkull());
        }

        //Spawn experience orb
        ExperienceOrb expOrb = (ExperienceOrb) player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
        int exp = player.getLevel() * 100;
        exp += (int)(player.getExp() * 100);
        expOrb.setExperience(exp);

        // Set player lvl at 0
        player.setTotalExperience(0);
        player.setLevel(0);
        player.setExp(0);

        //Restore health and food
        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue());
        player.setFoodLevel(PLAYER_MAX_FOOD_LEVEL);
        player.setSaturation(PLAYER_MAX_SATURATION);

        //Clear all effects
        clearEffects();

        //Send player into spectator mode
        player.setGameMode(GameMode.SPECTATOR);

        // Send notification about player death
        sendDeathNotification();
    }
    
    public void onRespawn() {
        if (!player.isOnline()) return;
        if (PlayerManager.getLives(player) <= 0) return;
        //Get player spawn location
        Location bedLocation = player.getBedSpawnLocation();
        Location spawnLocation = player.getServer().getWorlds().get(0).getSpawnLocation();
        Location respawnLocation = (bedLocation != null)?bedLocation:spawnLocation;

        //Respawn player
        player.teleport(respawnLocation);
        player.setGameMode(GameMode.SURVIVAL);
    }
    private void clearEffects() {
        //Extinguish a fire on player
        player.setFireTicks(0);

        // Remove potion effects, including wither, poison, etc.
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
    }
    private ItemStack getPlayerSkull() {
        SkullMeta skullMeta = (SkullMeta)Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
        skullMeta.setOwnerProfile(player.getPlayerProfile());
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        skull.setItemMeta(skullMeta);
        return skull;
    }

    public void sendDeathNotification() {
        //Death notification for players
        int playerLives = PlayerManager.getLives(player);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.equals(this.player)) {
                if (playerLives > 0) {
                    player.sendTitle(ChatColor.RED + "YOU DIED!", ChatColor.RED + String.valueOf(playerLives) + ((playerLives > 1) ? " lives" : " live") + " remaining!", 5, 100, 20);
                } else {
                    player.sendTitle(ChatColor.RED + "YOU DIED!", ChatColor.RED + "You have no more lives!", 5, 100, 20);
                }
            } else {
                switch (PlayerManager.getLives(this.player)) {
                    case 1 -> player.sendTitle(ChatColor.GOLD + this.player.getPlayerListName() + ChatColor.RED + " died!",
                            ChatColor.YELLOW + "They have " + ChatColor.RED + "1" + ChatColor.YELLOW + " more live",
                            5, 100, 20);

                    case 0 -> player.sendTitle(ChatColor.GOLD + this.player.getPlayerListName() + ChatColor.RED + " died!",
                            ChatColor.YELLOW + "They died " + ChatColor.RED + "forever",
                            5, 100, 20);

                    default -> player.sendTitle(ChatColor.GOLD + this.player.getPlayerListName() + ChatColor.RED + " died!",
                            ChatColor.YELLOW + "They have " + ChatColor.RED + PlayerManager.getLives(this.player) + ChatColor.YELLOW + " more lives",
                            5, 100, 20);
                }
            }
            if (playerLives > 0) {
                player.playSound(player, Sound.ENTITY_WITHER_SPAWN, 1, 1);
            } else {
                player.playSound(player, Sound.ENTITY_WITHER_DEATH, 1, 1);
            }
        }
        System.out.println();
        System.out.println("==================================>>>");
        System.out.println(this.player.getName() + " DIED! " + "THEY HAVE " + playerLives + " LIVE(S).");
        System.out.println("==================================>>>");
        System.out.println();
    }

    private ItemStack[] getDropItems(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        player.getInventory().clear();
        return contents;
    }

    private void dropItems(Player player, ItemStack[] items) {
        for (ItemStack is : items) {
            if (is != null) player.getWorld().dropItem(player.getLocation(), is);
        }
    }

    private void awardTotemOfUndyingAdvancement(Player player){
        Advancement target = Bukkit.getAdvancement(NamespacedKey.fromString("adventure/totem_of_undying"));
        AdvancementProgress progress = player.getAdvancementProgress(target);
        if(progress.isDone()) return;
        for(String criteria:  target.getCriteria()) {
            progress.awardCriteria(criteria);
        }
    }

}