package com.kavinduchamiran.sensorreader.model;

public class Controller {
    int x;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    int y;
    public Controller(int x, int y){
        this.x = x;
        this.y = y;
    }

}
