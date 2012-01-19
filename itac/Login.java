package itac;

import java.util.ArrayList;

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

import rfid.RFID;
import widgets.WidgetScene;


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
	
	//Déclaration des variables RFID
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
		
		//Récuppération des paramètres
		app = _app;
		itacWorkflow = _itacWorkflow;
		
		//On lie la variable de l'utilisateur courrant avec celle du lanceur Itac
		currentUser = itacWorkflow.currentUser;
		
		//Création du clavier numérique pour l'identification via mot de passe
		keyboard = new Clavier(app.width/2, app.height/2 + 100, 318, 280, app, this);
		
		//Création de la variable d'affichage des messages d'erreurs
		message = new MTTextArea(app, font);
		this.addChild(message);
		
		//Chargement des éléments graphiques
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
		
		
		//Affichage des notes de version
		MTTextArea version = new MTTextArea(app, font);
		this.addChild(version);
		version.setText("ITAC v1.0 - TouchDown 2011-2012");
		version.setPositionGlobal(new Vector3D(270, app.height-50));
		version.setNoStroke(true);
		version.setNoFill(true);
		
		//Affichage du bouton permettant la rotation de l'écran de login
		final MTRectangle rotate = new MTRectangle(app.width/2, app.height - 100, 118, 120, app);
		rotate.setTexture(app.loadImage("../ressources/login/rotate.png"));
		rotate.setNoStroke(true);
		rotate.getCenterPointGlobal();
		rotate.setPositionGlobal(new Vector3D(app.width/2, app.height - 200));
		rotate.unregisterAllInputProcessors();
		rotate.removeAllGestureEventListeners();
		this.addChild(rotate);
		
		//On lie l'interface de login dans une nouvelle variable
		final Login login = this;
		
		//On défini les actions sur le bouton de rotation
		rotate.registerInputProcessor(new TapProcessor(app, 15));
		rotate.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				
				if (te.isTapped())
				{
					//Création de l'interpolateur de mouvement
					MultiPurposeInterpolator interpolator = new MultiPurposeInterpolator(0, 180, 1500, 0.1f, 0.7f, 1);
					//Création de l'animation de la rotation
					Animation animation = new Animation("Rotation", interpolator, this, 0);
					animation.addAnimationListener(new IAnimationListener() {
						public void processAnimationEvent(AnimationEvent ae) {
							//On fait tourner l'objet contenu dans la varible liée pour faire tourner l'interface de login
							login.rotateZ(new Vector3D(app.width/2, app.height/2), ae.getCurrentStepDelta());
						}
					});
					animation.start();
				}
				if (te.isTapDown())
				{
					//Changement de texture lorsqu'on maintient le bouton enfoncé
					rotate.setTexture(app.loadImage("../ressources/login/rotate-hover.png"));
				}
				if(te.getId() == MTGestureEvent.GESTURE_ENDED)
				{
					//Changement de texture lorsqu'on relâche le bouton
					rotate.setTexture(app.loadImage("../ressources/login/rotate.png"));
				}
				
				return false;
			}
		});
		
		//Thread de vérification des lecteurs RFID
		Thread t = new Thread(){
			public void run() {
				try {
					rfid = new RFID(login);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				while(true)
				{	
					//Si un utilisateur passe un badge RFID
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
						
						//On change l'état de la varibale de connection via RFID
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
		//Paramètres d'affichage de l'interface
		this.setNoFill(true);
		this.setNoStroke(true);
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
		
		//Paramètres d'affichage du message d'erreur
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
	
	/**
	 * Fonction permettant d'afficher l'utilisateur sélectionné sur la scène, avec un contour
	 */
	public void ajouterUtilisateur(){
		this.addChild(userContour);
		this.addChild(current);
	}
	
	/**
	 * Vérifie si le mot de passe entré correspond à celui de l'utilisateur sélectionné.
	 * Si la condition est remplie, on charge le bureau de l'utilisateur.
	 */
	public void verifPassword(){
		
		//On ajoute un caractère indiquant qu'un 
		keyboard.champ.setText(keyboard.champ.getText() + "●");
		
		//Le mot de passe ne peut comporter que 6 caractères, si ce nombre est atteint, on check le mot de passe
		if(password.length() == 6)
		{
			masquerClavier();
			
			//Si le mot de passe est correct
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
				
				//Réinitialisation de l'affichage de l'interface de Login
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
						//On change la cible et on réinitialise les paramètres
						cible = user.getName();
						password = "";
						masquerClavier();
						afficherClavier();
						message.setVisible(false);
						
						//On masque le logo ITAC et la liste des utilisateurs
						logoItac.setVisible(false);
						listUsers.setVisible(false);
						
						//On affiche le contour de l'image utilisateur à la même position que celle-ci
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
}
