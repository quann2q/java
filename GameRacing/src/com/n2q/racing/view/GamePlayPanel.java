package com.n2q.racing.view;

import com.n2q.racing.manager.FileManager;
import com.n2q.racing.manager.GameManager;
import com.n2q.racing.manager.ImageStore;
import com.n2q.racing.model.BulletItem;
import com.n2q.racing.model.Car;
import com.n2q.racing.model.Fuel;
import com.n2q.racing.model.Money;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.BitSet;
import java.util.Random;

public class GamePlayPanel extends BasePanel {
    private GameManager gameManager;
    private BitSet bitSet;
    private KeyAdapter keyAdapter;
    private Graphics2D graphics2D;
    private Random random = new Random();

    private JLabel lbHighScore;
    private JLabel lbScore;
    private JLabel lbMoney;
    private JLabel lbFuel;
    private JLabel lbBullet;
    private JLabel lbBulletImg;
    private JLabel lbPause;

    private TextField tfHighScore;
    private TextField tfScore;
    private TextField tfMoney;

    private Font titleFont;
    private Font textFont;
    private FontMetrics titleFontMetrics;
    private FontMetrics textFontMetrics;
    private int heightTitleFont;
    private int heightTextFont;

    private static final int DISTANCE_X_MENU = 190;
    private static final int DISTANCE_Y_MENU = 30;
    private static final int WIDTH_TEXT_FIELD = 150;

    private static final int SCORES_INTERVAL_MAX = 100;
    private int scoresInterval = 0;

    private static final int SPEED_FUEL = 50;
    private static final int VALUE_INCREASE_EATTING_FUEL = 30;
    private static final int VALUE_INCREASE_BUY_FUEL = 50;
    private static final int MONEY_BUY_FUEL = 100;

    private static final String EATTING_FUEL = "EATTING_FUEL";
    private static final String BUY_FUEL = "BUY_FUEL";

    private OnMenuSelectListener onMenuSelectListener;
    public static final String ACTION_SHOW_DIALOG_PAUSE = "ACTION_SHOW_DIALOG_PAUSE";
    public static final String ACTION_SHOW_DIALOG_STOP = "ACTION_SHOW_DIALOG_STOP";

    private int buyInterval = 0;
    private static final int BUY_INTERVAL_MAX = 1000;

    private int valueFuel = WIDTH_TEXT_FIELD;
    private long numberOfSleep = 0;

    private static final int PLAY_GAME = 0;
    private static final int PAUSE_GAME = 1;
    private static final int STOP_GAME = 2;
    public static final int RESUME_GAME = 3;
    private int status;
    private int countPressPause;
    private int numberOfBullet = 0;

    @Override
    public void initContainer() {
        setLayout(null);
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    @Override
    public void initComponents() {
        gameManager = new GameManager();
        bitSet = new BitSet();
        keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                bitSet.set(e.getKeyCode(), true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                bitSet.set(e.getKeyCode(), false);
            }
        };
        addKeyListener(keyAdapter);

        titleFont = new Font( "Arial", Font.BOLD, 24);
        textFont  = new Font( "Arial", Font.PLAIN, 24);
        titleFontMetrics = getFontMetrics(titleFont);
        textFontMetrics  = getFontMetrics(textFont);
        heightTitleFont  = titleFontMetrics.getHeight();
        heightTextFont   = textFontMetrics.getHeight();
        setLabel();
        setTextField();
    }

    @Override
    public void registerListeners() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphics2D = (Graphics2D) g;

        gameManager.drawMap(graphics2D);
        gameManager.drawFuel(graphics2D);
        gameManager.drawMoney(graphics2D);
        gameManager.drawBulletItem(graphics2D);
        gameManager.drawMyBullets(graphics2D);
        gameManager.drawMyCar(graphics2D);
        gameManager.drawArrayCarsSystem(graphics2D);
        gameManager.drawArrayCarsPolice(Car.CAR_POLICE_UP, graphics2D);
        gameManager.drawBoom(graphics2D);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(new Color(240, 240, 240));
        g.fillRect(lbFuel.getX(),
                lbFuel.getY() + DISTANCE_Y_MENU,
                WIDTH_TEXT_FIELD,
                heightTextFont);
        g.setColor(Color.RED);

        g.fillRect(lbFuel.getX(),
                lbFuel.getY() + DISTANCE_Y_MENU,
                valueFuel,
                heightTextFont);
        if (numberOfSleep % SPEED_FUEL != 0){
            return;
        }
        if (valueFuel > 0){
            valueFuel--;
        } else {
            valueFuel = 0;
        }

    }

    public void startGame(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                gameManager.playAudioBackground();
                while (true){
                    if (numberOfSleep < Long.MAX_VALUE){
                        numberOfSleep++;
                    } else {
                        numberOfSleep = 0;
                    }
                    System.out.println("Thread");
                    gamePlayControl();
                    if (status == PLAY_GAME || status == RESUME_GAME){
                        gameManagerMap();
                        gameManagerMyCar();
                        gameManagerCarsSystem();
                        gameManagerCarsPolice();
                        gameManagerBoom();
                        gameManagerBullet();
                        gameManagerMoney();
                        gameManagerFuel();
                        gameManagerBulletItem();

                        setScores();
                        countdownScoresInterval();

                        repaint();
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (status == STOP_GAME){
                        FileManager fileManager = new FileManager();
                        int scores = Integer.parseInt(tfScore.getText());
                        int money  = Integer.parseInt(tfMoney.getText());
                        int highScores = scores + money;
                        fileManager.writeFile(highScores + "");
                        break;
                    }
                }
            }
        };
        Thread backgroundThread = new Thread(runnable);
        status = PLAY_GAME;
        backgroundThread.start();
    }

    private void gamePlayControl(){
        if (bitSet.get(KeyEvent.VK_R)){
            status = RESUME_GAME;
            countPressPause = 0;
            lbPause.setText("");
        }
        if (bitSet.get(KeyEvent.VK_P)){
            countPressPause++;
            if (countPressPause == 1){
                onMenuSelectListener.onMenuSelectedListener(ACTION_SHOW_DIALOG_PAUSE);
            }
            lbPause.setText("PAUSE");
            status = PAUSE_GAME;
        }
    }

    private void gameManagerMap(){
        gameManager.moveMap(numberOfSleep);
    }

    private void gameManagerMyCar(){
        if (bitSet.get(KeyEvent.VK_LEFT) || bitSet.get(KeyEvent.VK_A)){
            gameManager.changeMyCarDirection(Car.LEFT);
            gameManager.moveMyCar(numberOfSleep);
        }
        if (bitSet.get(KeyEvent.VK_UP) || bitSet.get(KeyEvent.VK_W)){
            gameManager.changeMyCarDirection(Car.UP);
            gameManager.moveMyCar(numberOfSleep);
        }
        if (bitSet.get(KeyEvent.VK_RIGHT) || bitSet.get(KeyEvent.VK_D)){
            gameManager.changeMyCarDirection(Car.RIGHT);
            gameManager.moveMyCar(numberOfSleep);
        }
        if (bitSet.get(KeyEvent.VK_DOWN) || bitSet.get(KeyEvent.VK_S)){
            gameManager.changeMyCarDirection(Car.DOWN);
            gameManager.moveMyCar(numberOfSleep);
        }
    }

    private void gameManagerCarsSystem(){
        gameManager.setArrayCarsSystem(new Car(
                Car.ROAD_SIDE_LEFT + random.nextInt(Car.ROAD_SIDE_RIGHT - Car.ROAD_SIDE_LEFT),
                -Car.HEIGHT_CAR,
                Car.DOWN,
                GameManager.SPEED_CAR_SYSTEM
        ));
        gameManager.countdownCarSystemInterval();
        gameManager.moveCarSystem(numberOfSleep);
        if (!gameManager.isMyCarCanMove()){
            onMenuSelectListener.onMenuSelectedListener(ACTION_SHOW_DIALOG_STOP);
            status = STOP_GAME;
        }
        gameManager.removeCarSystem();
        gameManager.removeCarSystemAndBullet();
    }

    private void gameManagerCarsPolice(){
        gameManager.setArrayCarsPolice(new Car(
                Car.ROAD_SIDE_LEFT + random.nextInt(Car.ROAD_SIDE_RIGHT - Car.ROAD_SIDE_LEFT),
                Gui.HEIGHT_FRAME,
                Car.UP,
                GameManager.SPEED_CAR_POLICE
        ));
        gameManager.moveCarPolice(numberOfSleep);
        gameManager.removeCarPolice();
        gameManager.changeCarPoliceDirection(gameManager.directionPoliceAndMyCar());
    }

    private void gameManagerBoom(){
        gameManager.moveBoom(numberOfSleep);
        gameManager.removeBoom();
    }

    private void gameManagerBullet(){
        if (bitSet.get(KeyEvent.VK_SPACE)){
            if (numberOfBullet > 0){
                if (gameManager.fireMyBulletByMyCar()){
                    numberOfBullet--;
                    lbBullet.setText(numberOfBullet + "x");
                }
            }
        }
        gameManager.moveMyBullets(numberOfSleep);
        gameManager.countdownFireInterval();

        gameManager.removeBullet();
    }

    private void gameManagerMoney(){
        gameManager.setMyMoneys(new Money(
                Car.ROAD_SIDE_LEFT + random.nextInt(Car.ROAD_SIDE_RIGHT - Car.ROAD_SIDE_LEFT),
                -Car.HEIGHT_CAR,
                GameManager.SPEED_ITEM
        ));
        gameManager.countdownMoneyInterval();
        gameManager.moveMoney(numberOfSleep);
        gameManager.removeMoney();
        if (gameManager.isEatingMoney()){
            int money = Integer.parseInt(tfMoney.getText());
            money += 50;
            tfMoney.setText(money + "");
        }
    }

    private void gameManagerFuel(){
        if (bitSet.get(KeyEvent.VK_B)){
            int valueMoney = Integer.parseInt(tfMoney.getText());
            if (valueMoney >= MONEY_BUY_FUEL){
                if (buyInterval == 0){
                    increaseFuel(BUY_FUEL);
                    valueMoney -= MONEY_BUY_FUEL;
                    tfMoney.setText(valueMoney + "");
                    buyInterval = BUY_INTERVAL_MAX;
                }

            }
        }
        countdownBuyInterval();
        gameManager.setMyFuels(new Fuel(
                Car.ROAD_SIDE_LEFT + random.nextInt(Car.ROAD_SIDE_RIGHT - Car.ROAD_SIDE_LEFT),
                -Car.HEIGHT_CAR,
                GameManager.SPEED_ITEM
        ));
        gameManager.moveFuel(numberOfSleep);
        gameManager.countdownFuelInterval();
        if (gameManager.isEatingFuel()){
            increaseFuel(EATTING_FUEL);
        }
        if(isOutOfFuel()){
            onMenuSelectListener.onMenuSelectedListener(ACTION_SHOW_DIALOG_STOP);
            status = STOP_GAME;
        }
    }

    private void gameManagerBulletItem(){
        gameManager.setMyBulletsItem(new BulletItem(
                Car.ROAD_SIDE_LEFT + random.nextInt(Car.ROAD_SIDE_RIGHT - Car.ROAD_SIDE_LEFT),
                -Car.HEIGHT_CAR,
                GameManager.SPEED_ITEM
        ));
        gameManager.countdownBulletItemInterval();
        gameManager.moveBulletItem(numberOfSleep);
        gameManager.removeBulletItem();
        if (gameManager.isEatingBulletItem()){
            numberOfBullet += 5;
            lbBullet.setText(numberOfBullet + "x");
        }
    }

    private void setLabel(){
        lbHighScore = new JLabel("High Scores");
        lbHighScore.setFont(titleFont);
        int lbHighScoreWidth = titleFontMetrics.stringWidth(lbHighScore.getText());
        lbHighScore.setSize(lbHighScoreWidth, heightTitleFont);
        lbHighScore.setLocation(Gui.WIDTH_FRAME - DISTANCE_X_MENU, DISTANCE_Y_MENU);
        add(lbHighScore);

        lbScore = new JLabel("Scores   ");
        lbScore.setFont(titleFont);
        int lbScoreWidth = titleFontMetrics.stringWidth(lbScore.getText());
        lbScore.setBounds(
                lbHighScore.getX(),
                lbHighScore.getY() + (3 * DISTANCE_Y_MENU),
                lbScoreWidth,
                heightTitleFont
        );
        add(lbScore);

        lbMoney = new JLabel("Money   ");
        lbMoney.setFont(titleFont);
        int lbMoneyWidth = titleFontMetrics.stringWidth(lbMoney.getText());
        lbMoney.setBounds(
                lbHighScore.getX(),
                lbScore.getY() + (3 * DISTANCE_Y_MENU),
                lbMoneyWidth,
                heightTitleFont
        );
        add(lbMoney);

        lbFuel = new JLabel("Fuel   ");
        lbFuel.setFont(titleFont);
        int lbFuelWidth = titleFontMetrics.stringWidth(lbFuel.getText());
        lbFuel.setBounds(
                lbMoney.getX(),
                lbMoney.getY() + (3 * DISTANCE_Y_MENU),
                lbFuelWidth,
                heightTitleFont
        );
        add(lbFuel);

        lbBullet = new JLabel(numberOfBullet + "x");
        lbBullet.setFont(textFont);
        lbBullet.setBounds(
                lbFuel.getX(),
                lbFuel.getY() + 3 * DISTANCE_Y_MENU,
                WIDTH_TEXT_FIELD,
                heightTextFont
        );
        add(lbBullet);

        lbBulletImg = new JLabel();
        lbBulletImg.setFont(textFont);
        int lbBulletImgWidth = 22;
        int lbBulletImgHeight = 25;
        lbBulletImg.setBounds(
                lbBullet.getX() + 40,
                lbBullet.getY(),
                lbBulletImgWidth,
                lbBulletImgHeight
        );
        Image img = ImageStore.IMG_BULLET_ITEM
                .getScaledInstance(lbBulletImgWidth, lbBulletImgHeight, Image.SCALE_SMOOTH);
        lbBulletImg.setIcon(new ImageIcon(img));
        add(lbBulletImg);

        lbPause = new JLabel();
        lbPause.setFont(new Font( "Arial", Font.BOLD, 50));
        int lbPauseWidth = 200;
        int lbPauseHeight = 50;
        lbPause.setBounds(
                Gui.WIDTH_FRAME - 200,
                Gui.HEIGHT_FRAME - 120,
                lbPauseWidth,
                lbPauseHeight
        );
        add(lbPause);
    }

    private void setTextField(){
        FileManager fileManager = new FileManager();
        fileManager.readFile();

        tfHighScore = new TextField(fileManager.getHighScores() + "");
        tfHighScore.setFont(textFont);
        tfHighScore.setBounds(
                lbHighScore.getX(),
                lbHighScore.getY() + DISTANCE_Y_MENU,
                WIDTH_TEXT_FIELD,
                heightTextFont
        );
        tfHighScore.setEnabled(false);
        add(tfHighScore);

        tfScore = new TextField("0");
        tfScore.setFont(textFont);
        tfScore.setBounds(
                lbScore.getX(),
                lbScore.getY() + DISTANCE_Y_MENU,
                WIDTH_TEXT_FIELD,
                heightTextFont
        );
        tfScore.setEnabled(false);
        add(tfScore);

        tfMoney = new TextField("0");
        tfMoney.setFont(textFont);
        tfMoney.setBounds(
                lbMoney.getX(),
                lbMoney.getY() + DISTANCE_Y_MENU,
                WIDTH_TEXT_FIELD,
                heightTextFont
        );
        tfMoney.setEnabled(false);
        add(tfMoney);
    }

    private void setScores(){
        if (scoresInterval == 0){
            int scores = Integer.parseInt(tfScore.getText());
            scores += 1;
            tfScore.setText(String.valueOf(scores));
            scoresInterval = SCORES_INTERVAL_MAX;
        }
    }

    private void countdownScoresInterval(){
        if (scoresInterval > 0){
            scoresInterval -= 1;
        } else {
            scoresInterval = 0;
        }
    }

    private boolean isOutOfFuel(){
        if (valueFuel < 1){
            gameManager.stopAudioBackground();
            return true;
        }
        return false;
    }

    private void increaseFuel(String typeIncrease){
        switch (typeIncrease){
            case EATTING_FUEL:
                if (valueFuel < WIDTH_TEXT_FIELD - VALUE_INCREASE_EATTING_FUEL){
                    valueFuel += VALUE_INCREASE_EATTING_FUEL;
                } else {
                    valueFuel = WIDTH_TEXT_FIELD;
                }
                gameManager.playAudioIncreaseFuel();
                break;
            case BUY_FUEL:
                if (valueFuel < WIDTH_TEXT_FIELD - VALUE_INCREASE_EATTING_FUEL){
                    valueFuel += VALUE_INCREASE_BUY_FUEL;
                } else {
                    valueFuel = WIDTH_TEXT_FIELD;
                }
                gameManager.playAudioIncreaseFuel();
                break;
            default:
                break;
        }
    }

    private void countdownBuyInterval(){
        if (buyInterval > 0){
            buyInterval--;
        } else {
            buyInterval = 0;
        }
    }

    public void setOnMenuSelectListener(OnMenuSelectListener onMenuSelectListener) {
        this.onMenuSelectListener = onMenuSelectListener;
    }

    public int getScore() {
        int scores = Integer.parseInt(tfScore.getText());
        int money  = Integer.parseInt(tfMoney.getText());
        return scores + money;

    }
}