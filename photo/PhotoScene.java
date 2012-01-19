package photo;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTSlider;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

public class PhotoScene extends MTRectangle{
	
	//Déclaration des variables
	private AbstractMTApplication app;
	private int x, y, width, height;
	private static org.jdom.Document document;
	private static Element racine;
	private MTList navigateur;
	private MTRectangle cadreNav;
	private MTRectangle poignee;
	
	//Propriétés du navigateur de fichiers
	private int navWidth = 473;
	private int poigneeWidth = 48;
	private int poigneeHeight = 123;
	
	//Propriétés des cellules
	private int cellWidth = 466;
	private int cellHeight = 330;
	private IFont font = FontManager.getInstance().createFont(app, "arial.ttf", 30, new MTColor(255,255,255));
	private int idCellOpened;
	private boolean isCellOpened = false;
	
	private Photo[][] photos;
	
	public PhotoScene(AbstractMTApplication _app, int _x, int _y, int _width, int _height) {
		super(_app, _x, _y, _width, _height);
		
		//Récupération des variables
		app = _app;
		x = _x;
		y = _y;
		width = _width;
		height = _height;
		
		//Paramètrage de l'application
		this.setNoStroke(true);
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
		
		//Image de fond de l'application
		this.setTexture(app.loadImage("../ressources/login/fond-ecran-default.png"));
		
		//Cadre du navigateur
		cadreNav = new MTRectangle(width - navWidth, 0, navWidth, height, app);
		cadreNav.setTexture(app.loadImage("../ressources/photo/fd_AppPhoto.png"));
		cadreNav.setPickable(false);
		cadreNav.setNoStroke(true);
		
		//Création de la poignée du volet
		poignee = new MTRectangle(0, 0, poigneeWidth, poigneeHeight, app);
		poignee.setPositionGlobal(new Vector3D(width - navWidth - poigneeWidth/2 + 5, height/2));
		poignee.setTexture(app.loadImage("../ressources/photo/fd_btn_AppPhoto.png"));
		poignee.setNoStroke(true);
		actionPoignee();
		
		//Création du navigateur de fichiers
		navigateur = new MTList(width - navWidth, 100, navWidth, height - 200, app);
		navigateur.setNoFill(true);
		navigateur.setNoStroke(true);
		this.addChild(cadreNav);
		cadreNav.addChild(navigateur);
		this.addChild(poignee);
		
		loadImages();
		
		
		final MTRectangle closeButton = new MTRectangle(app.width - 160, 30, 130, 36, app);
		closeButton.setNoStroke(true);
		closeButton.removeAllGestureEventListeners();
		closeButton.setTexture(app.loadImage("../ressources/photo/btn_quitter.png"));
		cadreNav.addChild(closeButton);
		
		final PhotoScene photoApp = this;
		
		closeButton.registerInputProcessor(new TapProcessor(app, 10));
		closeButton.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{
					photoApp.destroy();
				}
				if (te.isTapDown())
				{
					closeButton.setTexture(app.loadImage("../ressources/photo/btn_quitter_hover.png"));
				}
				if(te.getId() == MTGestureEvent.GESTURE_ENDED)
				{
					closeButton.setTexture(app.loadImage("../ressources/photo/btn_quitter.png"));
				}
				return false;
			}
		});
		
	}
	
	//Chargement des images
	private void loadImages(){
		//On crée une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
				
		try {
			//On crée un nouveau document JDOM avec en argument le fichier XML
			document = sxb.build(new File("../ressources/photo/albums.xml"));
			
			//On initialise un nouvel élément racine avec l'élément racine du document.
			racine = document.getRootElement();
			
			photos = new Photo[racine.getChildren("album").size()][];
			
			List listAlbums = racine.getChildren("album");
			Iterator i = listAlbums.iterator();
			
			int cpt = 0;
			
			//On parcours les albums
			while(i.hasNext())
			{
				Element courant = (Element)i.next();
				//System.out.println(courant.getChildText("titre"));
				
				//Création de la cellule
				Cellule cell = new Cellule(cellWidth, cellHeight, app);
				cell.setNoFill(true);
				cell.setNoStroke(true);
				cell.id = cpt;
				cell.setName(String.valueOf(cpt));
				navigateur.addChild(cell);
				
				photos[cpt] = new Photo[courant.getChild("photos").getChildren("photo").size()];
				
				List listPhotos = courant.getChild("photos").getChildren("photo");
				Iterator j = listPhotos.iterator();
				int k = courant.getChild("photos").getChildren("photo").size();
				int cptPhotos = 0;
				
				//On parcours les photos
				while(j.hasNext())
				{
					Element photoCourant = (Element)j.next();
					//System.out.println(photoCourant.getText());
					
					//Création de l'image
					photos[cpt][cptPhotos] = new Photo(app, app.loadImage("../images/" + photoCourant.getText()));
					photos[cpt][cptPhotos].setPositionRelativeToParent(new Vector3D(cellWidth/2, cellHeight/2 - 20));
					setTailleImage(photos[cpt][cptPhotos], cellWidth - 250, cellHeight - 150);
					
					//Orientation des images
					if(k != 1)
					{
						float angle = (float) Math.random() * 15;
						float randomBool = (float) Math.random();
						
						if(randomBool <= 0.5)
						{
							angle = -angle;
						}
						
						photos[cpt][cptPhotos].rotateZ(photos[cpt][cptPhotos].getCenterPointGlobal(), angle);
						photos[cpt][cptPhotos].angle = angle;
					}
					
					cell.addChild(photos[cpt][cptPhotos]);
					
					cptPhotos++;
					k--;
				}
				
				//Affichage du titre de l'album
				MTTextArea titre = new MTTextArea(app, font);
				titre.setText(courant.getChildText("titre"));
				titre.setNoFill(true);
				titre.setNoStroke(true);
				titre.setPositionRelativeToParent(new Vector3D(cellWidth/2, cellHeight - 30));
				cell.addChild(titre);
				
				titre.setName("titre");
				actionCellule(cell);
				
				cpt++;
			}
		}
		catch(Exception e){ System.out.println(e.toString()); }
	}
	
	//Actions
	
	private void actionPoignee(){
		poignee.unregisterAllInputProcessors();
		poignee.removeAllGestureEventListeners();
		poignee.registerInputProcessor(new DragProcessor(app));
		poignee.getCenterPointGlobal();
		poignee.addGestureListener(DragProcessor.class, new IGestureEventListener() {
		    public boolean processGestureEvent(MTGestureEvent ge) {
		        DragEvent de = (DragEvent)ge;
		        
		        Vector3D position = poignee.getPosition(TransformSpace.GLOBAL);
		        Vector3D vecteur = de.getTranslationVect();
		        
		        if(position.getX() >= (width - navWidth - poigneeWidth/2 + 5) && position.getX() <= (width - poigneeWidth/2 + 5))
		        {
		        	if((vecteur.getX() + position.getX()) >= (width - navWidth - poigneeWidth/2 + 5) &&
		        			(vecteur.getX() + position.getX()) <= (width - poigneeWidth/2 + 5))
		        	{
		        		de.getTargetComponent().translateGlobal(new Vector3D(vecteur.getX(), 0)); //Moves the component
		        		cadreNav.translateGlobal(new Vector3D(vecteur.getX(), 0));
		        	}
		        }
		        return false;
		    }
		});
	}
	
	private void actionCellule(final Cellule cell){
		final MTRectangle photoApp = this;
		cell.registerInputProcessor(new TapProcessor(app, 10));
		cell.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{
					if(isCellOpened == false)
					{
						idCellOpened = cell.id;
						
						for(int i = 0; i < photos[idCellOpened].length; i++)
						{
							cell.background.setVisible(true);
							
							photos[idCellOpened][i].removeFromParent();
							photos[idCellOpened][i].setPositionRelativeToParent(cell.getCenterPointGlobal());
							photos[idCellOpened][i].rotateZ(photos[idCellOpened][i].getCenterPointGlobal(), -photos[idCellOpened][i].angle);
							setTailleImage(photos[idCellOpened][i], 400, 400);
							photoApp.addChild(photos[idCellOpened][i]);
							
							final int j = i;
							
							int x = (int) ((Math.random() * (width - navWidth - 500) + 200));
							final int y = (int) ((Math.random() * (height-200) + 200));
							
							//Calcul du trajet vertical à parcourir
							final float DX = (x - photos[idCellOpened][i].getPosition(TransformSpace.GLOBAL).x)/20;
							final float DY = (y - photos[idCellOpened][i].getPosition(TransformSpace.GLOBAL).y)/20;
							
							
							//Définition de l'animation de la translation
							final Animation translation = new Animation("translation", new MultiPurposeInterpolator(photos[idCellOpened][j].getPosition(TransformSpace.GLOBAL).x, x, 500, 0, 1, 1) , photos[idCellOpened][i]);
							translation.addAnimationListener(new IAnimationListener(){
					        	@Override
					        	public void processAnimationEvent(AnimationEvent ae) {
					        		//photos[idCellOpened][j].setPositionGlobal(new Vector3D(ae.getValue(), y));
					        		photos[idCellOpened][j].translate(new Vector3D(DX, DY));
					        	}
					        }).start();
						}
						isCellOpened = true;
					}
					else
					{
						//Suppression des photo du workflow
						for(int i = 0; i < photos[idCellOpened].length; i++)
						{
							photos[idCellOpened][i].removeFromParent();
						}
						
						if(cell.id == idCellOpened)
						{
							MTComponent[] components = navigateur.getChildren();
							
							for(int i = 0; i < components.length; i++)
							{
								Cellule cell = (Cellule) components[i].getChildByIndex(idCellOpened);
								if(cell.id == idCellOpened)
								{
									for(int j = 0; j < photos[idCellOpened].length; j++)
									{	
										setTailleImage(photos[idCellOpened][j], cellWidth - 250, cellHeight - 150);
										photos[idCellOpened][j].setPositionRelativeToParent(new Vector3D(cellWidth/2, cellHeight/2 - 20));
										
										//photos[idCellOpened][j].setLocalMatrix(photos[idCellOpened][j].shape);
										//setTailleImage(photos[idCellOpened][j], cellWidth - 250, cellHeight - 150);
										photos[idCellOpened][j].rotateZ(photos[idCellOpened][j].getCenterPointGlobal(), photos[idCellOpened][j].angle);
										
										
										cell.addChild(photos[idCellOpened][j]);
									}	
								}
							}
							
							isCellOpened = false;
							cell.background.setVisible(false);
						}
						else
						{
							
						}
					}
				}
				return false;
			}
		});
	}
	
	//Méthodes
	/** SetTailleImage() */
	private void setTailleImage(Photo photo, int largeurMax, int hauteurMax){
		if(photo.getHeightXY(TransformSpace.LOCAL) > photo.getWidthXY(TransformSpace.LOCAL))
		{
			photo.setHeightXYGlobal(hauteurMax);
		}
		else
		{
			photo.setWidthXYGlobal(largeurMax);
		}
	}

}
