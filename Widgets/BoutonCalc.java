package Widgets;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;

public class BoutonCalc extends MTTextArea {

	public BoutonCalc(String texte, float x, float y, float width, float height, AbstractMTApplication pApplet) {
		super(x, y, width, height, pApplet);
		
		this.setText(texte);
		
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
	}
}
