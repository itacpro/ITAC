package photo;

import org.mt4j.AbstractMTApplication;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;


public class PhotoSceneTest extends AbstractScene{
	
	public PhotoSceneTest(AbstractMTApplication _app, String _name) {
		super(_app, _name);
		
		this.registerGlobalInputProcessor(new CursorTracer(_app, this));
		
		PhotoScene photoapp = new PhotoScene(_app, 0, 0, _app.width, _app.height);
		this.getCanvas().addChild(photoapp);
	}
}
