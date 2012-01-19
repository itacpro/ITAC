package widgets;

import java.util.Calendar;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PGraphics;

public class WidgetDate extends MTRectangle {
	
	public float x, y;
	public int jour, mois, annee, jourSemaine, heure, minutes, color = 0;
	private MTTextArea jourDisplay, moisDisplay, anneeDisplay, jourSemaineDisplay, heureDisplay;
	private IFont jourSemaineFont, jourFont, moisFont, anneeFont, heureFont;
	public MTRectangle overlay;
	private PApplet app;
	
	public WidgetDate(PApplet pApplet, float _x, float _y, int color) {
		super(pApplet, _x, _y, 300, 158);
		
		app = pApplet;
		x = _x;
		y = _y;
		this.color = color;
		
		jourDisplay = new MTTextArea(pApplet);
		moisDisplay = new MTTextArea(pApplet);
		anneeDisplay = new MTTextArea(pApplet);
		jourSemaineDisplay = new MTTextArea(pApplet);
		heureDisplay = new MTTextArea(pApplet);
		
		jourSemaineFont = FontManager.getInstance().createFont(app, "ChampagneBold.ttf", 70, new MTColor(12, 123, 195), true);
		moisFont = FontManager.getInstance().createFont(app, "ChampagneBold.ttf", 40, new MTColor(12, 123, 195), true);
		anneeFont = FontManager.getInstance().createFont(app, "ChampagneBold.ttf", 40, new MTColor(12, 123, 195), true);
		heureFont = FontManager.getInstance().createFont(app, "GeosansLight.ttf", 70, new MTColor(90, 12, 98), true);
		jourFont = FontManager.getInstance().createFont(app, "GeosansLight.ttf", 80, new MTColor(90, 12, 98), true);
		
		switch(this.color)
		{
			case 2:
				jourSemaineFont.setFillColor(new MTColor(0, 0, 0));
				moisFont.setFillColor(new MTColor(0, 0, 0));
				anneeFont.setFillColor(new MTColor(0, 0, 0));
				heureFont.setFillColor(new MTColor(62, 152, 238));
				jourFont.setFillColor(new MTColor(62, 152, 238));
				break;
			case 0:
				jourSemaineFont.setFillColor(new MTColor(255, 255, 255));
				moisFont.setFillColor(new MTColor(255, 255, 255));
				anneeFont.setFillColor(new MTColor(255, 255, 255));
				heureFont.setFillColor(new MTColor(0, 0, 0));
				jourFont.setFillColor(new MTColor(0, 0, 0));
				break;
			case 1:
				jourSemaineFont.setFillColor(new MTColor(12, 123, 195));
				moisFont.setFillColor(new MTColor(12, 123, 195));
				anneeFont.setFillColor(new MTColor(12, 123, 195));
				heureFont.setFillColor(new MTColor(90, 12, 98));
				jourFont.setFillColor(new MTColor(90, 12, 98));
				break;
		}
		
		overlay = new MTRectangle(pApplet, x, y, 300, 158);
		
		getDate();
		updateDisplay();
		setWidgetDesign();
		setOverlayTouchAction();
	}
	
	public void getDate(){
		Calendar date  = Calendar.getInstance();
		jour = date.get(Calendar.DAY_OF_MONTH);
		mois = date.get(Calendar.MONTH);
		annee = date.get(Calendar.YEAR);
		jourSemaine = date.get(Calendar.DAY_OF_WEEK);
		heure = date.get(Calendar.HOUR_OF_DAY);
		minutes = date.get(Calendar.MINUTE);
	}
	
	public void drawComponent(PGraphics g){
		getDate();
		updateDisplay();
		super.drawComponent(g);
	}
	
	public void setOverlayTouchAction(){
		overlay.unregisterAllInputProcessors();
		overlay.registerInputProcessor(new DragProcessor(app));
		overlay.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				DragEvent de = (DragEvent)ge;
				Vector3D vecteur = de.getTranslationVect();
				
				translateGlobal(vecteur);
				
				return false;
			}
		});
		
		overlay.registerInputProcessor(new TapProcessor(app, 100));
		overlay.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				if (te.isTapped())
				{
					switch(color)
					{
						case 0:
							jourSemaineFont.setFillColor(new MTColor(0, 0, 0));
							moisFont.setFillColor(new MTColor(0, 0, 0));
							anneeFont.setFillColor(new MTColor(0, 0, 0));
							heureFont.setFillColor(new MTColor(62, 152, 238));
							jourFont.setFillColor(new MTColor(62, 152, 238));
							color = 1;
							break;
						case 1:
							jourSemaineFont.setFillColor(new MTColor(255, 255, 255));
							moisFont.setFillColor(new MTColor(255, 255, 255));
							anneeFont.setFillColor(new MTColor(255, 255, 255));
							heureFont.setFillColor(new MTColor(0, 0, 0));
							jourFont.setFillColor(new MTColor(0, 0, 0));
							color = 2;
							break;
						case 2:
							jourSemaineFont.setFillColor(new MTColor(12, 123, 195));
							moisFont.setFillColor(new MTColor(12, 123, 195));
							anneeFont.setFillColor(new MTColor(12, 123, 195));
							heureFont.setFillColor(new MTColor(90, 12, 98));
							jourFont.setFillColor(new MTColor(90, 12, 98));
							color = 0;
							break;
					}
				}
				return false;
			}
		});
	}
	
	public void setWidgetDesign(){
		this.setNoFill(true);
		this.setNoStroke(true);
		
		jourSemaineDisplay.setInnerPadding(0);
		jourSemaineDisplay.setAnchor(PositionAnchor.UPPER_LEFT);
		jourSemaineDisplay.setPositionGlobal(new Vector3D(x, y));
		jourSemaineDisplay.setFont(jourSemaineFont);
		jourSemaineDisplay.setNoFill(true);
		jourSemaineDisplay.setNoStroke(true);
		this.addChild(jourSemaineDisplay);
		
		heureDisplay.setInnerPadding(0);
		heureDisplay.setAnchor(PositionAnchor.UPPER_LEFT);
		heureDisplay.setPositionGlobal(new Vector3D(x + jourSemaineDisplay.getWidthXY(TransformSpace.GLOBAL), y));
		heureDisplay.setFont(heureFont);
		heureDisplay.setNoFill(true);
		heureDisplay.setNoStroke(true);
		this.addChild(heureDisplay);
		
		jourDisplay.setInnerPadding(0);
		jourDisplay.setAnchor(PositionAnchor.UPPER_LEFT);
		jourDisplay.setPositionGlobal(new Vector3D(x + 30, y + jourSemaineDisplay.getHeightXY(TransformSpace.GLOBAL)));
		jourDisplay.setFont(jourFont);
		jourDisplay.setNoFill(true);
		jourDisplay.setNoStroke(true);
		this.addChild(jourDisplay);
		
		moisDisplay.setInnerPadding(0);
		moisDisplay.setAnchor(PositionAnchor.UPPER_LEFT);
		moisDisplay.setPositionGlobal(new Vector3D(x + 40 + jourDisplay.getWidthXY(TransformSpace.GLOBAL), y + jourSemaineDisplay.getHeightXY(TransformSpace.GLOBAL)));
		moisDisplay.setFont(moisFont);
		moisDisplay.setNoFill(true);
		moisDisplay.setNoStroke(true);
		this.addChild(moisDisplay);
		
		anneeDisplay.setInnerPadding(0);
		anneeDisplay.setAnchor(PositionAnchor.UPPER_LEFT);
		anneeDisplay.setPositionGlobal(new Vector3D(x + 40 + jourDisplay.getWidthXY(TransformSpace.GLOBAL), y + jourSemaineDisplay.getHeightXY(TransformSpace.GLOBAL) + moisDisplay.getHeightXY(TransformSpace.GLOBAL)));
		anneeDisplay.setFont(anneeFont);
		anneeDisplay.setNoFill(true);
		anneeDisplay.setNoStroke(true);
		this.addChild(anneeDisplay);
		
		overlay.setNoFill(true);
		overlay.setNoStroke(true);
		overlay.removeAllGestureEventListeners();
		this.addChild(overlay);
	}
	
	private void updateDisplay(){
		switch(jourSemaine)
		{
			case 1:
				jourSemaineDisplay.setText("DIM");
				break;
			case 2:
				jourSemaineDisplay.setText("LUN");
				break;
			case 3:
				jourSemaineDisplay.setText("MAR");
				break;
			case 4:
				jourSemaineDisplay.setText("MER");
				break;
			case 5:
				jourSemaineDisplay.setText("JEU");
				break;
			case 6:
				jourSemaineDisplay.setText("VEN");
				break;
			case 7:
				jourSemaineDisplay.setText("SAM");
				break;
		}
		
		switch(mois)
		{
			case 0:
				moisDisplay.setText("JANVIER");
				break;
			case 1:
				moisDisplay.setText("FEVRIER");
				break;
			case 2:
				moisDisplay.setText("MARS");
				break;
			case 3:
				moisDisplay.setText("AVRIL");
				break;
			case 4:
				moisDisplay.setText("MAI");
				break;
			case 5:
				moisDisplay.setText("JUIN");
				break;
			case 6:
				moisDisplay.setText("JUILLET");
				break;
			case 7:
				moisDisplay.setText("AOUT");
				break;
			case 8:
				moisDisplay.setText("SEPTEMBRE");
				break;
			case 9:
				moisDisplay.setText("OCTOBRE");
				break;
			case 10:
				moisDisplay.setText("NOVEMBRE");
				break;
			case 11:
				moisDisplay.setText("DECEMBRE");
				break;
		}
		
		if(jour < 10)
		{
			jourDisplay.setText("0" + jour);
		}
		else
		{
			jourDisplay.setText(jour + "");
		}
		
		anneeDisplay.setText(annee + "");
		
		if(heure < 10 && minutes < 10)
		{
			heureDisplay.setText("0" + heure + ":" + "0" + minutes);
		}
		else if(heure < 10 && minutes > 10)
		{
			heureDisplay.setText("0" + heure + ":" + minutes);
		}
		else if(heure > 10 && minutes < 10)
		{
			heureDisplay.setText(heure + ":" + "0" + minutes);
		}
		else
		{
			heureDisplay.setText(heure + ":" + minutes);
		}
	}

}
