package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Princess extends Decor {

    public String toString(){
        return "Princess";
    }

    public boolean plyCanMoveOn() {
        return true;
    }

    public void plyGetBonus(Player ply){
        if(!ply.isWinner()){
            ply.setWinner();
        }
    }

}