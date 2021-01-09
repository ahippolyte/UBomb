package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.view.image.ImageFactory;
import fr.ubx.poo.view.image.ImageResource;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class SpriteExplosion extends SpriteGameObject {
    private final ColorAdjust effect = new ColorAdjust();
    private Position position;

    public SpriteExplosion(Pane layer, Position position, Bomb bomb) {
        super(layer, null, bomb);
        this.position = position;
        updateImage();
    }

    public GameObject getGameObject(){
        return go;
    }

    public String toString(){
        return "SpriteExplosion";
    }

    public void updateImage() {
        setImage(ImageFactory.getInstance().get(ImageResource.Explosion));
    }

    public Position getPosition(){
        return position;
    }

}