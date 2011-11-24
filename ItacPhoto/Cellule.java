package ItacPhoto;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;

import processing.core.PApplet;
import processing.core.PImage;

public class Cellule extends MTListCell {
	public PApplet app;
	public MTList liste;
	public PImage pImage;
	
	public Cellule(PApplet applet, float x, float y, PImage _image) {
		super(applet, x, y);
		
		app = applet;
		pImage = _image;
		
		MTImage image = creerMTImage(pImage, app);
		
		//On effectue une rotation sur l'image pour qu'elle s'affiche correctement
		image.rotateZ(image.getCenterPointGlobal(), 90, TransformSpace.LOCAL);
		
		//On ajuste la position de l'image dans la cellule
		image.setPositionGlobal(getCenterPointLocal());
		
		setTailleImage(image, 180, 220);
		
		addChild(image);
		setFillColor(new MTColor(150,150,150,0));
		setNoStroke(true);
		
		onTap();
	}
	
	public void ouvertureImage() {
		Photo photo = new Photo(app, pImage, liste);
		
		destroy();
	}
	
	public void onTap() {
		registerInputProcessor(new TapProcessor(app, 15));
		addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{
					ouvertureImage();
				}
				return false;
			}
		});
	}
	
	/** CrŽerMTImage() */
	private MTImage creerMTImage(PImage image, PApplet app){
		MTImage mtImage = new MTImage(image, app);
		return mtImage;
	}
	
	/** SetTailleImage() */
	private void setTailleImage(MTImage photo, int hauteurMax, int largeurMax){
		if(photo.getHeightXY(TransformSpace.LOCAL) > photo.getWidthXY(TransformSpace.LOCAL))
		{
			photo.setHeightXYGlobal(hauteurMax);
		}
		else
		{
			photo.setWidthXYGlobal(largeurMax);
		}
	}
}
