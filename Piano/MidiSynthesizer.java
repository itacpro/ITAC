package Piano;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

public class MidiSynthesizer {
private Synthesizer synth;
private Instrument[] instruments;
    private MidiChannel[] midiChannels;
    private int voice;
     
public MidiSynthesizer(){
try{
synth = MidiSystem.getSynthesizer();
synth.open();
Soundbank sb = synth.getDefaultSoundbank();
instruments = sb.getInstruments();
synth.loadInstrument(instruments[0]);
midiChannels = synth.getChannels();
}
catch(Exception e){
e.printStackTrace();
}
voice = 0;
}
public void close() {
        if (synth != null) {
            synth.close();
        }
        synth = null;
        instruments = null;
        midiChannels = null;
    }
public void playSound(int kNum){
midiChannels[voice].noteOn(kNum, 64);
}
public void stopSound(int kNum){
midiChannels[voice].noteOff(kNum, 64);
}
}
