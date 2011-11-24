package Interface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import Widgets.WidgetScene;

public class Login extends AbstractScene {
	public AbstractMTApplication app;
	public boolean clavierOuvert = false;
	public MTTextArea username;
	public int nbUsers = 0;
	public ArrayList users = new ArrayList();
	public MTRectangle userList;
	public String cible;
	public String password;
	public MTRectangle keyboard;
	public MTTextArea erreur;
	
	public Login(AbstractMTApplication mtApplication, String name) {
		super(mtApplication, name);
		app = mtApplication;
		
		//Affichage des cercles de pointage
		this.registerGlobalInputProcessor(new CursorTracer(app, this));
		this.getCanvas().addChild(new MTBackgroundImage(app, app.loadImage("water.png"), false));
		
		MTRectangle logoItac = new MTRectangle(200, 166, app);
		logoItac.getCenterPointGlobal();
		logoItac.setPositionGlobal(new Vector3D(app.width/2f, 150));
		logoItac.setTexture(app.loadImage("itac.png"));
		logoItac.setNoStroke(true);
		logoItac.setPickable(false);
		
		IFont font = FontManager.getInstance().createFont(app, "arial.ttf", 20, new MTColor(0,0,0));
		
		username = new MTTextArea(app.width/2f, app.height/2f, 300, 30, font, app);
		username.getCenterPointGlobal();
		username.setPositionGlobal(new Vector3D(app.width/2f, 300));
		
		
		userList = new MTRectangle(app.width, 130, app);
		userList.setPositionGlobal(new Vector3D(app.width/2f, 350));
		userList.setNoFill(true);
		userList.setNoStroke(true);
		userList.unregisterAllInputProcessors();
		userList.removeAllGestureEventListeners();
		
		this.getCanvas().addChild(logoItac);
		this.getCanvas().addChild(userList);
		//this.getCanvas().addChild(username);
		//afficherClavier();
		countUsers();
		showUsers();
	}
	
	public void countUsers(){
		String chemin = "../users/";
		File dossier = new File(chemin);
		String [] listeUsers = dossier.list(); //On liste tous les fichiers du dossier
		
		for(int i = 0; i < listeUsers.length; i++){
			File fichier = new File(listeUsers[i]);
			
			if(fichier.isFile() == false)
			{
				String userName = fichier.getName();
				users.add(userName);
				nbUsers++;
			}
		}
		
		System.out.println(nbUsers);
	}
	
	public void showUsers(){
		for(int i = 0; i < nbUsers; i++){
			//String config = "../users/" + users.get(0) + "/params/config.txt";
			String image = "../users/" + users.get(i) + "/params/profile.png";
			//File params = new File(config);
			
			MTRectangle user = new MTRectangle(100, 100, app);
			user.setAnchor(PositionAnchor.CENTER);
			user.setNoStroke(true);
			user.unregisterAllInputProcessors();
			user.removeAllGestureEventListeners();
			
			int ecart = 150;
			int taille = (nbUsers * ecart) - ecart;
			user.setPositionRelativeToParent(new Vector3D((((app.width - taille) / 2) + i * ecart), 50));
			user.setTexture(app.loadImage(image));
			
			IFont font = FontManager.getInstance().createFont(app, "arial.ttf", 15, new MTColor(255,255,255));
			
			MTTextArea texte = new MTTextArea(app, font);
			texte.setNoFill(true);
			texte.setNoStroke(true);
			texte.setText((String)users.get(i));
			texte.setAnchor(PositionAnchor.CENTER);
			texte.setPositionRelativeToParent(new Vector3D(50, 120));
			user.addChild(texte);
			
			texte.unregisterAllInputProcessors();
			texte.removeAllGestureEventListeners();
			
			final int _i = i;
			//Ecouteur d'action
			user.registerInputProcessor(new TapProcessor(app, 15));
			user.addGestureListener(TapProcessor.class, new IGestureEventListener()
			{	
				public boolean processGestureEvent(MTGestureEvent ge)
				{
					TapEvent te = (TapEvent)ge;
					
					if (te.isTapped())
					{
						cible = (String)users.get(_i);
						password = "";
						afficherClavier();
					}
					
					return false;
				}
			});
			
			userList.addChild(user);
		}
	}
	
	public void verifPassword(){
		if(password.length() == 6)
		{
			masquerClavier();
			
			String passwordUser = "";
			String config = "../users/" + cible + "/params/config.txt";
			
			try {
				InputStream ips = new FileInputStream(config);
				InputStreamReader ipsr = new InputStreamReader(ips);
				BufferedReader br = new BufferedReader(ipsr);
				String ligne;
				
				while ((ligne = br.readLine()) != null) 
				{
					passwordUser = ligne;
				}
				
				if(password.equalsIgnoreCase(passwordUser))
				{
					this.getCanvas().removeAllChildren();
					
					IFont font = FontManager.getInstance().createFont(app, "arial.ttf", 30, new MTColor(255,255,255));
					erreur = new MTTextArea(app, font);
					erreur.setNoFill(true);
					erreur.setNoStroke(true);
					erreur.setText("Bienvenue " + cible + " !");
					erreur.setAnchor(PositionAnchor.CENTER);
					erreur.setPositionGlobal(new Vector3D(app.width/2f, app.height/2f));
					this.getCanvas().addChild(erreur);
					
					//WidgetScene widgets = new WidgetScene(app, "widget");
				}
				else
				{
					IFont font = FontManager.getInstance().createFont(app, "arial.ttf", 30, new MTColor(255,255,255));
					erreur = new MTTextArea(app, font);
					erreur.setNoFill(true);
					erreur.setNoStroke(true);
					erreur.setText("Mot de passe incorrect !");
					erreur.setAnchor(PositionAnchor.CENTER);
					erreur.setPositionGlobal(new Vector3D(app.width/2f, 500));
					this.getCanvas().addChild(erreur);
				}
				
			} catch (Exception e) {
				System.out.println(e.toString()); 
			}
			
		}
	}
	
	public void afficherClavier(){
		keyboard = new MTRectangle(300, 300, app);
		keyboard.getCenterPointGlobal();
		keyboard.setPositionGlobal(new Vector3D(app.width/2f, app.height - 200));
		keyboard.setNoFill(true);
		keyboard.setNoStroke(true);
		
		MTRectangle bouton1 = creerBouton("1", 0, 0);
		MTRectangle bouton2 = creerBouton("2", 100, 0);
		MTRectangle bouton3 = creerBouton("3", 200, 0);
		MTRectangle bouton4 = creerBouton("4", 0, 100);
		MTRectangle bouton5 = creerBouton("5", 100, 100);
		MTRectangle bouton6 = creerBouton("6", 200, 100);
		MTRectangle bouton7 = creerBouton("7", 0, 200);
		MTRectangle bouton8 = creerBouton("8", 100, 200);
		MTRectangle bouton9 = creerBouton("9", 200, 200);
		
		keyboard.unregisterAllInputProcessors();
		keyboard.removeAllGestureEventListeners();
		
		keyboard.addChild(bouton1);
		keyboard.addChild(bouton2);
		keyboard.addChild(bouton3);
		keyboard.addChild(bouton4);
		keyboard.addChild(bouton5);
		keyboard.addChild(bouton6);
		keyboard.addChild(bouton7);
		keyboard.addChild(bouton8);
		keyboard.addChild(bouton9);
		
		if(clavierOuvert == false)
		{
			this.getCanvas().addChild(keyboard);
		}
	}
	
	public void masquerClavier(){
		keyboard.removeAllChildren();
	}
	
	public MTRectangle creerBouton(final String name, int xPos, int yPos){
		final MTRectangle bouton = new MTRectangle(xPos, yPos, 100, 100, app);
		
		bouton.setTexture(app.loadImage("bouton.png"));
		bouton.setNoStroke(true);
		
		IFont font = FontManager.getInstance().createFont(app, "arial.ttf", 30, new MTColor(255,255,255));
		
		MTTextArea texte = new MTTextArea(app, font);
		texte.setNoFill(true);
		texte.setNoStroke(true);
		texte.setPositionRelativeToParent(new Vector3D(xPos + 40, yPos + 50));
		texte.setText(name);
		bouton.addChild(texte);
		
		texte.unregisterAllInputProcessors();
		texte.removeAllGestureEventListeners();
		
		bouton.unregisterAllInputProcessors();
		bouton.removeAllGestureEventListeners();
		
		//Ecouteur d'action
		texte.registerInputProcessor(new TapProcessor(app, 15));
		texte.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{
					username.setText(username.getText() + name);
					password = password + name;
					verifPassword();
				}
				if (te.isTapDown())
				{
					bouton.setTexture(app.loadImage("boutonHover.png"));
				}
				if(te.getId() == MTGestureEvent.GESTURE_ENDED)
				{
					bouton.setTexture(app.loadImage("bouton.png"));
				}
				return false;
			}
		});
		
		//Ecouteur d'action
		bouton.registerInputProcessor(new TapProcessor(app, 15));
		bouton.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{
					username.setText(username.getText() + name);
					password = password + name;
					verifPassword();
				}
				if (te.isTapDown())
				{
					bouton.setTexture(app.loadImage("boutonHover.png"));
				}
				if(te.getId() == MTGestureEvent.GESTURE_ENDED)
				{
					bouton.setTexture(app.loadImage("bouton.png"));
				}
				return false;
			}
		});
		
		return bouton;
	}
}
