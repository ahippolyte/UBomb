package fr.ubx.poo.model.go;

import fr.ubx.poo.game.*;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;

import static fr.ubx.poo.game.BombSteps.*;

public class Bomb extends GameObject {
    private BombSteps step;
    private long creationDate;
    public boolean[] destroyed = new boolean[2];

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
        boolean firstTime = true;
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
            destroyed[1] = true;
        }
    }

    public void Explode() {
        if (!destroyed[0]) {
            System.out.println("BOUUUM");

            if (game.getPlayer().getNumBomb() <= 0) {
                game.getPlayer().bombNumInc();
            }
            for (int i = -game.getPlayer().getRange(); i < game.getPlayer().getRange() + 1; i++) {
                Position xAxis = new Position(getPosition().x + i, getPosition().y);
                Position yAxis = new Position(getPosition().x, getPosition().y + i);

                if (game.getPlayer().getPosition().equals(xAxis) || game.getPlayer().getPosition().equals(yAxis)) {
                    game.getPlayer().livesNumDec();
                }
                Decor xDecor = game.getWorld().get(xAxis);
                Decor yDecor = game.getWorld().get(yAxis);

                if(!game.getWorld().isEmpty(xAxis)) {
                    if (xDecor.isBreakable()) {
                        game.getWorld().clear(xAxis);
                        game.getWorld().setChange(true);
                    }
                }

                if(!game.getWorld().isEmpty(yAxis)) {
                    if (yDecor.isBreakable()) {
                        game.getWorld().clear(xAxis);
                    }
                }
            }

            destroyed[0] = true;
        }
    }
}
