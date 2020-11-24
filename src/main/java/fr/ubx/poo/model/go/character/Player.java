/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.*;
import fr.ubx.poo.model.decor.*;
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

        if(nextPos.inside(game.getWorld().dimension)) {
            if (game.getWorld().isEmpty(nextPos)){
                return true;
            }
            if(nextDecor instanceof Princess){
                return true;
            }
            if(nextDecor instanceof Heart){
                return true;
           }
            if(nextDecor instanceof BombNumberInc){
                return true;
            }
            if(nextDecor instanceof BombNumberDec){
                return true;
            }
            if(nextDecor instanceof BombRangeInc){
                return true;
            }
            if(nextDecor instanceof BombRangeDec){
                return true;
            }
            if(nextDecor instanceof Key){
                return true;
            }


            if(nextDecor instanceof Box){
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

        if(nextDecor instanceof Princess){
            winner=true;
        }
        if(nextDecor instanceof Heart){
            game.getWorld().clear(nextPos);
            game.getWorld().setChange(true);
            lives++;
        }
        if(nextDecor instanceof BombNumberInc){
            game.getWorld().clear(nextPos);
            game.getWorld().setChange(true);
        }
        if(nextDecor instanceof BombNumberDec){
            game.getWorld().clear(nextPos);
            game.getWorld().setChange(true);
        }
        if(nextDecor instanceof BombRangeInc){
            game.getWorld().clear(nextPos);
            game.getWorld().setChange(true);
        }
        if(nextDecor instanceof BombRangeDec){
            game.getWorld().clear(nextPos);
            game.getWorld().setChange(true);
        }
        if(nextDecor instanceof Key){
            game.getWorld().clear(nextPos);
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
