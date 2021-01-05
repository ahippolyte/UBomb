package fr.ubx.poo.model.decor;

public class Door_opened extends Decor {

    public String toString(){
        return "Door_opened";
    }

    public boolean plyCanMoveOn() {
        return true;
    }

}