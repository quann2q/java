package com.n2q.racing.model;

import com.n2q.racing.manager.ImageStore;

import java.awt.*;

public class BulletItem extends BaseModel {
    public static final int WIDTH = 20;
    public static final int HEIGHT = 30;
    private int speed;

    public BulletItem(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;

        rectangle = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(ImageStore.IMG_BULLET_ITEM, x, y, WIDTH, HEIGHT, null);
    }

    public void move(long numberOfSleep){
        if (numberOfSleep % speed != 0){
            return;
        }
        y++;
        rectangle.setLocation(x, y);
    }
}