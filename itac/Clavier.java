package itac;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

/**
 * Clavier pour l'interface de login
 * @author Nicolas Lefebvre
 * @version 1.0
 */
public class Clavier extends MTRectangle{
	
	//Déclaration des variables
	public AbstractMTApplication app;
	public Login application;
	public float xPos, yPos, width, height;
	public IFont font = FontManager.getInstance().createFont(app, "arial.ttf", 30, new MTColor(255,255,255));
	public MTTextArea champ;
	
	/**
	 * Constructeur
	 * @param Position sur l'axe X
	 * @param Position sur l'axe Y
	 * @param Largeur du Clavier
	 * @param Hauteur du Clavier
	 * @param Application
	 * @param Interface de login
	 */
	public Clavier(float _xPos, float _yPos, float _width, float _height, AbstractMTApplication _app, Login _application) {
		super(_xPos, _yPos, _width, _height, _app);
		
		//Récupération des paramètres
		app = _app;
		application = _application;
		xPos = _xPos;
		yPos = _yPos;
		width = _width;
		height = _height;
		
		//On défini la position du clavier par rapport à son centre
		this.setAnchor(PositionAnchor.CENTER);
		this.setPositionGlobal(new Vector3D(xPos, yPos));
		
		//Input permettant d'afficher le mot de passe
		champ = new MTTextArea(xPos, yPos - 65, 318, 48, font, app);
		champ.setTexture(app.loadImage("../ressources/login/input-login.png"));
		champ.setPickable(false);
		champ.setNoStroke(true);
		this.addChild(champ);
		
		setKeyboardDesign();
		addButtons();
	}
	
	/**
	 * Définit l'apparence du clavier.
	 */
	public void setKeyboardDesign(){
		this.getCenterPointGlobal();
		this.setNoFill(true);
		this.setNoStroke(true);
		
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
	}
	
	/**
	 * Ajoute 9 boutons au clavier
	 */
	public void addButtons(){
		float x = xPos + 15;
		float y = yPos;
		
		//Création des 10 boutons
		Bouton bouton1 = new Bouton("1", x + 0, y + 0, 83, 79, app, application);
		Bouton bouton2 = new Bouton("2", x + 100, y + 0, 83, 79, app, application);
		Bouton bouton3 = new Bouton("3", x + 200, y + 0, 83, 79, app, application);
		Bouton bouton4 = new Bouton("4", x + 0, y + 90, 83, 79, app, application);
		Bouton bouton5 = new Bouton("5", x + 100, y + 90, 83, 79, app, application);
		Bouton bouton6 = new Bouton("6", x + 200, y + 90, 83, 79, app, application);
		Bouton bouton7 = new Bouton("7", x + 0, y + 180, 83, 79, app, application);
		Bouton bouton8 = new Bouton("8", x + 100, y + 180, 83, 79, app, application);
		Bouton bouton9 = new Bouton("9", x + 200, y + 180, 83, 79, app, application);
		Bouton bouton0 = new Bouton("0", x + 100, y + 270, 83, 79, app, application);
		
		//Ajout des 10 boutons
		this.addChild(bouton1);
		this.addChild(bouton2);
		this.addChild(bouton3);
		this.addChild(bouton4);
		this.addChild(bouton5);
		this.addChild(bouton6);
		this.addChild(bouton7);
		this.addChild(bouton8);
		this.addChild(bouton9);
		this.addChild(bouton0);
	}
}
