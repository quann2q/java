package com.n2q.racing.manager;

import com.n2q.racing.model.*;
import com.n2q.racing.view.Gui;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Random;

public class GameManager {
    private Map map1;
    private Map map2;
    private Car myCar;
    private ArrayList<Car> arrayCarsSystem;
    private ArrayList<Car> arrayCarsPolice;
    private ArrayList<Bullet> myBullets;
    private ArrayList<Money> myMoneys;
    private ArrayList<Fuel> myFuels;
    private ArrayList<BulletItem> myBulletsItem;
    private ArrayList<Boom> myBooms;

    private PlayerWav playerWav;
    private PlayerWav playerWavBackground;
    private PlayerWav playerWavBackgroundMenu;

    private static final int SPEED_CAR_USER = 5;
    private static final int SPEED_BULLET = 2;
    private static final int SPEED_MAP = 2;
    public static final int SPEED_CAR_SYSTEM = 3;
    public static final int SPEED_CAR_POLICE = 15;
    public static final int SPEED_ITEM = 2;
    public static final int DISTANCE_MAP = 800;

    private int fireInterval = 0;
    private static final int FIRE_INTERVAL_MAX = 1000;

    private int moneyInterval = 2 * 1000;
    private static final int MONEY_INTERVAL_MAX = 5 * 1000;

    private int fuelInterval = 5 * 1000;
    private static final int FUEL_INTERVAL_MAX = 5 * 1000;

    private int bulletItemInterval = 8 * 1000;
    private static final int BULLET_ITEM_INTERVAL_MAX = 15 * 1000;

    private int carSystemInterval = 0;
    private static final int CAR_SYSTEM_INTERVAL_MAX = 1000;

    private int numberOfTimesEatMoney = 1;

    public GameManager(){
        map1  = new Map(0, 0, SPEED_MAP);
        map2  = new Map(0, -DISTANCE_MAP, SPEED_MAP);
        myCar = new Car(
                (Car.ROAD_SIDE_LEFT + Car.ROAD_SIDE_RIGHT)/2 + 3,
                Gui.HEIGHT_FRAME - Car.HEIGHT_CAR - Car.DISTANCE_CAR_MAP,
                Car.UP,
                SPEED_CAR_USER);
        arrayCarsSystem = new ArrayList<>();
        arrayCarsPolice = new ArrayList<>();
        myBullets = new ArrayList<>();
        myMoneys  = new ArrayList<>();
        myFuels   = new ArrayList<>();
        myBooms   = new ArrayList<>();
        myBulletsItem = new ArrayList<>();
    }

    public void drawMap(Graphics2D graphics2D){
        map1.draw(graphics2D);
        map2.draw(graphics2D);
    }

    public void moveMap(long numberOfSpeed){
        map1.move(numberOfSpeed);
        map2.move(numberOfSpeed);
    }

    public void drawBoom(Graphics2D graphics2D){
        for (int i=0; i<myBooms.size(); i++){
            myBooms.get(i).draw(graphics2D);
        }
    }

    public void moveBoom(long numberOfSpeed){
        for (int i=0; i<myBooms.size(); i++){
            myBooms.get(i).move(numberOfSpeed);
        }
    }

    public void removeBoom(){
        for (int i=0; i<myBooms.size(); i++){
            if (myBooms.get(i).isDone()){
                myBooms.remove(i);
            }
        }
    }

    private void createBoom(Car car){
        int x = car.getX() + (Car.WIDTH_CAR - Boom.SIZE)/2;
        int y = car.getY() + (Car.HEIGHT_CAR - Boom.SIZE)/2;
        Boom boom = new Boom(x, y);
        myBooms.add(boom);
    }

    public void drawFuel(Graphics2D graphics2D){
        for (int i=0; i<myFuels.size(); i++){
            myFuels.get(i).draw(graphics2D);
        }
    }

    public void moveFuel(long numberOfSleep){
        for (int i=0; i<myFuels.size(); i++){
            myFuels.get(i).move(numberOfSleep);
        }
    }

    public void setMyFuels(Fuel fuel) {
        if (fuelInterval == 0){
            this.myFuels.add(fuel);
            fuelInterval = FUEL_INTERVAL_MAX;
        }
    }

    public void countdownFuelInterval(){
        if (fuelInterval > 0){
            fuelInterval--;
        } else {
            fuelInterval = 0;
        }
    }

    public void drawBulletItem(Graphics2D graphics2D) {
        for (int i=0; i<myBulletsItem.size(); i++){
            myBulletsItem.get(i).draw(graphics2D);
        }
    }

    public void moveBulletItem(long numberOfSleep){
        for (int i=0; i<myBulletsItem.size(); i++){
            myBulletsItem.get(i).move(numberOfSleep);
        }
    }

    public void setMyBulletsItem(BulletItem bulletsItem){
        if (bulletItemInterval == 0){
            this.myBulletsItem.add(bulletsItem);
            bulletItemInterval = BULLET_ITEM_INTERVAL_MAX;
        }
    }

    public void countdownBulletItemInterval(){
        if (bulletItemInterval > 0){
            bulletItemInterval--;
        } else {
            bulletItemInterval = 0;
        }
    }

    public void removeBulletItem(){
        for (int i=0; i<myBulletsItem.size(); i++){
            if (myBulletsItem.get(i).getY() > Gui.HEIGHT_FRAME){
                myBulletsItem.remove(i);
            }
        }
    }

    public boolean isEatingBulletItem(){
        for (int i=0; i<myBulletsItem.size(); i++){
            if (isCarCollisionBulletItem(myBulletsItem.get(i))){
                myBulletsItem.remove(i);
                playAudioEatingBullet();
                return true;
            }
        }
        return false;
    }

    private boolean isCarCollisionBulletItem(BulletItem bulletItem){
        Rectangle rectangleTemp = myCar.getRectangle().intersection(bulletItem.getRectangle());
        if (rectangleTemp.getWidth() > 5 && rectangleTemp.getHeight() > 5){
            return true;
        }
        return false;
    }

    public void drawMoney(Graphics2D graphics2D){
        for (int i=0; i<myMoneys.size(); i++){
            myMoneys.get(i).draw(graphics2D);
        }
    }

    public void moveMoney(long numberOfSleep){
        for (int i=0; i<myMoneys.size(); i++){
            myMoneys.get(i).move(numberOfSleep);
        }
    }

    public void setMyMoneys(Money money) {
        Random random = new Random();
        if (moneyInterval == 0){
            this.myMoneys.add(money);
            moneyInterval = MONEY_INTERVAL_MAX + random.nextInt(MONEY_INTERVAL_MAX);
        }
    }

    public void countdownMoneyInterval(){
        if (moneyInterval > 0){
            moneyInterval--;
        } else {
            moneyInterval = 0;
        }
    }

    public void removeMoney(){
        for (int i=0; i<myMoneys.size(); i++){
            if (myMoneys.get(i).getY() > Gui.HEIGHT_FRAME){
                myMoneys.remove(i);
            }
        }
    }

    public void drawMyCar(Graphics2D graphics2D){
        myCar.draw(Car.CAR_USER, graphics2D);
    }

    public void moveMyCar(long numberOfSleep){
        if (isMyCarCanMove()){
            myCar.move(Car.CAR_USER, numberOfSleep);
        }
    }

    public boolean isMyCarCanMove(){
        if (isMyCarCollisionCarSystem()){
            return false;
        }
        return true;
    }

    private boolean isMyCarCollisionCarSystem(){
        for (int i=0; i<arrayCarsSystem.size(); i++){
            if (isMyCarCollision(arrayCarsSystem.get(i))){
                createBoom(myCar);
                createBoom(arrayCarsSystem.get(i));
                playAudioBoom();
                stopAudioBackground();
                return true;
            }
        }
        for (int i=0; i<arrayCarsPolice.size(); i++){
            if (isMyCarCollision(arrayCarsPolice.get(i))){
                createBoom(myCar);
                playAudioBoom();
                stopAudioBackground();
                return true;
            }
        }
        return false;
    }

    private boolean isMyCarCollision(Car car){
        Rectangle rectangleTemp = myCar.getRectangle().intersection(car.getRectangle());
        if (rectangleTemp.getWidth() > 5 && rectangleTemp.getHeight() > 5){
            return true;
        }
        return false;
    }

    public void changeMyCarDirection(int direction){
        if (myCar.getDirection() != direction){
            myCar.setDirection(direction);
        }
    }

    public void drawArrayCarsSystem(Graphics2D graphics2D){
        for (int i=0; i<arrayCarsSystem.size(); i++){
            arrayCarsSystem.get(i).draw(Car.CAR_SYSTEM, graphics2D);
        }
    }

    public void moveCarSystem(long numberOfSleep){
        for(int i=0; i<arrayCarsSystem.size(); i++){
            arrayCarsSystem.get(i).move(Car.CAR_SYSTEM, numberOfSleep);
        }
    }

    public void setArrayCarsSystem(Car carSystem) {
        if (carSystemInterval == 0){
            this.arrayCarsSystem.add(carSystem);
            carSystemInterval = CAR_SYSTEM_INTERVAL_MAX;
        }
    }

    public void countdownCarSystemInterval(){
        if (carSystemInterval > 0){
            carSystemInterval -= 1;
        } else {
            carSystemInterval = 0;
        }
    }

    public void removeCarSystem(){
        for (int i=0; i<arrayCarsSystem.size(); i++){
            if (isCarSystemMoveOutMap(i)){
                arrayCarsSystem.remove(i);
            }
        }
    }

    private boolean isCarSystemMoveOutMap(int index){
        if (arrayCarsSystem.get(index).getY() == Gui.HEIGHT_FRAME){
            return true;
        }
        return false;
    }

    public void drawArrayCarsPolice(String typeCarPolice, Graphics2D graphics2D){
        for (int i=0; i<arrayCarsPolice.size(); i++){
            arrayCarsPolice.get(i).draw(typeCarPolice, graphics2D);
        }
    }

    public void moveCarPolice(long numberOfSleep){
        for (int i=0; i<arrayCarsPolice.size(); i++){
            for (int j=0; j<arrayCarsSystem.size(); j++){
                if (arrayCarsPolice.get(i).getRectangle()
                        .intersects(arrayCarsSystem.get(j).getRectangle())){
                    int x = arrayCarsPolice.get(i).getX();
                    int y = arrayCarsPolice.get(i).getY();
                    if (y > myCar.getY()){
                        if (x > myCar.getX()){
                            x--;
                        } else {
                            x++;
                        }
                    } else {
                        x++;
                    }

                    arrayCarsPolice.get(i).setX(x);
                } else {
                    arrayCarsPolice.get(i).move(Car.CAR_POLICE_UP, numberOfSleep);
                }
            }
        }
    }

    public void setArrayCarsPolice(Car carPolice) {
        if (numberOfTimesEatMoney % 5 != 0){
            return;
        }
        playAudioWarning();
        this.arrayCarsPolice.add(carPolice);
        numberOfTimesEatMoney++;
    }

    public void removeCarPolice(){
        for (int i=0; i<arrayCarsPolice.size(); i++){
            if (isCarPoliceMoveOutMap(i)){
                removedCarPolice(i);
            }
        }
    }

    private boolean isCarPoliceMoveOutMap(int index){
        if (arrayCarsPolice.get(index).getY() == -Car.HEIGHT_CAR){
            return true;
        }
        return false;
    }

    private void removedCarPolice(int index){
        arrayCarsPolice.remove(index);
    }

    public void drawMyBullets(Graphics2D graphics2D){
        for (int i=0; i<myBullets.size(); i++){
            myBullets.get(i).draw(graphics2D);
        }
    }

    public boolean fireMyBulletByMyCar(){
        if (fireInterval == 0){
            int x = myCar.getX() + (Car.WIDTH_CAR - Bullet.SIZE)/2;
            int y = myCar.getY() + (Car.HEIGHT_CAR - Bullet.SIZE)/2;
            Bullet bullet = new Bullet(x, y, SPEED_BULLET);
            myBullets.add(bullet);
            fireInterval = FIRE_INTERVAL_MAX;
            playAudioBullet();
            return true;
        }
        return false;
    }

    public void countdownFireInterval(){
        if (fireInterval > 0){
            fireInterval -= 1;
        } else {
            fireInterval = 0;
        }
    }

    public void moveMyBullets(long numberOfSleep){
        for (int i=0; i<myBullets.size(); i++){
            myBullets.get(i).move(numberOfSleep);
        }
    }

    public void changeCarSystemDirection(int direction){
        for (int i=0; i<arrayCarsSystem.size(); i++){
            if (arrayCarsSystem.get(i).getDirection() != direction){
                arrayCarsSystem.get(i).setDirection(direction);
            }
        }
    }

    public void changeCarPoliceDirection(int direction){
        for (int i=0; i<arrayCarsPolice.size(); i++){
            int y = arrayCarsPolice.get(i).getY();
            if (y > myCar.getY()){
                if (arrayCarsPolice.get(i).getDirection() != direction){
                    arrayCarsPolice.get(i).setDirection(direction);
                }
            } else {
                arrayCarsPolice.get(i).setDirection(Car.UP);
            }
        }
    }

    public int directionPoliceAndMyCar(){
        for (int i=0; i<arrayCarsPolice.size(); i++){
            int direction = arrayCarsPolice.get(i).getX() - myCar.getX();
            if (direction > 0){
                return Car.LEFT;
            }
        }
        return Car.RIGHT;
    }

    public void removeBullet(){
        for (int i=0; i<myBullets.size(); i++){
            if (isBulletMoveOutMap(i)){
                myBullets.remove(i);
            }
        }
    }

    public void removeCarSystemAndBullet(){
        for (int i=0; i<arrayCarsSystem.size(); i++){
            for (int j=0; j<myBullets.size(); j++){
                if (arrayCarsSystem.get(i).getRectangle().intersects(myBullets.get(j).getRectangle())){
                    createBoom(arrayCarsSystem.get(i));
                    playAudioBoom();
                    arrayCarsSystem.remove(i);
                    removedBullet(j);
                }
            }
        }
    }

    private boolean isBulletMoveOutMap(int index){
        if (myBullets.get(index).getY() == 0){
            return true;
        }
        return false;
    }

    private void removedBullet(int index){
        myBullets.remove(index);
    }

    public boolean isEatingMoney(){
        for (int i=0; i<myMoneys.size(); i++){
            if (isCarCollisionMoney(myMoneys.get(i))){
                myMoneys.remove(i);
                numberOfTimesEatMoney++;
                playAudioEatingMoney();
                return true;
            }
        }
        return false;
    }

    private boolean isCarCollisionMoney(Money money){
        Rectangle rectangleTemp = myCar.getRectangle().intersection(money.getRectangle());
        if (rectangleTemp.getWidth() > 5 && rectangleTemp.getHeight() > 5){
            return true;
        }
        return false;
    }

    public boolean isEatingFuel(){
        for (int i=0; i<myFuels.size(); i++){
            if (isCarCollisionFuel(myFuels.get(i))){
                myFuels.remove(i);
                return true;
            }
        }
        return false;
    }

    private boolean isCarCollisionFuel(Fuel fuel){
        Rectangle rectangleTemp = myCar.getRectangle().intersection(fuel.getRectangle());
        if (rectangleTemp.getWidth() > 5 && rectangleTemp.getHeight() > 5){
            return true;
        }
        return false;
    }

    public void playAudioBackground(){
        playerWavBackground = new PlayerWav("background");
        playerWavBackground.play();
        playerWavBackground.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopAudioBackground(){
        playerWavBackground.stop();
    }

    private void playAudioBullet(){
        playerWav = new PlayerWav("bullet");
        playerWav.play();
    }

    private void playAudioBoom(){
        playerWav = new PlayerWav("boom");
        playerWav.play();
    }

    private void playAudioEatingMoney(){
        playerWav = new PlayerWav("money");
        playerWav.play();
    }

    private void playAudioEatingBullet(){
        playerWav = new PlayerWav("eat_bullet");
        playerWav.play();
    }

    public void playAudioIncreaseFuel(){
        playerWav = new PlayerWav("fuel");
        playerWav.play();
    }

    private void playAudioWarning(){
        playerWav = new PlayerWav("warning");
        playerWav.play();
        playerWav.loop(1);
    }

    public void playAudioBackGroundMenu(){
        playerWavBackgroundMenu = new PlayerWav("Arc-MindVortex");
        playerWavBackgroundMenu.play();
        playerWavBackgroundMenu.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopAudioBackGroundMenu(){
        playerWavBackgroundMenu.stop();
    }

}