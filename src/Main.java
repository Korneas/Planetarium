import processing.core.*;

public class Main extends PApplet {
	
	Logica app;
	
	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "Main" };
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}
	
	@Override
	public void settings(){
		size(800,700);
		
		
	}
	
	@Override
	public void setup(){
		app = new Logica(this);
		colorMode(HSB,360,100,100);
		
	}
	
	@Override
	public void draw(){
		background(0);
		app.ejecutar();
		
	}
	
	@Override
	public void mouseClicked(){
		if (mouseEvent.getClickCount() == 2) {
			app.crearGrupo();
		}

	}
	
	@Override
	public void mousePressed(){
		app.click();
	}
	
	@Override
	public void mouseDragged(){
		app.drag();
	}
	
	@Override
	public void mouseReleased(){
		app.soltar();
	}

	@Override
	public void keyPressed(){
		app.tecla();
	}
}