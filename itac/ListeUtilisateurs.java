package itac;

import java.io.File;
import java.util.ArrayList;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.util.math.Vector3D;

/**
 * Liste des utilisateurs du système
 * @author Nicolas
 * @version 1.0
 */
public class ListeUtilisateurs extends MTRectangle {
	
	//Déclaration des variables
	private AbstractMTApplication app;
	public int x, y, width, height;
	private ArrayList<String> users = new ArrayList<String>();
	private int nbUsers = 0;
	//public MTRoundRectangle selection;
	
	/**
	 * Constructeur
	 * @param Position sur l'axe X
	 * @param Position sur l'axe Y
	 * @param Application
	 */
	public ListeUtilisateurs(int _x, int _y, int _width, int _height, AbstractMTApplication _app) {
		super(_x, _y, _app);
		
		//Récupération des paramètres
		app = _app;
		x = _x;
		y = _y;
		width = _width;
		height = _height;
		
		setListeDesign();
		compterUtilisateurs();
		listerUtilisateurs();
	}
	
	//Méthodes
	/**
	 * On compte le nombre d'utilisateurs et on ajoute leur nom à la liste d'utilisateurs.
	 */
	private void compterUtilisateurs(){
		String chemin = "../users/";
		File dossier = new File(chemin);
		
		//On liste tous les fichiers du dossier
		String [] listeUsers = dossier.list();
		
		for(int i = 0; i < listeUsers.length; i++)
		{
			File fichier = new File(listeUsers[i]);
			
			if(fichier.isFile() == false)
			{
				String userName = fichier.getName();
				users.add(userName);
				nbUsers++;
			}
		}
	}
	
	/**
	 * On affiche chacun des utilisateurs référencés dans la variable users.
	 */
	public void listerUtilisateurs(){
		int ecart = 350;
		int taille = (nbUsers * ecart) - ecart;
		
		for(int i = 0; i < nbUsers; i++)
		{
			int x = (((app.width - taille) / 2) + i * ecart);
			Utilisateur user = new Utilisateur((String)users.get(i), x, y, 250, 250, app);
			MTRoundRectangle userContour = new MTRoundRectangle(x, y, 0, 265, 265, 5, 5, app);
			userContour.getCenterPointGlobal();
			userContour.setPositionGlobal(new Vector3D(x, y));
			userContour.setPickable(false);
			
			//setTouchActions(user);
			
			this.addChild(userContour);
			this.addChild(user);
		}
	}
	
	/**
	 * Définit les paramètres d'affichage de la liste.
	 */
	public void setListeDesign(){
		this.setNoFill(true);
		this.setNoStroke(true);
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
	}
	
	/**
	 * Renvoie l'ensemble des utilisateurs de la liste.
	 * @return Array contenant les utilisateurs.
	 */
	public Utilisateur getUser(String nom){ return (Utilisateur) getChildByName(nom); }
	
	/**
	 * Renvoie le nombre des utilisateurs de la liste.
	 * @return Nombre d'utilisateurs.
	 */
	public int getNbUsers() { return nbUsers; }
	
	/**
	 * Renvoie le nom de chaque utilisateur.
	 * @return Nom de chaque utilisateur.
	 */
	public ArrayList<String> getUsersName() { return users; }
	
	/**
	 * Renvoie le mot de passe de l'utilisateur sélectionné.
	 * @return Mot de passe.
	 */
	public String getUserPassword(String userName) {
		Utilisateur user = (Utilisateur) getChildByName(userName);
		return user.getPassword();
	}
}
