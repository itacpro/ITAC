package Itac;

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
		background = app.loadImage("water.png");
		this.getCanvas().addChild(new MTBackgroundImage(app, background, false));
		
		//Application de démarrage
		Login login = new Login(0, 0, app.width, app.height, app, this);
		this.addApplication(login);
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
		
		changeBackground("water.png");
		
		Login login = new Login(0, 0, app.width, app.height, app, this);
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
