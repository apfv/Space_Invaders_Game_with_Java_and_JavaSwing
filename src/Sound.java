import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.IOException;

public class Sound {
    private final String hit;
    private final String shoot;
    private final String destroy;

    public Sound() {
        this.hit = "sound\\hit.wav";
        this.shoot = "sound\\shoot.wav";
        this.destroy = "sound\\destroy.wav";
    }

    public void soundHit() {
        play(hit);
    }

    public void soundShoot() {
        play(shoot);
    }

    public void soundDestroy() {
        play(destroy);
    }

    private void play(String url) {
        try {

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(url));

            Clip clip = AudioSystem.getClip();

            clip.open(audioIn);

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

            audioIn.close();

            clip.start();

        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.err.println(e);
        }
    }
}
