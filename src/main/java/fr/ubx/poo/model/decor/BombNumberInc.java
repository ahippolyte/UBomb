package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class BombNumberInc extends Bonus {

    public String toString(){ return "BombNumberInc"; }

    public void plyGetBonus(Player ply){
        ply.bombNumInc();
    }

}