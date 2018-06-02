package com.n2q.racing.model;

import com.n2q.racing.manager.GameManager;
import com.n2q.racing.manager.ImageStore;
import com.n2q.racing.view.Gui;

import java.awt.*;

public class Map extends BaseModel {
    private int speed;

    public Map(int x, int y, int speed){
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void draw(Graphics2D graphics2D){
        graphics2D.drawImage(ImageStore.IMG_MAP, x, y, Gui.WIDTH_FRAME, Gui.HEIGHT_FRAME, null);
    }

    public void move(long numberOfSleep){
        if (numberOfSleep % speed != 0){
            return;
        }
        y++;
        if (y == Gui.HEIGHT_FRAME - 10){
            y = -GameManager.DISTANCE_MAP;
        }
    }

}