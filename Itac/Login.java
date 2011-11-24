package Itac;

import java.util.ArrayList;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import Widgets.WidgetScene;

/**
 * Interface de Login
 * @author Nicolas Lefebvre
 * @version 1.0
 */
public class Login extends MTRectangle{
	
	//Déclaration des variables
	private AbstractMTApplication app;
	private Itac itacWorkflow;
	private float xPos, yPos;
	private Clavier keyboard;
	private IFont font = FontManager.getInstance().createFont(app, "arial.ttf", 30, new MTColor(255,255,255));
	public MTTextArea message;
	private ListeUtilisateurs listUsers;
	
	//Déclaration des données
	public String cible;
	public String password = "";
	public String passwordCible = "";
	public String currentUser;
	
	/**
	 * Constructeur
	 * @param Position de l'interface sur l'axe X
	 * @param Position de l'interface sur l'axe Y
	 * @param Taille de l'interface
	 * @param Hauteur de l'interface
	 * @param Application
	 * @param Scene
	 */
	public Login(float _xPos, float _yPos, float _width, float _height, AbstractMTApplication _app, Itac _itacWorkflow){
		super(_xPos, _yPos, _width, _height, _app);
		
		//Initialisation des variables
		app = _app;
		itacWorkflow = _itacWorkflow;
		currentUser = itacWorkflow.currentUser;
		
		//Définition des paramètres d'affichage de l'application
		keyboard = new Clavier(app.width/2f, app.height - 200, 300, 300, app, this);
		message = new MTTextArea(app, font);
		this.addChild(message);
		setApplicationDesign();
		
		//Affichage du logo ITAC
		afficherLogoItac();
		
		//Affichage de la liste des utilisateurs
		listUsers = new ListeUtilisateurs(app.width, 130, app);
		setTouchActionOnUserIcon();
		this.addChild(listUsers);
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
		MTRectangle logoItac = new MTRectangle(200, 166, app);
		logoItac.getCenterPointGlobal();
		logoItac.setPositionGlobal(new Vector3D(app.width/2f, 150));
		logoItac.setTexture(app.loadImage("itac.png"));
		logoItac.setNoStroke(true);
		logoItac.setPickable(false);
		
		this.addChild(logoItac);
	}
	
	/**
	 * Vérifie si le mot de passe entré correspond à celui de l'utilisateur sélectionné.
	 * Si la condition est remplie, on charge le bureau de l'utilisateur.
	 */
	public void verifPassword(){
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
				message.setPositionGlobal(new Vector3D(app.width/2f, 500));
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
							listUsers.selection.setPositionGlobal(user.getCenterPointGlobal());
						}
						//On change la cible et on réinitialise les paramètres
						cible = user.getName();
						password = "";
						masquerClavier();
						afficherClavier();
						message.setVisible(false);
					}
					
					return false;
				}
			});
		}
	}
	
}
