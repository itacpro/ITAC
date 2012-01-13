package Widgets;

import java.net.URL;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
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
	public MTTextArea infos;
	public MTRectangle image;
	
	public WidgetMeteo(int _x, int _y, int _width, int _height, AbstractMTApplication _app, int idStart) {
		super(_x, _y, _width, _height, _app);
		
		app = _app;
		x = _x;
		y = _y;
		
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
		
		MTList navigateur = new MTList(x, y + _height, _width, _height, app);
		this.addChild(navigateur);
		
		for(int i = 0; i < 10; i++)
		{
			Cellule cell = new Cellule(200, 50, app, i);
			MTTextArea texte = new MTTextArea(0, 0, 200, 50, app);
			texte.setText(ville[i].nom);
			texte.setFontColor(new MTColor(0, 0, 0));
			cell.addChild(texte);
			
			navigateur.addChild(cell);
			
			setTouchActions(cell);
		}
		
		getWeatherInfo(idStart);
		setWeatherDesign();
	}
	
	public void getWeatherInfo(int idVille){
		URL fichierXML = null;
		SAXBuilder sxb = new SAXBuilder();
		
		try {
			fichierXML = new URL(urlRacineXML + "forecastrss?w=" + idVille + "&u=c");
			document = sxb.build(fichierXML);
			racine = document.getRootElement();
			Element item = racine.getChild("channel");
			Namespace yweather = Namespace.getNamespace("yweather", "http://xml.weather.yahoo.com/ns/rss/1.0");
			
			nomVille = item.getChild("location", yweather).getAttributeValue("city");
			temperature = item.getChild("item").getChild("condition", yweather).getAttributeValue("temp");
			ImageCode = item.getChild("item").getChild("condition", yweather).getAttributeValue("code");
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public void updateText(){
		infos.setText(nomVille + " : " + temperature + " °C");
		image.setTexture(app.loadImage("../ressources/widget-weather/" + ImageCode + ".png"));
	}
	
	public void setWeatherDesign(){
		IFont fontArial = FontManager.getInstance().createFont(app, "arial.ttf", 20, new MTColor(255,255,255));
		
		this.setTexture(app.loadImage("weather.png"));
		this.setNoStroke(true);
		
		infos = new MTTextArea(app, fontArial);
		infos.setText(nomVille + " : " + temperature + " °C");
		infos.setFillColor(new MTColor(0, 0, 0, 0));
		infos.setPickable(false);
		infos.setNoStroke(true);
		infos.setPositionRelativeToParent(new Vector3D(x + 80, y + 50));
		
		image = new MTRectangle(93, 93, app);
		if(ImageCode != "")
		{
			image.setTexture(app.loadImage("../ressources/widget-weather/" + ImageCode + ".png"));
		}
		else
		{
			image.setTexture(app.loadImage("../ressources/widget-weather/44.png"));
		}
		image.setNoStroke(true);
		image.setPositionRelativeToParent(new Vector3D(x + this.getWidthXY(TransformSpace.LOCAL) - 55, y + 35));
		image.setPickable(false);
		
		this.addChild(infos);
		this.addChild(image);
		
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
		this.registerInputProcessor(new DragProcessor(app));
		this.addGestureListener(DragProcessor.class, new DefaultDragAction());
		this.addGestureListener(DragProcessor.class, new InertiaDragAction());
	}
	
	public void setTouchActions(final Cellule cell){
		cell.registerInputProcessor(new TapProcessor(app, 10));
		cell.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{
					getWeatherInfo(ville[cell.id].id);
					updateText();
				}
				return false;
			}
		});
	}

}

class Ville{
	public int id;
	public String nom;
	
	public Ville(int _id, String _nom){
		id = _id;
		nom = _nom;
	}
}

class Cellule extends MTListCell{
	
	public int id;
	
	public Cellule(float width, float height, PApplet applet, int _id) {
		super(width, height, applet);
		id = _id;
	}
	
}
