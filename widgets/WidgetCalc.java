package widgets;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

public class WidgetCalc extends MTRectangle {
	
	private AbstractMTApplication app;
	private int x, y;
	private String valeur1 = "0";
	private String valeur2 = "0";
	private boolean clicOperateur = false;
	private boolean virgule = false;
	private String operateur = "";
	private MTTextArea afficheur;
	
	/**
	 * Constructeur
	 * @param _app
	 * @param _x
	 * @param _y
	 * @param width
	 * @param height
	 */
	public WidgetCalc(AbstractMTApplication _app, int _x, int _y, float width, float height) {
		super(_app, _x, _y, width, height);
		
		//On récupère les paramètres
		app = _app;
		x = _x;
		y = _y;
		
		//On défini le design de la calculatrice
		setWidgetDesign();
		
		//On ajoute les boutons
		setButtons();
		
		//Paramètres de la police de l'afficheur
		IFont font = FontManager.getInstance().createFont(app, "LEDFont.ttf", 30, new MTColor(54, 54, 54));
		
		//Création de l'afficheur
		afficheur = new MTTextArea(app);
		afficheur.setAnchor(PositionAnchor.UPPER_LEFT);
		afficheur.setPositionGlobal(new Vector3D(x + 185, y + 25));
		afficheur.setFont(font);
		afficheur.setPickable(false);
		afficheur.setText("0");
		afficheur.setNoFill(true);
		afficheur.setNoStroke(true);
		
		this.addChild(afficheur);
		
	}
	
	/**
	 * Fonction permettant de définir les paramètres graphiques du widget
	 */
	private void setWidgetDesign(){
		//Image de fond
		this.setTexture(app.loadImage("../ressources/calc/fd_calculatrice.png"));
		this.setNoStroke(true);
		
		//Ajout de l'inertie sur le widget
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
		this.registerInputProcessor(new DragProcessor(app));
		this.addGestureListener(DragProcessor.class, new DefaultDragAction());
		this.addGestureListener(DragProcessor.class, new InertiaDragAction());
	}
	
	private void setButtons(){
		
		//Variables permettant de positionner facilement les boutons
		int refX = x + 25;
		int refY = y + 90;
		int tailleX = 44;
		int tailleY = 29;
		int ecartX = 5;
		int ecartY = 5;
		
		//Création de la variable container
		BoutonCalc[] bouton = new BoutonCalc[17];
		
		//Création des boutons numériques
		bouton[1] = new BoutonCalc("1", refX, refY, 44, 29, app, "../ressources/calc/btn_calculette.png");
		bouton[2] = new BoutonCalc("2", refX + tailleX + ecartX, refY, 44, 29, app, "../ressources/calc/btn_calculette.png");
		bouton[3] = new BoutonCalc("3", refX + 2 * (tailleX + ecartX), refY, 44, 29, app, "../ressources/calc/btn_calculette.png");
		
		bouton[4] = new BoutonCalc("4", refX, refY + tailleY + ecartY, 44, 29, app, "../ressources/calc/btn_calculette.png");
		bouton[5] = new BoutonCalc("5", refX + tailleX + ecartX, refY + tailleY + ecartY, 44, 29, app, "../ressources/calc/btn_calculette.png");
		bouton[6] = new BoutonCalc("6", refX + 2 * (tailleX + ecartX), refY + tailleY + ecartY, 44, 29, app, "../ressources/calc/btn_calculette.png");
		
		bouton[7] = new BoutonCalc("7", refX, refY + 2 * (tailleY + ecartY), 44, 29, app, "../ressources/calc/btn_calculette.png");
		bouton[8] = new BoutonCalc("8", refX + tailleX + ecartX, refY + 2 * (tailleY + ecartY), 44, 29, app, "../ressources/calc/btn_calculette.png");
		bouton[9] = new BoutonCalc("9", refX + 2 * (tailleX + ecartX), refY + 2 * (tailleY + ecartY), 44, 29, app, "../ressources/calc/btn_calculette.png");
		
		bouton[0] = new BoutonCalc("0", refX + tailleX + ecartX, refY + 3 * (tailleY + ecartY), 44, 29, app, "../ressources/calc/btn_calculette.png");
		
		//Création des boutons opérateurs
		bouton[10] = new BoutonCalc("+", refX + 3 * (tailleX + ecartX), refY, 44, 30, app, "../ressources/calc/operateur.png");
		bouton[11] = new BoutonCalc("-", refX + 3 * (tailleX + ecartX), refY + tailleY + ecartY, 44, 30, app, "../ressources/calc/operateur.png");
		bouton[12] = new BoutonCalc("*", refX + 3 * (tailleX + ecartX), refY + 2 * (tailleY + ecartY), 44, 30, app, "../ressources/calc/operateur.png");
		bouton[13] = new BoutonCalc("/", refX + 3 * (tailleX + ecartX), refY + 3 * (tailleY + ecartY), 44, 30, app, "../ressources/calc/operateur.png");
		
		//Création des boutons de fonction
		bouton[14] = new BoutonCalc(".", refX, refY + 3 * (tailleY + ecartY), 44, 29, app, "../ressources/calc/btn_calculette.png");
		bouton[15] = new BoutonCalc("=", refX, refY + 4 * (tailleY + ecartY), 193, 28, app, "../ressources/calc/egal.png");
		bouton[16] = new BoutonCalc("C", refX + 2 * (tailleX + ecartX), refY + 3 * (tailleY + ecartY), 44, 29, app, "../ressources/calc/btn_calculette.png");
		
		for(int i = 0; i < bouton.length; i++)
		{
			//On défini les actions pour chaque bouton, puis on les affiche
			setTouchAction(bouton[i]);
			this.addChild(bouton[i]);
		}
	}
	
	/**
	 * Fonction permettant de définir les actions d'un bouton
	 * @param bouton
	 */
	private void setTouchAction(final BoutonCalc bouton){
		bouton.registerInputProcessor(new TapProcessor(app, 15));
		bouton.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{	
			public boolean processGestureEvent(MTGestureEvent ge)
			{
				TapEvent te = (TapEvent)ge;
				
				if (te.isTapped())
				{
					//Action du bouton +
					if(bouton.getText().equals("+"))
					{
						operateur = "+";
						clicOperateur = true;
						virgule = false;
					}
					//Action du bouton -
					else if(bouton.getText().equals("-"))
					{
						operateur = "-";
						clicOperateur = true;
						virgule = false;
					}
					//Action du bouton *
					else if(bouton.getText().equals("*"))
					{
						operateur = "*";
						clicOperateur = true;
						virgule = false;
					}
					//Action du bouton /
					else if(bouton.getText().equals("/"))
					{
						operateur = "/";
						clicOperateur = true;
						virgule = false;
					}
					//Action du bouton C
					else if(bouton.getText().equals("C"))
					{
						valeur1 = "0";
						valeur2 = "0";
						afficheur.setText("0");
						afficheur.setAnchor(PositionAnchor.UPPER_LEFT);
						afficheur.setPositionRelativeToParent(new Vector3D(x + 215 - afficheur.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), y + 25));
						clicOperateur = false;
						virgule = false;
					}
					//Action du bouton .
					else if(bouton.getText().equals("."))
					{
						if(virgule == false)
						{
							valeur1 = valeur1 + bouton.getText();
							virgule = true;
							afficheur.setText(valeur1);
						}
					}
					//Actions du bouton =
					else if(bouton.getText().equals("="))
					{
						if(operateur.equals("+"))
						{
							addition(Double.valueOf(valeur1), Double.valueOf(valeur2));
							operateur = "";
							afficheur.setText(valeur1);
						}
						else if(operateur.equals("-"))
						{
							soustraction(Double.valueOf(valeur1), Double.valueOf(valeur2));
							operateur = "";
							afficheur.setText(valeur1);
						}
						else if(operateur.equals("*"))
						{
							produit(Double.valueOf(valeur1), Double.valueOf(valeur2));
							operateur = "";
							afficheur.setText(valeur1);
						}
						else if(operateur.equals("/"))
						{
							quotient(Double.valueOf(valeur1), Double.valueOf(valeur2));
							operateur = "";
							afficheur.setText(valeur1);
							valeur2 = "0";
						}
						
						//On actualise l'afficheur
						afficheur.setAnchor(PositionAnchor.UPPER_LEFT);
						afficheur.setPositionRelativeToParent(new Vector3D(x + 215 - afficheur.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), y + 25));
					}
					else
					{
						if(clicOperateur == false)
						{
							//On limite l'afficheur à 10 caractères
							if(afficheur.getText().length() != 10)
							{
								if(valeur1.equals("0"))
								{
									//On enlève le 0
									valeur1 = bouton.getText();
								}
								else
								{
									//On ajoute la valeur du bouton dans la variable
									valeur1 = valeur1 + bouton.getText();
								}
								
								//On actualise l'afficheur
								afficheur.setText(valeur1);
								afficheur.setAnchor(PositionAnchor.UPPER_LEFT);
								afficheur.setPositionRelativeToParent(new Vector3D(x + 215 - afficheur.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), y + 25));
							}
						}
						else
						{
							//On limite l'afficheur à 10 caractères
							if(valeur2.toString().length() < 10)
							{
								if(valeur2.equals("0"))
								{
									//On enlève le 0
									valeur2 = bouton.getText();
								}
								else
								{
									//On ajoute la valeur du bouton dans la variable
									valeur2 = valeur2 + bouton.getText();
								}
								
								//On actualise l'afficheur
								afficheur.setText(valeur2);
								afficheur.setAnchor(PositionAnchor.UPPER_LEFT);
								afficheur.setPositionRelativeToParent(new Vector3D(x + 215 - afficheur.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), y + 25));
							}
						}
					}
				}
				
				if (te.isTapDown())
				{
					//On modifie l'image du bouton en fonction de sa catégorie lorsqu'on le maintient enfoncé
					if(bouton.getText().equals("0") || bouton.getText().equals("1") || bouton.getText().equals("2") || bouton.getText().equals("3") || bouton.getText().equals("4") || bouton.getText().equals("5") || bouton.getText().equals("6") || bouton.getText().equals("7") || bouton.getText().equals("8") || bouton.getText().equals("9") || bouton.getText().equals("C") || bouton.getText().equals("."))
					{
						bouton.setTexture(app.loadImage("../ressources/calc/btn_calculette_hover.png"));
					}
					if(bouton.getText().equals("+") || bouton.getText().equals("-") || bouton.getText().equals("/") || bouton.getText().equals("*"))
					{
						bouton.setTexture(app.loadImage("../ressources/calc/operateur_clique.png"));
					}
					if(bouton.getText().equals("="))
					{
						bouton.setTexture(app.loadImage("../ressources/calc/egal_clique.png"));
					}
				}
				if(te.getId() == MTGestureEvent.GESTURE_ENDED)
				{
					//On modifie l'image du bouton en fonction de sa catégorie lorsqu'on le relâche
					if(bouton.getText().equals("0") || bouton.getText().equals("1") || bouton.getText().equals("2") || bouton.getText().equals("3") || bouton.getText().equals("4") || bouton.getText().equals("5") || bouton.getText().equals("6") || bouton.getText().equals("7") || bouton.getText().equals("8") || bouton.getText().equals("9") || bouton.getText().equals("C") || bouton.getText().equals("."))
					{
						bouton.setTexture(app.loadImage("../ressources/calc/btn_calculette.png"));
					}
					if(bouton.getText().equals("+") || bouton.getText().equals("-") || bouton.getText().equals("/") || bouton.getText().equals("*"))
					{
						bouton.setTexture(app.loadImage("../ressources/calc/operateur.png"));
					}
					if(bouton.getText().equals("="))
					{
						bouton.setTexture(app.loadImage("../ressources/calc/egal.png"));
					}
				}
				
				return false;
			}
		});
	}
	
	/**
	 * Fonction permettant d'additionner deux valeurs
	 * @param val1
	 * @param val2
	 */
	private void addition(double val1, double val2){
		valeur1 = Double.toString(val1 + val2);
	}
	
	/**
	 * Fonction permettant de soustraire 2 valeurs
	 * @param val1
	 * @param val2
	 */
	private void soustraction(double val1, double val2){
		valeur1 = Double.toString(val1 - val2);
	}
	
	/**
	 * Fonction permettant de multiplier 2 valeurs
	 * @param val1
	 * @param val2
	 */
	private void produit(double val1, double val2){
		valeur1 = Double.toString(val1 * val2);
	}
	
	/**
	 * Fonction permettant de diviser 2 valeurs
	 * @param val1
	 * @param val2
	 */
	private void quotient(double val1, double val2){
		valeur1 = Double.toString(val1 / val2);
	}
}

/**
 * Classe Bouton pour la calculatrice
 * @author Nicolas Lefebvre
 * @version 1.0
 */
class BoutonCalc extends MTRectangle {
	
	private String texte;
	
	/**
	 * Constructeur
	 * @param _texte
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param pApplet
	 * @param texture
	 */
	public BoutonCalc(String _texte, float x, float y, float width, float height, AbstractMTApplication pApplet, String texture) {
		super(x, y, width, height, pApplet);
		
		//Application des paramètres graphqiues
		this.setTexture(pApplet.loadImage(texture));
		this.setNoStroke(true);
		
		texte = _texte;
		
		Vector3D centre = this.getCenterPointGlobal();
		
		//Affichage du texte du bouton
		MTTextArea texteBouton = new MTTextArea(pApplet);
		texteBouton.setText(texte);
		texteBouton.getCenterPointGlobal();
		texteBouton.setPositionGlobal(new Vector3D(x + width/2, y + height/2));
		texteBouton.setNoFill(true);
		texteBouton.setNoStroke(true);
		texteBouton.setFontColor(new MTColor(255, 255, 255));
		texteBouton.setPickable(false);
		this.addChild(texteBouton);
		
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
	}
	
	/**
	 * Fonction permettant de récupèrer le texte du bouton
	 * @return
	 */
	public String getText(){
		return texte;
	}
}

