package com.n2q.racing.model;

import com.n2q.racing.manager.ImageStore;

import java.awt.*;

public class Boom extends BaseModel {
    public static final int SIZE = 350;

    private boolean isDone;
    private int index;

    public Boom(int x, int y){
        this.x = x;
        this.y = y;
        this.isDone = false;
        index = 0;
    }

    public void draw(Graphics2D graphics2D){
        if (index < ImageStore.IMGS_BOOM.length){
            graphics2D.drawImage(ImageStore.IMGS_BOOM[index], x, y, SIZE, SIZE, null);
        }
    }

    public void move(long numberOfSleep) {
        if (numberOfSleep % 50 == 0) {
            index++;
            if (index >= ImageStore.IMGS_BOOM.length) {
                isDone = true;
            }
        }
    }

    public boolean isDone() {
        return isDone;
    }
}