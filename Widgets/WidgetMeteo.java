package Widgets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

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
	
	public int x, y;
	public String nomVille;
	public int idVille = 584008;
	public String temperature;
	public int codeImageMeteoCourrante;
	public String urlRacineXML = "http://weather.yahooapis.com/";
	public String urlRacineImage = "http://weather.yahooapis.com/";
	public String urlImage = "";
	public AbstractMTApplication app;
	
	public WidgetMeteo(int _x, int _y, int _width, int _height, AbstractMTApplication _app) {
		super(_x, _y, _width, _height, _app);
		
		app = _app;
		x = _x;
		y = _y;
		
		URL fichierXML = null;
		
		try {
			fichierXML = new URL(urlRacineXML + "forecastrss?w=" + idVille + "&u=c");
		
			if(fichierXML != null)
			{
				BufferedReader in = null;
				
				in = new BufferedReader(new InputStreamReader(fichierXML.openStream()));
				
				if(in != null)
				{
					boolean codeObtenu = false;
					String contenuLigne;
					
					while ((contenuLigne = in.readLine()) != null)
					{
						//Affichage du fichier XML
						//System.out.println(contenuLigne);
						
						//Récupération du nom de la ville
						if(contenuLigne.contains("city"))
						{
							nomVille = contenuLigne;
							nomVille = nomVille.substring(nomVille.indexOf("city")+6); 
							nomVille = nomVille.substring(0, nomVille.indexOf("\"")); 
						}
						
						//Récupération de la température courrante
						if(contenuLigne.contains("temp"))
						{
							temperature = contenuLigne;
							temperature = temperature.substring(temperature.indexOf("temp")+6); 
							temperature = temperature.substring(0, temperature.indexOf("\"")); 
						}
						
						//Récupération du code de représentation du temps
						if(contenuLigne.contains("code"))
						{
							if(codeObtenu != true)
							{
								urlImage = contenuLigne;
								urlImage = urlImage.substring(urlImage.indexOf("code")+6); 
								urlImage = urlImage.substring(0, urlImage.indexOf("\"")); 
								codeObtenu = true;
							}
						}
					}
				}
			}
		} catch (Exception e){
			System.out.println("Impossible de récupèrer le flux : " + e);
		}
		
		setWeatherDesign();
	}
	
	public void setWeatherDesign(){
		//System.out.println(nomVille + " - " + temperature + "°C");
		
		IFont fontArial = FontManager.getInstance().createFont(app, "arial.ttf", 20, new MTColor(255,255,255));
		
		this.setTexture(app.loadImage("weather.png"));
		this.setNoStroke(true);
		
		MTTextArea infos = new MTTextArea(app, fontArial);
		infos.setText(nomVille + " : " + temperature + " °C");
		infos.setFillColor(new MTColor(0, 0, 0, 0));
		infos.setPickable(false);
		infos.setNoStroke(true);
		infos.setPositionRelativeToParent(new Vector3D(x + 100, y + 50));
		
		MTRectangle image = new MTRectangle(93, 93, app);
		if(urlImage != "")
		{
			image.setTexture(app.loadImage("../ressources/widget-weather/" + urlImage + ".png"));
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
