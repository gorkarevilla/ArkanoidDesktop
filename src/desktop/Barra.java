package desktop;

/* 
 * � Copyright 2009 Gorka Revilla Fernandez & Fernando Lozano Pajaron
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

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * 
 * La barra es la tabla que manejara el jugador mediante el raton, esta recibe su posicion media
 * el mouselistener. Tambien tiene un modo de juego que es automatico que se llama demo().
 *
 */
public class Barra extends Actor{

	/*
	 * Serial para el extends
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	
	/*
	 * Definimos los modos de la barra, cuando este en cada modo, realiza diferentes cosas.
	 */
	public boolean mododisparo=false; //Tiene la habilidad de poder disparar
	
	/*
	 * Estas son las medidas con respecto a la X de la tabla, es para una medida estandar:
	 * 
	 * 64=16*2+10*2+12
	 * 
	 * En los cambios de tamanyo de la barra habra que cambiar tambien estos valores a proporcion
	 * Como todos son divisibles entre dos no tendremos problemas al hacer la barra la mitad.
	 * 
	 * Son todo medidas relativas, las absolutas estan mas abajo.
	 */
	public int borde=16; //Ancho del borde de la barra
	public int zonanormal=10; //Ancho desde el borde de la barra hasta la zona central
	public int zonacentro=12; //Ancho de la zona central
	
	/*
	 * Vamos a definir 7 puntos de la barra que nos son muy utiles:
	 * 
	 * 1  2  3   4  5  6
	 *  <|====xxx====|>
	 * 7
	 * 
	 * 1-> Donde comienza la barra con borde, Es el punto (X) asi que no lo definimos
	 * 2-> Donde acaba el borde (X+borde)
	 * 3-> Donde comienza la zona central de la tabla (X+borde+zonanormal) 
	 * 4-> Donde acaba la zona centra de la tabla (X+borde+zonanormal+zonacentro)
	 * 5-> Donde empieza el segundo borde: (X+ancho-borde)
	 * 6-> Donde acaba la barra (Con borde): (X+ancho)
	 * 7-> Es la parte de abajo de la barra: (Y+alto)
	 * 
	 * SON todo MEDIDAS ABSOLUTAS!!
	 */
	public int finprimerborde; //2
	public int comienzocentro; //3
	public int fincentro; //4	
	public int comienzosegundoborde; //5
	public int finbarra; //6
	public int parteabajo; //7

	/*
	 * Este es el error que comente la barra cuando juega en modo demo.
	 */
	private int error=0;
	/*
	 * Con esto marca si el error es positivo o negativo
	 */
	private int positivo=1;
	/*
	 * Este boolean estara en true cuando haya que cambiarle la posicion a la barra para
	 * que rebote con otra parte distinta.
	 */
	private boolean cambio=true;
	/*
	 * En estas variables almacenaremos cuando se realizo el ultimo rebote con la barra, sirve
	 * para que la demo realize el restore automatico en caso de bloqueo de la pelota.
	 */
	private int ultimoReboteHora=0,ultimoReboteMinuto=0,ultimoReboteSegundo=0;
	
	
	/**
	 * Constructor de la barra. Hay que especificar donde queremos anyadilar
	 * 
	 * @param Stage
	 */
	public Barra(Stage stage){
		super(stage);
		setNombreSprite("barrasinsombra.gif");
		DeltaX=0;
	}

	/**
	 * Comienza la barra a funcionar con el siguiente metodo
	 */
	public void start(){
		/*
		 * Actualizamos todos los valores de las variables de las zona de la barra.
		 */
		finprimerborde= x+this.borde; //2
		comienzocentro= x+this.borde+this.zonanormal; //3
		fincentro= x+this.borde+this.zonanormal+this.zonacentro; //4
		comienzosegundoborde=x+this.ancho-this.borde; //5
		finbarra= x+this.ancho; //6
		parteabajo= y+this.alto; //7
		if(Arkanoid.mododemo){
			demo(); //Activamos la demo
		}
		setX(getX()+DeltaX);
	}


	/**
	 * Juega la barra sola, es una demostracion de como juega la bola
	 *
	 */
	public void demo(){
		/*
		 * Creamos la pelota cogiendola de su arraylist de pelotas
		 */
		Bola pelota;
		for(int i=0;i<Arkanoid.bolas.size();++i){
			pelota=Arkanoid.bolas.get(i);

			/*
			 * Si la pelota no esta lanzada la lanzamos
			 */
			if(pelota.parada){
				pelota.parada=false;
			}
			
			/*
			 * El error que comete es cuando los segundos sean multiplos de cero y no sea igual que 0.
			 * Esto ultimo es para evitar que al comenzar el juego la barra se mueva mucho.
			 * 
			 * Realiza cambios cada 5 segundos.
			 */
			if(Arkanoid.segundos%5==0 && Arkanoid.segundos!=0){
				if(cambio){
					error=(int)(Math.random()*(((this.getAncho())/2)+(this.getAncho()/8)) );
					/*
					 * Cambiamos tambien si el error es a la izquierda o a la derecha
					 */
					if(Math.random()>0.5){ //aleatoriamente calculamos el error
						positivo=-1;
					}else positivo=1;
					/*
					 * Marcamos cambio como falso para que no lo vuelva a realizar
					 */
					cambio=false;
				}
			} 
			if(Arkanoid.segundos%5==1 && Arkanoid.segundos!=0){
				/*
				 * Marcamos para que haya un momento que no sea en los multiplos de 5
				 * que modifique el booleano para que se pueda volver a cambiar de posicion
				 * el rebote de la demo.
				 */
				cambio=true;
			}

			/*
			 * Hacemos que la barra le siga a la pelota con un margen de error, de esta forma puede ser
			 * que se le escape la pelota y falle o y que le de con diferentes partes de la tabla, para asi
			 * tener un rebote diferente y no ser tan monotono.
			 */


			//this.setX(pelota.x-(ancho/2)+(error*positivo));
			this.setX(pelota.getX()-(getAncho()/2)+(pelota.getAncho()/2)+(error*positivo));
		}
		
		/*
		 * Para que dispare siempre que tenga el mododisparo
		 */
		if(mododisparo){
			mouseClicked(null);
		}
		
		/*
		 * En caso de que se quede bloqueada la pelota mas de 30segundos reiniciamos la pelota
		 */
		if(diferenciaRebote()>=30){
			restaurar();
		}
	}


	
	/**
	 * Se encarga de disparar un misil, tiene que estar en modo disparo para poder disparar
	 * Solo se pueden disparar 5 disparos.
	 */
	public void disparar(){
		if(Arkanoid.disparos.size()<5){ // Si no ha llegado a los 5 disparos
			Disparo disparo = new Disparo(stage); //Creamos el disparo
			disparo.setY(Arkanoid.jugador.getY()); //Obtenemos la y del jugador para que lo dispare desde la barra
			disparo.setX(Arkanoid.jugador.getX() + (Arkanoid.jugador.getAncho()/2) - (disparo.getAncho()/2)); // Y obtenemos la X del jugador para que lo dispare desde el medio de la barra
			Arkanoid.disparos.add(disparo); //A�adimos al arraylist de disparos el disparo
		}
		
		
	}
	
	

	/**
	 * Cambia el tamanyo de la barra.
	 * 
	 * Tamanyos de la barra:
	 * 
	 * 1-> Grande
	 * 2-> Normal
	 * 4-> Pequenyo  
	 *
	 * @param tamano
	 */
	public void cambiartamano(int tamano){
		/*
		 * Cambiamos tanto la imagen que se muestra como las medidas de sus zonas.
		 */
		if(tamano==1){
			setNombreSprite("barrapeque.gif");
			borde=8;
			zonanormal=5;
			zonacentro=6;
		}else if(tamano==2){
			setNombreSprite("barrasinsombra.gif");
			borde=16;
			zonanormal=10;
			zonacentro=12;
		}else if(tamano==4){
			setNombreSprite("barragrande.gif");
			borde=32;
			zonanormal=20;
			zonacentro=24;
		}else System.out.println("Tamanyo de la imagen no especificado");
	}


	/**
	 * Nos cambia la imagen de la barra dependiendo si estamos en mododisparo o no.
	 * @param b
	 */
	public void setModoDisparo(boolean b) {
		mododisparo=b;
		if(mododisparo){
			Arkanoid.jugador.setNombreSprite("barrasinsombra.gif");
		}
	}
	
	/**
	 * Almacena en que momento se realizo el ultimo rebote con la barra.
	 * 
	 * @param hora
	 * @param minuto
	 * @param segundo
	 */
	public void setUltimoRebote(int hora, int minuto, int segundo){
		ultimoReboteHora=hora;
		ultimoReboteMinuto=minuto;
		ultimoReboteSegundo=segundo;
	}
	
	/**
	 * Devuelve cuantos segundos hace que se realizo el ultimo rebote con la barra. Se le 
	 * pasa por parametro la hora actual de juego.
	 * 
	 * @param hora
	 * @param minuto
	 * @param segundo
	 * @return
	 */
	public int diferenciaRebote(){
		int diferencia=0;
		/*
		 * Realizamos el calculo del tiempo en segundos
		 */
		diferencia=diferencia+((Arkanoid.horas-ultimoReboteHora)*60*60);
		diferencia=diferencia+((Arkanoid.minutos-ultimoReboteMinuto)*60);
		diferencia=diferencia+(Arkanoid.segundos-ultimoReboteSegundo);
		
		/*
		 * Si el calculo es positivo devolvemos el tiempo, en caso contrario devolvemos 0
		 */
		if(diferencia>0){
			return diferencia;
		}else return 0;
		
	}
	
	/**
	 * Se encarga de restaurar la partida, esto es:
	 * -Quita una vida
	 * -Borra todas las pelotas
	 * -Crea una pelota en modo parada para volver a empezar
	 * 
	 * Se usara con la tecla R y sirve para cuando todas las pelotas se quedan bloqueadas o
	 * quieres volver a realizar un lanzamiento.
	 *
	 */
	public void restaurar(){
		if(Arkanoid.vidas>1){
			Arkanoid.bolas.clear();
			Arkanoid.vidas--;
			setModoDisparo(false);
			cambiartamano(2);
			Bola pelotanueva = new Bola(stage);
			pelotanueva.parada=true;
			pelotanueva.stop();
			Arkanoid.bolas.add(pelotanueva);
			setUltimoRebote(Arkanoid.horas,Arkanoid.minutos,Arkanoid.segundos);
		}

	}
	
	/**
	 * Este metodo nos permite recoger informacion de cuando esta una tecla pulsada
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE) { // Si pulsamos el espape
			if(Arkanoid.modomenu){ // Y ademas estamos en modo menu
				Arkanoid.modomenu=false; // Saldra del menu
			}else Arkanoid.modomenu=true;//Si estamos jugando nos llevara al menu
		}
		else if(e.getKeyCode()==KeyEvent.VK_SPACE && !Arkanoid.mododemo){ // Si pulsamos el espacio
			mouseClicked(null); //Lanzamos la pelota
		}
		else if(e.getKeyCode()==KeyEvent.VK_LEFT && !Arkanoid.mododemo){ // Si pulsamos a la izquierda
			setX(getX()-20); //Obtenemos la coordenada actual X de la barra y le restamos una cantidad para que se mueva hacia la izquierda
			/*
			 * Esta parte es por si todavia no ha empezado la partida y queremos que la pelota siga a la barra
			 * antes de lanzarla.
			 */
			Bola pelota; 
			for(int i=0;i<Arkanoid.bolas.size();++i){
				pelota=Arkanoid.bolas.get(i);
				if(pelota.parada){
					pelota.x=Arkanoid.jugador.getX()+Arkanoid.jugador.getAncho()/2;
				}
			}
		}
		else if(e.getKeyCode()==KeyEvent.VK_RIGHT && !Arkanoid.mododemo){ // Si pulsamos a la derecha
			setX(getX()+20); //Obtenemos la coordenada X de la barra y le a�adimos un numero para que se mueva
			/*
			 * Esta parte es por si todabia no a empezado la partida y queremos que la pelota siga a la barra
			 * antes de lanzarla.
			 */
			Bola pelota;
			for(int i=0;i<Arkanoid.bolas.size();++i){
				pelota=Arkanoid.bolas.get(i);
				if(pelota.parada){
					pelota.x=Arkanoid.jugador.getX()+Arkanoid.jugador.getAncho()/2;
				}
			}
		}
		/*
		 * Si pulsamos la letra R te quita una vida y vuelve a empezar la pelota, es por si se
		 * queda bloqueada la pelota rebotando y nunca baja.
		 */
		else if((e.getKeyCode()==KeyEvent.VK_R && !Arkanoid.mododemo)){
			restaurar();
		}
	}
	
	/**
	 * Este metodo sirve para obtener informacion de todo lo que tiene ver cuando hemos pulsado alguna tecla
	 * 
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		
	}

	/**
	 * El metodo necesario para que la barra se mueva por el raton
	 * 
	 * @param arg0
	 */
	public void mouseMoved(MouseEvent arg0) {

		setX(arg0.getX()-ancho/2); // Con esto situamos la parte central de la barra en donde este el cursor

		/*
		 * Esta parte es por si todavia no ha empezado la partida y queremos que la pelota siga a la barra
		 * antes de lanzarla.
		 */
		Bola pelota;
		for(int i=0;i<Arkanoid.bolas.size();++i){
			pelota=Arkanoid.bolas.get(i);
			if(pelota.parada){
				pelota.x=Arkanoid.jugador.getX()+Arkanoid.jugador.getAncho()/2;
			}
		}
	}


	/**
	 * Metodo que realiza el lanzamiento de la pelota.
	 * 
	 * @param arg0
	 */
	public void mouseClicked(MouseEvent arg0){
		/*
		 * Aqui estaremos en el juego
		 */
		Arkanoid.inicio = true; //Cuando clickeemos empezamos

		/*
		 * Al realizar el click ponemos las pelotas que esten en juego en funcionamiento.
		 */
		for (int i = 0; i < Arkanoid.bolas.size(); i++) {
			Bola pelota = Arkanoid.bolas.get(i);
			if(pelota.parada){
				pelota.parada=false;
			}
		}
		if(mododisparo){ // Si estamos en modoDisparo
			disparar(); // Disparamos
		}
	}

}
