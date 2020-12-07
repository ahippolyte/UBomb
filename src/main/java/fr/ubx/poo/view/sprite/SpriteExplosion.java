package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.image.ImageResource;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class SpriteExplosion extends SpriteDecor {
    private final ColorAdjust effect = new ColorAdjust();

    public SpriteExplosion(Pane layer, Position position) {
        super(layer, null, position);
        updateImage();
    }

    public void updateImage() {
        setImage(ImageFactory.getInstance().get(ImageResource.Explosion));
    }

}