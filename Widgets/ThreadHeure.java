package Widgets;

public class ThreadHeure extends Thread {
	
	public WidgetHeure widget;
	private boolean c = true;
	
	public ThreadHeure(String name, WidgetHeure _widget){
		super(name);
		
		widget = _widget;
	}
	
    public void run() {
    	while(c){
    		widget.definirHeure();
    		widget.afficherHeure();
    		
    		try {
    			sleep(1000);
    		} catch (Exception e) {
    			System.out.println("Le Thread du widget heure à planté :" + e.toString());
    		}
    	}
    }
    
    public void interrupt(){
    	c = false;
    }
}
