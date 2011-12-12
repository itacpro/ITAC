package Widgets;

import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

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
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;
import processing.core.PImage;

public class WidgetTV extends MTRectangle {
	private static org.jdom.Document document;
	private static Element racine;
	private int x, y;
	private AbstractMTApplication app;
	private MTList liste;
	
	public WidgetTV(PApplet pApplet, float _x, float _y, float width, float height) {
		super(pApplet, _x, _y, width, height);
		
		app = (AbstractMTApplication) pApplet;
		x = (int) _x;
		y = (int) _y;
		
		liste = new MTList(app, 0, 0, 270, 170);
		
		URL fichierXML = null;
		SAXBuilder sxb = new SAXBuilder();
		
		try {
			fichierXML = new URL("http://feeds.feedburner.com/programme-television");
			document = sxb.build(fichierXML);
			racine = document.getRootElement();
			Element item = racine.getChild("channel");
			
			List listParams = item.getChildren("item");
			
			List<String> chaines = new LinkedList<String>();
			
			int cpt = 0;
			
			//On crée un Iterator sur notre liste
			Iterator i = listParams.iterator();
			while(i.hasNext())
			{
				Element courant = (Element)i.next();
				
				String programmeCourrant = courant.getChild("title").getText();
				//String[] chaine = programmeCourrant.split("|");
				
				StringTokenizer st = new StringTokenizer(programmeCourrant, "|");
				String chaine = st.nextToken();
				
				StringTokenizer st2 = new StringTokenizer(st.nextToken(), "(");
				String prog = st2.nextToken();
				prog = prog.replaceFirst(" ", "");
				
				boolean chaineExiste = false;
				
				for(int j = 0; j < chaines.size(); j++)
				{
					if(chaines.get(j).equals(chaine) == true)
					{
						chaineExiste = true;
					}
				}
				
				if(chaineExiste == false)
				{
					cpt++;
					
					chaines.add(chaine);
					System.out.println(chaine);
					
					MTRectangle image = new MTRectangle(1, 10, 70, 30, app);
					image.setTexture(app.loadImage("../ressources/widget-tv/" + cpt + ".gif"));
					image.setNoStroke(true);
					image.setPickable(false);
					
					MTTextArea texte = new MTTextArea(65, 5, 190, 65, app);
					//texte.setPositionRelativeToParent(new Vector3D(70, 10));
					texte.setText(prog);
					texte.setNoFill(true);
					texte.setNoStroke(true);
					
					MTListCell cellule = new MTListCell(278, 65, app);
					cellule.addChild(image);
					cellule.addChild(texte);
					
					liste.addListElement(cellule);
				}
				
				if(cpt == 6)
				{
					break;
				}
			}
			
		}
		catch(Exception e) {
			System.out.println("Erreur : " + e);
		}
		
		setTVDesign();
	}
	
	public void setTVDesign(){
		IFont fontArial = FontManager.getInstance().createFont(app, "arial.ttf", 20, new MTColor(255,255,255));
		
		this.setTexture(app.loadImage("weather.png"));
		this.setNoStroke(true);
		
		liste.setFillColor(new MTColor(150,150,150,100));
		liste.setNoStroke(true);
		liste.setAnchor(PositionAnchor.CENTER);
		liste.setPositionGlobal(this.getCenterPointGlobal());
		
		this.addChild(liste);
		
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
		this.registerInputProcessor(new DragProcessor(app));
		this.addGestureListener(DragProcessor.class, new DefaultDragAction());
		this.addGestureListener(DragProcessor.class, new InertiaDragAction());
	}
}
