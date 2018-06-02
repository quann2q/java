package com.n2q.racing.model;

import com.n2q.racing.manager.ImageStore;
import com.n2q.racing.view.Gui;

import java.awt.*;
import java.beans.beancontext.BeanContextServiceRevokedEvent;
import java.util.Random;

public class Car extends BaseModel {
    public static final int WIDTH_CAR = 30 * 2;
    public static final int HEIGHT_CAR = 66 * 2;
    public static final int LEFT = 0;
    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 3;

    public static final int ROAD_SIDE_LEFT = 135;
    public static final int ROAD_SIDE_RIGHT = 415;
    public static final int DISTANCE_CAR_MAP = 100;

    public static final String CAR_USER = "CAR_USER";
    public static final String CAR_SYSTEM = "CAR_SYSTEM";
    public static final String CAR_POLICE_DOWN = "CAR_POLICE_DOWN";
    public static final String CAR_POLICE_UP = "CAR_POLICE_UP";

    private int direction;
    private int speed;
    private static final int INTERVAL_POLICE_DIRECTION = 4;

    public Car(int x, int y, int direction, int speed){
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;

        rectangle = new Rectangle(x, y, WIDTH_CAR, HEIGHT_CAR);
    }

    public void draw(String typeCar, Graphics2D graphics2D){
        Image imgCar;
        switch (typeCar){
            case CAR_USER:
                imgCar = ImageStore.IMG_CAR_USER;
                break;
            case CAR_SYSTEM:
                imgCar = ImageStore.IMG_CAR_SYSTEM;
                break;
            case CAR_POLICE_UP:
                imgCar = ImageStore.IMG_CAR_POLICE_UP;
                break;
            default:
                imgCar = ImageStore.IMG_CAR_USER;
                break;
        }
        graphics2D.drawImage(imgCar, x, y, WIDTH_CAR, HEIGHT_CAR, null);
    }

    public void move(String typeCar, long numberOfSleep){
        if (numberOfSleep % speed != 0){
            return;
        }
        switch (typeCar){
            case CAR_USER:
                moveMyCar();
                break;
            case CAR_SYSTEM:
                moveCarSystem();
                break;
            case CAR_POLICE_UP:
                moveCarPolice(numberOfSleep);
                break;
            default:
                break;
        }
    }

    private void moveMyCar(){
        switch (direction){
            case LEFT:
                if (x > ROAD_SIDE_LEFT){
                    x--;
                    rectangle.setLocation(x, y);
                }
                break;
            case UP:
                if (y > HEIGHT_CAR){
                    y--;
                    rectangle.setLocation(x, y);
                }
                break;
            case RIGHT:
                if (x < ROAD_SIDE_RIGHT){
                    x++;
                    rectangle.setLocation(x, y);
                }
                break;
            case DOWN:
                if (y < Gui.HEIGHT_FRAME - Car.HEIGHT_CAR - DISTANCE_CAR_MAP){
                    y++;
                    rectangle.setLocation(x, y);
                }
                break;
            default:
                break;
        }
    }

    private void moveCarSystem(){
        switch (direction){
            case LEFT:
                if (x > ROAD_SIDE_LEFT){
                    x--;
                    rectangle.setLocation(x, y);
                }
                break;
            case UP:
                if (y > -HEIGHT_CAR){
                    y--;
                    rectangle.setLocation(x, y);
                }
                break;
            case RIGHT:
                if (x < ROAD_SIDE_RIGHT){
                    x++;
                    rectangle.setLocation(x, y);
                }
                break;
            case DOWN:
                if (y < Gui.HEIGHT_FRAME){
                    y++;
                    rectangle.setLocation(x, y);
                }
                break;
            default:
                break;
        }
    }

    private void moveCarPolice(long numberOfSleep){
        switch (direction){
            case LEFT:
                if (x > ROAD_SIDE_LEFT){
                    y--;
                    rectangle.setLocation(x, y);
                    if (numberOfSleep % INTERVAL_POLICE_DIRECTION != 0){
                        break;
                    }
                    x--;
                }
                break;
            case UP:
                if (y > -HEIGHT_CAR){
                    y--;
                    rectangle.setLocation(x, y);
                }
                break;
            case RIGHT:
                if (x < ROAD_SIDE_RIGHT){
                    y--;
                    rectangle.setLocation(x, y);
                    if (numberOfSleep % INTERVAL_POLICE_DIRECTION != 0){
                        break;
                    }
                    x++;
                }
                break;
            case DOWN:
                if (y < Gui.HEIGHT_FRAME){
                    y++;
                    rectangle.setLocation(x, y);
                }
                break;
            default:
                break;
        }
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

}