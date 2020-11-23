package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.game.Position;



public class Box extends Decor implements Movable {

    private Position position;

    public String toString(){
        return "Box";
    }

    public boolean canMove(Direction direction){
        return true;
    }

    public void doMove(Direction direction){
        return;
    }
}