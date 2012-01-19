package widgets;

import itac.Utilisateur;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

import appLauncher.AppLauncher;

import photo.PhotoScene;
import piano.MTPiano;

public class WidgetScene extends MTRectangle {
	
	public AbstractMTApplication app;
	public float xPos, yPos;
	private Utilisateur user;
	static org.jdom.Document document;
	static Element racine;
	private AppLauncher launcher;
	
	/**
	 * Constructeur
	 * @param _xPos
	 * @param _yPos
	 * @param _width
	 * @param _height
	 * @param _app
	 * @param _user
	 */
	public WidgetScene(float _xPos, float _yPos, float _width, float _height, AbstractMTApplication _app, Utilisateur _user) {
		super(_xPos, _yPos, _width, _height, _app);
		
		app = _app;
		user = _user;
		
		//On crée une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		
		try {
			//On crée un nouveau document JDOM avec en argument le fichier XML
			document = sxb.build(new File("../users/" + user.getUserName() + "/params/config.xml"));
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
		
		//On initialise un nouvel élément racine avec l'élément racine du document.
		racine = document.getRootElement();
		//On récupère l'élément widgets
		Element widgets = racine.getChild("widgets");
		//Puis tous les éléments widget
		List listInfos = widgets.getChildren("widget");
		
		Iterator i = listInfos.iterator();
		while(i.hasNext())
		{
			Element courant = (Element)i.next();
			
			//Chargement du widget Météo
			if(courant.getChild("type").getText().equals("weather"))
			{
				int x, y;
				x = Integer.parseInt(courant.getChild("posX").getText());
				y = Integer.parseInt(courant.getChild("posY").getText());
				WidgetMeteo meteo = new WidgetMeteo(x, y, app, Integer.parseInt(courant.getChild("weatherCode").getText()));
				meteo.setAnchor(PositionAnchor.UPPER_LEFT);
				meteo.setName(courant.getAttributeValue("id"));
				setTouchAction(meteo);
				this.addChild(meteo);
			}
			
			//Chargement du widget Heure
			if(courant.getChild("type").getText().equals("time"))
			{
				int x, y;
				x = Integer.parseInt(courant.getChild("posX").getText());
				y = Integer.parseInt(courant.getChild("posY").getText());
				WidgetHeure heure = new WidgetHeure(x, y, 483, 183, app);
				heure.setAnchor(PositionAnchor.UPPER_LEFT);
				heure.setName(courant.getAttributeValue("id"));
				setTouchAction(heure);
				this.addChild(heure);
			}
			
			//Chargement du widget Date
			if(courant.getChild("type").getText().equals("date"))
			{
				int x, y, color;
				x = Integer.parseInt(courant.getChild("posX").getText());
				y = Integer.parseInt(courant.getChild("posY").getText());
				color = Integer.parseInt(courant.getChild("color").getText());
				WidgetDate date = new WidgetDate(app, x, y, color);
				date.setAnchor(PositionAnchor.UPPER_LEFT);
				date.setName(courant.getAttributeValue("id"));
				setTouchAction(date);
				this.addChild(date);
			}
			
			//Chargement du widget Programme TV
			if(courant.getChild("type").getText().equals("tv"))
			{
				int x, y;
				x = Integer.parseInt(courant.getChild("posX").getText());
				y = Integer.parseInt(courant.getChild("posY").getText());
				WidgetTV tv = new WidgetTV(app, x, y, 270, 280);
				tv.setAnchor(PositionAnchor.UPPER_LEFT);
				tv.setName(courant.getAttributeValue("id"));
				setTouchAction(tv);
				this.addChild(tv);
			}
			
			//Chargement du widget Calculatrice
			if(courant.getChild("type").getText().equals("calc"))
			{
				int x, y;
				x = Integer.parseInt(courant.getChild("posX").getText());
				y = Integer.parseInt(courant.getChild("posY").getText());
				WidgetCalc calc = new WidgetCalc(app, x, y, 242, 291);
				calc.setAnchor(PositionAnchor.UPPER_LEFT);
				calc.setName(courant.getAttributeValue("id"));
				setTouchAction(calc);
				this.addChild(calc);
			}
			
			//Chargement du widget Lecteur MP3
			if(courant.getChild("type").getText().equals("mp3Player"))
			{
				int x, y;
				x = Integer.parseInt(courant.getChild("posX").getText());
				y = Integer.parseInt(courant.getChild("posY").getText());
				WidgetMP3Player player = new WidgetMP3Player(app, x, y);
				player.setAnchor(PositionAnchor.UPPER_LEFT);
				player.setName(courant.getAttributeValue("id"));
				setTouchAction(player);
				this.addChild(player);
			}
		}
		
		//Bouton de lancement de l'application photo
		MTRectangle button = new MTRectangle(app.width-176, app.height-166, 176, 166, app);
		button.setTexture(app.loadImage("../ressources/launcher/bouton-menu.png"));
		button.setNoStroke(true);
		button.removeAllGestureEventListeners();
		this.addChild(button);
		
		final WidgetScene scene = this;
		
		launcher = new AppLauncher(app);
		
		//Action du bouton de lancement
		button.registerInputProcessor(new TapProcessor(app, 10));
		button.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{	
					//Launcher d'applications
			        launcher.open(scene);
				}
				
				return false;
			}
		});
		
		//On charge les paramètres graphiques de la scène
		this.setApplicationDesign();
	}
	
	/**
	 * Fonction permettant de charger les paramètres graphiques de la scène
	 */
	public void setApplicationDesign(){
		this.setNoFill(true);
		this.setNoStroke(true);
		
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
	}
	
	/**
	 * Défini les actions lorsqu'on déplace un widget
	 * @param widget
	 */
	public void setTouchAction(final MTRectangle widget){
		
		widget.registerInputProcessor(new TapProcessor(app, 15));
		widget.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				DragEvent te = (DragEvent)ge;
				
				if (te.getId() == MTGestureEvent.GESTURE_ENDED)
				{
					//On sauvegarde les coordonnées du widget dans le fichier de configuration XML
					Element widgets = racine.getChild("widgets");
					
					List listInfos = widgets.getChildren("widget");
					
					Iterator i = listInfos.iterator();
					while(i.hasNext())
					{
						Element courant = (Element)i.next();
						if(courant.getAttributeValue("id").equals(widget.getName()))
						{
							widget.setAnchor(PositionAnchor.UPPER_LEFT);
							Vector3D pos = widget.getPosition(TransformSpace.GLOBAL);
							String x = String.valueOf((int) pos.getX());
							String y = String.valueOf((int) pos.getY());
							courant.getChild("posX").setText(x);
							courant.getChild("posY").setText(y);
						}
					}
					
					try {
						//On enregistre le fichier XML
						XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
						sortie.output(document, new FileOutputStream("../users/" + user.getUserName() + "/params/config.xml"));
					} catch (java.io.IOException e){}
				}
				
				return false;
			}
		});
		
		if(widget instanceof WidgetDate)
		{
			final WidgetDate overlay = (WidgetDate) widget;
			overlay.overlay.registerInputProcessor(new TapProcessor(app, 15));
			overlay.overlay.addGestureListener(DragProcessor.class, new IGestureEventListener()
			{	
				public boolean processGestureEvent(MTGestureEvent ge)
				{
					DragEvent te = (DragEvent)ge;
					
					if (te.getId() == MTGestureEvent.GESTURE_ENDED)
					{
						//On sauvegarde les coordonnées du widget dans le fichier de configuration XML
						Element widgets = racine.getChild("widgets");
						
						List listInfos = widgets.getChildren("widget");
						
						Iterator i = listInfos.iterator();
						while(i.hasNext())
						{
							Element courant = (Element)i.next();
							if(courant.getAttributeValue("id").equals(widget.getName()))
							{
								widget.setAnchor(PositionAnchor.UPPER_LEFT);
								Vector3D pos = widget.getPosition(TransformSpace.GLOBAL);
								String x = String.valueOf((int) pos.getX());
								String y = String.valueOf((int) pos.getY());
								courant.getChild("posX").setText(x);
								courant.getChild("posY").setText(y);
								courant.getChild("color").setText(String.valueOf(overlay.color));
							}
						}
						
						try {
							//On enregistre le fichier XML
							XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
							sortie.output(document, new FileOutputStream("../users/" + user.getUserName() + "/params/config.xml"));
						} catch (java.io.IOException e){}
					}
					
					return false;
				}
			});
		}
		
		if(widget instanceof WidgetMeteo)
		{
			final WidgetMeteo overlay = (WidgetMeteo) widget;
			overlay.overlay.registerInputProcessor(new TapProcessor(app, 15));
			overlay.overlay.addGestureListener(DragProcessor.class, new IGestureEventListener()
			{	
				public boolean processGestureEvent(MTGestureEvent ge)
				{
					DragEvent te = (DragEvent)ge;
					
					if (te.getId() == MTGestureEvent.GESTURE_ENDED)
					{
						//On sauvegarde les coordonnées du widget dans le fichier de configuration XML
						Element widgets = racine.getChild("widgets");
						
						List listInfos = widgets.getChildren("widget");
						
						Iterator i = listInfos.iterator();
						while(i.hasNext())
						{
							Element courant = (Element)i.next();
							if(courant.getAttributeValue("id").equals(widget.getName()))
							{
								widget.setAnchor(PositionAnchor.UPPER_LEFT);
								Vector3D pos = widget.getPosition(TransformSpace.GLOBAL);
								String x = String.valueOf((int) pos.getX());
								String y = String.valueOf((int) pos.getY());
								courant.getChild("posX").setText(x);
								courant.getChild("posY").setText(y);
							}
						}
						
						try {
							//On enregistre le fichier XML
							XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
							sortie.output(document, new FileOutputStream("../users/" + user.getUserName() + "/params/config.xml"));
						} catch (java.io.IOException e){}
					}
					
					return false;
				}
			});
		}
	}
	
	public void destroyComponent(){
		for(int i = 0; i < this.getChildren().length; i++)
		{
			this.getChildren()[i].destroy();
		}
	}
}