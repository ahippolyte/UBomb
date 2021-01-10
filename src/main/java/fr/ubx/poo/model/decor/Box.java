package fr.ubx.poo.model.decor;

public class Box extends Decor {

    public String toString(){
        return "Box";
    }

    public boolean plyCanMoveOn() {
        return true;
    }

    public boolean isBreakable(){
        return true;
    }

}