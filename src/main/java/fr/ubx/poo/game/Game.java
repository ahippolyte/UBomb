/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.*;
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
    public String prefix;
    public int numLevels;

    public Game(String worldPath) {
        this.worldPath = worldPath;
        loadConfig(worldPath);
        world = new WorldStatic();
        loadLevel(worldPath, 2);
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

    public int getInitPlayerBomb() {
        return initPlayerBomb;
    }

    public int getInitPlayerRange() {
        return initPlayerRange;
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return this.player;
    }

    public List<Monster> getMonsterList() {
        return monsterList;
    }

    public List<Bomb> getBombList() {
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
            prefix = prop.getProperty("prefix", "level");
            numLevels = Integer.parseInt(prop.getProperty("levels", "3"));
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    private int[] getLevelDimension(String path, int N) {

        int[] dimensions = new int[2];

        String fileName = prefix+N+".txt";

        try {
            InputStream input = new FileInputStream(new File(path, fileName));

            int width = -1;
            while ((input.read()) != '\n') {
                width++;
            }
            int totalNbChar = 0;
            while ((input.read()) != -1) {
                totalNbChar++;
            }

            int height = totalNbChar/width+1;

            dimensions[0] = width;
            dimensions[1] = height;
        }
        catch (FileNotFoundException ex) {
            System.err.println("File not found");
        }
        catch (IOException e) {
            System.err.println("Error loading level");
        }

        return dimensions;
    }

    private void loadLevel(String path, int N) {

        int[] dimensions = getLevelDimension(path, N);

        String fileName = prefix+N+".txt";

        try {
            InputStream input = new FileInputStream(new File(path, fileName));

            WorldEntity[][] level = new WorldEntity[dimensions[0]][dimensions[1]];

            BufferedReader br = new BufferedReader(new FileReader(new File(path, fileName)));

            try {
                int y = 0;
                String line;
                while ((line = br.readLine()) != null) {
                    for (int x = 0; x < dimensions[0]; x++) {
                        int content = input.read();
                        char c = (char) content;
                        if (WorldEntity.fromCode(c).isPresent()) {
                            level[x][y] = WorldEntity.fromCode(c).get();
                        }
                        else{
                            level[x][y] = WorldEntity.Empty;
                        }
                    }
                    y++;
                }
            } finally {
                br.close();
            }

            //display tab
            for(int y=0; y<dimensions[1]; y++){
                for(int x=0; x<dimensions[0]; x++){
                    System.out.print(level[x][y]);
                }
                System.out.println();
            }
        }
        catch (FileNotFoundException ex) {
            System.err.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean monsterAtPos(Position position){
        for(Monster monster: monsterList){
            if(monster.getPosition().equals(position)){
                return true;
            }
        }
        return false;
    }

}
