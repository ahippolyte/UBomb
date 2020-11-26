/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.sprite.Sprite;

public class Game {

    private final World world; //Pour plusieurs mondes, implémenter un tableau de World
    private final Player player;
    private final String worldPath;
    public int initPlayerLives;
    public int initPlayerKey;
    public int initPlayerBomb;
    public int initPlayerRange;
    public List<Bomb> bombList = new ArrayList<>();
    private boolean bombSpriteChange = false;

    public Game(String worldPath) {
        world = new WorldStatic();
        this.worldPath = worldPath;
        loadConfig(worldPath);
        Position positionPlayer = null;
        initPlayerLives = 3;
        initPlayerKey = 0;
        initPlayerBomb = 1;
        initPlayerRange = 1;
        try {
            positionPlayer = world.findPlayer();
            player = new Player(this, positionPlayer);
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
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
    public boolean bombSpriteHasChanged(){ return bombSpriteChange; }

    public World getWorld() {
        return world;
    }
    public Player getPlayer() {
        return this.player;
    }

    //setters
    public void bombSpriteSetChanged(boolean bool){ bombSpriteChange = bool; }

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


}
