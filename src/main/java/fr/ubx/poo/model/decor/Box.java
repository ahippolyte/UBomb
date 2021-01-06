package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;

public class Box extends Decor {

    public String toString(){
        return "Box";
    }

    public boolean plyCanMoveOn() {
        return true;
    }

}