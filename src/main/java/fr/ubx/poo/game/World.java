/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;

import java.util.*;
import java.util.function.BiConsumer;

public class World {
    private final Map<Position, Decor> grid;
    private final WorldEntity[][] raw;
    public final Dimension dimension;
    public boolean needDecorRefresh = false;

    public World(WorldEntity[][] raw) {
        this.raw = raw;
        dimension = new Dimension(raw.length, raw[0].length);
        grid = WorldBuilder.build(raw, dimension);
    }

    public Position findPlayer() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Player) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Player");
    }

    public List<Position> findMonsters(){
        List<Position> monsterPositions = new LinkedList<>();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Monster) {
                    monsterPositions.add(new Position(x,y));
                }
            }
        }
        return monsterPositions;
    }

    public Decor get(Position position) {
        return grid.get(position);
    }

    public boolean bombAtPos(Position position){
        if(raw[position.x][position.y] == WorldEntity.Bomb){
            return true;
        }
        return false;
    }

    public void set(Position position, Decor decor) {
        grid.put(position, decor);
    }

    public void clear(Position position) {
        grid.remove(position);
    }

    public void forEach(BiConsumer<Position, Decor> fn) {
        grid.forEach(fn);
    }

    public Collection<Decor> values() {
        return grid.values();
    }

    public boolean isInside(Position position) {
        if(position.x >=0 && position.x < dimension.width){
            if(position.y >=0 && position.y < dimension.height){
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty(Position position) {
        return grid.get(position) == null;
    }
}
