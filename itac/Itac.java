package itac;

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
	
	/**
	 * Constructeur permettant d'initialiser le logiciel
	 * @param _app
	 * @param _name
	 */
	public Itac(AbstractMTApplication _app, String _name) {
		super(_app, _name);
		
		//R�cup�ration des param�tres
		app = _app;
		
		//Affichage des cercles de pointage
		this.registerGlobalInputProcessor(new CursorTracer(app, this));
		
		//Chargement du fond d'�cran par d�faut
		background = app.loadImage("../ressources/login/fond-ecran-default.png");
		this.getCanvas().addChild(new MTBackgroundImage(app, background, false));
		
		//Cr�ation d'un calque noir pour le d�marrage de l'application
		final MTRectangle start = new MTRectangle(0, 0, 50, app.width, app.height, app);
		start.setFillColor(new MTColor(0, 0, 0));
		start.setNoStroke(true);
		start.removeAllGestureEventListeners();
		this.getCanvas().addChild(start);
		
		//Disparition en fondu du calque noir
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
		
		//Lancement de la musique d'introduction
		Mp3Player.play("../ressources/sound/initialyze.mp3");
		
		//D�marrage de l'�cran de login
		Login login = new Login(0, 0, app.width, app.height, 1000, app, this);
		this.addApplication(login);
	}
	
	/**
	 * Fonction permettant de lancer une application
	 * @param application
	 */
	public void addApplication(MTRectangle application){
		this.getCanvas().addChild(application);
	}
	
	/**
	 * Fonction permettant de changer le fond d'�cran
	 * @param bg
	 */
	public void changeBackground(String bg){
		this.getCanvas().addChild(new MTBackgroundImage(app, app.loadImage(bg), false));
	}
	
	/**
	 * Fonction permettant de d�connecter l'utilisateur connect�
	 */
	public void deconnexion(){
		//On r�initialise le canvas en d�truisant tous les composants
		currentUser = "";
		MTComponent[] children = this.getCanvas().getChildren();
		
		for (int i = 0; i < children.length; i++)
		{
			children[i].destroy();
		}
		
		//On recr�� l'affichage des pointeurs, ainsi que le fond d'�cran (d�truits pr�c�demment)
		this.registerGlobalInputProcessor(new CursorTracer(app, this));
		changeBackground("../ressources/login/fond-ecran-default.png");
		
		//On lance l'interface de login
		Login login = new Login(0, 0, app.width, app.height, 1000, app, this);
		this.addApplication(login);
	}
	
	/**
	 * Fontion permettant l'affichage de l'utilisateur connect� en haut � gauche du bureau, ainsi qu'un bouton de d�connexion
	 */
	public void afficherUtilisateur(){
		
		//Cr�ation d'un contour blanc autour de l'image de l'utilisateur
		MTRoundRectangle userBg = new MTRoundRectangle(30, 30, 0, 110, 110, 5, 5, app);
		userBg.removeAllGestureEventListeners();
		
		//On r�cup�re les informations de l'utilisateur connect�
		Utilisateur user = new Utilisateur(currentUser, 85, 85, 100, 100, app);
		user.texte.setVisible(false);
		
		//Affichage du nom de l'utilisateur
		IFont font = FontManager.getInstance().createFont(app, "arial.ttf", 30, new MTColor(255,255,255));
		MTTextArea texte = new MTTextArea(app, font);
		texte.setNoFill(true);
		texte.setNoStroke(true);
		texte.setText(user.getName());
		texte.setAnchor(PositionAnchor.UPPER_LEFT);
		texte.setPositionGlobal(new Vector3D(150, 30));
		
		//Affichage du bouton de d�connexion
		final MTRectangle deconnect = new MTRectangle(150, 100, 94, 33, app);
		deconnect.setTexture(app.loadImage("../ressources/login/btn_deconnexion.png"));
		deconnect.removeAllGestureEventListeners();
		deconnect.setNoStroke(true);
		
		//Actions du bouton d�connexion
		deconnect.registerInputProcessor(new TapProcessor(app, 15));
		deconnect.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				
				if (te.isTapped())
				{
					//D�connexion au tap
					deconnexion();
				}
				if (te.isTapDown())
				{
					//Changement de texture du bouton lorsqu'on le maintient appuy�
					deconnect.setTexture(app.loadImage("../ressources/login/btn_deconnexion_hover.png"));
				}
				if(te.getId() == MTGestureEvent.GESTURE_ENDED)
				{
					//Changement de texture du bouton lorsqu'on le rel�che
					deconnect.setTexture(app.loadImage("../ressources/login/btn_deconnexion.png"));
				}
				
				return false;
			}
		});
		
		//Ajout des �l�ments du bouton dans le canvas
		this.getCanvas().addChild(userBg);
		this.getCanvas().addChild(user);
		this.getCanvas().addChild(texte);
		this.getCanvas().addChild(deconnect);
	}
}