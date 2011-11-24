package RFID;

import com.phidgets.*;

public class Rfid {
	private static RFIDPhidget rfid;
	
	
	public static final void main(String args[]) throws Exception
	{
		try{
			rfid = new RFIDPhidget();
			rfid.setAntennaOn(true);
		}
		catch (Exception e){
			
		}
	}
}
