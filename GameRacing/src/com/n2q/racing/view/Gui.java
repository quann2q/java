package com.n2q.racing.view;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class Gui extends JFrame implements ViewInitializer, OnMenuSelectListener {
    public static final int WIDTH_FRAME = 800;
    public static final int HEIGHT_FRAME = 800;
    private MenuPanel menuPanel;
    private GamePlayPanel gamePlayPanel;

    public Gui(){
        initContainer();
        initComponents();
        registerListeners();
    }

    @Override
    public void initContainer() {
        setTitle("RACING CAR");
        setLayout(new CardLayout());
        setSize(WIDTH_FRAME, HEIGHT_FRAME);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void initComponents() {
        menuPanel = new MenuPanel();
        getContentPane().add(menuPanel);
    }

    @Override
    public void registerListeners() {
        menuPanel.setOnMenuSelectListener(this);
    }

    @Override
    public void onMenuSelectedListener(String name) {
        switch (name){
            case MenuPanel.ACTION_PLAY_GAME:
                remove(menuPanel);
                gamePlayPanel = new GamePlayPanel();
                gamePlayPanel.setOnMenuSelectListener(this);
                getContentPane().add(gamePlayPanel);
                gamePlayPanel.startGame();
                gamePlayPanel.requestFocusInWindow();
                getContentPane().validate();
                break;
            case MenuPanel.ACTION_EXIT_GAME:
                System.exit(0);
                break;
            case GamePlayPanel.ACTION_SHOW_DIALOG_STOP:
                System.out.println("Hiển thị JPanel Dialog STOP");
                String[] buttonsStop = { "OK", "Play Again", "Back To Menu", "Exit" };
                UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(
                        "Arial", Font.BOLD, 30)));
                int rcStop = JOptionPane.showOptionDialog(null,
                        "Your Score: " + gamePlayPanel.getScore(), "Your Score",
                        JOptionPane.WARNING_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, buttonsStop, buttonsStop[0]);
                switch (rcStop){
                    case 0:
                        break;
                    case 1:
                        remove(gamePlayPanel);
                        gamePlayPanel = new GamePlayPanel();
                        gamePlayPanel.setOnMenuSelectListener(this);
                        getContentPane().add(gamePlayPanel);
                        gamePlayPanel.startGame();
                        gamePlayPanel.requestFocusInWindow();
                        getContentPane().validate();
                        break;
                    case 2:
                        remove(gamePlayPanel);
                        menuPanel = new MenuPanel();
                        menuPanel.setOnMenuSelectListener(this);
                        getContentPane().add(menuPanel);
                        menuPanel.requestFocusInWindow();
                        getContentPane().validate();
                        break;
                    case 3:
                        System.exit(0);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}