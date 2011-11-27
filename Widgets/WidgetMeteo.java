package Widgets;

import java.net.URL;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

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
	
	public WidgetMeteo(int _x, int _y, int _width, int _height, AbstractMTApplication _app) {
		super(_x, _y, _width, _height, _app);
		
		app = _app;
		x = _x;
		y = _y;
		
		
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
		
		setWeatherDesign();
	}
	
	public void setWeatherDesign(){
		IFont fontArial = FontManager.getInstance().createFont(app, "arial.ttf", 20, new MTColor(255,255,255));
		
		this.setTexture(app.loadImage("weather.png"));
		this.setNoStroke(true);
		
		MTTextArea infos = new MTTextArea(app, fontArial);
		infos.setText(nomVille + " : " + temperature + " ¡C");
		infos.setFillColor(new MTColor(0, 0, 0, 0));
		infos.setPickable(false);
		infos.setNoStroke(true);
		infos.setPositionRelativeToParent(new Vector3D(x + 80, y + 50));
		
		MTRectangle image = new MTRectangle(93, 93, app);
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

}
