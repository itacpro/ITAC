package Itac;

import java.io.IOException;
import java.util.ArrayList;

import javazoom.jl.decoder.JavaLayerException;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import com.phidgets.PhidgetException;
import com.phidgets.RFIDPhidget;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.TagGainEvent;
import com.phidgets.event.TagGainListener;
import com.phidgets.event.TagLossEvent;
import com.phidgets.event.TagLossListener;

import RFID.RFID;
import Widgets.WidgetScene;

/**
 * Interface de Login
 * @author Nicolas Lefebvre
 * @version 1.0
 */
public class Login extends MTRectangle{
	
	//Déclaration des variables
	public AbstractMTApplication app;
	public Itac itacWorkflow;
	private float xPos, yPos;
	private Clavier keyboard;
	private IFont font = FontManager.getInstance().createFont(app, "arial.ttf", 30, new MTColor(255,255,255));
	public MTTextArea message;
	MTRectangle logoItac;
	private ListeUtilisateurs listUsers;
	private Utilisateur current;
	private MTRoundRectangle userContour;
	private MTRectangle rfidR;
	private MTRectangle rfidL;
	
	//Déclaration des données
	public String cible;
	public String password = "";
	public String passwordCible = "";
	public String currentUser;
	public Login login;
	
	public RFID rfid = null;
	public boolean rfidConnect = false;
	public boolean rfidConnectInput = false;
	public String UserID = "01068e27b6";
	public String UserName = "zzz";
	
	/**
	 * Constructeur
	 * @param Position de l'interface sur l'axe X
	 * @param Position de l'interface sur l'axe Y
	 * @param Taille de l'interface
	 * @param Hauteur de l'interface
	 * @param Application
	 * @param Scene
	 */
	public Login(float _xPos, float _yPos, float _zPos, float _width, float _height, AbstractMTApplication _app, Itac _itacWorkflow){
		super(_xPos, _yPos, _width, _height, _app);
		
		//Initialisation des variables
		app = _app;
		itacWorkflow = _itacWorkflow;
		currentUser = itacWorkflow.currentUser;
		
		//Définition des paramètres d'affichage de l'application
		keyboard = new Clavier(app.width/2, app.height/2 + 100, 318, 280, app, this);
		
		message = new MTTextArea(app, font);
		this.addChild(message);
		setApplicationDesign();
		
		//Affichage du logo ITAC
		afficherLogoItac();
		
		//Affichage de la liste des utilisateurs
		listUsers = new ListeUtilisateurs(0, 600, app.width, 300, app);
		setTouchActionOnUserIcon();
		this.addChild(listUsers);
		
		//Icones RFID
		rfidL = new MTRectangle(167, 172, app);
		rfidL.setTexture(app.loadImage("../ressources/login/fleche-rfid-gauche.png"));
		rfidL.getCenterPointGlobal();
		rfidL.setPositionGlobal(new Vector3D(90, app.height/2));
		rfidL.setNoStroke(true);
		rfidL.setPickable(false);
		this.addChild(rfidL);
		
		rfidR = new MTRectangle(167, 172, app);
		rfidR.setTexture(app.loadImage("../ressources/login/fleche-rfid-droite.png"));
		rfidR.getCenterPointGlobal();
		rfidR.setPositionGlobal(new Vector3D(app.width-90, app.height/2));
		rfidR.setNoStroke(true);
		rfidR.setPickable(false);
		this.addChild(rfidR);
		
		
		//Version
		MTTextArea version = new MTTextArea(app, font);
		this.addChild(version);
		version.setText("ITAC v1.0 - TouchDown 2011-2012");
		version.setPositionGlobal(new Vector3D(270, app.height-50));
		version.setNoStroke(true);
		version.setNoFill(true);
		
		//Rotation de l'écran
		final MTRectangle rotate = new MTRectangle(app.width/2, app.height - 100, 118, 120, app);
		rotate.setTexture(app.loadImage("../ressources/login/rotate.png"));
		rotate.setNoStroke(true);
		rotate.getCenterPointGlobal();
		rotate.setPositionGlobal(new Vector3D(app.width/2, app.height - 200));
		rotate.unregisterAllInputProcessors();
		rotate.removeAllGestureEventListeners();
		this.addChild(rotate);
		
		final MTRectangle rec = this;
		
		rotate.registerInputProcessor(new TapProcessor(app, 15));
		rotate.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				
				if (te.isTapped())
				{
					MultiPurposeInterpolator interpolator = new MultiPurposeInterpolator(0, 180, 1500, 0.1f, 0.7f, 1);
					Animation animation = new Animation("Rotation", interpolator, this, 0);
					animation.addAnimationListener(new IAnimationListener() {
						public void processAnimationEvent(AnimationEvent ae) {
							rec.rotateZ(new Vector3D(app.width/2, app.height/2), ae.getCurrentStepDelta());
						}
					});
					animation.start();
				}
				if (te.isTapDown())
				{
					rotate.setTexture(app.loadImage("../ressources/login/rotate-hover.png"));
				}
				if(te.getId() == MTGestureEvent.GESTURE_ENDED)
				{
					rotate.setTexture(app.loadImage("../ressources/login/rotate.png"));
				}
				
				return false;
			}
		});
		
		login = this;
		
		final Thread t = new Thread(){
			public void run() {
				
				try {
					setRFID();
				} catch (PhidgetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				while(true)
				{	
					if(rfidConnect == true && rfidConnectInput == false)
					{
						//On enregistre les paramètres de l'utilisateur
						cible = rfid.UserName;
						itacWorkflow.currentUser = cible;
						itacWorkflow.changeBackground("../users/" + itacWorkflow.currentUser + "/params/background.jpg");
						
						//On charge le bureau de l'utilisateur
						WidgetScene widgets = new WidgetScene(0, 0, 300, 200, app, new Utilisateur(itacWorkflow.currentUser, 60, 60, 100, 100, app));
						itacWorkflow.addApplication(widgets);
						itacWorkflow.afficherUtilisateur();
						
						
						rfidConnectInput = true;
					}
				}
			}
		};
		t.start();
	}
	
	//Méthodes
	
	/**
	 * Affiche le clavier.
	 */
	public void afficherClavier(){
		this.addChild(keyboard);
	}
	
	/**
	 * Maque le clavier.
	 */
	public void masquerClavier(){
		this.removeChild(keyboard);
	}
	
	/**
	 * Définit les paramètres d'affichage de l'application.
	 */
	public void setApplicationDesign(){
		//Paramètre de l'interface
		this.setNoFill(true);
		this.setNoStroke(true);
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
		
		//Paramètres du message d'erreur
		message.setVisible(false);
		message.setNoFill(true);
		message.setNoStroke(true);
		message.setPickable(false);
	}
	
	/**
	 * Affiche le logo ITAC.
	 */
	public void afficherLogoItac(){
		logoItac = new MTRectangle(322, 322, app);
		logoItac.getCenterPointGlobal();
		logoItac.setPositionGlobal(new Vector3D(app.width/2f, 250));
		logoItac.setTexture(app.loadImage("../ressources/login/logo-itac.png"));
		logoItac.setNoStroke(true);
		logoItac.setPickable(false);
		
		this.addChild(logoItac);
	}
	
	public void ajouterUtilisateur(){
		this.addChild(userContour);
		this.addChild(current);
	}
	
	/**
	 * Vérifie si le mot de passe entré correspond à celui de l'utilisateur sélectionné.
	 * Si la condition est remplie, on charge le bureau de l'utilisateur.
	 */
	public void verifPassword(){
		
		keyboard.champ.setText(keyboard.champ.getText() + "●");
		
		if(password.length() == 6)
		{
			masquerClavier();
			
			if(password.equalsIgnoreCase(listUsers.getUserPassword(cible)))
			{
				//Affichage du message de connexion
				message.setVisible(true);
				message.setText("Bienvenue " + cible + " !");
				message.setAnchor(PositionAnchor.CENTER);
				message.setPositionGlobal(new Vector3D(app.width/2f, app.height/2f));
				
				//On enregistre les paramètres de l'utilisateur
				itacWorkflow.currentUser = cible;
				itacWorkflow.changeBackground("../users/" + itacWorkflow.currentUser + "/params/background.jpg");
				
				//On charge le bureau de l'utilisateur
				WidgetScene widgets = new WidgetScene(0, 0, 300, 200, app, new Utilisateur(itacWorkflow.currentUser, 60, 60, 100, 100, app));
				itacWorkflow.addApplication(widgets);
				itacWorkflow.afficherUtilisateur();
				
				//On détruit l'interface de login
				this.destroy();
			}
			else
			{
				//Affichage du message d'erreur
				message.setVisible(true);
				message.setText("Mot de passe incorrect !");
				message.setAnchor(PositionAnchor.CENTER);
				message.setPositionGlobal(new Vector3D(app.width/2f, 900));
				
				keyboard.champ.setText("");
				logoItac.setVisible(true);
				current.setVisible(false);
				userContour.setVisible(false);
				listUsers.setVisible(true);
			}
			
		}
	}
	
	/**
	 * Définit les actions lorsqu'on touche l'icône d'un utilisateur :
	 *	- Change la cible sur l'écran de login
	 * 	- Affiche le clavier
	 * 	- Efface les messages d'erreur
	 * @param Utilisateur
	 */
	private void setTouchActionOnUserIcon(){
		//On récupère le nom des utilisateurs
		ArrayList<String> nomUtilisateurs = listUsers.getUsersName();
		
		for(int i = 0; i < listUsers.getNbUsers(); i++)
		{
			//On récupère le nom de l'utilisateur courrant
			final Utilisateur user = listUsers.getUser(nomUtilisateurs.get(i));
			
			//Ecouteur d'action
			user.registerInputProcessor(new TapProcessor(app, 15));
			user.addGestureListener(TapProcessor.class, new IGestureEventListener()
			{	
				public boolean processGestureEvent(MTGestureEvent ge)
				{
					TapEvent te = (TapEvent)ge;
					
					if (te.isTapped())
					{
						if(cible == "" || cible != user.getName())
						{
							//On déplace le rectangle de sélection sur l'icone de l'utilisateur
							//listUsers.selection.setPositionGlobal(user.getCenterPointGlobal());
							
							
						}
						
						//On change la cible et on réinitialise les paramètres
						cible = user.getName();
						password = "";
						masquerClavier();
						afficherClavier();
						message.setVisible(false);
						
						logoItac.setVisible(false);
						listUsers.setVisible(false);
						
						userContour = new MTRoundRectangle(app.width/2, 250, 0, 265, 265, 5, 5, app);
						userContour.getCenterPointGlobal();
						userContour.setPositionGlobal(new Vector3D(app.width/2, 250));
						userContour.setPickable(false);
						
						current = new Utilisateur(cible, app.width/2, 250, 250, 250, app);
						ajouterUtilisateur();
					}
					
					return false;
				}
			});
		}
	}
	
	public void setRFID() throws PhidgetException, IOException{
		RFIDPhidget rfid;
		
		//System.out.println(Phidget.getLibraryVersion());

		rfid = new RFIDPhidget();
		rfid.addAttachListener(new AttachListener() {
			public void attached(AttachEvent ae)
			{
				try
				{
					((RFIDPhidget)ae.getSource()).setAntennaOn(true);
					((RFIDPhidget)ae.getSource()).setLEDOn(true);
				}
				catch (PhidgetException ex) { }
				//System.out.println("attachment of " + ae);
			}
		});
		
		//Méthode de détection des tags RFID en Input
		rfid.addTagGainListener(new TagGainListener()
		{
			public void tagGained(TagGainEvent gain)
			{
				//System.out.println("Tag : " + gain.getValue());
				
				if(gain.getValue().equals(UserID))
				{
					if(rfidConnect == false) 
					{
						System.out.println("Connexion de "+ UserName +" !");
						rfidConnect = true;
					}
				}
			}
		});

		rfid.openAny();
		
		System.in.read();
		System.out.print("closing...");
		rfid.close();
		rfid = null;
		System.out.println(" ok");
		if (false) {
			System.out.println("wait for finalization...");
			System.gc();
		}
	}
}
