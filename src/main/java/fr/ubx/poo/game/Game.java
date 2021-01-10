/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;

public class Game {

    private final World world; //Pour plusieurs mondes, implémenter un tableau de World
    private final Player player;
    private List<Monster> monsterList = new LinkedList<>();
    public List<Bomb> bombList = new LinkedList<>();
    private final String worldPath;
    public int initPlayerLives;
    public int initPlayerKey;
    public int initPlayerBomb;
    public int initPlayerRange;

    public Game(String worldPath) {
        world = new WorldStatic();
        this.worldPath = worldPath;
        loadConfig(worldPath);
        Position positionPlayer = null;
        try {
            positionPlayer = world.findPlayer();
            player = new Player(this, positionPlayer);
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        for (Position monsterPos : getWorld().findMonsters()) {
            monsterList.add(new Monster(this, monsterPos));
        }
    }

    //getters:
    public int getInitPlayerLives() {
        return initPlayerLives;
    }
    public int getInitPlayerKey() {
        return initPlayerKey;
    }
    public int getInitPlayerBomb() { return initPlayerBomb; }
    public int getInitPlayerRange() { return initPlayerRange; }

    public World getWorld() {
        return world;
    }
    public Player getPlayer() {
        return this.player;
    }
    public List<Monster> getMonsterList(){
        return monsterList;
    }
    public List<Bomb> getBombList(){
        return bombList;
    }

    //methods:
    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));
            initPlayerKey = Integer.parseInt(prop.getProperty("key", "0"));
            initPlayerBomb = Integer.parseInt(prop.getProperty("bomb", "1"));
            initPlayerRange = Integer.parseInt(prop.getProperty("range", "1"));
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    /**private void loadWorld(String path) {
        try (InputStream input = new FileInputStream(new File(path, "level1.txt"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));
            initPlayerKey = Integer.parseInt(prop.getProperty("key", "0"));
            initPlayerBomb = Integer.parseInt(prop.getProperty("bomb", "1"));
            initPlayerRange = Integer.parseInt(prop.getProperty("range", "1"));
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }**/

    public boolean monsterAtPos(Position position){
        for(Monster monster: monsterList){
            if(monster.getPosition().equals(position)){
                return true;
            }
        }
        return false;
    }

    public boolean bombAtPos(Position position){
        for(Bomb bomb: bombList){
            if(bomb.getPosition().equals(position)){
                return true;
            }
        }
        return false;
    }


}
