
import java.util.ArrayList;
import processing.core.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;

public class Logica {
	private PApplet app;

	/*
	 * Variables para el cambio de pantallas, los planetas iniciales, la linea
	 * de String que leeran los nuevos planetas Y el contador para la
	 * eliminacion de un grupo
	 */
	private int pantalla, numPlanetasIni, k, contador,people;

	// PImage para el fondo y la interfaz general de la app
	private PImage fondo, logo, space, esquema;

	// Variables para la animación del fondo y transparencia en el inicio
	private float angulo, opacidad;

	// Variable que genera las características random de los planetas
	private String dato;

	// Vectores para los planetas y el sol (Grupo)
	private PShape[] estilos;
	private PShape sol;

	// Arreglo de Strings que carga el .txt
	private String[] datos;

	// Booleanos para controlar la animación y la creación de un nuevo planeta
	private boolean opa, tiempo;

	// Objetos null para la manipulación de los objetos en mouseDragged y
	// mouseReleased
	private Planeta elegido;
	private Grupo gr;

	// Archivos generados para la escritura en el .txt
	private File archivo;
	private FileOutputStream flujo;
	private PrintWriter escritura;

	// Arrays que guardan los objetos Planeta y Grupos
	private ArrayList<Planeta> planetas;
	private ArrayList<Grupo> grupos;

	public Logica(PApplet app) {
		this.app = app;
		numPlanetasIni = 10;
		k = 10;
		contador = 0;
		elegido = null;
		gr = null;

		// Se generan los datos random para la escritura
		dato = (int) app.random(40, 70) + "," + (int) app.random(5) + "," + (int) app.random(5) + ","
				+ (int) app.random(6) + "," + app.random((float) 1.5) + "," + app.random((float) 0.01, (float) 0.09)
				+ "," + (int) app.random(50, 100) + "," + (int) app.random(4);

		// Escritura de datos iniciales, la generacion de 1 linea de mas para
		// cuando se cree un planeta y la carga de el .txt
		cargarData();
		generarData();
		loadData();

		planetas = new ArrayList<Planeta>();
		grupos = new ArrayList<Grupo>();
		fondo = app.loadImage("FondoSpace.png");
		space = app.loadImage("PantallaSpace.png");
		logo = app.loadImage("logo.png");
		esquema = app.loadImage("Pintar.png");
		sol = app.loadShape("Sun.svg");
		pantalla = 0;
		opacidad = 255;
		estilos = new PShape[4];
		for (int i = 0; i < estilos.length; i++) {
			estilos[i] = app.loadShape("Planetas-0" + i + ".svg");
		}
		for (int i = 0; i < numPlanetasIni; i++) {
			planetas.add(new Planeta(app, estilos[(int) app.random(4)], datos[i]));
		}
	}

	public void ejecutar() {
		// Boolean para confirmar la creación de un nuevo planeta
		if (app.frameCount % 60 == 0) {
			tiempo = true;
		}

		// Fondo de el universo rotando
		app.tint(255, 255);
		app.background(0);
		app.pushMatrix();
		app.translate(app.width / 2, app.height / 2);
		angulo += 0.002;
		app.rotate(angulo);
		app.imageMode(PConstants.CENTER);
		app.image(fondo, 0, 0);
		app.popMatrix();
		switch (pantalla) {
		case 0:
			// Se carga la pantalla de inicio
			inicio();
			break;

		case 1:
			// Se carga la pantalla de interacción y se carga el .txt
			// constantemente para la sobreescritura.
			pintar();
			loadData();
			break;
		}
	}

	public void inicio() {
		// Logo de inicio de la app, Planetarium
		app.tint(255);
		app.imageMode(PConstants.CENTER);
		app.image(logo, app.width / 2, app.height / 2);

		// Animación y pintar del texto "Spacebar para comenzar"
		// La animación se da mediante un tint y la variable opacidad.
		if (opacidad >= 255) {
			opa = false;
		} else if (opacidad <= 80) {
			opa = true;
		}
		if (opa == false) {
			opacidad -= 12;
		} else if (opa == true) {
			opacidad += 12;
		}
		app.tint(255, opacidad);
		app.image(space, app.width / 2, app.height / 2);
	}

	public void pintar() {

		// Se pintan los planetas y los grupos
		app.imageMode(PConstants.CENTER);
		app.image(esquema, app.width / 2, app.height / 2);
		for (int i = 0; i < planetas.size(); i++) {
			planetas.get(i).exist(datos[i]);
		}
		for (int i = 0; i < grupos.size(); i++) {
			grupos.get(i).pintar();
			grupos.get(i).rotar();
		}

		// Si el contador es 3 entonces se elimina el grupo y vuelve a su estado
		// original
		if (contador >= 3) {
			eliminarGrupo();
			contador = 0;
		}

		// Si se presiona el mouse el contador ira sumando cada segundo
		if (app.mousePressed) {
			if (app.frameCount % 60 == 0) {
				contador++;
			}
		}

		// Elipses que aparecen segun el tiempo restante para explotar
		if (gr != null) {
			for (int i = 0; i < contador + 1; i++) {
				app.noStroke();
				app.fill(0, 80, 50 + (i * 15));
				app.ellipse(gr.getxG(), gr.getyG(), 15 + i * 8, 15 + i * 8);
			}
		}
	}

	public void tecla() {
		// Cambio de pantalla inicial a pantalla de interacción
		if (app.key == ' ' && pantalla == 0) {
			pantalla = 1;
			tiempo = false;
		}

		// Generacion de nuevos planetas y datos random para la escritura de sus
		// características en el .txt
		if (app.key == ' ' && pantalla == 1 && tiempo == true) {
			dato = (int) app.random(40, 70) + "," + (int) app.random(5) + "," + (int) app.random(5) + ","
					+ (int) app.random(6) + "," + app.random((float) 1.5) + "," + app.random((float) 0.01, (float) 0.09)
					+ "," + (int) app.random(50, 100) + "," + (int) app.random(4);
			generarData();
			planetas.add(new Planeta(app, estilos[(int) app.random(4)], datos[k]));
			k++;
		}

		// Se limpia el lienzo de planetas y grupos
		if (app.key == PConstants.ENTER && pantalla == 1) {
			planetas.clear();
			grupos.clear();
		}
	}

	public void click() {
		// Se rellena la variable elegido para su movimiento o interacción con
		// un grupo
		for (int i = 0; i < planetas.size(); i++) {
			if (planetas.get(i).validar(app.mouseX, app.mouseY)) {
				elegido = planetas.get(i);
				elegido.setMoviendose(true);
			}
		}

		// Se rellena la variable gr para su movimiento
		for (int i = 0; i < grupos.size(); i++) {
			if (grupos.get(i).validar(app.mouseX, app.mouseY)) {
				gr = grupos.get(i);
				gr.setSeMueve(true);
			}
		}

	}

	public void drag() {
		// Movimiento de un planeta elegido
		if (elegido != null) {
			elegido.mover(app.mouseX, app.mouseY);
		}

		// Movimiento de un grupo vacío elegido
		if (gr != null) {
			gr.mover(app.mouseX, app.mouseY);
		}
	}

	public void soltar() {

		// El contador para la eliminación de grupos es 0
		contador = 0;

		// Agregar elementos a un grupo o si no, resetear su posición
		if (elegido != null) {
			for (int i = 0; i < grupos.size(); i++) {
				if (elegido.validar(app.mouseX, app.mouseY) == grupos.get(i).validar(app.mouseX, app.mouseY)) {
					elegido.reset();
					grupos.get(i).agregar(elegido);
					planetas.remove(elegido);

				} else if (elegido.isMoviendose()) {
					elegido.reset();
					elegido.setMoviendose(false);
				}
			}
			if (elegido.isMoviendose()) {
				elegido.reset();
				elegido.setMoviendose(false);
			}
			// Si se suelta un planeta o un grupo se disvinculara este del
			// elegido o gr para poder elegir otro
			elegido = null;
		}
		// Restauras posicion original de grupo y luego hacer gr un null
		if (gr != null && gr.isSeMueve() == true) {
			gr.reset();
			gr.setSeMueve(false);
		}
		gr = null;

	}

	public void crearGrupo() {
		// Crear grupo
		grupos.add(new Grupo(app, sol, app.mouseX, app.mouseY));
	}

	public void eliminarGrupo() {
		// Eliminar grupo y añadir los elementos del grupo al array original de
		// planetas
		if (gr.tamano() > 0) {
			planetas.addAll(gr.devolver());
		}
		grupos.remove(gr);
		gr = null;
	}

	public void generarData() {
		// Se escriben los datos random en el .txt una vez para cuando se
		// agregan planetas
		try {
			archivo = new File("data/planetario.txt");
			if (archivo.canWrite() == true) {
				flujo = new FileOutputStream(archivo, true);
				escritura = new PrintWriter(flujo);
				escritura.write(dato);
				escritura.println();
				escritura.flush();
				escritura.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadData() {
		// Se carga el .txt en el arreglo de Strings
		datos = app.loadStrings("planetario.txt");
	}

	public void cargarData() {
		// Se escriben los datos random iniciales en el .txt
		for (int i = 0; i < numPlanetasIni; i++) {
			try {
				archivo = new File("data/planetario.txt");
				if (archivo.canWrite() == true) {
					flujo = new FileOutputStream(archivo, true);
					escritura = new PrintWriter(flujo);
					escritura.println(dato);
					escritura.flush();
					escritura.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			dato = (int) app.random(40, 70) + "," + (int) app.random(5) + "," + (int) app.random(5) + ","
					+ (int) app.random(6) + "," + app.random((float) 1.5) + "," + app.random((float) 0.01, (float) 0.09)
					+ "," + (int) app.random(50, 100) + "," + (int) app.random(4);
		}

	}
}
