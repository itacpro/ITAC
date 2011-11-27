package Widgets;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

import Itac.Utilisateur;

public class WidgetScene extends MTRectangle {
	
	public AbstractMTApplication app;
	public float xPos, yPos;
	public ThreadHeure threadHeure;
	private Utilisateur user;
	static org.jdom.Document document;
	static Element racine;
	private boolean isWidgetHeure = false;
	
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
		
		Element widgets = racine.getChild("widgets");
		
		List listInfos = widgets.getChildren("widget");
		
		Iterator i = listInfos.iterator();
		while(i.hasNext())
		{
			Element courant = (Element)i.next();
			
			if(courant.getChild("type").getText().equals("weather"))
			{
				int x, y;
				x = Integer.parseInt(courant.getChild("posX").getText());
				y = Integer.parseInt(courant.getChild("posY").getText());
				WidgetMeteo meteo = new WidgetMeteo(x, y, 300, 200, app);
				meteo.setName(courant.getAttributeValue("id"));
				setTouchAction(meteo);
				this.addChild(meteo);
			}
			
			if(courant.getChild("type").getText().equals("time"))
			{
				int x, y;
				x = Integer.parseInt(courant.getChild("posX").getText());
				y = Integer.parseInt(courant.getChild("posY").getText());
				WidgetHeure heure = new WidgetHeure(x, y, 201, 198, app);
				heure.setName(courant.getAttributeValue("id"));
				setTouchAction(heure);
				this.addChild(heure);
				threadHeure = new ThreadHeure("Widget Heure", heure);
				threadHeure.start();
				isWidgetHeure  = true;
			}
		}
		
		
		this.setApplicationDesign();
	}
	
	public void setApplicationDesign(){
		this.setNoFill(true);
		this.setNoStroke(true);
		
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
	}
	
	public void setTouchAction(final MTRectangle widget){
		
		widget.registerInputProcessor(new TapProcessor(app, 15));
		widget.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				DragEvent te = (DragEvent)ge;
				
				if (te.getId() == MTGestureEvent.GESTURE_ENDED)
				{
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
						//On utilise ici un affichage classique avec getPrettyFormat()
						XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
						//Remarquez qu'il suffit simplement de créer une instance de FileOutputStream
						//avec en argument le nom du fichier pour effectuer la sérialisation.
						sortie.output(document, new FileOutputStream("../users/" + user.getUserName() + "/params/config.xml"));
					} catch (java.io.IOException e){}
				}
				
				return false;
			}
		});
	}
	
	public void destroy(){
		if(isWidgetHeure == true)
		{
			threadHeure.interrupt();
		}
	}
}