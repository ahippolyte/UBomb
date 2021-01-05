package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class BombRangeInc extends Decor {

    public String toString(){ return "BombRangeInc"; }

    public boolean plyCanMoveOn() {
        return true;
    }

    public void plyGetBonus(Player ply){
        ply.bombRangeInc();
    }

}