package desktop;

/* 
 *  Copyright 2009 Gorka Revilla Fernandez & Fernando Lozano Pajaron
 * 
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version. This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program; if not, write to
 * the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA 0211	0-1301 USA.
 * 
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Es el Nucleo del programa, clase con el lanzador del programa y que controla todo el funcionamiento
 * del Arkanoid. Pinta la pantalla, realiza los movimientos, crea la ventana, detecta las
 * teclas pulsadas...
 */

public class Arkanoid extends Canvas implements Stage, KeyListener, MouseListener, MouseMotionListener, Runnable
{	
	/*
	 * Serial para el extends
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Definimos para leer el archivo de entrada que contendra el nivel que queremos leer para
	 * jugar.
	 */
	public static String INPUT_PATH="niveles/1";  

	/*
	 * Definimos el cronometro
	 */
	public static boolean inicio = false;
	Thread crono;
	javax.swing.JLabel tiempo = new javax.swing.JLabel();


	/*
	 * 	Crear mensajes en forma de dialogos
	 */
	public acm.io.IODialog dialog = new acm.io.IODialog(); //para crear dialogos

	/*
	 * Creamos el buffer para refrescar la pantalla
	 */
	public BufferStrategy strategy;

	/*
	 * Lo usaremos para controlar los fps. Almacena el tiempo que a tardado en refrescar cada
	 * pantalla
	 */
	public static long tiempoDeDuracion;

	/*
	 * Creamos la cache del sprite
	 */
	private SpriteCache spriteCache;

	/*
	 * Creamos el menu
	 */
	public Menu menu;
	
	/*
	 * Guarda la imagen del fondo de cada nivel.
	 * 
	 * Por defecto ponemos una, en caso de que no se carge ninguna en el primer nivel se cargara esta.
	 * En los siguientes niveles si no hay ninguna especificacion del fondo, se cargara el del nivel 
	 * anterior.
	 * 
	 * Las imagenes tienen que ser del tamanyo: 480x640. (Ancho x Alto)
	 * 
	 */
	public String imagenfondo="fondos/playa.gif";

	/*
	 * Este boolean controla cuando hay que cargar un nuevo nivel y cuando no
	 * al empezar el programa sera true para que carge el primer nivel
	 */
	public static boolean nuevonivel=true;

	/*
	 * Boolean que define cuando se esta en modo juego, esto quiere decir que se esta jugando
	 */
	public static boolean jugar=true;

	/*
	 * Tiempo de juego controlado por el cronometro
	 */
	public static int minutos=0, segundos=0, horas=0;

	/*
	 * Si queremos que se juege en modo demostracion este boolean estara activado
	 */
	public static boolean mododemo=false;

	/*
	 * Esta activo cuando el menu tenga que mostrarse
	 */
	public static boolean modomenu=true;

	/*
	 * Guarda el nombre del jugador por defecto ponemos demo por si queremos que juege
	 * la maquina sola.
	 */
	public String nombre="demo";

	/*
	 * Nivel en el que se inicia el juego
	 */
	public int nivelinicial=1;
	
	/*
	 * Nivel en el que estamos, lo inicializamos con nivel inicial para cuando empieze el juego
	 */
	public int nivel=nivelinicial;

	/*
	 * Ultimo nivel, este numero de nivel tambien se jugara, si por ejemplo es diez se jugara
	 * hasta el nivel 10 este incluido.
	 */
	public int nivelMaximo=10;
	
	/*
	 * Determina el nivel de vidas cuando empieza la partida
	 */
	public static int vidasinicial=3;
	
	/*
	 * Guarda el numero de vidas actual, las inicializamos como vidas inicial para cuando
	 * empieze la partida 
	 */
	public static int vidas = vidasinicial;

	/*
	 * Guarda los puntos que llevamos, por defecto sera 0 para comenzar la partida
	 */
	public static int marcador=0;

	/*
	 * Contador que nos sirve para comenzar el cronometro al click
	 */
	public int contador = 1;
	
	/*
	 * Creamos un arraylist donde guardaremos el inventario de las bolas en pantalla.
	 */
	public static ArrayList<Bola> bolas;

	/*
	 * Lista donde almacenaremos los disparos que se estan mostrando en la pantalla.
	 */
	public static ArrayList<Disparo> disparos;

	/*
	 * Creamos la barra del jugador
	 */
	public static Barra jugador;

	/*
	 * Array donde almacenaremos todos los ladrillos que queremos visualizar
	 * 
	 *  Los arrays de dos dimensiones son del tipo: [columna(j)][fila(i)].
	 */
	public static Ladrillo[][] muro= new Ladrillo[10][10];
	
	/*
	 * Array donde almacenaremos todos los premios que estan almacenados en los ladrillos
	 * 
	 * es del mismo tamanyo que el array de ladrillos.
	 * 
	 */
	public static Bonus[][] premios= new Bonus[10][10];
	

	/*
	 * Musica de fondo del juego
	 */
	public static Toca musica;
	
	/*
	 * Se encarga de los efectos del juego
	 * 
	 * De momento en deshuso
	 */
	public static Toca efecto;

	/**
	 * Constructor de la clase Arkanoid
	 * Nos crea la ventana y la zona de juego y detecta los movimientos de raton y las
	 * pulsaciones del teclado
	 *
	 */
	
	public Arkanoid(){
		/*
		 * Creamos el sprite de las imagenes
		 */
		spriteCache = new SpriteCache();

		JFrame ventana = new JFrame("Arkanoid | Autores: Gorka Revilla y Fernando Lozano | UPV/EHU - 08.09");  //Creamos la ventana
		ventana.setResizable(false);  //No se puede modificar el tamao
		ventana.setSize(Stage.WIDTH, Stage.HEIGHT); //Definimos el tamao de la pantalla
		ventana.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE); //Para que se cierre bien
		ventana.setLocation(PosX,PosY); //Lo colocamos en la posicion incial
		ventana.setVisible(true);  //La ponemos visible

		/*
		 * Creamos el objeto cronometro y lo colocamos tiene que ser un nuevo hilo para que el programa
		 * no se pare.
		 */

		crono = new Thread(this);  //Creamos el objeto

		/*
		 * Creamos un panel donde pintaremos todo desde los marcadores hasta los objetos,
		 * tanto la barra, bonus, bolas etc es un panel de Canvas
		 */
		
		JPanel panel = (JPanel)ventana.getContentPane(); //Creamos el panel donde pintaremos
		setBounds(0,0,Stage.WIDTH,Stage.HEIGHT); //Lo definimos el tamao en toda la ventana
		panel.setPreferredSize(new Dimension(Stage.WIDTH,Stage.HEIGHT)); 
		panel.setLayout(null); 
		panel.add(this);  //Lo aadimos

		/*
		 * Se llama al mtodo createBufferStratgy(n) de esa ventana, pasando como 
		 * parmetro el nmero de buffers que queremos tener
		 */
		createBufferStrategy(2);

		/*
		 * Utilizamos el mtodo getBufferStrategy() de la ventana para obtener un objeto 
		 * de tipo BufferStrategy, que representa bsicamente una ristra de buffers de
		 * memoria - tantos como fue el parmetro que pasamos a createBufferStrategy()
		 */
		strategy = getBufferStrategy();

		/*
		 * Anyadimos para que lea las teclas que pulsamos
		 */
		addKeyListener(this); //le decimos que escuche el evento del teclado
		addMouseMotionListener(this); //le decimos que escuche el evento de mover el mouse
		addMouseListener(this); //le decimos que escuche los click del mouse

		requestFocus();

	}

	/**
	 * Arrancamos todas las cosas necesarias en el juego:
	 * -Barra.
	 * -Bola.
	 * Y ademas de ello creamos sin arrancar los disparos para el bonus
	 *
	 */
	public void ArrancarMundo() {

		/*
		 * Creamos la Barra, la posicionamos y la ponemos de tamayo normal
		 */
		jugador = new Barra(this);
		jugador.setX(200);
		jugador.setY(472);
		jugador.cambiartamano(2);


		/*
		 * Creamos la lista de bolas
		 */
		bolas = new ArrayList<Bola>();

		/*
		 * Creamos lalista de disparos
		 */
		disparos = new ArrayList<Disparo>();
		
		
		/*
		 * Creamos la pelota y la anyadimos al inventario de pelotas
		 * Debera de empezar parada la pelota (pegada a la barra).
		 * Lanzamos a .stop() para que la situe con la barra.
		 */
		Bola pelota = new Bola(this);
		pelota.parada=true;
		bolas.add(pelota);
		pelota.stop();
		

		/*
		 * Leemos el nivel y lo creamos, se encarga de leer el archivo de entrada y colocar los ladrillos
		 */
		LeerNivel(nivel);

		/*
		 * En caso de que estemos en modo demo, arrancamos el juego
		 */
		if(mododemo){
			inicio=true;
			jugador.start();
		}

	}



	/**
	 * Actualiza el mundo haciendo que cada objeto haga la accion que tenga que hacer,
	 * asi cada vez que se llame a este metodo todos los objetos que tenian definidos una
	 * accin, la harn.
	 */
	
	public void actualizarMundo(){
		/*
		 * Actualizamos el estado de las bolas
		 */
		for (int i = 0; i < bolas.size(); i++) {
			Bola pelota = bolas.get(i);
			/*
			 * Si sobrepasa el limite de la zona de juego por abajo es que hay que destruir la bola
			 */
			if(pelota.y >= FinY){
				bolas.remove(i);
				/*
				 * Si nos quedamos sin bolas en la lista, perdemos una vida y reiniciamos
				 * tanto la barra como las pelotas. (Sin habilidades)
				 */
				if(bolas.size()==0){
					vidas--;
					jugador.setModoDisparo(false);
					jugador.cambiartamano(2);
					Bola pelotanueva = new Bola(this);
					pelotanueva.parada=true;
					pelotanueva.stop();
					bolas.add(pelotanueva);
				}
				/*
				 * Si los efectos estan activados en el menu de opciones, y perdemos una vida,
				 * que ejecute un sonido. "explode3.wav"
				 */
				if(Menu.efectosactivos){
					new Toca("explode3.wav").start();
				}
				
				/*
				 * Si hemos perdido todas las vidas, y ademas no estamos en el modo demostracion, imprimimos 
				 * por medio de un dialog, que se acabo el juego, y finalmente acabamos el juego, llamando al 
				 * metodo finJuego();
				 */
				if(vidas==0){
					if(!mododemo){
						dialog.println("GAME OVER, vuelve a intentarlo");
						menu.println("GAME OVER");
					}
					finJuego();
				}


			}

			if(inicio==true){ //Cuando hagamos click empezamos
				if(contador==1){ //Solo empieza una vez el cronometro
					crono.start(); //Empezamos el cronometro
					contador--;
				}

				
				if(pelota.parada){
					pelota.stop();
				}else{
					pelota.start();
				}
			}
		}


		jugador.start(); //Actualizamos la tabla del jugador

		/*
		 * Actualizamos el estado de los ladrillos, recorriendo todo el array del mismo
		 */
		for (int i=0;i<10;++i){ //recorremos las filas
			for (int j=0;j<10;++j){ //recorremos las columnas
				Ladrillo unladrillo=muro[j][i]; //creamos un objeto llamado unladrillo
				if(unladrillo!=null) unladrillo.start();//comprobamos como estan
			}
		}
		
		/*
		 * Actualizamos el estado de los bonus, recorriendo todo el array del mismo
		 */
		for (int i=0;i<10;++i){ //recorremos las filas
			for (int j=0;j<10;++j){//recorremos las columnas
				Bonus premio=premios[j][i]; //creamos un objeto llamado premio
				if(premio!=null) premio.start(j,i); //comprobamos como estan
			}
		}

		/*
		 * Actualizamos el estado de los disparos
		 */
		for (int i = 0; i < disparos.size(); i++) { //recorremos el arrayList de disparos
			Disparo disparo = disparos.get(i);//vamos obteniendo todos los disparos
			disparo.start(i); // Y comprobamos como estan y que habilidades pueden surgir a cada disparo
		}

		
		
		/*
		 * En caso de que se haya acabado el nivel lo limpiamos
		 */
		if(nuevonivel){
			finNivel();
		}

	}



	/**
	 * Pintamos toda la pantalla, todo lo que tiene que ver con el aspecto grafico, est en este 
	 * metodo, desde el marcador, fondos, objetos, decoracion ... etc
	 */
	public void paintMundo() 
	{

		/*
		 * Creamos el objeto Graphics2D que lo hemos importado desde el mismo eclipse, para poder pintar 
		 * en el panel, "import java.awt.Graphics2D;"
		 */
		
		Graphics2D g = (Graphics2D)strategy.getDrawGraphics();

		/*
		 * Pintamos todo el fondo de pantalla de color negro, aplicandole en toda la zona en la que queremos que lo pinte
		 * en este caso, como queremos que pinte toda la pantalla, aplicaremos en todo el panel.
		 */
		g.setColor(Color.black); //Escogemos el color
		g.fill3DRect(0,0,Stage.WIDTH,Stage.HEIGHT,true); //La zona a pintar

		/*
		 * Creamos la pantalla de juego, para ello creamos un objeto Actor con el nombre de 
		 * fondo y escogemos en donde queremos que lo pinte, y con que imagen y lo pintamos
		 */
		Actor fondo = new Actor(this); //Creamos el objeto
		fondo.setX(InicX); //En que lugar lugar del eje X ponemos el objeto
		fondo.setY(InicY); //En que lugar lugar del eje Y ponemos el objeto
		fondo.setNombreSprite(imagenfondo); //Que imagen le ponemos, la obtendremos del archivo de entrada
		fondo.paint(g); //La pintamos

		/*                             
		 * Pintamos bordes izquierdo del panel, para ello hacemos exactamente igual que como hemos hecho anteriormente
		 * con el fondo
		 */
		Marco bordeIzquierdo = new Marco(this);
		bordeIzquierdo.setNombreSprite("BarraIzquierda.jpg");
		bordeIzquierdo.setX(InicX-bordeIzquierdo.ancho);
		bordeIzquierdo.setY(InicY-bordeIzquierdo.ancho);
		bordeIzquierdo.paint(g);

		/*
		 * Pintamos bordes derecho del panel, para ello hacemos exactamente igual como hemos hecho antes, en el otro 
		 * borde
		 */
		Marco bordeDerecho = new Marco(this);
		bordeDerecho.setNombreSprite("BarraDerecha.jpg");
		bordeDerecho.setX(FinX+InicX);
		bordeDerecho.setY(InicY-bordeDerecho.ancho);
		bordeDerecho.paint(g);


		/*
		 * Pintamos bordes arriba, para ello hacemos exactamente igual como hemos hecho antes, en el otro 
		 * borde
		 */
		Marco bordeArriba = new Marco(this);
		bordeArriba.setNombreSprite("BarraArriba.jpg");
		bordeArriba.setX(InicX);
		bordeArriba.setY(InicY-bordeIzquierdo.ancho);
		bordeArriba.paint(g);


		/*
		 * Pintamos fuego, para ello hacemos exactamente igual como hemos hecho antes, en el otro 
		 * borde
		 */
		Marco fuego = new Marco(this);
		fuego.setNombreSprite("fuego.gif");
		fuego.setX(InicX);
		fuego.setY(Stage.HEIGHT - fuego.alto);
		fuego.paint(g);




		/*
		 * Ponemos color de fondo al Marcador, y decimos en que zona del panel lo queremos pintar
		 */
		g.setColor(Color.black);
		g.fill3DRect(InicX, InicY+FinY+margen,FinX,largo,true);


		/*
		 * Creamos el Rectagulo del Marcador y decimos en que zona del panel lo queremos pintar
		 */
		g.setColor(Color.white);
		g.draw3DRect(InicX, InicY+FinY+margen,FinX, largo, true); //Dibujamos el rectangulo

		/*
		 * Mostramos la informacion del jugador, dentro del rectangulo del marcador
		 */
		g.setFont(new Font("Arial", Font.BOLD, 15)); //Cambiamos el tipo de letra a "Arial", y lo ponemos en negrita con un tamao de 15
		g.drawString("Jugador: " + nombre,primeracolumna, primeralinea); //Mostramos el nombre del Jugador
		g.drawString("Marcador: " + marcador,primeracolumna, segundalinea); //Mostramos el Marcador

		/*
		 * Mostramos el tiempo de juego, dentro del rectangulo del marcador en el sitio deseado
		 */
		g.drawString("Tiempo jugado : " + horas+":"+minutos+":"+segundos,segundacolumna, primeralinea); //Mostramos el nombre del Jugador

		/*
		 * Mostramos numero de Vidas, dentro del rectangulo del marcador en el sitio deseado
		 */

		g.drawString("Vidas: " + vidas,terceracolumna, primeralinea);

		/*
		 * Mostramos el nivel actual, dentro del rectangulo del marcador en el sitio deseado
		 */
		g.drawString("Nivel: "+nivel, terceracolumna, segundalinea);

		/*
		 * Este es el contador que cuenta la cantidad de fps cuando muestre --- es que no hay actualizacion o
		 * que la actualizacion es muy rapida.
		 */
		if (tiempoDeDuracion > 0){
			g.drawString(String.valueOf(1000/tiempoDeDuracion)+" fps",segundacolumna,segundalinea);
		}else{
			g.drawString("--- fps",segundacolumna,segundalinea); //Lo pintamos dentro del rectangulo en el sitio deseado
		}

		
		/*
		 * Ahora pintamos a todos los objetos de nuevo, despues de actualizar todas sus posiciones, y estados lo pintamos con todo actualizado
		 * Primero las bolas, recorriendo su arrayList entero y volviendolo a pintar
		 */
		for (int i = 0; i < bolas.size(); i++) {
			Bola pelota = bolas.get(i);
			pelota.paint(g);
		}

		/*
		 * Pintamos los ladrillos,  para ello recorremos todo su Array como hemos hecho anteriormente al actualizar sus posiciones
		 * Puede que algunas esten vacias y por lo tanto lo comprobamos para
		 * esas no pintarlas
		 */
		for (int i=0;i<10;++i){
			for (int j=0;j<10;++j){
				Ladrillo unladrillo=muro[j][i];
				if(unladrillo!=null) unladrillo.paint(g);
			}
		}
		
		/*
		 * Pintamos los premios, para ello recorremos todo su Array como hemos hecho anteriormente al actualizar sus posiciones
		 * Puede que algunas esten vacias y por lo tanto lo comprobamos para
		 * esas no pintarlas
		 */
		for (int i=0;i<10;++i){
			for (int j=0;j<10;++j){
				Bonus premio=premios[j][i];
				if(premio!=null){
					if(premio.activo){
						premio.paint(g);
					}
				}
			}
		}
		
		
		/*
		 * Pintamos disparos,  para ello recorremos todo su ArrayList como hemos hecho anteriormente al actualizar sus posiciones
		 */
		for (int i = 0; i < disparos.size(); i++) {
			Disparo disparo = disparos.get(i);
			disparo.paint(g);
		}
		
		
		/*
		 * Pintamos la tabla del jugador, con sus posiciones y estados actualizados
		 */
		jugador.paint(g);
		/*
		 * Si estamos en modo disparo pintamos tambien el pitorro que dispara los misiles.
		 * Lo pintaremos justo encima de la barra leyendo los valores de la barra.
		 */
		if(jugador.mododisparo){
			Marco disparador = new Marco(this);
			disparador.setNombreSprite("disparador.gif");
			disparador.setX(jugador.getX()+(jugador.getAncho()/2)-(disparador.getAncho()/2));
			disparador.setY(jugador.getY()-disparador.getAlto());
			disparador.paint(g);
		}
		
		
		/*
		 * En todo momento hay un "buffer de dibujo" y un "buffer visible". Para poder pintar 
		 * encima del buffer de dibujo, llamamos al mtodo getDrawGraphics() del objeto 
		 * BufferStrategy obtenido anteriormente.
		 */
		strategy.show();

	}




	/**
	 * Se encarga de leer el archivo de entrada con la informacion sobre el nivel nuevo
	 * 
	 * @param nivel
	 */
	public void LeerNivel(int nivel){

		/*
		 * Miramos que nivel nos piden y leemos su archivo correspondiente lo pasamos
		 * el numero del nivel a string y lo guardamos en INPUT_PATH que es el nombre
		 * del archivo.
		 */
		String nombrenivel=String.valueOf(nivel);
		INPUT_PATH="niveles/"+nombrenivel;

		/*
		 * Creamos el input para leer el archivo
		 */
		try {

			System.setIn(new java.io.FileInputStream(new java.io.File(INPUT_PATH)));
		} catch (java.io.FileNotFoundException e) {

			e.printStackTrace(); 
			System.exit(0);     
		}
		java.util.Scanner input = new java.util.Scanner(System.in);



		/*
		 * Leemos todo el archivo de entrada, damos por hecho que los niveles estan
		 * correctamente escritos. Usaremos dos for utilizando las variables i y j para ir guardando
		 * dentro del array Muro todos los ladrillos que crearemos.
		 * 
		 * En la ultima linea del archivo debe de estar espeficificado el nombre del archivo de
		 * la imagen de fondo del nivel. Por ejemplo:
		 * 
		 * playa.gif
		 */
		
		/*
		 * Creamos un string texto donde almacenaremos temporalmente lo que
		 * leamos como texto
		 */
		String texto="";
		
		/*
		 * Recorremos todo el array
		 */
		for(int i=0;i<10;++i){
			for(int j=0;j<10;++j){
				/*
				 * Primero leemos un Int
				 */
				if(input.hasNextInt()){
					/*
					 * Ahora haremos las comprobaciones para saber de que color
					 * es el ladrillo a insertar:
					 * 0-> No se introduce ladrillo
					 * 1-> Ladrillo Gris
					 * 2-> Ladrillo Azul
					 * 3-> Ladrillo Amarillo
					 * 4-> Ladrillo Morado
					 * 5-> Ladrillo Naranja
					 * 6-> Ladrillo Rojo
					 * 7-> Ladrillo Verde
					 */
					int numladrillo=input.nextInt();
					if(numladrillo>=1 && numladrillo<=7){
						Ladrillo unladrillo = new Ladrillo(this); //Creamos el objeto unladrillo para meterle el bonus
						muro[j][i]=unladrillo; //Lo obtenemos
						unladrillo.setX(unladrillo.convertirX(j)); // Convierte el valor i del array en la posicion X de la pantalla
						unladrillo.setY(unladrillo.convertirY(i)); // Convierte el valor i del array en la posicion Y de la pantalla
						unladrillo.setColor(numladrillo); //Ponemos el color al ladrillo depende del archivo de entrada
					}
				}
				
				/*
				 * Ahora puede ser que nos venga o un char o un String, el char querra decir que hay
				 * un bonus en esa casilla, en cambio si es un string quiere decir que
				 * es el nombre del fondo
				 */
				if(!input.hasNextInt() && input.hasNext()){
					/*
					 * Lo almacenamos en la variable temporal y vemos si es un char o si es un string.
					 */
					texto=input.next();
					if(texto.length()==1){
						char tipobonus=texto.charAt(0);
						if(tipobonus>='b' && tipobonus<='z'){ // Si esta dentro del rango de los bonus, es un bonus
							Bonus premio = new Bonus(this); //Creamos el objeto premio
							premios[j][i]=premio; // En que ladrillo hay premio
							premio.setTipo(tipobonus); // Que tipo de premio
							premio.setX(premio.convertirX(j,i)); //En que coordenada X lo aadimos
							premio.setY(premio.convertirY(j,i)); //En que coordenada Y lo aadimos
						}
					}
					else imagenfondo="fondos/"+texto; // y Obtenemos la imagen de fondo
				}
			}
		}
		/*
		 * En ultimo caso puede que todabia nos quede por cargar la imagen, asi que la cargamos
		 */
		if(input.hasNext()){
			imagenfondo="fondos/"+input.next();
		}


		nuevonivel=false; //Lo activamos para que no carga ningun nivel nuevo

	}



	/**
	 * Cuando se acabe el nivel realizara las siguientes acciones, entre ellas
	 * avanzar en nivel, borrar el nivel, y arrancar otra vez el nivel nuevo
	 */
	
	public void finNivel(){
		nuevonivel=false; 
		if(nivel<nivelMaximo){//Mientras no hayamos llegado el ultimo nivel
			if(Menu.efectosactivos){ //Si el audio de los efectos esta activo
			new Toca("goodgame.wav").start();} //Reproducimos el archivo de audio goodgame.wav
			if(!mododemo){ //Si no estamos en el modo demo
				dialog.println("Has superado el nivel " +nivel +" en " + horas+":"+minutos+":"+segundos  );} //Imprimimos mediante un dialog que nivel ha completado y en cuanto tiempo
			++nivel; //Avanzamos de nivel
			borrarNivel();//Borramos todo el nivel
			ArrancarMundo();//Y arrancamos el siguiente
		}else jugar=false; // Si hemos llegado al ultimo nivel ya no se juega mas.

	}

	/**
	 * Borra todo lo que hay en el nivel, ladrillos, bonus, disparos, bolas y
	 * la barra del jugador, para en cualquier momento poder llamar a este metodo
	 * y poder volver a comenzar el juego
	 */
	public void borrarNivel(){
		/*
		 * Borramos todos los ladrillos del array muro
		 */
		for(int i=0;i<10;++i){
			for(int j=0;j<10;++j){
				muro[j][i]=null;
			}
		}
		
		/*
		 * Borramos todos los bonus del array premios
		 */
		for(int i=0;i<10;++i){
			for(int j=0;j<10;++j){
				premios[j][i]=null;
			}
		}
		
		/*
		 * Borramos todos los disparos
		 */
		disparos.clear();
		

		
		/*
		 * Borramos todas las bolas
		 */
		bolas.clear();

		/*
		 * Borramos al jugador
		 */
		jugador.setModoDisparo(false);
		jugador=null;
		
	}


	/**
	 * Al morir o al pasarse el juego se tendra que realizar esto, cuando se llame a este
	 * metodo significara que o esta muerto o se ha pasado el juego.
	 */
	public void finJuego(){
		/*
		 * Cuando llegue aqui es que a acabado la partida
		 */
		if(!mododemo){
			dialog.println("Jugador: "+nombre+" || Marcador: "+marcador + " || Tiempo jugado: " + horas+":"+minutos+":"+segundos);
		}

		/*
		 * Escribimos los records en caso de que haya que escribirlos y los mostramos
		 */
		if(!mododemo){
			Records resultado = new Records(nombre,marcador,horas,minutos,segundos); //Creamos el objeto de los records con los parametros con el que ha acabado el juego o ha muerto
			resultado.start(); // Realiza la comprobacion de los records y los almacena en caso de que sea record.
			modomenu=true; // Lo llevamos al menu
			menu.modogeneral=false; // Y no lo llevamos al menu general
			menu.modorecords=true; // Lo llevamos a los records
		}

		borrarNivel(); // Borramos todo el nivel 
		nivel=nivelinicial; // Ponemos de nuevo el nivel inicial
		Arkanoid.jugar=true; // Que pueda jugar de nuevo
		vidas=vidasinicial; // Las vidas iniciales de nuevo
		segundos=0; //Otra vez ponemos a 0 los contadores del cronometro
		minutos=0; //Otra vez ponemos a 0 los contadores del cronometro
		horas=0; //Otra vez ponemos a 0 los contadores del cronometro
		marcador=0; //Otra vez ponemos a 0 los contadores del marcador
		inicio=false; //Que no empiece a jugar hasta que se le clickee
		ArrancarMundo(); // Y arrancamos el mundo de nuevo
	}

	/**
	 * Se encarga de cambiar el valor de la variable nivelMaximo, en funcion de lo que
	 * ponga en el archivo, en caso de que no haya nada escrito en el archivo va a poner
	 * 10 de valor por defecto y habra que tener cuidad con los niveles. Lee desde el archivo
	 * datos/maximonivel.txt .
	 * 
	 * 
	 */
	public void LeerMaximoNivel(){
		INPUT_PATH="datos/maximonivel.txt";

		/*
		 * Creamos el input para leer el archivo
		 */
		try {

			System.setIn(new java.io.FileInputStream(new java.io.File(INPUT_PATH)));
		} catch (java.io.FileNotFoundException e) {

			e.printStackTrace(); 
			System.exit(0);     
		}
		
		java.util.Scanner input = new java.util.Scanner(System.in);
		
		/*
		 * Leemos desde el archivo y actualizamos el valor con el integer
		 * que este escrito en el archivo, en caso de que no haya ningun integer
		 * ponemos 10 de nivelMaximo por defecto.
		 */
		if(input.hasNextInt()){
			nivelMaximo=input.nextInt();
		}
		else nivelMaximo=10;
		
		
	}
	
	
	
	/**
	 * Metodo que arranca la maquina, uno de los metodos ms importantes, ya que el bucle while hace
	 * que constantemente haga todo de nuevo que lo repita indefinidamente hasta que nos salgamos del juego.
	 *
	 */
	public void start(){

		menu = new Menu(this); //Creamos el menu
		musica = new Toca("florida.wav");
		if(Menu.musicaactiva){
			musica.start(); //Aplicamos sonido general
		}
		
		LeerMaximoNivel(); //Leemos de archivo el nivel maximo


		tiempoDeDuracion=1000;  //1000=1 milisegundo, que es en lo que se miden los fps

		/*
		 * Arrancamos el mundo
		 * Arrancamos todas las cosas necesarias en el juego: -Barra. -Bola. 
		 * Y ademas de ello creamos sin arrancar los disparos para el bonus
		 */
		ArrancarMundo();

		/*
		 * Bucle principal del juego que siempre estara activo hasta que se cierre el juego
		 */
		while(true){
			/*
			 * Guardamos el momento en el que empezamos 
			 */
			long empezarCronometro = System.currentTimeMillis();

			/*
			 * Esta es la parte importante del programa, es la que hace que se este dibujando todo
			 * el rato la pantalla
			 */

			if(!modomenu)
			{
				actualizarMundo(); 	//Actualizamos el mundo
				paintMundo(); 		//Pintamos el mundo

			}else{
				menu.actualizarMenu(); //Actualizamos el menu
				menu.paintMenu();      //Pintamos el menu
			}

			/*
			 * Hacemos el calculo del tiempo que hemos tardado en refrescar la pantalla
			 */
			tiempoDeDuracion = System.currentTimeMillis()-empezarCronometro;

			/*
			 * Hacemos una pausa segun la velocidad a la que queremos que vaya
			 */
			try { 
				Thread.sleep(SPEED);
			} catch (InterruptedException e) {}

			/*
			 * Si no estamos en jugar que acabe el juego
			 */
			if(!jugar){
				finJuego(); //Al morir o al pasarse el juego se tendra que realizar esto, 
							//cuando se llame a este metodo significara que o esta muerto o se ha pasado el juego.
			}
		}



	}

	/**
	 * El main principal del programa que directamente nos arranca el juego
	 */
	public static void main(String[] args) {
		Arkanoid juego = new Arkanoid();
		juego.start();
	}

	/**
	 * Se encaga de realizar los dibujos de las imagenes, 
	 * usa un HasMap para optimizar la carga de imagenes y solo cargarlas una vez.
	 * @return spriteCache;
	 */
	public SpriteCache getSpriteCache() {
		return spriteCache;
	}



	/**
	 * Este metodo sirve para obtener informacion de todo lo que tiene ver cuando tenemos alguna tecla pulsada
	 *	@param e
	 */
	public void keyPressed(KeyEvent e) {
		if(modomenu){ // Si estamos en el modo menu
			menu.keyPressed(e); // que le pase lo que escucha del teclado 
		}else  //Si no estamos en el modo menu que se lo pase a la barra
			jugador.keyPressed(e);

	}
	
	/**
	 * Este metodo sirve para obtener informacion de todo lo que tiene ver cuando hemos pulsado alguna tecla
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		//jugador.keyReleased(e); // que le pase lo que escucha del teclado a la barra

	}

	/**
	 * Este metodo sirve para obtener informacion de todo lo que tiene ver cuando hemos tecleado una tecla 
	 * @param e
	 */
	public void keyTyped(KeyEvent e) {

	}


	/**
	 * Este metodo sirve para obtener informacion de todo lo que tiene ver cuando hemos clickeado en algun sitio
	 * @param arg0
	 */
	
	public void mouseClicked(MouseEvent arg0) {

		/*
		 * Si estamos en modo menu le pasamos los datos al menu.
		 */
		if(modomenu){
			menu.mouseClicked(arg0);
		}else{
			jugador.mouseClicked(arg0);
		}
	}


	/**
	 * Los siguientes cinco metodos no son utilizados pero son mas tipos de propiedades
	 * del raton y del teclado, pero como he dicho no hemos utilizado
	 * @param arg0 
	 */
	public void mouseEntered(MouseEvent arg0) {

	}


	public void mouseExited(MouseEvent arg0) {

	}


	public void mousePressed(MouseEvent arg0) {

	}


	public void mouseReleased(MouseEvent arg0) {

	}


	public void mouseDragged(MouseEvent arg0) {

	}

	/**
	 * Este metodo sirve para obtener informacion de todo lo que tiene ver cuando hemos movido el raton 
	 * @param arg0
	 */
	public void mouseMoved(MouseEvent arg0) {
		/*
		 * Lo rodeamos con una try catch porque puede que falle.
		 */
		try {
			/*
			 * Cuando este en modo menu se lo pasamos al menu
			 */
			if(modomenu){
				menu.mouseMoved(arg0);	//Le pasamos la informacion a la barra
			}

			/*
			 * En el caso de que este en modo demo se lo pasamos al jugador.
			 */
			if(!mododemo){
				jugador.mouseMoved(arg0); 	//Le pasamos la informacion a la barra
			}


		} catch (RuntimeException e) {
			//Si hay error no sacamos nada por pantalla y que continue el programa
		}

	}


	/**
	 * Metodo run utilizado para el cronometro, para que funcione correctamente
	 */
	@SuppressWarnings("static-access")
	public void run(){ 
		try {
			for(;;) { //Que comience a contar todo el rato +1; en cada interaccion
				if(segundos==59) { // Si llegamos el minuto
					segundos=0; minutos++;} // Aadimos un minuto mas
				if(minutos==59) { minutos=0; horas++;} // Si llegamos a la hora
				segundos++;//Aadimos una hora mas
				tiempo.setText(horas+":"+minutos+":"+segundos); //El tiempo a imprimir
				crono.sleep(1000); }
		}

		catch (InterruptedException e) { System.out.println(e.getMessage()); }
	}


}