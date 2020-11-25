/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.*;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.sprite.SpriteDecor;
import javafx.scene.layout.Pane;

import static fr.ubx.poo.view.image.ImageResource.Door_opened;

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
    public int getBomb() {return bomb; }
    public int getRange() {return range; }

    public Direction getDirection() {
        return direction;
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
            if(nextDecor instanceof Door_closed){
                if (key>0){
                    return true;
                }
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
            bomb++;
        }
        if(nextDecor instanceof BombNumberDec){
            game.getWorld().clear(nextPos);
            game.getWorld().setChange(true);
            if (bomb>1){
                bomb--;
            }
        }
        if(nextDecor instanceof BombRangeInc){
            game.getWorld().clear(nextPos);
            game.getWorld().setChange(true);
            range++;
        }
        if(nextDecor instanceof BombRangeDec){
            game.getWorld().clear(nextPos);
            game.getWorld().setChange(true);
            if (range>1){
                range--;
            }
        }
        if(nextDecor instanceof Key){
            game.getWorld().clear(nextPos);
            game.getWorld().setChange(true);
            /*game.getWorld().clear(nextPos);
            game.getWorld().set(nextPos,game.getWorld().get(Door_opened));*/
            key++;

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
