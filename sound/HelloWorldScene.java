package sound;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

public class HelloWorldScene extends AbstractScene {

        public HelloWorldScene(MTApplication mtApplication, String name) {
                super(mtApplication, name);
                
                MTColor white = new MTColor(255,255,255);
                this.setClearColor(new MTColor(146, 150, 188, 255));
                //Show touches
                this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
                
                IFont fontArial = FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                                50,     //Font size
                                white); //Font color
                //Create a textfield
                MTTextArea textField = new MTTextArea(mtApplication, fontArial); 
                
                textField.setNoStroke(true);
                textField.setNoFill(true);
                // add click noise for cursor up and down
                this.registerGlobalInputProcessor(new Mp3CursorTracer("../ressources/sound/click.mp3", Mp3CursorTracer.INPUT_DETECTED));
                this.registerGlobalInputProcessor(new Mp3CursorTracer("../ressources/sound/click.mp3", Mp3CursorTracer.INPUT_ENDED));
                //textField.addGestureListener(DragProcessor.class, new Mp3GestureAction("../ressources/sound/click.mp3"));
                
                textField.setText("Hello World!");
                //Center the textfield on the screen
                textField.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height/2f));
                //Add the textfield to our canvas
                this.getCanvas().addChild(textField);
        }
        
        public void onEnter() {}
        
        public void onLeave() {}
}
