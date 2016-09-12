import processing.core.PApplet;

public class Esfera {
	private PApplet app;
	//Variables de posicion para los crateres y las estrellas
	private float x, y, xC, yC, xCf, yCf;
	private float xEf, yEf, xEe, yEe, xE1, yE1, xE2, yE2, diam;

	public Esfera(PApplet app, float x, float y) {
		this.app = app;
		this.x = x;
		this.y = y;
		
		// Se rellenan las variables de posicion tanto para crateres como para
		// las estrellas
		xC = (int) app.random(-20, 20);
		yC = (int) app.random(-20, 20);
		xE1 = (int) app.random(-40, -30);
		yE1 = (int) app.random(-40, -30);
		xE2 = (int) app.random(30, 40);
		yE2 = (int) app.random(30, 40);
		diam = (int) app.random(-10, 10);
		switch ((int) app.random(2)) {
		case 0:
			xEf = xE1;
			break;
		case 1:
			xEf = xE2;
			break;
		}
		switch ((int) app.random(2)) {
		case 0:
			yEf = yE1;
			break;
		case 1:
			yEf = yE2;
			break;
		}
	}

	public void pintar(float x, float y) {
		this.x = x;
		this.y = y;
		xCf = x + xC;
		yCf = y + yC;
		// Se pintan los crateres
		app.noFill();
		app.stroke(0);
		app.strokeWeight(1);
		app.ellipse(xCf, yCf, diam, diam);
	}

	public void pintarEstrellas(float x, float y) {
		this.x = x;
		this.y = y;
		// Se pintan las estrellas
		xEe = x + xEf;
		yEe = y + yEf;
		app.noStroke();
		app.fill(360);
		app.ellipse(xEe, yEe, diam, diam);
	}
}
