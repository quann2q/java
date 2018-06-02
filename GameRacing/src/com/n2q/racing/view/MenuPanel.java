package com.n2q.racing.view;

import com.n2q.racing.manager.GameManager;
import com.n2q.racing.manager.ImageStore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPanel extends BasePanel {
    public static final String ACTION_PLAY_GAME = "ACTION_PLAY_GAME";
    public static final String ACTION_EXIT_GAME = "ACTION_EXIT_GAME";
    public static final String ACTION_MUTE_AUDIO_GAME = "ACTION_MUTE_AUDIO_GAME";
    public static final String ACTION_UN_MUTE_AUDIO_GAME = "ACTION_UN_MUTE_AUDIO_GAME";
    private boolean statusAudio = true;
    private static final int SIZE_LABEL = 100;
    private Color color;
    private JLabel lbPlayGame;
    private JLabel lbExitGame;
    private JLabel lbAudio;

    private Font textFont;
    private FontMetrics textFontMetrics;
    private int heightTextFont;
    private GameManager gameManager;

    private OnMenuSelectListener onMenuSelectListener;

    @Override
    public void initContainer() {
        setLayout(null);
        setBackground(null);
        color = new Color(255, 255, 255);
        gameManager = new GameManager();
        gameManager.playAudioBackGroundMenu();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(ImageStore.IMG_BACKGROUND, 0, 0, this);
    }

    @Override
    public void initComponents() {
        textFont = new Font( "Arial", Font.BOLD, 50);
        textFontMetrics = getFontMetrics(textFont);
        heightTextFont  = textFontMetrics.getHeight();
        setLabel();
    }

    @Override
    public void registerListeners() {
        lbPlayGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onMenuSelectListener.onMenuSelectedListener(ACTION_PLAY_GAME);
                gameManager.stopAudioBackGroundMenu();
            }
        });

        lbExitGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onMenuSelectListener.onMenuSelectedListener(ACTION_EXIT_GAME);
            }
        });

        lbAudio.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (statusAudio){
                    gameManager.stopAudioBackGroundMenu();
                    onMenuSelectListener.onMenuSelectedListener(ACTION_MUTE_AUDIO_GAME);
                    Image imgMuteAudio = ImageStore.IMG_SOUND_OFF.getScaledInstance
                            (SIZE_LABEL, SIZE_LABEL, Image.SCALE_SMOOTH);
                    lbAudio.setIcon(new ImageIcon(imgMuteAudio));
                    statusAudio = !statusAudio;
                } else {
                    gameManager.playAudioBackGroundMenu();
                    onMenuSelectListener.onMenuSelectedListener(ACTION_UN_MUTE_AUDIO_GAME);
                    Image imgUnMuteAudio = ImageStore.IMG_SOUND_ON.getScaledInstance
                            (SIZE_LABEL, SIZE_LABEL, Image.SCALE_SMOOTH);
                    lbAudio.setIcon(new ImageIcon(imgUnMuteAudio));
                    statusAudio = !statusAudio;
                }
            }
        });
    }

    private void setLabel(){
        lbPlayGame = new JLabel("PLAY");
        lbPlayGame.setFont(textFont);
        lbPlayGame.setForeground(color);
        int lbPlayGameWidth = textFontMetrics.stringWidth(lbPlayGame.getText() + " ");
        lbPlayGame.setSize(lbPlayGameWidth, heightTextFont);
        lbPlayGame.setLocation((Gui.WIDTH_FRAME - lbPlayGameWidth)/2, Gui.HEIGHT_FRAME - 190);
        add(lbPlayGame);

        lbExitGame = new JLabel("EXIT");
        lbExitGame.setFont(textFont);
        lbExitGame.setForeground(color);
        int lbExitGameWidth = textFontMetrics.stringWidth(lbExitGame.getText() + " ");
        lbExitGame.setSize(lbExitGameWidth, heightTextFont);
        lbExitGame.setLocation((Gui.WIDTH_FRAME - lbExitGameWidth)/2, lbPlayGame.getY() + heightTextFont + 20);
        add(lbExitGame);

        lbAudio = new JLabel();
        lbAudio.setBounds(
                (Gui.WIDTH_FRAME - SIZE_LABEL)/2,
                100,
                SIZE_LABEL, SIZE_LABEL);
        Image imgUnMuteAudio = ImageStore.IMG_SOUND_ON.getScaledInstance
                (SIZE_LABEL, SIZE_LABEL, Image.SCALE_SMOOTH);
        lbAudio.setIcon(new ImageIcon(imgUnMuteAudio));
        add(lbAudio);
    }

    public void setOnMenuSelectListener(OnMenuSelectListener onMenuSelectListener) {
        this.onMenuSelectListener = onMenuSelectListener;
    }

}