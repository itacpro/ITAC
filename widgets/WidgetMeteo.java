package widgets;

import java.net.URL;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.css.util.CSSKeywords.Position;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class WidgetMeteo extends MTRectangle{
	
	private static org.jdom.Document document;
	private static Element racine;
	public int x, y;
	public String nomVille;
	public int idVille = 584008;
	public String temperature;
	public String urlRacineXML = "http://weather.yahooapis.com/";
	public String ImageCode = "";
	public AbstractMTApplication app;
	public Ville[] ville = new Ville[10];
	public MTList navigateur;
	public MTTextArea infos, tempDay1, tempDay2, tempUp, weatherUp, text1, text2, text1Up, text2Up;
	public MTRectangle image, tiroir, bg, poignee, overlay;
	private IFont fontBig, fontNav, fontMedium, fontSmall;
	private MTRectangle arrowUp, arrowDown;
	
	public WidgetMeteo(int _x, int _y, AbstractMTApplication _app, int idStart) {
		super(_app, _x, _y, 377, 293);
		
		//Récupération des paramètres
		app = _app;
		x = _x;
		y = _y;
		
		//Fonts
		fontBig = FontManager.getInstance().createFont(app, "Impact.ttf", 40, new MTColor(255, 255, 255), true);
		fontNav = FontManager.getInstance().createFont(app, "Impact.ttf", 20, new MTColor(0, 0, 0), true);
		fontMedium = FontManager.getInstance().createFont(app, "Impact.ttf", 25, new MTColor(255, 255, 255), true);
		fontSmall = FontManager.getInstance().createFont(app, "Impact.ttf", 15, new MTColor(17, 122, 188), true);
		
		//Création du tiroir
		tiroir = new MTRectangle(app, x + 163, y + 2, 240, 291);
		this.addChild(tiroir);
		
		//Liste des villes
		ville[0] = new Ville(584008, "Chambéry");
		ville[1] = new Ville(615702, "Paris");
		ville[2] = new Ville(609125, "Lyon");
		ville[3] = new Ville(610264, "Marseille");
		ville[4] = new Ville(593720, "Grenoble");
		ville[5] = new Ville(623868, "ST-Etienne");
		ville[6] = new Ville(2459115, "New York");
		ville[7] = new Ville(44418, "Londres");
		ville[8] = new Ville(1118370, "Tokyo");
		ville[9] = new Ville(2122265, "Moscou");
		
		//Liste/slider d'affichage des villes
		navigateur = new MTList(x + 210, y + 45, 163, 200, app);
		navigateur.setNoFill(true);
		navigateur.setNoStroke(true);
		tiroir.addChild(navigateur);
		
		//On ajoute une cellule au slider pour chaque ville
		for(int i = 0; i < 10; i++)
		{
			Cellule cell = new Cellule(163, 31, app, i);
			
			//Nom de la ville
			MTTextArea texte = new MTTextArea(app);
			texte.setText(ville[i].nom);
			texte.setAnchor(PositionAnchor.CENTER);
			texte.setPositionGlobal(cell.getCenterPointGlobal());
			texte.setFont(fontNav);
			texte.setNoFill(true);
			texte.setNoStroke(true);
			cell.addChild(texte);
			cell.setNoFill(true);
			cell.setNoStroke(true);
			
			//Ajout de la cellule dans le slider
			navigateur.addChild(cell);
			
			//On défini les actions lorsqu'on clique sur le nom d'une ville
			setTouchActions(cell);
		}
		
		//Flèche supérieure
		arrowUp = new MTRectangle(app, navigateur.getCenterPointGlobal().x , y + 30, 13, 12);
		arrowUp.setTexture(app.loadImage("../ressources/widget-weather/arrow.png"));
		arrowUp.rotateZ(arrowUp.getCenterPointGlobal(), 180);
		arrowUp.setNoStroke(true);
		tiroir.addChild(arrowUp);
		
		//Flèche inférieure
		arrowDown = new MTRectangle(app, navigateur.getCenterPointGlobal().x , y + 250, 13, 12);
		arrowDown.setTexture(app.loadImage("../ressources/widget-weather/arrow.png"));
		arrowDown.setNoStroke(true);
		tiroir.addChild(arrowDown);
		
		//Création du fond du widget
		bg = new MTRectangle(app, x, y, 377, 293);
		this.addChild(bg);
		
		//Création de la poignée
		poignee = new MTRectangle(app, x + 377, y + 2, 26, 291);
		this.addChild(poignee);
		
		//Création des variables d'affichage du texte
		infos = new MTTextArea(app);
		tempDay1 = new MTTextArea(app);
		tempDay2 = new MTTextArea(app);
		tempUp = new MTTextArea(app);
		weatherUp = new MTTextArea(app);
		text1 = new MTTextArea(app);
		text2 = new MTTextArea(app);
		text1Up = new MTTextArea(app);
		text2Up = new MTTextArea(app);
		
		//On affiche la ville par défaut
		getWeatherInfo(idStart);
		
		//On défini les paramètres d'affichage du widget
		setWeatherDesign();
		
		//On actualise les infos
		updateText();
		
		//On défini les paramètres de la poignée
		setPoigneeActions();
	}
	
	/**
	 * Fonction qui permet de récupérer les infos météorologiques d'une ville à partir de son id YahooWeather
	 * @param idVille
	 */
	public void getWeatherInfo(int idVille){
		//Préparation des variables XML
		URL fichierXML = null;
		SAXBuilder sxb = new SAXBuilder();
		
		try {
			//Récupération du fichier XML de la ville
			fichierXML = new URL(urlRacineXML + "forecastrss?w=" + idVille + "&u=c");
			document = sxb.build(fichierXML);
			racine = document.getRootElement();
			
			//On récupère l'élément channel
			Element item = racine.getChild("channel");
			Namespace yweather = Namespace.getNamespace("yweather", "http://xml.weather.yahoo.com/ns/rss/1.0");
			
			//On récupère les informations météorologiques
			nomVille = item.getChild("location", yweather).getAttributeValue("city");
			temperature = item.getChild("item").getChild("condition", yweather).getAttributeValue("temp");
			ImageCode = item.getChild("item").getChild("condition", yweather).getAttributeValue("code");
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	
	/**
	 * Fonction permettant d'actualiser le texte d'affichage de la météo
	 */
	public void updateText(){
		infos.setText(nomVille);
		tempDay1.setText(temperature + "°");
		//image.setTexture(app.loadImage("../ressources/widget-weather/" + ImageCode + ".png"));
	}
	
	/**
	 * Fonction permettant de définir les éléments graphiques du widget
	 */
	public void setWeatherDesign(){
		//Image de fond
		bg.setTexture(app.loadImage("../ressources/widget-weather/fond.png"));
		bg.setNoStroke(true);
		bg.removeAllGestureEventListeners();
		this.setNoFill(true);
		this.setNoStroke(true);
		
		//Tiroir
		tiroir.setTexture(app.loadImage("../ressources/widget-weather/tiroir.png"));
		tiroir.setNoStroke(true);
		tiroir.removeAllGestureEventListeners();
		
		//Poignee
		poignee.setNoStroke(true);
		poignee.setNoFill(true);
		
		//Image représentant la météo
		image = new MTRectangle(128, 135, app);
		if(ImageCode != "")
		{
			//image.setTexture(app.loadImage("../ressources/widget-weather/" + ImageCode + ".png"));
			image.setTexture(app.loadImage("../ressources/widget-weather/32_34_36.png"));
		}
		else
		{
			//Image par défaut
			image.setTexture(app.loadImage("../ressources/widget-weather/44.png"));
		}
		image.setNoStroke(true);
		image.setAnchor(PositionAnchor.UPPER_LEFT);
		image.setPositionGlobal(new Vector3D(x + 4, y + 4));
		image.setPickable(false);
		
		//Texte Ville
		infos.setAnchor(PositionAnchor.UPPER_LEFT);
		infos.setText(nomVille);
		infos.setPickable(false);
		infos.setNoStroke(true);
		infos.setNoFill(true);
		infos.setFont(fontBig);
		infos.setPositionRelativeToParent(new Vector3D(x + 20, y + 30));
		
		//Affichage "Aujourd'hui : "
		text1Up.setPickable(false);
		text1Up.setNoStroke(true);
		text1Up.setNoFill(true);
		text1Up.setText("Aujourd'hui:");
		text1Up.setFont(fontSmall);
		text1Up.setPositionGlobal(new Vector3D(x + 260, y + 40));
		
		//Affichage "Temps : "
		text2Up.setPickable(false);
		text2Up.setNoStroke(true);
		text2Up.setNoFill(true);
		text2Up.setText("Temps:");
		text2Up.setFont(fontSmall);
		text2Up.setPositionGlobal(new Vector3D(x + 260, y + 80));
		
		//Affichage de la température actuelle
		tempUp.setPickable(false);
		tempUp.setNoStroke(true);
		tempUp.setNoFill(true);
		tempUp.setText("0°");
		tempUp.setFont(fontMedium);
		tempUp.setPositionGlobal(new Vector3D(x + 330, y + 40));
		
		//Affichage de la météo actuelle
		weatherUp.setPickable(false);
		weatherUp.setNoStroke(true);
		weatherUp.setNoFill(true);
		weatherUp.setText("Clair");
		weatherUp.setFont(fontMedium);
		weatherUp.setPositionGlobal(new Vector3D(x + 330, y + 80));
		
		//Affichage "Aujourd'hui :"
		text1.setPickable(false);
		text1.setNoStroke(true);
		text1.setNoFill(true);
		text1.setText("Aujourd'hui:");
		text1.setFont(fontMedium);
		text1.setPositionGlobal(new Vector3D(x + 95, y + 180));
		
		//Affichage "Demain :"
		text2.setPickable(false);
		text2.setNoStroke(true);
		text2.setNoFill(true);
		text2.setText("Demain:");
		text2.setFont(fontMedium);
		text2.setPositionGlobal(new Vector3D(x + 285, y + 180));
		
		//Affichage Temperature Jour 1
		tempDay1.setPickable(false);
		tempDay1.setNoStroke(true);
		tempDay1.setNoFill(true);
		tempDay1.setText("0°");
		tempDay1.setFont(fontBig);
		tempDay1.setPositionGlobal(new Vector3D(x + 95, y + 230));
		
		//Affichage Temperature Jour 2
		tempDay2.setPickable(false);
		tempDay2.setNoStroke(true);
		tempDay2.setNoFill(true);
		tempDay2.setText("0°");
		tempDay2.setFont(fontBig);
		tempDay2.setPositionGlobal(new Vector3D(x + 285, y + 230));
		
		//Overlay
		overlay = new MTRectangle(app, x, y, 370, 293);
		overlay.setNoFill(true);
		overlay.setNoStroke(true);
		setOverlayTouchAction();
		
		this.addChild(image);
		this.addChild(infos);
		this.addChild(text1Up);
		this.addChild(text2Up);
		this.addChild(tempUp);
		this.addChild(weatherUp);
		this.addChild(text1);
		this.addChild(tempDay1);
		this.addChild(text2);
		this.addChild(tempDay2);
		this.addChild(overlay);
	}
	
	/**
	 * Fonction permettant de définir les actions sur les cellules de la liste/slider
	 * @param cell
	 */
	public void setTouchActions(final Cellule cell){
		cell.registerInputProcessor(new TapProcessor(app, 10));
		cell.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				if(te.isTapped())
				{
					cell.setNoFill(true);
					//On récupère les infos météorologiques de la ville sélectionnée
					getWeatherInfo(ville[cell.id].id);
					updateText();
				}
				
				if(te.isTapDown())
				{
					cell.setNoFill(false);
					cell.setTexture(app.loadImage("../ressources/widget-weather/etat_clique.png"));
				}
				
				if(te.isTapCanceled())
				{
					cell.setNoFill(true);
				}
				return false;
			}
		});
	}
	
	public void setOverlayTouchAction(){
		overlay.removeAllGestureEventListeners();
		overlay.unregisterAllInputProcessors();
		overlay.registerInputProcessor(new DragProcessor(app));
		overlay.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				DragEvent de = (DragEvent)ge;
				Vector3D vecteur = de.getTranslationVect();
				
				translateGlobal(vecteur);
				
				return false;
			}
		});
	}
	
	public void setPoigneeActions(){
		poignee.unregisterAllInputProcessors();
		poignee.removeAllGestureEventListeners();
		poignee.registerInputProcessor(new DragProcessor(app));
		poignee.setAnchor(PositionAnchor.UPPER_LEFT);
		poignee.addGestureListener(DragProcessor.class, new IGestureEventListener() {
		    public boolean processGestureEvent(MTGestureEvent ge) {
		        DragEvent de = (DragEvent)ge;
		        
		        Vector3D position = poignee.getPosition(TransformSpace.GLOBAL);
		        Vector3D vecteur = de.getTranslationVect();
		        
		        if(position.getX() >= getPosition(TransformSpace.GLOBAL).x + 377 && position.getX() <= getPosition(TransformSpace.GLOBAL).x + 540)
		        {
		        	if(vecteur.getX() + position.getX() >= getPosition(TransformSpace.GLOBAL).x + 377 && vecteur.getX() + position.getX() <= getPosition(TransformSpace.GLOBAL).x + 540)
		        	{
		        		de.getTarget().translateGlobal(new Vector3D(vecteur.getX(), 0));
		        		tiroir.translateGlobal(new Vector3D(vecteur.getX(), 0));
		        	}
		        }
		        
		        return false;
		    }
		});
	}

}

/**
 * Classe Ville 
 * @author Nicolas Lefebvre
 * @version 1.0
 */
class Ville{
	public int id;
	public String nom;
	
	public Ville(int _id, String _nom){
		id = _id;
		nom = _nom;
	}
}

/**
 * Classe Cellule 
 * @author Nicolas Lefebvre
 * @version 1.0
 */
class Cellule extends MTListCell{
	
	public int id;
	
	public Cellule(float width, float height, PApplet applet, int _id) {
		super(width, height, applet);
		id = _id;
	}
	
}
