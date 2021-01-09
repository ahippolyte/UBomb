package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;

public class Monster extends GameObject implements Movable {
    private boolean alive;
    Direction direction;
    public boolean moveRequested = false;

    public Monster(Game game, Position position) {
        super(game, position);
        this.direction = Direction.random();
        alive = true;
    }

    public String toString(){
        return "Monster "+getPosition()+"";
    }

    //getters
    public Direction getDirection() { return direction; }
    public boolean isAlive(){
        return alive;
    }

    //setters

    public boolean canMove(Direction direction) {
        return false;
    }

    public void doMove(Direction direction) {
    }
    public void setAlive(boolean bool){
        alive = bool;
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }
}
