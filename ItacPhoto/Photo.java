package ItacPhoto;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;

public class Photo extends MTImage {

	public MTList liste;
	public PApplet app;
	public PImage pImage;
	
	public Photo(PApplet pApplet, PImage texture, MTList _liste) {
		super(pApplet, texture);
		
		liste = _liste;
		app = pApplet;
		pImage = texture;
		
		ouverturePhoto();
		onDoubleTap();
		
	}
	
	/** OuverturePhoto() */
	public void ouverturePhoto() {
		//Positionnement aux coordonnŽes de la cellule
		setPositionRelativeToParent(liste.getCenterPointGlobal());
		
		//DŽfinition de l'animation de la translation
		final Animation translation = new Animation("translation", new MultiPurposeInterpolator(app.width/2f, app.height/3f, 500, 0, 1, 1) , this);
		translation.addAnimationListener(new IAnimationListener(){
        	@Override
        	public void processAnimationEvent(AnimationEvent ae) {
        		//On rŽcupre les coordonnŽes actuelles de la photo
        		Vector3D photoPos = new Vector3D(getCenterPointRelativeToParent());
        		
        		//Calcul des variables du vecteur
        		int x = (int) (app.width/2f - photoPos.x)/5;
        		int y = (int) (app.height/3f - photoPos.y)/5;
        		
        		//Application de la translation
        		translate(new Vector3D(x, y), TransformSpace.RELATIVE_TO_PARENT);
        	}
        }).start();
		
		setTailleImage(400, 400);
		
		//Ajout de l'inertie sur la photo
		addGestureListener(DragProcessor.class, new InertiaDragAction(125, 0.90f, 25));
	}
	
	/** FermeturePhoto() */
	public void fermeturePhoto() {
		final MultiPurposeInterpolator interpolator = new MultiPurposeInterpolator(0, 499, 500, 0, 1, 1);
		
		final Animation scale = new Animation("scale", interpolator, this);
		scale.addAnimationListener(new IAnimationListener(){
        	@Override
        	public void processAnimationEvent(AnimationEvent ae) {
        		
        		scale((float)0.7, (float)0.7, 0, getCenterPointGlobal());
        		
        		if(interpolator.isFinished())
        		{
        			Cellule cellule = new Cellule(app, 0, 0, pImage);
        			liste.addChild(cellule);
        			
        			destroy();
        		}
        	}
        }).start();
	}
	
	/** OnDoubleTap() */
	public void onDoubleTap() {
		registerInputProcessor(new TapProcessor(app, 25, true, 350));
		addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				
				if (te.isDoubleTap()){
					fermeturePhoto();
				}
				return false;
			}
		});
	}
	
	/** SetTailleImage() */
	private void setTailleImage(int hauteurMax, int largeurMax){
		if(getHeightXY(TransformSpace.LOCAL) > getWidthXY(TransformSpace.LOCAL))
		{
			setHeightXYGlobal(hauteurMax);
		}
		else
		{
			setWidthXYGlobal(largeurMax);
		}
	}
}
