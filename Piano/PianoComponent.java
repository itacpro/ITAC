package Piano;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mt4j.components.visibleComponents.AbstractVisibleComponent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.MultipleDragProcessor;
import org.mt4j.util.math.Plane;
import org.mt4j.util.math.Ray;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * The PianoComponent is a class that extends AbstractVisibleComponent.
 * This component draws a piano on the screen and plays the piano notes
 * according to the pressed key.
 *
 */
public class PianoComponent extends AbstractVisibleComponent {
        private PApplet app;
        private Plane plane;
        private NotePlayer notePlayer;
        private Set<NotePlayer.Key> activeKeys;
        private Map<Long, Vector3D> activeIds;
        private PImage backgroundImage;
        private PImage pressedLeftKey;
        private PImage pressedMiddleKey;
        private PImage pressedRightKey;
        private PImage pressedSustKey;
        private PImage colorMap;
        
                
        public PianoComponent(PApplet applet) {
                super(applet);
                
                notePlayer = new NotePlayer(applet);
                
                this.app = applet;

                this.activeKeys = new HashSet<NotePlayer.Key>();
                this.activeIds = new HashMap<Long, Vector3D>();
                
                this.registerInputProcessor(new MultipleDragProcessor(app));
                this.addGestureListener(MultipleDragProcessor.class, new DragListener());
                
                Vector3D norm = new Vector3D(0,0,1);
                Vector3D pointInPlane = new Vector3D(0,0,0);
                plane = new Plane(pointInPlane, norm);
                
                this.backgroundImage = this.app.loadImage("../ressources/piano/keys.png");
                this.pressedLeftKey = this.app.loadImage("../ressources/piano/key_left_pressed.png");
                this.pressedMiddleKey = this.app.loadImage("../ressources/piano/key_middle_pressed.png");
                this.pressedRightKey = this.app.loadImage("../ressources/piano/key_right_pressed.png");
                this.pressedSustKey = this.app.loadImage("../ressources/piano/key_sust_pressed.png");
                this.colorMap = this.app.loadImage("../ressources/piano/colormap.png");
                this.colorMap.loadPixels();
                
                this.setNoStroke(false);
                this.setStrokeWeight(0.8f);
        }
        
        private void pressKey(NotePlayer.Key k) {
                notePlayer.playNote(k);
        }

        private void releaseKey(NotePlayer.Key k) {
                notePlayer.stopNote(k);
        }
        
        private void verifyKeys() {
                // press new keys
                for (Long id : activeIds.keySet()) {
                        Vector3D point = activeIds.get(id);
                        NotePlayer.Key k = getKey(point.x, point.y);
                        if (k != null && k != NotePlayer.Key.KEY_TYPE_INVALID) {
                                if (!activeKeys.contains(k)) {
                                        activeKeys.add(k);
                                        pressKey(k);
                                }
                        }
                }
                
                // release active keys that does not have a point pressing
                List<NotePlayer.Key> activeKeysToRemove = new LinkedList<NotePlayer.Key>();
                for (NotePlayer.Key k : activeKeys) {
                        boolean keyFound = false;
                        for (Long id : activeIds.keySet()) {
                                Vector3D point = activeIds.get(id);
                                NotePlayer.Key auxK = getKey(point.x, point.y);
                                if (auxK != null && auxK != NotePlayer.Key.KEY_TYPE_INVALID && auxK == k) {
                                        keyFound = true;
                                        break;
                                }
                        }
                        if (!keyFound) {
                                activeKeysToRemove.add(k);
                        }
                }
                
                for (NotePlayer.Key k : activeKeysToRemove) {
                        releaseKey(k);
                        activeKeys.remove(k);
                }
        }
        
        private NotePlayer.Key getKey(float x, float y) {
                NotePlayer.Key result = NotePlayer.Key.KEY_TYPE_INVALID;
                
                int pixelColor = 0;
                try {
                        pixelColor = this.colorMap.pixels[Math.round(y) * colorMap.width + Math.round(x)];
                } catch (Exception e) {}
                
                switch (pixelColor) {
                case -65536:
                {
                        result = NotePlayer.Key.KEY_TYPE_C;
                }       
                        break;

                case -16767233:
                {
                        result = NotePlayer.Key.KEY_TYPE_C_SUST;
                }       
                        break;

                case -38400:
                {
                        result = NotePlayer.Key.KEY_TYPE_D;
                }       
                        break;
                
                case -14614401:
                {
                        result = NotePlayer.Key.KEY_TYPE_D_SUST;
                }       
                        break;
                
                case -10240:
                {
                        result = NotePlayer.Key.KEY_TYPE_E;
                }       
                        break;
                
                case -4784384:
                {
                        result = NotePlayer.Key.KEY_TYPE_F;
                }       
                        break;
                
                case -5111553:
                {
                        result = NotePlayer.Key.KEY_TYPE_F_SUST;
                }       
                        break;
                
                case -11731200:
                {
                        result = NotePlayer.Key.KEY_TYPE_G;
                }       
                        break;
                
                case -65316:
                {
                        result = NotePlayer.Key.KEY_TYPE_G_SUST;
                }       
                        break;

                case -16711792:
                {
                        result = NotePlayer.Key.KEY_TYPE_A;
                }       
                        break;

                case -65426:
                {
                        result = NotePlayer.Key.KEY_TYPE_A_SUST;
                }       
                        break;
                case -16711681:
                {
                        result = NotePlayer.Key.KEY_TYPE_B;
                }       
                        break;

                case -16739073:
                {
                        result = NotePlayer.Key.KEY_TYPE_C_UP;
                }       
                        break;
                        
                default:
                        break;
                }
                
                //System.out.println("color: " + pixelColor);
                return result;
        }
        
        private class DragListener implements IGestureEventListener{
                public boolean processGestureEvent(MTGestureEvent ge) {
                        DragEvent de = (DragEvent)ge;
                        Vector3D to = de.getTo();
                        switch (de.getId()) {
                        case DragEvent.GESTURE_DETECTED:{       
                                activeIds.put(de.getDragCursor().getId(), to);
                        }break;
                        case DragEvent.GESTURE_UPDATED:{
                                activeIds.put(de.getDragCursor().getId(), to);
                        }break;
                        case DragEvent.GESTURE_ENDED:{
                                activeIds.remove(de.getDragCursor().getId());
                        }break;
                        default:
                                break;
                        }
                        verifyKeys();
                        return true;
                }
        }
        
        public void drawPiano() {
                // setting piano background
                this.app.image(this.backgroundImage, 0, 0);
          
                for (NotePlayer.Key k : activeKeys) {
                        switch (k) {
                        case KEY_TYPE_C:
                                this.app.image(pressedLeftKey, 0, 0);
                                break;
                        case KEY_TYPE_C_SUST:
                                this.app.image(pressedSustKey, 70, 0);
                                break;

                        case KEY_TYPE_D:
                                this.app.image(pressedMiddleKey, 100, 0);
                                break;
                                
                        case KEY_TYPE_D_SUST:
                                this.app.image(pressedSustKey, 170, 0);
                                break;
                        
                        case KEY_TYPE_E:
                                this.app.image(pressedRightKey, 200, 0);
                                break;
                        
                        case KEY_TYPE_F:
                                this.app.image(pressedLeftKey, 300, 0);
                                break;
                        
                        case KEY_TYPE_F_SUST:
                                this.app.image(pressedSustKey, 370, 0);
                                break;
                        
                        case KEY_TYPE_G:
                                this.app.image(pressedMiddleKey, 400, 0);
                                break;

                        case KEY_TYPE_G_SUST:
                                this.app.image(pressedSustKey, 470, 0);
                                break;
                                
                        case KEY_TYPE_A:
                                this.app.image(pressedMiddleKey, 500, 0);
                                break;
                                
                        case KEY_TYPE_A_SUST:
                                this.app.image(pressedSustKey, 570, 0);
                                break;
                                
                        case KEY_TYPE_B:
                                this.app.image(pressedRightKey, 600, 0);
                                break;
                        
                        case KEY_TYPE_C_UP:
                                this.app.image(pressedLeftKey, 700, 0);
                                break;
                                
                        default:
                                break;
                        }
          }                               
        }

        @Override
        public void drawComponent(PGraphics g) {                 
                drawPiano();
        }


        @Override
        protected boolean componentContainsPointLocal(Vector3D testPoint) {
                return plane.componentContainsPointLocal(testPoint);
        }

        @Override
        public Vector3D getIntersectionLocal(Ray ray) {
                return plane.getIntersectionLocal(ray);
        }
        
}
