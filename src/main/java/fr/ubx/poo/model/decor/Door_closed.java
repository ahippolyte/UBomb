package fr.ubx.poo.model.decor;

public class Door_closed extends Decor {

    public String toString(){
        return "Door_closed";
    }

    public boolean plyCanMoveOn() {
        return false;
    }

}