package Itac;

import java.io.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.filter.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class ConfigUtilisateur {
	static org.jdom.Document document;
	static Element racine;

	public static void main(String[] args)
	{
		//On cr�e une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		
		try
		{
			//On cr�e un nouveau document JDOM avec en argument le fichier XML
			//Le parsing est termin� ;)
			document = sxb.build(new File("../users/admin/params/config.xml"));
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		
		//On initialise un nouvel �l�ment racine avec l'�l�ment racine du document.
		racine = document.getRootElement();
		
		//afficheALL();
		//String[] param = getUserInformations();
		getWidgets();
	}
	   
	static void afficheALL()
	{
		//On cr�e une List contenant tous les noeuds "etudiant" de l'Element racine
		List listEtudiants = racine.getChildren("user");

		//On cr�e un Iterator sur notre liste
		Iterator i = listEtudiants.iterator();
		while(i.hasNext())
		{
			//On recr�e l'Element courant � chaque tour de boucle afin de
			//pouvoir utiliser les m�thodes propres aux Element comme :
			//selectionner un noeud fils, modifier du texte, etc...
			Element courant = (Element)i.next();
			//On affiche le nom de l'element courant
			System.out.println(courant.getChild("name").getText());
			System.out.println(courant.getChild("password").getText());
			System.out.println(courant.getChild("mail").getText());
			System.out.println(courant.getChild("urlImageProfile").getText());
			System.out.println(courant.getChild("urlBackground").getText());
		}
	}
	
	public String[] getUserInformations(){
		String[]  userInfo = null;
		List listInfos = racine.getChildren("user");
		
		Iterator i = listInfos.iterator();
		while(i.hasNext())
		{
			Element courant = (Element)i.next();
			userInfo[0] = courant.getChild("name").getText();
			userInfo[1] = courant.getChild("password").getText();
			userInfo[2] = courant.getChild("mail").getText();
			userInfo[3] = courant.getChild("urlImageProfile").getText();
			userInfo[4] = courant.getChild("urlBackground").getText();
		}
		
		return userInfo;
	}
	
	public static void getWidgets(){
		Element widgets = racine.getChild("widgets");
		
		List listInfos = widgets.getChildren("widget");
		
		Iterator i = listInfos.iterator();
		while(i.hasNext())
		{
			Element courant = (Element)i.next();
			System.out.println(courant.getChild("type").getText());
			System.out.println(courant.getChild("posX").getText());
			System.out.println(courant.getChild("posY").getText());
			System.out.println("");
		}
		
		//System.out.println(widgets.get(0));
	}
}
