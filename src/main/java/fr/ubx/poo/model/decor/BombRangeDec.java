package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class BombRangeDec extends Bonus {

    public String toString(){ return "BombRangeDec"; }

    public void plyGetBonus(Player ply){
        if(ply.getRange() > 1){
            ply.bombRangeDec();
        }
    }
}