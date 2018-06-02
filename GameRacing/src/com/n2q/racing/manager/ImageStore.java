package com.n2q.racing.manager;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ImageStore {
    public static final Image IMG_MAP           = getImage("/res/assets/map.png");
    public static final Image IMG_CAR_USER      = getImage("/res/drawable/car/car_user.png");
    public static final Image IMG_CAR_SYSTEM    = getImage("/res/drawable/car/car_system.png");
    public static final Image IMG_CAR_POLICE_UP = getImage("/res/drawable/car/car_police_up.png");
    public static final Image IMG_BULLET        = getImage("/res/drawable/item/bullet.png");
    public static final Image IMG_BULLET_ITEM   = getImage("/res/drawable/item/bullet_item.png");
    public static final Image IMG_MONEY         = getImage("/res/drawable/item/money.png");
    public static final Image IMG_FUEL          = getImage("/res/drawable/item/fuel.png");

    public static final Image IMG_SOUND_OFF = getImage("/res/drawable/menu/sound_off_white.png");
    public static final Image IMG_SOUND_ON  = getImage("/res/drawable/menu/sound_on_white.png");

    public static final Image IMG_BACKGROUND    = getImage("/res/drawable/background.png");

    public static final Image[] IMGS_BOOM = new Image[]{
            getImage("/res/drawable/boom/imgBoom5.png"),
            getImage("/res/drawable/boom/imgBoom4.png"),
            getImage("/res/drawable/boom/imgBoom3.png"),
            getImage("/res/drawable/boom/imgBoom2.png"),
            getImage("/res/drawable/boom/imgBoom1.png")
    };

    private static Image getImage(String path){
        URL url = ImageStore.class.getResource(path);
        return new ImageIcon(url).getImage();
    }
}