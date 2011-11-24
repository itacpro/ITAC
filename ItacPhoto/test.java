package ItacPhoto;

import java.io.File;
import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
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

public class test extends AbstractScene {
	
	private String repertoire = new String("../images/");
	private boolean isOpened = true;
	
	public test(final AbstractMTApplication mtApplication, String name) {
		super(mtApplication, name);
		
		//Affichage des cercles de pointage
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		//Background
		this.getCanvas().addChild(new MTBackgroundImage(mtApplication, mtApplication.loadImage("water.png"), false));
		
		/*------------------------------------*/
		/*   AFFICHAGE DE LA VUE NAVIGATION   */
		/*------------------------------------*/
		
		//On récupère le contenu du dossier
		String[] contenuRepertoire = listerFichiers(repertoire);
		
		//Création de la liste qui va permettre d'afficher la vue "Navigation"
		MTList navigation = new MTList(mtApplication,0, mtApplication.height-200, 200, mtApplication.width);
		this.getCanvas().addChild(navigation);
		
		//Par défaut la liste s'affiche de manière verticale, on effectue donc une rotation pour un affichage horizontal.
		navigation.rotateZ(navigation.getCenterPointLocal(), -90, TransformSpace.LOCAL);
		navigation.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height-100));
		navigation.setFillColor(new MTColor(150,150,150,100));
		navigation.setNoStroke(true);
		
		//Création des cellules de la liste
		for (int i = 0; i < contenuRepertoire.length; i++)
		{
			File fichier = new File(contenuRepertoire[i]);
			
			if(contenuRepertoire[i].endsWith(".jpg") == true
					|| contenuRepertoire[i].endsWith(".png") == true
					|| contenuRepertoire[i].endsWith(".JPG") == true
					|| contenuRepertoire[i].endsWith(".PNG") == true)
			{
				PImage miniature = mtApplication.loadImage(repertoire + contenuRepertoire[i]);
				ajouterCellule(miniature, navigation, mtApplication);
			}
			else if(fichier.isDirectory())
			{
				//MTListCell celluleDossier = new MTListCell();
			}
		}
		
		setShowHideNavButton(navigation, mtApplication);
		
		Photo test = new Photo(mtApplication, mtApplication.loadImage(repertoire + "01.jpg"), navigation);
		this.getCanvas().addChild(navigation);
	}
	
	/** ListerFichiers() */
	private String[] listerFichiers(String cheminDossier){
		File dossier = new File(cheminDossier);
		String [] liste = dossier.list(); //On liste tous les fichiers du dossier
		return liste;
	}
	
	
	/** AjouterCellule() */
	private void ajouterCellule(final PImage pImage, final MTList liste, final AbstractMTApplication app){
		
		Cellule cell = new Cellule(app, 200, 300, pImage);
		
		liste.addListElement(cell);
	}
	
	/** SetShowHideNavButton */
	private void setShowHideNavButton(final MTList liste, final AbstractMTApplication app){
		
		@SuppressWarnings("deprecation")
		final MTRectangle showHideNav = new MTRectangle(100, 40, app);
		//showHideNav.setPickable(false);
		showHideNav.setPositionGlobal(new Vector3D(60, app.height-220));
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
						final MultiPurposeInterpolator interpolator = new MultiPurposeInterpolator(app.height-100, app.height+100, 650, 0, 1, 1);
						
						Animation translation = new Animation("translation", interpolator, liste);
						translation.addAnimationListener(new IAnimationListener(){
				        	@Override
				        	public void processAnimationEvent(AnimationEvent ae) {
				        		//Application de la translation
				        		liste.translate(new Vector3D(0, (float)10), TransformSpace.GLOBAL);
				        		showHideNav.translate(new Vector3D(0, (float)10), TransformSpace.GLOBAL);
				        		if(interpolator.isFinished())
				        		{
				        			liste.setPositionGlobal(new Vector3D(app.width/2f, app.height+100));
				        			showHideNav.setPositionGlobal(new Vector3D(60, app.height-20));
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
				        		liste.translate(new Vector3D(0, (float)-10), TransformSpace.GLOBAL);
				        		showHideNav.translate(new Vector3D(0, (float)-10), TransformSpace.GLOBAL);
				        		if(interpolator.isFinished())
				        		{
				        			liste.setPositionGlobal(new Vector3D(app.width/2f, app.height-100));
				        			showHideNav.setPositionGlobal(new Vector3D(60, app.height-220));
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