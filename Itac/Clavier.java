package Itac;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
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
	
	//D�claration des variables
	public AbstractMTApplication app;
	public Login application;
	public float xPos, yPos;
	public IFont font = FontManager.getInstance().createFont(app, "arial.ttf", 30, new MTColor(255,255,255));
	
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
		
		//R�cup�ration des param�tres
		app = _app;
		application = _application;
		xPos = _xPos;
		yPos = _yPos;
		
		setKeyboardDesign();
		addButtons();
	}
	
	/**
	 * D�finit l'apparence du clavier.
	 */
	public void setKeyboardDesign(){
		this.getCenterPointGlobal();
		this.setPositionGlobal(new Vector3D(app.width/2f, app.height - 200));
		this.setNoFill(true);
		this.setNoStroke(true);
		
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
	}
	
	/**
	 * Ajoute 9 boutons au clavier
	 */
	public void addButtons(){
		float x = app.width/2f;
		float y = app.height - 200;
		
		Bouton bouton1 = new Bouton("1", x + 0, y + 0, 100, 100, app, application);
		Bouton bouton2 = new Bouton("2", x + 100, y + 0, 100, 100, app, application);
		Bouton bouton3 = new Bouton("3", x + 200, y + 0, 100, 100, app, application);
		Bouton bouton4 = new Bouton("4", x + 0, y + 100, 100, 100, app, application);
		Bouton bouton5 = new Bouton("5", x + 100, y + 100, 100, 100, app, application);
		Bouton bouton6 = new Bouton("6", x + 200, y + 100, 100, 100, app, application);
		Bouton bouton7 = new Bouton("7", x + 0, y + 200, 100, 100, app, application);
		Bouton bouton8 = new Bouton("8", x + 100, y + 200, 100, 100, app, application);
		Bouton bouton9 = new Bouton("9", x + 200, y + 200, 100, 100, app, application);
		
		this.addChild(bouton1);
		this.addChild(bouton2);
		this.addChild(bouton3);
		this.addChild(bouton4);
		this.addChild(bouton5);
		this.addChild(bouton6);
		this.addChild(bouton7);
		this.addChild(bouton8);
		this.addChild(bouton9);
	}
}
