package com.linda.simplehardcore.entity;

import org.bukkit.entity.Player;

import java.util.UUID;

public class SHPlayer {

    private Player player;
    private int lives;

    public void setLives(int lives) {this.lives = lives;}
    public Player getPlayer() {
        return player;
    }
    public int getLives() {return lives;}
    public String getName() {return player.getName();}
    public UUID getUuid() {return player.getUniqueId();}

    public SHPlayer(Player player, int lives){
        this.player = player;
        this.lives = lives;
    }

    @Override
    public boolean equals(Object obj) {
        if(player.equals(obj)){
            return true;
        }
        /*if((obj instanceof Player) &&
                (((Player) obj).getUniqueId() == player.getUniqueId())){
            return true;
        }*/
        return false;
    }
    public void revokeLive(){
        lives = Math.max(lives - 1, 0);
    }
}
