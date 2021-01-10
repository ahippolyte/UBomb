package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Key extends Bonus {

    public String toString(){
        return "Key";
    }

    public void plyGetBonus(Player ply){
        if(ply.getKey() == 0){
            ply.keyNumInc();
        }
    }

    public boolean isBreakable(){
        return false;
    }

}