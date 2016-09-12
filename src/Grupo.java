import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PShape;

public class Grupo {
	private PApplet app;

	// Imagen vectorial representante de un grupo
	private PShape sol;

	// Variables para animar la rotación, distribuir alrededor del grupo y mover
	// los planetas a una posición indicada para que estos pueden distribuirse
	// correctamente
	private float anguloPet, distribucion, move;
	private boolean seMueve;
	private ArrayList<Planeta> ref;

	// Posición del grupo
	private float xG, yG, xSuma, ySuma;

	public Grupo(PApplet app, PShape sol, float xG, float yG) {
		this.app = app;
		this.sol = sol;
		this.xG = xG;
		this.yG = yG;
		move = 75;
		ref = new ArrayList<Planeta>();
	}

	public void pintar() {

		// Se pinta la ilustración del sol como centro del grupo
		sol.disableStyle();
		app.noStroke();
		app.fill(60, 70, 90);
		app.shape(sol, xG, yG, 70, 70);
		for (int i = 0; i < 4; i++) {
			app.fill(50 - (i * 15), 60 - (i * 3), 95);
			app.ellipse(xG, yG, 40 - (i * 10), 40 - (i * 10));
		}
	}

	public void mover(float xG, float yG) {
		// Mover grupo
		this.xG = xG;
		this.yG = yG;
	}

	public void distribuir() {
		// Distribuir los planetas alrededor del sol (Grupo)
		for (int i = 0; i < ref.size(); i++) {
			distribucion = 360 / ref.size();
			app.rotate(PApplet.radians(distribucion));
			ref.get(i).pintarGrupo();
			ref.get(i).moverGrupo(0 + move, 0);
		}
	}

	public void medir() {
		// Se promedia la posición del grupo basado en las características de
		// todos los planetas agregados
		if (ref.size() > 0) {
			xSuma = 0;
			ySuma = 0;
			for (int i = 0; i < ref.size(); i++) {
				xSuma += ref.get(i).getX();
				ySuma += ref.get(i).getY();
			}
			xG = xSuma / ref.size();
			yG = ySuma / ref.size();
		}
	}

	public void rotar() {
		// Rotación de los planetas alrededor del sol
		app.pushMatrix();
		app.translate(xG, yG);
		anguloPet -= 0.04;
		app.rotate(anguloPet);
		distribuir();
		app.popMatrix();
	}

	public void agregar(Planeta planet) {
		// Agrega nuevo planeta
		ref.add(planet);

		// Cuando se agreguen más de 6 planetas entonces se amplia el circulo de
		// estos alrededor del sol
		if (ref.size() > 6) {
			move = 100;
		}

		if (seMueve == false) {
			medir();
		}
	}

	public void reset() {
		if (ref.size() > 0) {
			xG = ref.get(0).getXgrupo();
			yG = ref.get(0).getYgrupo();
		}
	}

	public boolean validar(float posX, float posY) {
		// Valida posición del mouse respecto al grupo para la manipulación de
		// su posición en lógica
		if (PApplet.dist(posX, posY, xG, yG) < 25) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList devolver() {
		return ref;
	}

	public int tamano() {
		return ref.size();
	}

	public float getxG() {
		return xG;
	}

	public float getyG() {
		return yG;
	}

	public boolean isSeMueve() {
		return seMueve;
	}

	public void setSeMueve(boolean seMueve) {
		this.seMueve = seMueve;
	}

}
