package Photo;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;
import processing.core.PImage;

public class Photo extends MTRectangle {
	
	public int x, y, width, height;
	public float angle;
	public AbstractMTApplication app;
	
	public Photo(AbstractMTApplication app , PImage texture) {
		super(app, texture);
		this.app = app;
	}
}
