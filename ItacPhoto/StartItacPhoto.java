package ItacPhoto;

import org.mt4j.MTApplication;
import org.mt4j.input.inputSources.MacTrackpadSource;

public class StartItacPhoto extends MTApplication{
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		initialize();
	}
	@Override
	public void startUp() {
		addScene(new ItacPhotoScene(this, "ItacPhoto Scene"));
		
		//Gestion du touchpad MT des MacBooks.
		getInputManager().registerInputSource(new MacTrackpadSource(this));
	}
}