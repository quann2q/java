package com.n2q.racing.model;

import com.n2q.racing.manager.ImageStore;

import java.awt.*;

public class Bullet extends BaseModel {
    public static final int SIZE = 10;
    private int speed;

    public Bullet(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;

        rectangle = new Rectangle(x, y, SIZE, SIZE);
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(ImageStore.IMG_BULLET, x, y, SIZE, SIZE, null);
    }

    public void move(long numberOfSleep){
        if (numberOfSleep % speed != 0){
            return;
        }
        y--;
        rectangle.setLocation(x, y);
    }
}