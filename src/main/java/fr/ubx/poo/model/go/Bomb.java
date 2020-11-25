package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.Entity;

import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends GameObject {

    public Bomb(Game game, Position position){
        super(game, position);
        Explode();
    }

    public String toString(){
        return "Bomb";
    }

    public void Explode(){
        TimerTask task = new TimerTask() {
            public void run() {
                System.out.println("BOUUUM");
            }
        };

        Timer timer = new Timer("Bomb Timer");
        long delay = 4000L;
        timer.schedule(task, delay);
    }
}
