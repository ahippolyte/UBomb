package fr.ubx.poo.model.go;

import fr.ubx.poo.game.*;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.decor.Decor;

import static fr.ubx.poo.game.BombSteps.*;

public class Bomb extends GameObject {
    private BombSteps step;
    private long creationDate;
    public boolean[] destroyed = new boolean[2];
    public int[] spriteRange = new int[4];

    public Bomb(Game game, Position position, long date){
        super(game, position);
        this.step = BombA;
        this.creationDate = date;
        this.destroyed[0] = false;
        this.destroyed[1] = false;
    }

    public String toString(){
        return "Bomb";
    }

    public BombSteps getBombStep() {
        return step;
    }

    public void setBombStep(BombSteps step) {
        this.step = step;
    }

    public void update(long now){
        if(now-creationDate >= 1000000000L) {
            setBombStep(BombB);
        }
        if(now-creationDate >= 2000000000L) {
            setBombStep(BombC);
        }
        if(now-creationDate >= 3000000000L) {
            setBombStep(BombD);
        }
        if(now-creationDate >= 4000000000L) {
            setBombStep(Explosion);
            Explode();
        }
        if(now-creationDate >= 4500000000L) {
            destroyed[1] = true; //bomb can be deleted from list and free from game
        }
    }

    public void Explode() {
        if (!destroyed[0]) {

            //Bomb consumption
            if (game.getPlayer().getNumBomb() <= 0) {
                game.getPlayer().bombNumInc();
            }

            //damage at bombPos
            Position bombPos = getPosition();
            damageDecorAtPos(bombPos);

            //damage decor around and store ranges for sprites (used in GameEngine)
            spriteRange[0] = destroyInDirection(Direction.N);
            spriteRange[1] = destroyInDirection(Direction.S);
            spriteRange[2] = destroyInDirection(Direction.W);
            spriteRange[3] = destroyInDirection(Direction.E);

            destroyed[0] = true; //bomb has exploded, ready to display corresponding sprites
        }
    }

    public int destroyInDirection(Direction dir){
        Position bombPos = getPosition();
        Position newPos = dir.nextPosition(bombPos);
        int i=1;
        while(i<game.getPlayer().getRange() && game.getWorld().isEmpty(newPos)){
            i++;
            if (game.getPlayer().getPosition().equals(newPos)) {
                game.getPlayer().livesNumDec();
            }
            newPos = dir.nextPosition(newPos);
        }
        damageDecorAtPos(newPos);
        return i;
    }

    public void damageDecorAtPos(Position pos){
        if(!game.getWorld().isEmpty(pos)){
            Decor decor = game.getWorld().get(pos);
            if(decor.isBreakable()){
                game.getWorld().clear(pos);
                game.getWorld().setChange(true);
            }
        }
        if (game.getPlayer().getPosition().equals(pos)) {
            game.getPlayer().livesNumDec();
        }
    }

}