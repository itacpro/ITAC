package Itac;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

/**
 * Classe utilisateur
 * @author Nicolas Lefebvre
 * @version 1.0
 */
public class Utilisateur extends MTRectangle{
	
	//Déclaration des variables
	private static org.jdom.Document document;
	private static Element racine;
	private AbstractMTApplication app;
	private float xPos, yPos, width, height;
	private IFont font = FontManager.getInstance().createFont(app, "Impact.ttf", 40, new MTColor(255,255,255));
	private MTTextArea texte;
	
	//Déclaration des variables d'information sur l'utilisateur
	private String name;
	private String password;
	private String mail;
	private String image;
	private String urlImageProfile;
	private String urlBackground;
	
	/**
	 * Constructeur
	 * @param Nom de l'utilisateur
	 * @param Position sur l'axe X
	 * @param Position sur l'axe Y
	 * @param Largeur de l'icone de l'utilisateur
	 * @param Hauteur de l'icone de l'utilisateur
	 * @param Application
	 */
	public Utilisateur(String _name, float _xPos, float _yPos, float _width, float _height, AbstractMTApplication _app) {
		super(_xPos, _yPos, _width, _height, _app);
		
		//Récupération des paramètres
		app = _app;
		xPos = _xPos;
		yPos = _yPos;
		width = _width;
		height = _height;
		name = _name;
		//image = "../users/" + name + "/params/profile.png";
		recupererParams();
		
		image = urlImageProfile;
		this.setName(name);
		
		//recupererMotDePasse();
		afficherUtilisateur();
	}
	
	//Méthodes
	
	/**
	 * Définit les paramètres d'affichage de l'icône d'utilisateur.
	 */
	private void afficherUtilisateur(){
		this.setAnchor(PositionAnchor.CENTER);
		this.setPositionGlobal(new Vector3D(xPos, yPos));
		this.setNoStroke(true);
		this.setTexture(app.loadImage(image));
		
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
		
		texte = new MTTextArea(app, font);
		texte.setNoFill(true);
		texte.setNoStroke(true);
		texte.setText(name);
		texte.setAnchor(PositionAnchor.CENTER);
		texte.setPositionGlobal(new Vector3D(xPos + width/2, yPos + height + 30));
		this.addChild(texte);
		
		texte.unregisterAllInputProcessors();
		texte.removeAllGestureEventListeners();
	}
	
	/**
	 * Cherche puis stocke le mot de passe de l'utilisateur
	 */
	private void recupererMotDePasse() {
		String config = "../users/" + name + "/params/config.txt";
		
		InputStream ips;
		try {
			ips = new FileInputStream(config);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			
			while ((ligne = br.readLine()) != null) 
			{
				password = ligne;
			}
		} catch (Exception e) {
			System.out.println(e.toString()); 
		}
	}
	
	/**
	 * Récupère les informations contenues dans le fichier config.xml et les stocke.
	 */
	private void recupererParams(){
		//On crée une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		
		try {
			//On crée un nouveau document JDOM avec en argument le fichier XML
			document = sxb.build(new File("../users/" + name + "/params/config.xml"));
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
		
		//On initialise un nouvel élément racine avec l'élément racine du document.
		racine = document.getRootElement();
		
		//On crée une List contenant tous les noeuds "etudiant" de l'Element racine
		List listParams = racine.getChildren("user");

		//On crée un Iterator sur notre liste
		Iterator i = listParams.iterator();
		while(i.hasNext())
		{
			Element courant = (Element)i.next();
			
			password = courant.getChild("password").getText();
			mail = courant.getChild("mail").getText();
			urlImageProfile = courant.getChild("urlImageProfile").getText();
			urlBackground = courant.getChild("urlBackground").getText();
		}
	}
	
	//Setters
	public void setUserName(String value) { name = value; }
	public void setPassword(String value) { password = value; }
	public void setMail(String value) { mail = value; }
	public void setUrlImageProfile(String value) { urlImageProfile = value; }
	public void setUrlBackground(String value) { urlBackground = value; }
	
	//Getters
	public String getUserName() { return name; }
	public String getPassword() { return password; }
	public String getMail() { return mail; }
	public String getUrlImageProfile() { return urlImageProfile; }
	public String getUrlBackground() { return urlBackground; }
}
