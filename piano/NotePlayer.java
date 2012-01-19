package piano;

import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import ddf.minim.AudioOutput;
import ddf.minim.Minim;

public class NotePlayer {

        private PApplet app;

        public NotePlayer(PApplet app) {
                this.app = app;
                keys = new HashMap<Key, NoteMinim>();
        }

        public enum Key {
                KEY_TYPE_C,
                KEY_TYPE_C_SUST,
                KEY_TYPE_D,
                KEY_TYPE_D_SUST,
                KEY_TYPE_E,
                KEY_TYPE_F,
                KEY_TYPE_F_SUST,
                KEY_TYPE_G,
                KEY_TYPE_G_SUST,
                KEY_TYPE_A,
                KEY_TYPE_A_SUST,
                KEY_TYPE_B,
                KEY_TYPE_C_UP,
                KEY_TYPE_INVALID
        }

        private Map<Key, NoteMinim> keys;
       
        public void playNote(Key k) {
                closeMinim(k);

                Minim minim = new Minim(app);
                AudioOutput out = minim.getLineOut(Minim.MONO, 2048);
               
                NoteMinim nm = new NoteMinim();
                nm.setMinim(minim);
                nm.setAudioOutput(out);
               
                keys.put(k, nm);

                if (k == Key.KEY_TYPE_A) {
                        out.playNote("A");
                } else if (k == Key.KEY_TYPE_B) {
                        out.playNote("B");
                } else if (k == Key.KEY_TYPE_C) {
                        out.playNote("C");
                } else if (k == Key.KEY_TYPE_D) {
                        out.playNote("D");
                } else if (k == Key.KEY_TYPE_E) {
                        out.playNote("E");
                } else if (k == Key.KEY_TYPE_F) {
                        out.playNote("F");
                } else if (k == Key.KEY_TYPE_G) {
                        out.playNote("G");
                } else if (k == Key.KEY_TYPE_C_SUST) {
                        out.playNote("C#");
                } else if (k == Key.KEY_TYPE_D_SUST) {
                        out.playNote("D#");
                } else if (k == Key.KEY_TYPE_F_SUST) {
                        out.playNote("F#");
                } else if (k == Key.KEY_TYPE_G_SUST) {
                        out.playNote("G#");
                } else if (k == Key.KEY_TYPE_A_SUST) {
                        out.playNote("A#");
                } else if (k == Key.KEY_TYPE_C_UP) {
                        out.playNote("C5");
                }
               
                if (out.isMuted()) {
                        out.unmute();
                }
                /*
                out.pan();

                // create a SquareWave with a frequency of 440 Hz,
                // an amplitude of 1 and the same sample rate as out
                SquareWave square;
                if (k == NotePlayer.Key.KEY_TYPE_C) {
                        square = new SquareWave(240, 1, 44100);
                } else {
                        square = new SquareWave(440, 1, 44100);
                }
                // create a LowPassSP filter with a cutoff frequency of 200 Hz
                // that expects audio with the same sample rate as out
                LowPassSP lowpass = new LowPassSP(200, 44100);

                // now we can attach the square wave and the filter to our output
                out.addSignal(square);
                out.addEffect(lowpass);

                if (out.isMuted()) {
                        out.unmute();
                }*/
        }

        public void stopNote(Key k) {
               
        }

        private void closeMinim(Key k) {
                if (keys.containsKey(k)) {
                        NoteMinim nm = keys.get(k);
                        nm.getAudioOutput().mute();
                        nm.getAudioOutput().close();
                        nm.getMinim().stop();
                }
        }
       
        private class NoteMinim {
                private AudioOutput out;
                private Minim minim;

                public AudioOutput getAudioOutput() {
                        return out;
                }

                public void setAudioOutput(AudioOutput out) {
                        this.out = out;
                }

                public Minim getMinim() {
                        return minim;
                }

                public void setMinim(Minim minim) {
                        this.minim = minim;
                }
        }
}