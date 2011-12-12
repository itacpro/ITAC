package Itac;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;

import com.sun.org.apache.xpath.internal.operations.Gte;

import processing.core.PImage;

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
		Utilisateur user = new Utilisateur(currentUser, 60, 60, 100, 100, app);
		
		//Ecouteur d'action
		user.registerInputProcessor(new TapProcessor(app, 15));
		user.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				
				if (te.isTapped())
				{
					deconnexion();
				}
				
				return false;
			}
		});
		
		this.getCanvas().addChild(user);
	}
}
