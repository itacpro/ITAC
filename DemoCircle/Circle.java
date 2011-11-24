package DemoCircle;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class Circle extends AbstractScene {

	public Circle(AbstractMTApplication app, String name) {
		super(app, name);
		
		final MTEllipse cercle = new MTEllipse(app, new Vector3D(app.width/2f, app.height/2f), 200, 200);
		
		final MTEllipse appli = new MTEllipse(app, new Vector3D(cercle.getWidthXY(TransformSpace.LOCAL), cercle.getHeightXY(TransformSpace.LOCAL)), 50, 50);
		appli.setFillColor(new MTColor(0, 0, 0));
		
		
		cercle.addChild(appli);
		
		//cercle.setPickable(false);
		
		cercle.unregisterAllInputProcessors();
		cercle.removeAllGestureEventListeners();
		cercle.registerInputProcessor(new TapProcessor(app, 15));
		cercle.addGestureListener(DragProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
					
				cercle.rotateZ(cercle.getCenterPointGlobal(), 1);
				
				return false;
			}
		});
		
		getCanvas().addChild(cercle);
	}

}
