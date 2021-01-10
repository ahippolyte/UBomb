/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.character.*;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.awt.*;

public class SpritePlayer extends SpriteGameObject {
    private final ColorAdjust effect = new ColorAdjust();

    public SpritePlayer(Pane layer, Player player) {
        super(layer, null, player);
        updateImage();
    }

    public String toString(){
        return "SpritePlayer";
    }

    @Override
    public void updateImage() {
        Player player = (Player) go;
        setImage(ImageFactory.getInstance().getPlayer(player.getDirection()));
    }

    public void render() {
        if (imageView != null) {
            remove();
        }
        updateImage();
        imageView = new ImageView(this.image);
        imageView.setX(getPosition().x * size);
        imageView.setY(getPosition().y * size);
        imageView.setEffect(effect);
        layer.getChildren().add(imageView);
    }

    public void changeColor(boolean bool){
        if (bool) {
            effect.setContrast(0.10);
            effect.setHue(-0.9);
            effect.setBrightness(0.6);
            effect.setSaturation(0.6);
        } else {
            effect.setContrast(0);
            effect.setHue(0);
            effect.setBrightness(0);
            effect.setSaturation(0);
        }
    }

}
