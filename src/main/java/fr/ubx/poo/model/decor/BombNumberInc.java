package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class BombNumberInc extends Decor {

    public String toString(){ return "BombNumberInc"; }

    public boolean plyCanMoveOn() {
        return true;
    }

    public void plyGetBonus(Player ply){
        ply.bombNumInc();
    }

    public boolean isBreakable(){
        return true;
    }

}