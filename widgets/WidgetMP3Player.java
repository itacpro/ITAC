package widgets;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTSlider;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;

import processing.core.PGraphics;

public class WidgetMP3Player extends MTRectangle{
    
     private static final int WIDTH = 408;
     private static final int HEIGHT = 220;
     private int x;
     private int y;
    
     private AudioTrack audioTrack;
     private MTEllipse playButton;
     private MTTextArea infoPanel;
     private MTSlider slider;
     private MTRectangle cover;
    
     private boolean sliderUpdating = false;
    
     public WidgetMP3Player(final AbstractMTApplication mtapp, int _x, int _y){
           super(mtapp, _x, _y, WIDTH, HEIGHT);
           
           x = _x;
           y = _y;
           
           this.setTexture(mtapp.loadImage("../ressources/widget-player-mp3/background.png"));
           this.setNoStroke(true);
           
           addGestureListener(DragProcessor.class, new InertiaDragAction());
           audioTrack = new AudioTrack("../medias/musique/Avicii - Levels.mp3", (MTApplication) mtapp);
          
           infoPanel = new MTTextArea(mtapp, x + 130, y, WIDTH, HEIGHT*5/8);
           infoPanel.setText("Track : "+ audioTrack.getTitle()+ "\n"+ "Artist : " + audioTrack.getArtist()+ "\n"+ getFormattedTime(audioTrack.getPosition()/1000) +" / "+ getFormattedTime(audioTrack.getLength()/1000));
           infoPanel.setFont(FontManager.getInstance().createFont(mtapp, "AlteHaasGroteskRegular.ttf", 24, new MTColor(220, 222, 224), true));
           infoPanel.setNoFill(true);
           infoPanel.setNoStroke(true);
          
           infoPanel.setPickable(false);
          
           slider = new MTSlider(mtapp, x + 20, y + 160, 361, 19, 0, audioTrack.getLength());
           slider.getOuterShape().setTexture(mtapp.loadImage("../ressources/widget-player-mp3/sliderbg.png"));
           slider.getOuterShape().setNoStroke(true);
           
           slider.getKnob().addGestureListener(DragProcessor.class, new IGestureEventListener() {
                 public boolean processGestureEvent(MTGestureEvent ge) {
                       DragEvent de = (DragEvent)ge;
                       switch (de.getId()) {
                       case MTGestureEvent.GESTURE_STARTED:
                                   sliderUpdating = true;
                             break;
                       case MTGestureEvent.GESTURE_ENDED:
                                   sliderUpdating = false;
                                   audioTrack.cue((int)slider.getValue());  
                             break;
                       default:
                             break;
                       }                
                       return false;
                 }
           });
          
           int playBtnRadius = HEIGHT/8;
           playButton = new MTEllipse(mtapp, new Vector3D(x + 170, y + 130), playBtnRadius, playBtnRadius);
           playButton.setTexture(mtapp.loadImage("../ressources/widget-player-mp3/play.png"));
           playButton.setNoStroke(true);
           audioTrack.pause();
           sliderUpdating = false;
           playButton.unregisterAllInputProcessors();
           playButton.registerInputProcessor(new TapProcessor(mtapp));
           playButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
                 @Override
                 public boolean processGestureEvent(MTGestureEvent ge) {
                       TapEvent te = (TapEvent)ge;
                       if(te.getId() == MTGestureEvent.GESTURE_ENDED){
                             if(!audioTrack.isPlaying()){
                                   audioTrack.play();
                                   playButton.setTexture(mtapp.loadImage("../ressources/widget-player-mp3/play-down.png"));
                             }
                             else{
                                   audioTrack.pause();
                                   playButton.setTexture(mtapp.loadImage("../ressources/widget-player-mp3/play.png"));
                             }
                       }
                      
                       return false;
                 }
           });
          
           cover = new MTRectangle(mtapp, x + 15, y + 15, 120, 120);
           cover.setTexture(mtapp.loadImage("../medias/musique/Avicii - Levels.jpg"));
           cover.setStrokeColor(MTColor.BLACK);
           cover.removeAllGestureEventListeners();
           
           this.addChild(infoPanel);
           this.addChild(slider);
           this.addChild(playButton);
           this.addChild(cover);
     }
    
     @Override
     public void drawComponent(PGraphics g){
    	 if(!sliderUpdating)slider.setValue( audioTrack.getPosition());
         infoPanel.setText("Track : "+ audioTrack.getTitle()+ "\n"+ "Artist : " + audioTrack.getArtist()+ "\n"+ getFormattedTime(audioTrack.getPosition()/1000) +" / "+ getFormattedTime(audioTrack.getLength()/1000));
         super.drawComponent(g);
     }
    
     /**
      * Convert time from milliseconds to a string time format mm:ss
      * @param sec time in milliseconds in integers
      * @return the formatted string
      */
     private String getFormattedTime(int sec){
           int min = sec / 60;
           sec %= 60;
           String time = ""+sec;
           if(sec < 10)time = "0"+time;
           time = min+":"+time;
           if(min < 10)time = "0"+time;
           return time;
     }
    
     /**
      * destroy the component visually but also dispose of the audio track
      */
     @Override
     public void destroyComponent(){
           super.destroyComponent();
           audioTrack.close();
     }
}