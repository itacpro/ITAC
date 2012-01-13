package Widgets;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

public class BoutonCalc extends MTRectangle {
	
	private String texte;
	
	public BoutonCalc(String _texte, float x, float y, float width, float height, AbstractMTApplication pApplet, String texture) {
		super(x, y, width, height, pApplet);
		
		this.setTexture(pApplet.loadImage(texture));
		this.setNoStroke(true);
		
		texte = _texte;
		
		Vector3D centre = this.getCenterPointGlobal();
		
		MTTextArea texteBouton = new MTTextArea(pApplet);
		texteBouton.setText(texte);
		texteBouton.getCenterPointGlobal();
		texteBouton.setPositionGlobal(new Vector3D(x + width/2, y + height/2));
		texteBouton.setNoFill(true);
		texteBouton.setNoStroke(true);
		texteBouton.setFontColor(new MTColor(255, 255, 255));
		texteBouton.setPickable(false);
		this.addChild(texteBouton);
		
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
	}
	
	public String getText(){
		return texte;
	}
}
