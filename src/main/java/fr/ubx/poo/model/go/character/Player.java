/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.*;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;

public class Player extends GameObject implements Movable {

    private final boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private boolean winner;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
    }

    public int getLives() {
        return lives;
    }

    public Direction getDirection() {
        return direction;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor nextDecor = game.getWorld().get(nextPos);
        Position nextNextPos = direction.nextPosition(nextPos);

        if(nextPos.inside(game.getWorld().dimension)){
            if(game.getWorld().isEmpty(nextPos))
                return true;
            else if(nextDecor instanceof Box){
                if(game.getWorld().isEmpty(nextNextPos) & nextNextPos.inside(game.getWorld().dimension)){
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor nextDecor = game.getWorld().get(nextPos);
        Position nexNextPos = direction.nextPosition(nextPos);

        if(nextDecor instanceof Box){
            game.getWorld().clear(nextPos);
            game.getWorld().set(nexNextPos, nextDecor);
            game.getWorld().setChange(true);
        }

        setPosition(nextPos);
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        return alive;
    }

}
