package Piano;

import java.util.ArrayList;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PGraphics;
/**
 * MTPiano class. 
 * 
 * Code adapted from:
 * http://www.oracle.com/technetwork/java/index-139508.html
 * @author Andrew McGlynn November 2011
 *
 */
public class MTPiano extends MTRectangle{
	private PianoKey[] whiteKeys = new PianoKey[42];
	private ArrayList<PianoKey> blackKeys = new ArrayList<PianoKey>();
	private float width, height;
	private MidiSynthesizer synth;
	private int[] whiteKeyNotes = {24, 26, 28, 29, 31, 33, 35, 36, 38, 40, 41, 43, 45, 47, 48, 50, 52, 53, 55, 57, 59, 60, 62, 64, 65, 67, 69, 71, 72, 74, 76, 77, 79, 81, 83, 84, 86, 88, 89, 91, 93, 95};
	private int[] blackKeyNotes = {25, 27, 30, 32, 34, 37, 39, 42, 44, 46, 49, 51, 54, 56, 58, 61, 63, 66, 68, 70, 73, 75, 78, 80, 82, 85, 87, 90, 92, 94};
	
	private long lastUpdate = 0;
	public MTPiano(MTApplication mtapp, Vector3D upperLeft){
		super(upperLeft.x,upperLeft.y,820,120,mtapp);
		width = 800;
		height = 100;
		synth = new MidiSynthesizer();
		setFillColor(MTColor.GRAY);
		float padding = 10;
		MTRectangle interactionLayer = new MTRectangle(upperLeft.x + padding,upperLeft.y+ padding,width,height,mtapp);
		MTColor filll = new MTColor(70,70,70,70);
		interactionLayer.setFillColor(filll);
		interactionLayer.removeAllGestureEventListeners();
		interactionLayer.setNoFill(true);
		interactionLayer.setNoStroke(true);
		interactionLayer.addInputListener(new IMTInputEventListener() {
			public boolean processInputEvent(MTInputEvent inEvt){
				AbstractCursorInputEvt posEvt = (AbstractCursorInputEvt)inEvt;
				InputCursor m = posEvt.getCursor();
				float x = (int)m.getCurrentEvtPosX();
				float y = (int)m.getCurrentEvtPosY();
				for(int i=0; i < blackKeys.size(); i++){
					if(blackKeys.get(i).containsPointGlobal(new Vector3D(x,y))){
						blackKeys.get(i).activate(100);
						return false; //return so the white key doesn't get triggered as well 
					}
				}
				// if the key was pressed, keep it active for 100 msec
				for(int i=0; i < whiteKeys.length; i++){
					if(whiteKeys[i].containsPointGlobal(new Vector3D(x,y))){
						whiteKeys[i].activate(100);
					}
				}
				return false;
			}
		});
		float whiteKeyWidth = width/whiteKeys.length;
		for(int i = 0; i<whiteKeys.length; i++){
			whiteKeys[i] = new PianoKey(mtapp, upperLeft.x+padding+i*whiteKeyWidth, upperLeft.y+padding, whiteKeyWidth, 100, whiteKeyNotes[i], MTColor.WHITE);
			addChild(whiteKeys[i]);
		}
		//black keys  ,    public Key(int x, int y, int width, int height, int note)
		int keyNum = 0;
		for (int i = 0, x = 0; i < 6; i++, x += whiteKeyWidth) {
			PianoKey key = new PianoKey(mtapp,upperLeft.x+padding+(x += whiteKeyWidth)-4, upperLeft.y+padding, whiteKeyWidth/2, height/2, blackKeyNotes[keyNum++], MTColor.BLACK);
			addChild(key);
			blackKeys.add(key);
		
			key = new PianoKey(mtapp,upperLeft.x+padding+(x += whiteKeyWidth)-4, upperLeft.y+padding, whiteKeyWidth/2, height/2, blackKeyNotes[keyNum++],MTColor.BLACK);
			addChild(key);
			blackKeys.add(key);
		            
			x += whiteKeyWidth;
			key = new PianoKey(mtapp,upperLeft.x+padding+(x += whiteKeyWidth)-4, upperLeft.y+padding, whiteKeyWidth/2, height/2, blackKeyNotes[keyNum++],MTColor.BLACK);
			addChild(key);
			blackKeys.add(key);
		            
			key = new PianoKey(mtapp,upperLeft.x+padding+(x += whiteKeyWidth)-4, upperLeft.y+padding, whiteKeyWidth/2, height/2, blackKeyNotes[keyNum++],MTColor.BLACK);
			addChild(key);
			blackKeys.add(key);
			            
			key = new PianoKey(mtapp,upperLeft.x+padding+(x += whiteKeyWidth)-4, upperLeft.y+padding, whiteKeyWidth/2, height/2, blackKeyNotes[keyNum++],MTColor.BLACK);
			addChild(key);
			blackKeys.add(key);
		            
		}
		addChild(interactionLayer);
	}
	@Override
	public void drawComponent(PGraphics g){
		long now = System.currentTimeMillis();
		for(PianoKey key: whiteKeys){
			key.update(now - lastUpdate);
		}
		for(PianoKey key: blackKeys){
			key.update(now - lastUpdate);
		}
		super.drawComponent(g);
		lastUpdate = System.currentTimeMillis();
	}
	
	private class PianoKey extends MTRectangle{
		private boolean on;
		private int note;
		private long timeToLive = 0;
		private MTColor keyColor;
		
		public PianoKey(MTApplication mtapp, float x, float y, float w, float h, int note, MTColor keyColor){
			super(x, y, w, h, mtapp);
			this.keyColor = keyColor;
			this.on = false;
			this.note = note;
			setPickable(false);
			setStrokeColor(MTColor.BLACK);
			setFillColor(keyColor);
		}
		
		public void on(){
			if(!on)synth.playSound(note);
			this.on = true;
			setFillColor(MTColor.BLUE);
		}
		
		public void off(){
			this.on = false;
			synth.stopSound(note);
			setFillColor(keyColor);
		}
		
		public void update(long time){
			if(timeToLive <= 0){
				if(on){
					off();
					on = false;
				}
			}
			else{
				timeToLive -= time;
			}
		}
		
		public void activate(long time){
			timeToLive = time;
			on();
		}
	}
}