package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class BombRangeDec extends Decor {

    public String toString(){ return "BombRangeDec"; }

    public boolean plyCanMoveOn() {
        return true;
    }

    public void plyGetBonus(Player ply){
        if(ply.getRange() > 1){
            ply.bombRangeDec();
        }
    }

    public boolean isBreakable(){
        return true;
    }

}