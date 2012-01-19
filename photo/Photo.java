package photo;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.bounds.IBoundingShape;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.GeometryInfo;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;
import processing.core.PImage;

public class Photo extends MTRectangle {
	
	public int x, y, width, height;
	public float angle;
	public AbstractMTApplication app;
	public Matrix shape;
	
	public Photo(AbstractMTApplication app , PImage texture) {
		super(app, texture);
		this.app = app;
		
		MTRectangle background = new MTRectangle(0, 0, this.getWidthXYGlobal() + 30, this.getHeightXYGlobal() + 30, app);
		background.setEnabled(false);
	
		shape = this.getGlobalMatrix();
		//this.addChild(background);
		
		this.addGestureListener(DragProcessor.class, new InertiaDragAction());
	}
}
