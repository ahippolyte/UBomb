package fr.ubx.poo.model.go;

import fr.ubx.poo.game.*;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.view.image.ImageResource;

import java.util.Timer;
import java.util.TimerTask;

import static fr.ubx.poo.game.BombSteps.BombA;
import static fr.ubx.poo.game.BombSteps.BombB;
import static fr.ubx.poo.game.BombSteps.BombC;
import static fr.ubx.poo.game.BombSteps.BombD;

public class Bomb extends GameObject {
    private BombSteps step;

    public Bomb(Game game, Position position){
        super(game, position);
        this.step = BombA;
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

    public void Explode(){
        System.out.println("BOUUUM");
        if(game.getPlayer().getNumBomb() <= 0){
            game.getPlayer().bombNumInc();
        }

        for(int i=-1; i<1; i++) {
            Position aroundPos = new Position(getPosition().x, getPosition().y);
            Decor aroundDecor = game.getWorld().get(aroundPos);

            if (game.getPlayer().getPosition() == aroundPos) {
                game.getPlayer().livesNumDec();
            }
        }
    }
}
