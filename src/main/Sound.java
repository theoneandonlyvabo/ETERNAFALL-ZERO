package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

    Clip clips[] = new Clip[30];
    URL soundURL[] = new URL[30];

    float volumeURL[] = new float[30];

    public Sound() {

        soundURL[0]  = getClass().getResource("/sound/ambience_wind.wav");
        volumeURL[0] = 1.0f;

        soundURL[1]  = getClass().getResource("/sound/ambience_water.wav");
        volumeURL[1] = 0.1f;

        soundURL[2]  = getClass().getResource("/sound/load_dungeon.wav");
        volumeURL[2] = 1.0f;

        soundURL[3]  = getClass().getResource("/sound/sfx_nav.wav");
        volumeURL[3] = 1.0f;

    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clips[i] = AudioSystem.getClip();
            clips[i].open(ais);
            setVolume(i, volumeURL[i]);
            System.out.println("Audio loaded: " + soundURL[i]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(int i)  { if (clips[i] != null) clips[i].start(); }
    public void loop(int i)  { if (clips[i] != null) clips[i].loop(Clip.LOOP_CONTINUOUSLY); }
    public void stop(int i)  { if (clips[i] != null) clips[i].stop(); }

    public void pause(int i) {
        if (clips[i] != null && clips[i].isRunning()) clips[i].stop();
    }

    public void resume(int i) {
        if (clips[i] != null && !clips[i].isRunning()) clips[i].start();
    }

    // =========================================================
    // DYNAMIC SFX — load dan play langsung dari resource path
    // =========================================================
    public void playSfx(String resourcePath) {
        try {
            URL url = getClass().getResource(resourcePath);
            if (url == null) return;
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            // silent fail — sfx bukan critical
        }
    }

    // =========================================================

    static final float VOLUME_MIN = 0.0001f;
    static final float VOLUME_MAX = 1.0f;

    public void setVolume(int i, float volume) {
        if (clips[i] == null) return;
        try {
            javax.sound.sampled.FloatControl fc = (javax.sound.sampled.FloatControl)
                clips[i].getControl(javax.sound.sampled.FloatControl.Type.MASTER_GAIN);
            volume = Math.max(VOLUME_MIN, Math.min(volume, VOLUME_MAX));
            float dB = (float) (Math.log10(volume) * 20);
            fc.setValue(Math.max(fc.getMinimum(), Math.min(dB, fc.getMaximum())));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void stopAll() {
        for (Clip c : clips) if (c != null && c.isRunning()) c.stop();
    }

    public void pauseAll() {
        for (Clip c : clips) if (c != null && c.isRunning()) c.stop();
    }

    public void resumeAll() {
        for (Clip c : clips) if (c != null && !c.isRunning()) c.start();
    }

}