import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PShape;

public class Planeta {
	private PApplet app;

	// Variables para posiciones o características del planeta
	private float x, y, xG, yG, xE, yE, vel, wiggle, girar, angulo, t;
	private int h, s, escala, estrellas, robustez, onda;

	// Variable para saber si es cogido para que el wiggle no se vea afectado
	private boolean moviendose;

	// Arreglo de string para guardar las caracteristicas
	private String[] cart;

	// Imagen vectorial del planeta
	private PShape estilo;

	// ArrayList de las estrellas y crateres del planeta
	private ArrayList<Esfera> crateres;
	private ArrayList<Esfera> estrel;

	public Planeta(PApplet app, PShape estilo, String cartB) {
		this.app = app;
		this.estilo = estilo;

		// Se rellenan variables
		escala = 50;
		xG = 0;
		yG = 0;
		t = 0;
		h = (int) app.random(360);

		// Se cargan las variables del metodo cadena
		cadena(cartB);
		posicion();
		crateres = new ArrayList<Esfera>();
		estrel = new ArrayList<Esfera>();

		// Se añaden crateres y estrellas dependiendo de el numero que se haya
		// obtenido de estos
		for (float i = 0; i < robustez; i++) {
			crateres.add(new Esfera(app, xE, yE));
		}
		for (float i = 0; i < estrellas; i++) {
			estrel.add(new Esfera(app, xE, yE));
		}
	}

	public void exist(String cartB) {
		// Se pinta la version final de los planetas con todas sus
		// cracteristicas
		rotar(cartB);
	}

	public void pintar(String cartB) {
		// Se mantiene actualizado el arreglo de strings que guarda sus
		// caracteristicas
		cadena(cartB);

		// Se pinta la onda que tiene el planeta atras dependiendo de el valor
		// de la variable onda
		for (int i = 0; i < onda; i++) {
			app.pushMatrix();
			app.noStroke();
			app.fill(0, 0, 100, 30);
			app.ellipse(0, 0, (i * 10) + escala + 10, (i * 10) + escala + 10);
			app.popMatrix();
		}

		// Se pinta el vector y el relleno de este
		app.tint(360);
		app.fill(h, 80, s);
		app.noStroke();
		app.ellipse(0, 0, escala - 8, escala - 8);
		app.shapeMode(app.CENTER);
		app.shape(estilo, 0, 0, escala + 7, escala + 7);
	}

	public void pintarGrupo() {
		// Metodo pintar para las grupos ya que en ese caso los planetas no
		// poseeran un wiggle

		// Se pintan las estrellas
		for (Esfera estr : estrel) {
			estr.pintarEstrellas(xG, yG);
		}
		// Se pintan las ondas
		for (int i = 0; i < onda; i++) {
			app.pushMatrix();
			app.noStroke();
			app.fill(0, 0, 100, 30);
			app.ellipse(xG, yG, (i * 10) + escala + 10, (i * 10) + escala + 10);
			app.popMatrix();
		}
		// Se pintan los planetas y su relleno
		app.tint(360);
		app.fill(h, 80, s);
		app.noStroke();
		app.ellipse(xG, yG, escala - 8, escala - 8);
		app.shapeMode(app.CENTER);
		app.shape(estilo, xG, yG, escala + 7, escala + 7);
		// Se pintan los crateres
		for (Esfera cra : crateres) {
			cra.pintar(xG, yG);
		}
	}

	public void mover(float x, float y) {
		// Metodo para mover el planeta en logica
		this.x = x;
		this.y = y;
	}

	public void moverGrupo(float xG, float yG) {
		// Metodo para mover el planeta en los grupos
		this.xG = xG;
		this.yG = yG;
	}

	public void adiciones() {
		for (Esfera cra : crateres) {
			cra.pintar(x, y);
		}
	}

	public void rotar(String cartB) {
		// Se pintan las estrellas
		for (Esfera estr : estrel) {
			estr.pintarEstrellas(x, y);
		}
		app.pushMatrix();
		t += vel;
		// Generacion del wiggle
		if (moviendose == false) {
			x = (wiggle * 12) * PApplet.cos(3 * t + 1) + xE;
			y = (wiggle * 7) * PApplet.sin(5 * t + 2) + yE;
		}
		app.translate(x, y);
		angulo -= girar;
		// Rotacion de los planetas segun el valor de giro
		app.rotate(angulo);
		pintar(cartB);
		app.popMatrix();
		// Se pintan los crateres
		adiciones();
	}

	public void reset() {
		// Restaura el movimiento del wiggle cuando no se agrega a un grupo
		x = (wiggle * 12) * PApplet.cos(3 * t + 1) + xE;
		y = (wiggle * 7) * PApplet.sin(5 * t + 2) + yE;
	}

	public void posicion() {
		// Se determina la posicion en x y en y del planeta basado en sus
		// características
		xE = 400 + (((((estrellas * 5) + (wiggle * 5)) + 42) - ((escala + (girar * 100)) + 1)) * 8);
		yE = 350 + (((((vel * 100) * 10) + (robustez * 10)) - (((onda + 1) * 10) + (s - 50))) * 5);
	}

	public void cadena(String cartB) {
		// Metodo para guardar en un arreglo de string las caracteristicas y que
		// cada una de estas guarde su propio valor
		cart = (PApplet.split(cartB, ','));
		escala = Integer.parseInt(cart[0]);//
		vel = Integer.parseInt(cart[1]);//
		vel = (float) (vel * 0.01);
		robustez = Integer.parseInt(cart[2]);//
		estrellas = Integer.parseInt(cart[3]);//
		wiggle = Float.parseFloat(cart[4]);
		girar = Float.parseFloat(cart[5]);//
		s = Integer.parseInt(cart[6]);//
		onda = Integer.parseInt(cart[7]);
	}

	public boolean validar(float posX, float posY) {
		// Validacion para la manipulación de la posición del planeta en logica
		if (PApplet.dist(posX, posY, x, y) < 25) {
			return true;
		} else {
			return false;
		}
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getXgrupo() {
		return (wiggle * 12) * PApplet.cos(3 * t + 1) + xE;
	}

	public float getYgrupo() {
		return (wiggle * 7) * PApplet.sin(5 * t + 2) + yE;
	}

	public boolean isMoviendose() {
		return moviendose;
	}

	public void setMoviendose(boolean moviendose) {
		this.moviendose = moviendose;
	}

}
