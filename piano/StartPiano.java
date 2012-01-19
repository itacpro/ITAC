package piano;

import org.mt4j.MTApplication;
import org.mt4j.input.inputSources.MacTrackpadSource;

public class StartPiano extends MTApplication {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        /**
         * @param args
         */
        public static void main(String[] args) {
                initialize();
        }
       
        @Override
        public void startUp() {
                addScene(new PianoScene(this, "Scene 1"));
              //Gestion du touchpad MT des MacBooks.
        		getInputManager().registerInputSource(new MacTrackpadSource(this));
        }

}