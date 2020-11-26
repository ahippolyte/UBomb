package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.BombSteps;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

import java.util.Timer;
import java.util.TimerTask;

import static fr.ubx.poo.view.image.ImageResource.*;

public class SpriteBomb extends SpriteGameObject {
    private final ColorAdjust effect = new ColorAdjust();

    public SpriteBomb(Pane layer, Bomb bomb) {
        super(layer, null, bomb);
        updateImage();
    }

    public void updateImage() {
        Bomb bomb = (Bomb) go;
        setImage(ImageFactory.getInstance().getBombStepImage(bomb.getBombStep()));
    }

}