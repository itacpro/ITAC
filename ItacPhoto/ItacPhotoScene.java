package ItacPhoto;

import java.io.File;
import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;
import processing.core.PImage;

public class ItacPhotoScene extends AbstractScene {
	
	public String repertoire = new String("../images/");
	public String repertoireCourrant = repertoire;
	public boolean isOpened = true;
	public AbstractMTApplication mtApplication;
	public MTList navigation;
	
	
	public ItacPhotoScene(final AbstractMTApplication _mtApplication, String name) {
		super(_mtApplication, name);
		
		mtApplication = _mtApplication;
		
		//Affichage des cercles de pointage
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		//Background
		this.getCanvas().addChild(new MTBackgroundImage(mtApplication, mtApplication.loadImage("water.png"), false));
		
		/*------------------------------------*/
		/*   AFFICHAGE DE LA VUE NAVIGATION   */
		/*------------------------------------*/
		
		//Création de la liste qui va permettre d'afficher la vue "Navigation"
		//MTList navigation = new MTList(mtApplication,0, mtApplication.height-200, 200, mtApplication.width);
		navigation = new MTList(mtApplication,mtApplication.width-300, 0, 300, mtApplication.height);
		this.getCanvas().addChild(navigation);
		
		//Par défaut la liste s'affiche de manière verticale, on effectue donc une rotation pour un affichage horizontal.
		//navigation.rotateZ(navigation.getCenterPointLocal(), -90, TransformSpace.LOCAL);
		//navigation.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height-100));
		//navigation.setPositionGlobal(new Vector3D(mtApplication.height/2f, mtApplication.width-100));
		navigation.setFillColor(new MTColor(150,150,150,100));
		navigation.setNoStroke(true);
		
		listerContenuDossier();
		
		setShowHideNavButton(navigation, mtApplication);
	}
	
	public void listerContenuDossier() {
		navigation.removeAllListElements();
		
		//On récupère le contenu du dossier
		String[] contenuRepertoire = listerFichiers(repertoireCourrant);
		
		//Création des cellules de la liste
		for (int i = 0; i < contenuRepertoire.length; i++)
		{
			File fichier = new File(contenuRepertoire[i]);
			System.out.println(repertoireCourrant);
			if(contenuRepertoire[i].endsWith(".jpg") == true
					|| contenuRepertoire[i].endsWith(".png") == true
					|| contenuRepertoire[i].endsWith(".JPG") == true
					|| contenuRepertoire[i].endsWith(".PNG") == true)
			{
				PImage miniature = mtApplication.loadImage(repertoireCourrant + contenuRepertoire[i]);
				ajouterCellule(miniature, navigation, mtApplication);
			}
			
			if(fichier.isFile() == false)
			{
				creerDossier(contenuRepertoire[i]);
				System.out.println(contenuRepertoire[i]);
				System.out.println(fichier.isFile());
			}
		}
	}
	
	public void creerDossier(final String nomDossier) {
		final String chemin = repertoireCourrant + nomDossier + "/";
		String[] contenuRepertoire = listerFichiers(chemin);
		
		final MTListCell cell = new MTListCell(300, 200, mtApplication); //On créé la cellule
		
		for (int i = 0; i < 5; i++)
		{	
			if(contenuRepertoire[i].endsWith(".jpg") == true
					|| contenuRepertoire[i].endsWith(".png") == true
					|| contenuRepertoire[i].endsWith(".JPG") == true
					|| contenuRepertoire[i].endsWith(".PNG") == true)
			{
				PImage miniature = mtApplication.loadImage(chemin + contenuRepertoire[i]);
				MTImage image = creerMTImage(miniature, mtApplication);
				setTailleImage(image, 180, 220);
				cell.addChild(image);
				
				//On ajuste la position de l'image dans la cellule
				image.setPositionGlobal(cell.getCenterPointGlobal());
			}
		}
		
		cell.setFillColor(new MTColor(150,150,150,0));
		cell.setNoStroke(true);
		
		//Ecouteur d'action
		cell.registerInputProcessor(new TapProcessor(mtApplication, 15));
		cell.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{
					cell.destroy();
					repertoireCourrant = chemin;
					listerContenuDossier();
				}
				return false;
			}
		});
		
		//On ajoute la cellule à la liste
		navigation.addListElement(cell);
	}
	
	/** ListerFichiers() */
	private String[] listerFichiers(String cheminDossier){
		File dossier = new File(cheminDossier);
		String [] liste = dossier.list(); //On liste tous les fichiers du dossier
		return liste;
	}
	
	/** CréerMTImage() */
	private MTImage creerMTImage(PImage image, AbstractMTApplication app){
		@SuppressWarnings("deprecation")
		MTImage mtImage = new MTImage(image, app);
		return mtImage;
	}
	
	/** AjouterCellule() */
	private void ajouterCellule(final PImage pImage, final MTList liste, final AbstractMTApplication app){
		@SuppressWarnings("deprecation")
		//final MTListCell cell = new MTListCell(200, 300, app); //On créé la cellule
		final MTListCell cell = new MTListCell(300, 200, app); //On créé la cellule
		MTImage image = creerMTImage(pImage, app);
		
		//On effectue une rotation sur l'image pour qu'elle s'affiche correctement
		//image.rotateZ(image.getCenterPointGlobal(), 90, TransformSpace.LOCAL);
		
		setTailleImage(image, 180, 220);
		
		cell.addChild(image);
		cell.setFillColor(new MTColor(150,150,150,0));
		cell.setNoStroke(true);
		
		
		//Ecouteur d'action
		cell.registerInputProcessor(new TapProcessor(app, 15));
		cell.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{
					@SuppressWarnings("deprecation")
					IMTComponent3D target = te.getTargetComponent();
					if(target instanceof MTListCell)
					{
						MTComponent mtComp = cell.getChildByIndex(0);
						MTImage mtPhoto = (MTImage) mtComp;
						
						if(mtPhoto instanceof MTImage)
						{
							ajouterPhoto(pImage, liste, cell, app);
							cell.destroy();
						}
					}
				}
				return false;
			}
		});
		
		
		//On ajuste la position de l'image dans la cellule
		image.setPositionGlobal(cell.getCenterPointGlobal());
		
		//On ajoute la cellule à la liste
		liste.addListElement(cell);
	}
	
	/** AjouterPhoto */
	private void ajouterPhoto(final PImage pImage, final MTList list, final MTListCell cell, final AbstractMTApplication app){
		final MTImage photo = creerMTImage(pImage, app);
		
		/*---------------*/
		/*   Animation   */
		/*---------------*/
		
		//Positionnement aux coordonnées de la cellule
		photo.setPositionRelativeToParent(cell.getCenterPointGlobal());
		
		//Définition de l'animation de la translation
		final Animation translation = new Animation("translation", new MultiPurposeInterpolator(app.width/2f, app.height/3f, 500, 0, 1, 1) , photo);
		translation.addAnimationListener(new IAnimationListener(){
        	@Override
        	public void processAnimationEvent(AnimationEvent ae) {
        		//On récupère les coordonnées actuelles de la photo
        		Vector3D photoPos = new Vector3D(photo.getCenterPointRelativeToParent());
        		
        		//Calcul des variables du vecteur
        		int x = (int) (app.width/2f - photoPos.x)/5;
        		int y = (int) (app.height/3f - photoPos.y)/5;
        		
        		//Application de la translation
        		photo.translate(new Vector3D(x, y), TransformSpace.RELATIVE_TO_PARENT);
        	}
        }).start();
		
		setTailleImage(photo, 400, 400);
		
		/*---------------*/
		/*    Actions    */
		/*---------------*/
		
		//Ajout de l'inertie sur la photo
		photo.addGestureListener(DragProcessor.class, new InertiaDragAction(125, 0.90f, 25));
		
		photo.registerInputProcessor(new TapProcessor(app, 25, true, 350));
		photo.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				
				if (te.isDoubleTap()){
					
					final MultiPurposeInterpolator interpolator = new MultiPurposeInterpolator(0, 499, 500, 0, 1, 1);
					
					final Animation scale = new Animation("scale", interpolator, photo);
					scale.addAnimationListener(new IAnimationListener(){
			        	@Override
			        	public void processAnimationEvent(AnimationEvent ae) {
			        		
			        		photo.scale((float)0.7, (float)0.7, 0, photo.getCenterPointGlobal());
			        		
			        		if(interpolator.isFinished())
			        		{
			        			ajouterCellule(pImage, list, app);
			        			photo.destroy();
			        		}
			        	}
			        }).start();
				}
				return false;
			}
		});
		
		
		//Ajout de la photo dans la fenêtre
		getCanvas().addChild(photo);
	}
	
	/** SetTailleImage() */
	private void setTailleImage(MTImage photo, int hauteurMax, int largeurMax){
		if(photo.getHeightXY(TransformSpace.LOCAL) > photo.getWidthXY(TransformSpace.LOCAL))
		{
			photo.setHeightXYGlobal(hauteurMax);
		}
		else
		{
			photo.setWidthXYGlobal(largeurMax);
		}
	}
	
	/** SetShowHideNavButton */
	private void setShowHideNavButton(final MTList liste, final AbstractMTApplication app){
		
		@SuppressWarnings("deprecation")
		//final MTRectangle showHideNav = new MTRectangle(100, 40, app);
		final MTRectangle showHideNav = new MTRectangle(40, 100, app);
		//showHideNav.setPickable(false);
		//showHideNav.setPositionGlobal(new Vector3D(60, app.height-220));
		showHideNav.setPositionGlobal(new Vector3D(app.width-320, 60));
		showHideNav.setFillColor(new MTColor(150,150,150,100));
		showHideNav.setNoStroke(true);
		
		//Ecouteur d'action
		showHideNav.unregisterAllInputProcessors();
		showHideNav.removeAllGestureEventListeners();
		showHideNav.registerInputProcessor(new TapProcessor(app, 15));
		showHideNav.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{
					if(isOpened)
					{	
						//Définition de l'animation de la translation
						final MultiPurposeInterpolator interpolator = new MultiPurposeInterpolator(0, 650, 650, 640, 1, 1);
						
						Animation translation = new Animation("translation", interpolator, liste);
						translation.addAnimationListener(new IAnimationListener(){
				        	@Override
				        	public void processAnimationEvent(AnimationEvent ae) {
				        		//Application de la translation
				        		liste.translate(new Vector3D(15, 0), TransformSpace.GLOBAL);
				        		showHideNav.translate(new Vector3D(15, 0), TransformSpace.GLOBAL);
				        		if(interpolator.isFinished())
				        		{
				        			//liste.setPositionGlobal(new Vector3D(app.width/2f, app.height+100));
				        			//showHideNav.setPositionGlobal(new Vector3D(60, app.height-20));
				        			liste.setPositionGlobal(new Vector3D(app.width+150, app.height/2f));
				        			showHideNav.setPositionGlobal(new Vector3D(app.width-20, 60));
				        		}
				        	}
				        }).start();
						
						isOpened = false;
					}
					else
					{
						//Définition de l'animation de la translation
						final MultiPurposeInterpolator interpolator = new MultiPurposeInterpolator(app.height+100, app.height-100, 650, 0, 1, 1);
						Animation translation = new Animation("translation", interpolator, liste);
						translation.addAnimationListener(new IAnimationListener(){
				        	@Override
				        	public void processAnimationEvent(AnimationEvent ae) {
				        		//Application de la translation
				        		liste.translate(new Vector3D(-15, (float)0), TransformSpace.GLOBAL);
				        		showHideNav.translate(new Vector3D(-15, (float)0), TransformSpace.GLOBAL);
				        		if(interpolator.isFinished())
				        		{
				        			//liste.setPositionGlobal(new Vector3D(app.width/2f, app.height-100));
				        			//showHideNav.setPositionGlobal(new Vector3D(60, app.height-220));
				        			liste.setPositionGlobal(new Vector3D(app.width-150, app.height/2f));
				        			showHideNav.setPositionGlobal(new Vector3D(app.width-320, 60));
				        		}
				        	}
				        }).start();
						
						isOpened = true;
					}
				}
				return false;
			}
		});
		
		this.getCanvas().addChild(showHideNav);
	}
}