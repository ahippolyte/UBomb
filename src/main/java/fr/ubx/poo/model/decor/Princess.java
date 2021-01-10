package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Princess extends Bonus {

    public String toString(){
        return "Princess";
    }

    public void plyGetBonus(Player ply){
        if(!ply.isWinner()){
            ply.setWinner();
        }
    }

    public boolean isBreakable(){
        return false;
    }

}