package photo;

import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class Cellule extends MTListCell {
	
	public int id;
	public MTRectangle background;
	
	public Cellule(float width, float height, PApplet applet) {
		super(applet, width, height);
		// TODO Auto-generated constructor stub
		
		background = new MTRectangle(0, 0, 198, 198, applet);
		background.setPositionRelativeToParent(new Vector3D(width/2, 150));
		background.setTexture(applet.loadImage("../ressources/photo/album.png"));
		background.setNoStroke(true);
		background.setVisible(false);
		
		this.addChild(background);
	}

}
