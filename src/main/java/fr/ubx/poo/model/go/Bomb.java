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

            //Bomb consumption
            if (game.getPlayer().getNumBomb() <= 0) {
                game.getPlayer().bombNumInc();
            }

            //player and decor damages
            Position bombPos = getPosition();
            damageAtPos(bombPos);

            //top
            int i = 1;
            Position topIterPos = new Position(bombPos.x, bombPos.y-i);
            while(i<game.getPlayer().getRange() && game.getWorld().isEmpty(topIterPos)){
                i++;
                topIterPos = new Position(bombPos.x, bombPos.y-i);
            }
            damageAtPos(topIterPos);
            spriteRange[0] = i;

            //bottom
            int j = 1;
            Position bottomIterPos = new Position(bombPos.x, bombPos.y+j);
            while(j<game.getPlayer().getRange() && game.getWorld().isEmpty(bottomIterPos)){
                j++;
                bottomIterPos = new Position(bombPos.x, bombPos.y+j);
            }
            damageAtPos(bottomIterPos);
            spriteRange[1] = j;

            //left
            int k = 1;
            Position leftIterPos = new Position(bombPos.x-k, bombPos.y);
            while(k<game.getPlayer().getRange() && game.getWorld().isEmpty(leftIterPos)){
                k++;
                leftIterPos = new Position(bombPos.x-k, bombPos.y);
            }
            damageAtPos(leftIterPos);
            spriteRange[2] = k;

            //right
            int l = 1;
            Position rightIterPos = new Position(bombPos.x+l, bombPos.y);
            while(l<game.getPlayer().getRange() && game.getWorld().isEmpty(rightIterPos)){
                l++;
                rightIterPos = new Position(bombPos.x+l, bombPos.y);
            }
            damageAtPos(rightIterPos);
            spriteRange[3] = l;

            System.out.println("Portée haut : " + i);
            System.out.println("Portée bas : " + j);
            System.out.println("Portée gauche : " + k);
            System.out.println("Portée droite : " + l);

            destroyed[0] = true;
        }
    }

    public void damageAtPos(Position pos){
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