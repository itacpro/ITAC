package Itac;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
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
 * Bouton pour le clavier de l'interface de login
 * @author Nicolas Lefebvre
 * @version 1.0
 */
public class Bouton extends MTRectangle{
	
	//D�claration des variables
	public AbstractMTApplication app;
	public Login application;
	public float xPos, yPos;
	public String name;
	public MTTextArea texte;
	public IFont font = FontManager.getInstance().createFont(app, "arial.ttf", 30, new MTColor(255,255,255));
	
	/**
	 * Constructeur
	 * @param Valeur que devra renvoyer le bouton lorsqu'on appuiera dessus
	 * @param Position sur l'axe X
	 * @param Position sur l'axe Y
	 * @param Largeur du bouton
	 * @param Hauteur du bouton
	 * @param Application
	 * @param Interface de login
	 */
	public Bouton(String _name, float _xPos, float _yPos, float _width, float _height, AbstractMTApplication _app, Login _application) {
		super(_xPos, _yPos, _width, _height, _app);
		
		//R�cup�ration des param�tres
		app = _app;
		application = _application;
		name = _name;
		xPos = _xPos;
		yPos = _yPos;
		
		this.setButtonDesign();
		this.setTouchActions();
		texte.setText(name);
	}
	
	/**
	 * D�finit l'apparence du bouton.
	 */
	public void setButtonDesign(){
		//Apparence du bouton
		this.setTexture(app.loadImage("bouton.png"));
		this.setNoStroke(true);
		
		//Apparence du texte
		texte = new MTTextArea(app, font);
		texte.setNoFill(true);
		texte.setNoStroke(true);
		texte.setPositionRelativeToParent(new Vector3D(xPos + 40, yPos + 50));
		
		//R�initialisation des �couteurs d'�v�nements
		texte.unregisterAllInputProcessors();
		texte.removeAllGestureEventListeners();
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
		
		this.addChild(texte);
	}
	
	/**
	 * D�finit l'action du bouton
	 */
	public void setTouchActions() {
		//Ecouteur d'action
		this.registerInputProcessor(new TapProcessor(app, 15));
		this.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{
					application.password = application.password + name;
					application.verifPassword();
				}
				if (te.isTapDown())
				{
					changeTexture("boutonHover.png");
				}
				if(te.getId() == MTGestureEvent.GESTURE_ENDED)
				{
					changeTexture("bouton.png");
				}
				return false;
			}
		});
		
		texte.registerInputProcessor(new TapProcessor(app, 15));
		texte.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{
					application.password = application.password + name;
					application.verifPassword();
				}
				if (te.isTapDown())
				{
					changeTexture("boutonHover.png");
				}
				if(te.getId() == MTGestureEvent.GESTURE_ENDED)
				{
					changeTexture("bouton.png");
				}
				return false;
			}
		});
	}
	
	/**
	 * Modifie la texture du bouton
	 * @param Chemin vers la texture
	 */
	public void changeTexture(String url) {
		this.setTexture(app.loadImage(url));
	}
}