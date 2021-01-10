package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class BombRangeInc extends Bonus {

    public String toString(){ return "BombRangeInc"; }

    public void plyGetBonus(Player ply){
        ply.bombRangeInc();
    }
}