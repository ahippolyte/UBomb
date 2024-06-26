/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.decor.Door;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.*;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;


public final class SpriteFactory {

    public static SpriteDecor createDecor(Pane layer, Position position, Decor decor) {
        ImageFactory factory = ImageFactory.getInstance();
        if (decor instanceof Stone)
            return new SpriteDecor(layer, factory.get(STONE), position);
        if (decor instanceof Tree)
            return new SpriteDecor(layer, factory.get(TREE), position);
        if (decor instanceof Box)
            return new SpriteDecor(layer, factory.get(BOX), position);
        if (decor instanceof Princess)
            return new SpriteDecor(layer, factory.get(PRINCESS), position);
        if (decor instanceof Heart)
            return new SpriteDecor(layer, factory.get(Heart), position);
        if (decor instanceof BombNumberDec)
            return new SpriteDecor(layer, factory.get(BombNumberDec), position);
        if (decor instanceof BombNumberInc)
            return new SpriteDecor(layer, factory.get(BombNumberInc), position);
        if (decor instanceof BombRangeInc)
            return new SpriteDecor(layer, factory.get(BombRangeInc), position);
        if (decor instanceof BombRangeDec)
            return new SpriteDecor(layer, factory.get(BombRangeDec), position);
        if (decor instanceof Key)
            return new SpriteDecor(layer, factory.get(Key), position);
        if (decor instanceof Door)
            return new SpriteDecor(layer, factory.get(Door_closed), position);
        //if (decor instanceof Door_opened)
        //    return new SpriteDecor(layer, factory.get(Door_opened), position);


        throw new RuntimeException("Unsupported sprite for decor " + decor);
    }

    public static SpritePlayer createPlayer(Pane layer, Player player) {
        return new SpritePlayer(layer, player);
    }


    public static SpriteBomb createBomb(Pane layer, Bomb bomb){
        return new SpriteBomb(layer, bomb);
    }

    public static SpriteExplosion createExplosion(Pane layer, Position position, Bomb bomb){
        return new SpriteExplosion(layer, position, bomb);
    }

    public static SpriteMonster createMonster(Pane layer, Monster monster){
        return new SpriteMonster(layer, monster);
    }
}
