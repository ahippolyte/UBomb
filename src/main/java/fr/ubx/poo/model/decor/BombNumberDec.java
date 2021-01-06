package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class BombNumberDec extends Decor {

    public String toString(){
        return "BombNumberDec";
    }

    public boolean plyCanMoveOn() {
        return true;
    }

    public void plyGetBonus(Player ply){
        if(ply.getNumBomb() > 1){
            ply.bombNumDec();
        }
    }

    public boolean isBreakable(){
        return true;
    }

}