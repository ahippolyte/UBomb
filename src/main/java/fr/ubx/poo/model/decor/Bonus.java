package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public abstract class Bonus extends Decor{

    public String toString(){
        return "Bonus";
    }

    public boolean plyCanMoveOn() {
        return true;
    }

    public abstract void plyGetBonus(Player ply);

    public boolean isBreakable(){
        return true;
    }
}
