package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class BombNumberDec extends Bonus {

    public String toString(){
        return "BombNumberDec";
    }

    public void plyGetBonus(Player ply){
        if(ply.getNumBomb() > 1){
            ply.bombNumDec();
        }
    }
}