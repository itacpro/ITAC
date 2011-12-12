package Widgets;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;
import processing.core.PImage;

public class WidgetCalc extends MTRectangle {
	
	private AbstractMTApplication app;
	private int x, y;
	private double valeur1 = 0;
	private double valeur2 = 0;
	private double result = 0;
	private boolean clicOperateur = false;
	private String operateur = "";
	private MTTextArea afficheur;
	
	public WidgetCalc(AbstractMTApplication _app, int _x, int _y, float width, float height) {
		super(_app, _x, _y, width, height);
		
		app = _app;
		x = _x;
		y = _y;
		
		setWidgetDesign();
		setButtons();
		
		afficheur = new MTTextArea(15, 25, 200, 50, app);
		afficheur.setPickable(false);
		afficheur.setText("");
		
		this.addChild(afficheur);
		
	}
	
	private void setWidgetDesign(){
		this.setTexture(app.loadImage("calc.png"));
		this.setNoStroke(true);
		
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
		this.registerInputProcessor(new DragProcessor(app));
		this.addGestureListener(DragProcessor.class, new DefaultDragAction());
		this.addGestureListener(DragProcessor.class, new InertiaDragAction());
	}
	
	private void setButtons(){
		BoutonCalc[] bouton = new BoutonCalc[10];
		bouton[1] = new BoutonCalc("1", 15, 100, 50, 30, app);
		bouton[2] = new BoutonCalc("2", 70, 100, 50, 30, app);
		bouton[3] = new BoutonCalc("3",125, 100, 50, 30, app);
		bouton[4] = new BoutonCalc("4",15, 135, 50, 30, app);
		bouton[5] = new BoutonCalc("5", 70, 135, 50, 30, app);
		bouton[6] = new BoutonCalc("6", 125, 135, 50, 30, app);
		bouton[7] = new BoutonCalc("7", 15, 170, 50, 30, app);
		bouton[8] = new BoutonCalc("8", 70, 170, 50, 30, app);
		bouton[9] = new BoutonCalc("9", 125, 170, 50, 30, app);
		bouton[0] = new BoutonCalc("0", 15, 205, 105, 30, app);
		
		for(int i = 0; i < bouton.length; i++)
		{
			this.addChild(bouton[i]);
		}
	}
	
	private void setTouchAction(final BoutonCalc bouton){
		bouton.registerInputProcessor(new TapProcessor(app, 15));
		bouton.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				
				if (te.isTapped())
				{
					/*if(clicOperateur == true)
					{
						if(operateur == "+")
						{
							addition(valeur1, valeur2);
							afficheur.setText(String.valueOf(result));
						}
						else if(operateur == "-")
						{
							soustraction(valeur1, valeur2);
							afficheur.setText(String.valueOf(result));
						}
						else if(operateur == "*")
						{
							produit(valeur1, valeur2);
							afficheur.setText(String.valueOf(result));
						}
						else if(operateur == "/")
						{
							quotient(valeur1, valeur2);
							afficheur.setText(String.valueOf(result));
						}
					}*/
					if(clicOperateur == false)
					{
						valeur1 = Double.parseDouble(bouton.getText());
					}
				}
				
				return false;
			}
		});
	}
	
	private void addition(double val1, double val2){
		result = val1 + val2;
	}
	
	private void soustraction(double val1, double val2){
		result = val1 - val2;
	}
	
	private void produit(double val1, double val2){
		result = val1 * val2;
	}
	
	private void quotient(double val1, double val2){
		result = val1 / val2;
	}
}
