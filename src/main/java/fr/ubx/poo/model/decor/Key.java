package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Key extends Decor {

    public String toString(){
        return "Key";
    }

    public boolean plyCanMoveOn() {
        return true;
    }

    public void plyGetBonus(Player ply){
        if(ply.getKey() == 0){
            ply.keyNumInc();
        }
    }

}