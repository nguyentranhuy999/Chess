package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class Sound {

    Clip clip;
    Clip background;
    URL soundURL[] = new URL[30];

    // Constructor
    public Sound(){
        soundURL[0] = getClass().getResource("/sound/eat.wav");
        soundURL[1] = getClass().getResource("/sound/move.wav");
    }

    // Set sound file
    public void setFile(int i) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {

        }
    }

    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }
}