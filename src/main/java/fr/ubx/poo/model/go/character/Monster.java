package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;

public class Monster extends GameObject implements Movable {
    private boolean alive = true;
    Direction direction;

    public Monster(Game game, Position position) {
        super(game, position);
        this.direction = Direction.random();
    }

    //getters
    public Direction getDirection() { return direction; }

    //setters

    public boolean canMove(Direction direction) {
        return false;
    }

    public void doMove(Direction direction) {
    }

    public void update(long now) {
        if (canMove(direction)) {
            doMove(direction);
        }
    }
}
