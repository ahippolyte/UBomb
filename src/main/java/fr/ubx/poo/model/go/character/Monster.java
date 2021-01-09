package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;

public class Monster extends GameObject implements Movable {
    private boolean alive;
    Direction direction;
    public boolean moveRequested = false;
    public boolean moveEnd = true;
    public long delay;

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
        Position nextPos = direction.nextPosition(getPosition());
        //Decor nextDecor = game.getWorld().get(nextPos);

        if (nextPos.inside(game.getWorld().dimension)) {
            if (game.getWorld().isEmpty(nextPos)) {
                return true;
            }
            return false;
        }
        return false;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
        if(getPosition().equals(game.getPlayer().getPosition())){
            game.getPlayer().livesNumDec();
        }
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

        monsterMoveDelay(now);
    }

    public void monsterMoveDelay(long now){
        if(!moveRequested) {
            if (moveEnd) {
                delay = now;
                moveEnd = false;
            }
            if(now - delay >= 1500000000L){
                moveRequested = true;
                direction = Direction.random();
                moveEnd = true;
            }
        }
    }
}
