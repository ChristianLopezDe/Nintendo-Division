package org.depaul.logic.sound;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
public class Sound {
    Clip clip;
    URL soundURL[] = new URL[7];
    boolean isPlayable = true;

    public Sound() {
        soundURL[0] = getClass().getResource("/sound/calm1.wav");
        soundURL[1] = getClass().getResource("/sound/Shooting-Stars_1.wav");
        soundURL[2] = getClass().getResource("/sound/hurt.wav");
        soundURL[3] = getClass().getResource("/sound/levelup.wav");
        soundURL[4] = getClass().getResource("/sound/Pac-Death-2.wav");
        soundURL[5] = getClass().getResource("/sound/Pop-1.wav");
        soundURL[6] = getClass().getResource("/sound/Welcome-Home.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream aInputStream = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(aInputStream);
        } catch(Exception e) {
            System.out.println("\nError sound did not load\n" + e);
            isPlayable = false;
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    public void close() {
        clip.close();
    }

    public void reset() {
        clip.setMicrosecondPosition(0);
    }

    public boolean isPlaying() {
        return clip.isRunning();
    }

}
