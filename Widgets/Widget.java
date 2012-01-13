package Widgets;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;

import Itac.Utilisateur;

import processing.core.PImage;

public class Widget extends AbstractScene{
	
	public AbstractMTApplication app;
	
	public Widget(AbstractMTApplication _app, String _name) {
		super(_app, _name);
		
		app = _app;
		
		//Affichage des cercles de pointage
		this.registerGlobalInputProcessor(new CursorTracer(app, this));
		PImage background = app.loadImage("../ressources/login/fond-ecran-default.png");
		this.getCanvas().addChild(new MTBackgroundImage(app, background, false));
		
		//Application de démarrage
		WidgetScene scene = new WidgetScene(0, 0, app.width, app.height, app,  new Utilisateur("zzz", 0, 0, 250, 250, app));
		this.getCanvas().addChild(scene);
		
	}
}
