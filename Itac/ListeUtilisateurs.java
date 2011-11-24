package Itac;

import java.io.File;
import java.util.ArrayList;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

/**
 * Liste des utilisateurs du système
 * @author Nicolas
 * @version 1.0
 */
public class ListeUtilisateurs extends MTRectangle {
	
	//Déclaration des variables
	private AbstractMTApplication app;
	private ArrayList<String> users = new ArrayList<String>();
	private int nbUsers = 0;
	private String userSelected = "";
	public MTRoundRectangle selection;
	
	/**
	 * Constructeur
	 * @param Position sur l'axe X
	 * @param Position sur l'axe Y
	 * @param Application
	 */
	public ListeUtilisateurs(float _xPos, float _yPos, AbstractMTApplication _app) {
		super(_xPos, _yPos, _app);
		
		//Récupération des paramètres
		app = _app;
		
		//Affichage du rectangle de sélection
		selection = new MTRoundRectangle(-1000, 0, 0, 103, 103, 4, 4, app);
		this.addChild(selection);
		
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
		int ecart = 150;
		int taille = (nbUsers * ecart) - ecart;
		
		for(int i = 0; i < nbUsers; i++)
		{
			int x = (((app.width - taille) / 2) + i * ecart);
			Utilisateur user = new Utilisateur((String)users.get(i), x, 50, 100, 100, app);
			
			//setTouchActions(user);
			
			this.addChild(user);
		}
	}
	
	/**
	 * Définit les paramètres d'affichage de la liste.
	 */
	public void setListeDesign(){
		this.setPositionGlobal(new Vector3D(app.width/2f, 350));
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
