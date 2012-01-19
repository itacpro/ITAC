package widgets;

import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.mt4j.AbstractMTApplication;
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

import processing.core.PApplet;

public class WidgetTV extends MTRectangle {
	private static org.jdom.Document document;
	private static Element racine;
	private int x, y;
	private AbstractMTApplication app;
	private MTList liste;
	
	public WidgetTV(PApplet pApplet, float _x, float _y, float width, float height) {
		super(pApplet, _x, _y, width, height);
		
		//R�cup�ration des param�tres
		app = (AbstractMTApplication) pApplet;
		x = (int) _x;
		y = (int) _y;
		
		//Cr�ation de la liste permettant d'afficher le programme TV dans un slider
		liste = new MTList(app, 0, 150, 230, 210);
		
		//On cr�� les variables permettant de r�cup�rer le flux XML du programme TV sur internet
		URL fichierXML = null;
		SAXBuilder sxb = new SAXBuilder();
		
		try {
			//R�cup�ration du flux XML
			fichierXML = new URL("http://feeds.feedburner.com/programme-television");
			document = sxb.build(fichierXML);
			racine = document.getRootElement();
			
			//On r�cup�re les informations contenues dans la balise channel
			Element item = racine.getChild("channel");
			
			//On r�cup�re toutes les balises item
			List listParams = item.getChildren("item");
			
			//On pr�pare la variable qui contiendra ls informations sur les cha�nes
			List<String> chaines = new LinkedList<String>();
			
			int cpt = 0;
			
			//On cr�e un Iterator sur la liste des item
			Iterator i = listParams.iterator();
			while(i.hasNext())
			{
				Element courant = (Element)i.next();
				
				//On r�cup�re la valeur de la balise
				String programmeCourrant = courant.getChild("title").getText();
				
				//On d�coupe la chaine de caract�re retourn�e pour pouvoir s�parer les informations
				StringTokenizer st = new StringTokenizer(programmeCourrant, "|");
				String chaine = st.nextToken();
				
				StringTokenizer st2 = new StringTokenizer(st.nextToken(), "(");
				String prog = st2.nextToken();
				prog = prog.replaceFirst(" ", "");
				
				boolean chaineExiste = false;
				
				//Le programme n'affiche que les informations du prime time
				//On s'assure de ne pas r�cup�rer d'autres informations
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
					
					//On ajoute la chaine � la liste des chaines
					chaines.add(chaine);
					
					//On charge le logo de la chaine
					MTRectangle image = new MTRectangle(1, 10, 50, 21, app);
					image.setTexture(app.loadImage("../ressources/widget-tv/" + cpt + ".gif"));
					image.setNoStroke(true);
					image.setPickable(false);
					
					//Param�tres de la police
					IFont fontArial = FontManager.getInstance().createFont(app, "arial.ttf", 15, new MTColor(0, 0, 0));
					
					//On affiche les informations du programme TV
					MTTextArea texte = new MTTextArea(55, 5, 190, 45, fontArial, app);
					texte.setText(prog);
					texte.setNoFill(true);
					texte.setNoStroke(true);
					
					//On cr�� une cellule dans laquelle on ajoute tous les �l�ments cr�� pr�c�demment
					MTListCell cellule = new MTListCell(230, 45, app);
					cellule.setNoStroke(true);
					cellule.addChild(image);
					cellule.addChild(texte);
					
					//On ajoute la cellule dans la liste/slider d'affichage
					liste.addListElement(cellule);
				}
				
				//On ne r�cup�re que les 6 premi�res cha�nes
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
	
	/**
	 * On d�fini le design du widget
	 */
	public void setTVDesign(){
		//Image de fond
		this.setTexture(app.loadImage("../ressources/widget-tv/fd_programmeTV.png"));
		this.setNoStroke(true);
		
		//Param�tres d'affichage de la liste/slider
		liste.setNoFill(true);
		liste.setAnchor(PositionAnchor.CENTER);
		liste.setPositionGlobal(this.getCenterPointGlobal());
		liste.setPositionGlobal(new Vector3D(x + 135, y + 165));
		
		this.addChild(liste);
		
		//Ajout de l'inertie sur le widget
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
		this.registerInputProcessor(new DragProcessor(app));
		this.addGestureListener(DragProcessor.class, new DefaultDragAction());
		this.addGestureListener(DragProcessor.class, new InertiaDragAction());
	}
}
