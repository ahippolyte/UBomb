package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Heart extends Bonus {

    public String toString(){
        return "Heart";
    }

    public void plyGetBonus(Player ply){
        ply.livesNumInc();
    }

}