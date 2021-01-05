package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Heart extends Decor {

    public String toString(){
        return "Heart";
    }

    public boolean plyCanMoveOn() {
        return true;
    }

    public void plyGetBonus(Player ply){
        ply.livesNumInc();
    }

}