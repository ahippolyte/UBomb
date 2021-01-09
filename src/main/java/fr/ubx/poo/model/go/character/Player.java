/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.*;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;

public class Player extends GameObject implements Movable {
    private boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives ;
    private int key ;
    private int bomb;
    private int range;
    private boolean winner;
    private boolean god = false;
    private boolean godEnd = true;
    public long delay;


    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
        this.key = game.getInitPlayerKey();
        this.bomb = game.getInitPlayerBomb();
        this.range = game.getInitPlayerRange();
    }

    //getters:
    public int getNumBomb() {return bomb; }
    public int getLives() { return lives; }
    public int getRange() {return range; }
    public int getKey() { return key; }
    public boolean isWinner() { return winner; }
    public boolean isAlive() { return alive; }
    public Direction getDirection() { return direction; }
    public boolean isGod(){
        return god;
    }

    //setters:
    public void bombNumInc() { bomb++; }
    public void bombNumDec() { bomb--; }
    public void livesNumInc() { lives++; }
    public void livesNumDec() {
        if(!god) {
            lives--;
            god = true;
        }
    }
    public void bombRangeInc() { range++; }
    public void bombRangeDec() { range--; }
    public void keyNumInc(){ key++; }
    public void setWinner(){ winner = true; }
    public void setAlive(boolean bool) {alive = bool;}
    public void setGod(boolean bool){
        god = bool;
    }

    //methods:
    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor nextDecor = game.getWorld().get(nextPos);

        if (nextPos.inside(game.getWorld().dimension)) {
            if (game.getWorld().isEmpty(nextPos) || nextDecor.plyCanMoveOn()) {
                return true;
            }
            return false;
        }
        return false;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor nextDecor = game.getWorld().get(nextPos);
        if (game.getWorld().isEmpty(nextPos)) {
            setPosition(nextPos);
        }
        else if (nextDecor.toString() == "Box") {
            Position nextNextPos = direction.nextPosition(nextPos);
            if (game.getWorld().isEmpty(nextNextPos) & nextNextPos.inside(game.getWorld().dimension) & !game.monsterAtPos(nextNextPos)) {
                setPosition(nextPos);
                game.getWorld().clear(nextPos);
                game.getWorld().set(nextNextPos, nextDecor);
                game.getWorld().needDecorRefresh = true;
            }
        }
        else {
            setPosition(nextPos);
            game.getWorld().clear(nextPos);
            nextDecor.plyGetBonus(game.getPlayer());
            game.getWorld().needDecorRefresh = true;
        }

        for(Monster monster: game.getMonsterList()){
            if(getPosition().equals(monster.getPosition())){
                livesNumDec();
            }
        }
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;

        if(getLives() <= 0){
            setAlive(false);
        }

        GodMode(now);
    }

    public void GodMode(long now){
        if(god){
            if (godEnd) {
                System.out.println("God for 2 seconds");
                delay = now;
                godEnd = false;
            }
            if (now - delay >= 2000000000L) {
                god = false;
                godEnd = true;
                System.out.println("Not god anymore");
            }
        }
    }

}