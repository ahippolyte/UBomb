package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Door extends Decor{
    public boolean opened = false;

    static public boolean isOpened(Player player){
        if (player.getKey()==1){
            return true;
        }
        return false;
    }

    public String toString() {
        return "Door";
    }

    public boolean plyCanMoveOn() {
        if (!opened){
            return false;
        }
        return true;
    }
}

