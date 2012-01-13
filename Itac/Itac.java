package Itac;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import sound.Mp3Player;

public class Itac extends AbstractScene{
	
	public AbstractMTApplication app;
	public String currentUser;
	public PImage background;
	
	public Itac(AbstractMTApplication _app, String _name) {
		super(_app, _name);
		
		app = _app;
		
		//Affichage des cercles de pointage
		this.registerGlobalInputProcessor(new CursorTracer(app, this));
		background = app.loadImage("../ressources/login/fond-ecran-default.png");
		this.getCanvas().addChild(new MTBackgroundImage(app, background, false));
		
		final MTRectangle start = new MTRectangle(0, 0, 50, app.width, app.height, app);
		start.setFillColor(new MTColor(0, 0, 0));
		start.setNoStroke(true);
		start.removeAllGestureEventListeners();
		this.getCanvas().addChild(start);
		
		MultiPurposeInterpolator in = new MultiPurposeInterpolator(255, 0, 4000, 0, 0, 1);
		Animation animation = new Animation("fading", in, this);
		animation.addAnimationListener(new IAnimationListener() {
		    public void processAnimationEvent(AnimationEvent ae) {
		        //fade using ae.getValue() as alpha
		    	start.setFillColor(new MTColor(0, 0, 0, ae.getValue()));
		    	
		    	if(ae.getValue() == 0)
				{
					start.destroy();
				}
		    }
		});
		animation.start(); 
		
		
		Mp3Player.play("../ressources/sound/initialyze.mp3");
		
		//Application de démarrage
		Login login = new Login(0, 0, app.width, app.height, 1000, app, this);
		this.addApplication(login);
		
		int[] pixels = new int[16 * 16];
		Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
		Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "invisibleCursor");
		app.setCursor(transparentCursor);
	}
	
	public void addApplication(MTRectangle application){
		this.getCanvas().addChild(application);
	}
	
	public void changeBackground(String bg){
		this.getCanvas().addChild(new MTBackgroundImage(app, app.loadImage(bg), false));
		//this.getCanvas().addChild(new MTBackgroundImage(app, app.loadImage("../users/" + currentUser + "/params/background.jpg"), false));
	}
	
	public void deconnexion(){
		currentUser = "";
		//this.getCanvas().removeAllChildren();
		
		MTComponent[] children = this.getCanvas().getChildren();
		
		for (int i = 0; i < children.length; i++)
		{
			System.out.println(children[i].getName());
			children[i].destroy();
			System.out.println("Détruit !");
		}
		this.registerGlobalInputProcessor(new CursorTracer(app, this));
		changeBackground("../ressources/login/fond-ecran-default.png");
		
		Login login = new Login(0, 0, app.width, app.height, 1000, app, this);
		this.addApplication(login);
	}
	
	public void afficherUtilisateur(){
		
		@SuppressWarnings("deprecation")
		MTRoundRectangle userBg = new MTRoundRectangle(30, 30, 0, 110, 110, 5, 5, app);
		userBg.removeAllGestureEventListeners();
		
		//Image utilisateur
		Utilisateur user = new Utilisateur(currentUser, 85, 85, 100, 100, app);
		user.texte.setVisible(false);
		
		//Nom de l'utilisateur
		IFont font = FontManager.getInstance().createFont(app, "arial.ttf", 40, new MTColor(255,255,255));
		MTTextArea texte = new MTTextArea(app, font);
		texte.setNoFill(true);
		texte.setNoStroke(true);
		texte.setText(user.getName());
		texte.setAnchor(PositionAnchor.UPPER_LEFT);
		texte.setPositionGlobal(new Vector3D(150, 30));
		
		//Bouton de déconnexion
		
		final MTRectangle deconnect = new MTRectangle(150, 100, 94, 33, app);
		deconnect.setTexture(app.loadImage("../ressources/login/btn_deconnexion.png"));
		deconnect.removeAllGestureEventListeners();
		deconnect.setNoStroke(true);
		
		//Ecouteur d'action
		deconnect.registerInputProcessor(new TapProcessor(app, 15));
		deconnect.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				
				if (te.isTapped())
				{
					deconnexion();
				}
				if (te.isTapDown())
				{
					deconnect.setTexture(app.loadImage("../ressources/login/btn_deconnexion_hover.png"));
				}
				if(te.getId() == MTGestureEvent.GESTURE_ENDED)
				{
					deconnect.setTexture(app.loadImage("../ressources/login/btn_deconnexion.png"));
				}
				
				return false;
			}
		});
		this.getCanvas().addChild(userBg);
		this.getCanvas().addChild(user);
		this.getCanvas().addChild(texte);
		this.getCanvas().addChild(deconnect);
	}
}
