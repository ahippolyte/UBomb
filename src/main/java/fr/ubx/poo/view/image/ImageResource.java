package fr.ubx.poo.view.image;

import javafx.scene.image.Image;

public enum ImageResource {
    BANNER_BOMB ("banner_bomb.png"),
    BANNER_RANGE ("banner_range.png"),
    HEART("heart.png"),
    KEY("key.png"),
    DIGIT_0 ("banner_0.jpg"),
    DIGIT_1 ("banner_1.jpg"),
    DIGIT_2 ("banner_2.jpg"),
    DIGIT_3 ("banner_3.jpg"),
    DIGIT_4 ("banner_4.jpg"),
    DIGIT_5 ("banner_5.jpg"),
    DIGIT_6 ("banner_6.jpg"),
    DIGIT_7 ("banner_7.jpg"),
    DIGIT_8 ("banner_8.jpg"),
    DIGIT_9 ("banner_9.jpg"),
    PLAYER_DOWN("player_down.png"),
    PLAYER_LEFT("player_left.png"),
    PLAYER_RIGHT("player_right.png"),
    PLAYER_UP("player_up.png"),
    STONE("stone.png"),
    TREE("tree.png"),
    BOX("box.png"),
    PRINCESS("bomberwoman.png"),
    Heart("heart.png"),
    BombNumberInc("bonus_bomb_nb_inc.png"),
    BombNumberDec("bonus_bomb_nb_dec.png"),
    BombRangeInc("bonus_bomb_range_inc.png"),
    BombRangeDec("bonus_bomb_range_dec.png"),
    Bomb4("bomb4.png"),
    Bomb3("bomb3.png"),
    Bomb2("bomb2.png"),
    Bomb1("bomb1.png"),
    Key("key.png"),
    Monster_down("monster_down.png"),
    Monster_left("monster_left.png"),
    Monster_up("monster_up.png"),
    Monster_right("monster_right.png"),
    Door_closed("door_closed.png"),
    Door_opened("door_opened.png"),
    ;

    private final String FileName;

    ImageResource(String fileName) {
        this.FileName = fileName;
    }

    public String getFileName() {
        return FileName;
    }
}
