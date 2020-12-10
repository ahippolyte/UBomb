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
    private int lives ;
    private int key ;
    private int bomb;
    private int range;
    private boolean winner;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
        this.key = game.getInitPlayerKey();
        this.bomb = game.getInitPlayerBomb();
        this.range = game.getInitPlayerRange();
    }

    //getters:
    public int getLives() {
        return lives;
    }
    public int getKey() { return key; }
    public int getNumBomb() {return bomb; }
    public int getRange() {return range; }

    public Direction getDirection() {
        return direction;
    }


    //setters:
    public void bombNumInc() {
        bomb++;
    }
    public void bombNumDec() {
        bomb--;
    }

    public void livesNumInc() {
        lives++;
    }
    public void livesNumDec() {
        lives--;
    }

    public void rangeInc() {
        range++;
    }
    public void rangeDec() {
        range--;
    }

    //methods:
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

        if(nextPos.inside(game.getWorld().dimension)) {
            if (game.getWorld().isEmpty(nextPos)) {
                return true;
            }
            if (nextDecor != null) {
                if (nextDecor.toString() == "Box") {
                    Position nextNextPos = direction.nextPosition(nextPos);
                    if (game.getWorld().isEmpty(nextNextPos) & nextNextPos.inside(game.getWorld().dimension)) {
                        return true;
                    }
                    return false;
                } else if (nextDecor.toString() == "Princess") {
                    return true;
                } else if (nextDecor.toString() == "Heart") {
                    return true;
                } else if (nextDecor.toString() == "BombNumberInc") {
                    return true;
                } else if (nextDecor.toString() == "BombNumberDec") {
                    return true;
                } else if (nextDecor.toString() == "BombRangeInc") {
                    return true;
                } else if (nextDecor.toString() == "BombRangeDec") {
                    return true;
                } else if (nextDecor.toString() == "Key") {
                    return true;
                } else if (nextDecor.toString() == "Door_closed") {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor nextDecor = game.getWorld().get(nextPos);

        if(nextDecor != null){
            if(nextDecor.toString() == "Box"){
                game.getWorld().clear(nextPos);
                Position nexNextPos = direction.nextPosition(nextPos);
                game.getWorld().set(nexNextPos, nextDecor);
                game.getWorld().setChange(true);
            }
            else if(nextDecor.toString() == "Princess"){
                winner=true;
            }
            else if(nextDecor.toString() == "Heart"){
                game.getWorld().clear(nextPos);
                game.getWorld().setChange(true);
                lives++;
            }
            else if(nextDecor.toString() == "BombNumberInc"){
                game.getWorld().clear(nextPos);
                game.getWorld().setChange(true);
                bomb++;
            }
            else if(nextDecor.toString() == "BombNumberDec"){
                game.getWorld().clear(nextPos);
                game.getWorld().setChange(true);
                if (bomb>1){
                    bomb--;
                }
            }
            else if(nextDecor.toString() == "BombRangeInc"){
                game.getWorld().clear(nextPos);
                game.getWorld().setChange(true);
                range++;
            }
            else if(nextDecor.toString() == "BombRangeDec"){
                game.getWorld().clear(nextPos);
                game.getWorld().setChange(true);
                if (range>1){
                    range--;
                }
            }
            else if(nextDecor.toString() == "Key"){
                game.getWorld().clear(nextPos);
                game.getWorld().setChange(true);
                key++;
            }
            else if(nextDecor.toString() == "Door_closed"){
                game.getWorld().clear(nextPos);
                game.getWorld().setChange(true);
            }
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
