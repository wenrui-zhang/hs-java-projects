package Jarkanoid.UI;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
    private AudioInputStream audioInputStream = null;
    private Clip clip = null;
    private String soundPath;
    private float volumeIncrease;
    public SoundPlayer(String soundPath) {
        this(soundPath, -10f);
    }

    public SoundPlayer(String soundPath, float volumeIncrease) {
        this.soundPath = soundPath;
        this.volumeIncrease = volumeIncrease;
        load();
    }

    public void loop(int count) {
        clip.loop(count);
    }

    public void load() {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(
                    new File(soundPath));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volumeIncrease); // increase volume by volumeIncrease decibels.
    }

    public void start() {
        load();
        clip.start();
    }

    public void stop() {
        clip.stop();
    }
}
