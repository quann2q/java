package com.n2q.racing.manager;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class PlayerWav {
    private Clip clip; // chỉ mở .wav

    public PlayerWav(String filename) {
        try {
            URL url = getClass().getResource("/res/raw/" + filename + ".wav");
            AudioInputStream input = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(input);
        } catch (LineUnavailableException
                | UnsupportedAudioFileException
                | IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip.isOpen() && !clip.isRunning()) {
            clip.start();
        }
    }

    public void stop() {
        clip.stop();
    }

    public void loop(int count) {
        // Clip.LOOP_CONTINUOUSLY: chạy liên tục
        clip.loop(count);
    }

}