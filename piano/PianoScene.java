package piano;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import appLauncher.AppLauncher;


/**
 * Scene to use the PianoComponent.
 *
 */
public class PianoScene extends AbstractScene {
        private MTApplication mtApp;
        private PianoComponent piano;
       
        public PianoScene(MTApplication mtApplication, String name) {
                super(mtApplication, name);
                this.mtApp = mtApplication;
                this.setClearColor(new MTColor(0, 0, 0));
               /*
                //Create tail component
                piano = new PianoComponent(mtApp);
                this.getCanvas().addChild(piano);
               
                //Add tap&hold gesture to clear all tails
                TapAndHoldProcessor tapAndHold = new TapAndHoldProcessor(mtApplication);
                tapAndHold.setMaxFingerUpDist(10);
                tapAndHold.setHoldTime(3000);
                piano.registerInputProcessor(tapAndHold);
                piano.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
                        public boolean processGestureEvent(MTGestureEvent ge) {
                                TapAndHoldEvent t = (TapAndHoldEvent)ge;
                                if (t.getId() == TapAndHoldEvent.GESTURE_ENDED && t.isHoldComplete()){
                                        //
                                }
                                return false;
                        }
                });
                piano.addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(mtApp, getCanvas()));
               
               */
                
                //MTPiano piano = new MTPiano(mtApp, new Vector3D(mtApp.width/2, mtApp.height/2));
                //getCanvas().addChild(piano);
                
                this.getCanvas().addChild(new MTBackgroundImage(mtApp, mtApp.loadImage("../ressources/login/fond-ecran-default.png"), false));
                
                AppLauncher launcher = new AppLauncher(mtApp);
                getCanvas().addChild(launcher);
                
                //AirHockeyScene test = new AirHockeyScene(mtApplication, "Test");
                //this.getCanvas().addChild(test);
                
                //Add touch feedback
                this.registerGlobalInputProcessor(new CursorTracer(mtApp, this));
        }
       
        @Override
        public void init() {
                // TODO Auto-generated method stub
               
        }

        @Override
        public void shutDown() {
                // TODO Auto-generated method stub
               
        }

}