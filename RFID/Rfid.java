/*
 * Copyright 2007 Phidgets Inc.  All rights reserved.
 */

package rfid;

import itac.Login;

import com.phidgets.*;
import com.phidgets.event.*;

public class RFID
{
	public boolean connecte = false;
	public String UserID = "01068e27b6";
	public String UserName = "zzz";
	
	public RFID(final Login login) throws Exception{      
		RFIDPhidget rfid;
		
		//System.out.println(Phidget.getLibraryVersion());

		rfid = new RFIDPhidget();
		rfid.addAttachListener(new AttachListener() {
			public void attached(AttachEvent ae)
			{
				try
				{
					((RFIDPhidget)ae.getSource()).setAntennaOn(true);
					((RFIDPhidget)ae.getSource()).setLEDOn(true);
				}
				catch (PhidgetException ex) { }
				//System.out.println("attachment of " + ae);
			}
		});
		
		//Méthode de détection des tags RFID en Input
		rfid.addTagGainListener(new TagGainListener()
		{
			public void tagGained(TagGainEvent gain)
			{
				//System.out.println("Tag : " + gain.getValue());
				
				if(gain.getValue().equals(UserID))
				{
					if(connecte == false) 
					{
						System.out.println("Connexion de "+ UserName +" !");
						connecte = true;
						login.rfidConnect = true;
					}
					/*
					else
					{
						System.out.println("Déconnexion de "+ UserName +"!");
						//connecter(true);
					}*/
				}
			}
		});
		
		//Méthode de détection des tags RFID en Output
		rfid.addTagLossListener(new TagLossListener()
		{
			public void tagLost(TagLossEvent lost)
			{
				//System.out.println("Lost : " + lost.getValue());
				//System.out.println("Connecte : " + connecte);
				//System.out.println("Déconnecte : " + deconnecte);
			}
		});

		rfid.openAny();
		
		System.in.read();
		System.out.print("closing...");
		rfid.close();
		rfid = null;
		System.out.println(" ok");
		if (false) {
			System.out.println("wait for finalization...");
			System.gc();
		}
	}
	
	public final void connecter(boolean b)
	{
		connecte = b;
	}
}
