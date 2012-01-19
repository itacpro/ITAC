package appLauncher;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;

import photo.PhotoScene;
import piano.MTPiano;
import processing.core.PApplet;
import processing.core.PImage;

public class AppLauncher extends MTRectangle{
	
	private PApplet app;
	private MTEllipse logoCentral;
	private String[] applications;
	private AppLauncher launcher;
	private PImage background;
	
	public AppLauncher(PApplet pApplet) {
		super(pApplet, pApplet.width, pApplet.height);
		
		app = pApplet;
		launcher = this;
		
		background = app.loadImage("../ressources/launcher/interface.png");
		
		logoCentral = new MTEllipse(pApplet, new Vector3D(pApplet.getWidth()/2, pApplet.getHeight()/2 + 5), 266, 271);
		logoCentral.setVisible(false);
		
		applications = new String[2];
		applications[0] = "photos";
		applications[1] = "piano";
		
		
		setLaucherDesign();
	}
	
	public void launchApp(String name){
		if(name.equals("photos"))
		{
			PhotoScene photoApp = new PhotoScene((AbstractMTApplication) app, 0, 0, app.width, app.height);
			this.getParent().addChild(photoApp);
		}
		
		if(name.equals("piano"))
		{
			MTPiano piano = new MTPiano((MTApplication) app, new Vector3D(0, app.height/2));
			this.getParent().addChild(piano);
		}
		
		this.close();
	}
	
	public void setIconTouchAction(MTEllipse icon, final String name){
		icon.registerInputProcessor(new TapProcessor(app, 1000));
		icon.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{	
					launchApp(name);
				}
				
				return false;
			}
		});
	}
	
	public void open(MTComponent parent){
		parent.addChild(this);
		
		//Apparition en fondu
		MultiPurposeInterpolator in = new MultiPurposeInterpolator(0, 200, 500, 0, 0, 1);
		Animation animation = new Animation("fading", in, this);
		animation.addAnimationListener(new IAnimationListener() {
		    public void processAnimationEvent(AnimationEvent ae) {
		        //fade using ae.getValue() as alpha
		    	launcher.setFillColor(new MTColor(255, 255, 255, ae.getValue()));
		    	
		    	for(int i = 0; i < applications.length; i++)
				{
					MTEllipse appli = new MTEllipse(app, new Vector3D(app.getWidth()/2, app.getHeight()/2 - 300), 50, 50);
					appli.setStrokeColor(MTColor.BLACK);
					appli.rotateZ(logoCentral.getCenterPointGlobal(), i * 40);
					appli.removeAllGestureEventListeners();
					launcher.addChild(appli);
					
					setIconTouchAction(appli, applications[i]);
				}
				
				logoCentral.setVisible(true);
				setVoidTouchAction();
		    }
		});
		animation.start();
	}
	
	public void close(){
		
		//Apparition en fondu du calque blanc
		MultiPurposeInterpolator in = new MultiPurposeInterpolator(200, 0, 500, 0, 0, 1);
		Animation animation = new Animation("fading", in, this);
		animation.addAnimationListener(new IAnimationListener() {
		    public void processAnimationEvent(AnimationEvent ae) {
		        //fade using ae.getValue() as alpha
		    	launcher.setFillColor(new MTColor(255, 255, 255, ae.getValue()));
		    	
		    	if(ae.getValue() == 0)
				{
					launcher.removeFromParent();
				}
		    }
		});
		animation.start();
	}
	
	public void setVoidTouchAction(){
		this.registerInputProcessor(new TapProcessor(app, 1000));
		this.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{	
					close();
				}
				
				return false;
			}
		});
	}
	
	public void setLaucherDesign(){
		this.removeAllGestureEventListeners();
		this.setNoStroke(true);
		this.setFillColor(new MTColor(255, 255, 255, 200));
		this.setTexture(background);
		
		logoCentral.removeAllGestureEventListeners();
		//logoCentral.setTexture(app.loadImage("../ressources/launcher/logo.png"));
		logoCentral.setNoStroke(true);
		//this.addChild(logoCentral);
	}

}
