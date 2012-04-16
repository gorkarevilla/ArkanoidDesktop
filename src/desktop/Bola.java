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

/**
 * La bola sera el objeto principal, debera de rebotar con las paredes, con la barra del jugador
 * y con los ladrillos, los ladrillos tambien los debera de destrozar.
 */

public class Bola extends Actor{

	/*
	 * Serial para el extends
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;


	/*
	 * Posicion del Punto Centro de la bola
	 */
	public int centroX=x+(this.ancho/2);
	public int centroY=y+(this.alto/2);

	/*
	 * Definimos la velocidad a la que se mueve la bola:
	 * 1-> Velocidad Lenta.
	 * 2-> Velocidad Normal.
	 * 4-> Velocidad Muy Rapida.
	 * 
	 * De momento NO USAR!
	 */
	public int velocidad=2;

	/*
	 * Booleano que controla cuando la pelota tiene que ser lanzada o cuando esta en juego,
	 * en true es que todavia no ha sido lanzada y permanecera pegada a la barra, en false estara
	 * jugando la pelota
	 */
	public boolean parada = false;

	/*
	 * Almacena si la pelota rebota o no. En caso de que no rebote atravesara los ladrillos de un tiron
	 */
	public boolean rebota = true;

	/**
	 * Constructor de la bola. Hay que especificar donde queremos anyadirla.
	 * 
	 * @param stage
	 */
	public Bola(Stage stage){
		super(stage);
		setNombreSprite("bolablanca.gif"); //Con la imagen siguiente
	}

	/**
	 * La bola empezara a funcionar cuando se ejecute esta instruccion
	 *
	 */
	public void start(){


		/*
		 * Si toca los bordes de la pantalla cambia de direccion
		 */
		if (this.x<=Stage.InicX || this.x+this.ancho>=Stage.FinX+Stage.InicX){ 
			DeltaX=-DeltaX; //Va hacia los lados y vuelve hacia el lado contrario
			if(Menu.efectosactivos){
				new Toca("sonido2.wav").start(); //Aplicamos sonido al rebote ("sonido2.wav")
			}
		}

		if (this.y<=Stage.InicY || this.y+this.alto>=Stage.FinY+Stage.InicY){
			DeltaY=-DeltaY; //Va hacia arriba
			if(Menu.efectosactivos){
				new Toca("sonido2.wav").start(); //Aplicamos sonido al rebote
			}

		}



		/*
		 * Antes de nada creamos un jugador que sea de donde leeremos los datos de la posicion
		 * de la barra
		 */
		Barra jugador=Arkanoid.jugador;

		/*
		 * Si toca la barra en la parte gris tambien cambia de direccion
		 * 
		 * Lo acotamos de la siguiente manera, hay varias zonas(1,2,3,4,5,6,7) una por cada if:
		 * 
		 *   1  2   3   4  5
		 *  [ )[  )[ ](  ]( ]
		 *   <|====xxx====|>   
		 *   
		 */


		//AQUI ES DONDE CALCULAMOS EL REBOTE DE LA PELOTA CON LA BARRA! 

		/*
		 * Si coinciden las Y por arriba o por abajo y las x por la izquierda y la derecha
		 */
		if(this.getY()+this.getAlto()>=jugador.y && this.getY()<=jugador.y+jugador.getAlto()){
			/*
			 * Rebote en la parte uno
			 */
			if(this.x+this.ancho>=jugador.x && this.centroX<=jugador.finprimerborde){
				rebote(1);
			}


			// Rebote en la parte dos

			else if(this.centroX>=jugador.finprimerborde && this.centroX<=jugador.comienzocentro){
				rebote(2);
			}

			// Rebote de la parte tres

			else if(this.centroX>=jugador.comienzocentro && this.centroX<=jugador.fincentro){
				rebote(3);
			}

			// Rebote de la parte cuatro

			else if(this.centroX>=jugador.fincentro && this.centroX<=jugador.comienzosegundoborde){
				rebote(4);
			}

			// Rebote de la parte cinco

			else if(this.centroX>=jugador.comienzosegundoborde && this.x<=jugador.finbarra){
				rebote(5);
			}


		}




		//REBOTE CON LOS LADRILLOS
		/*
		 * Rebote con los ladrillos
		 */
		Ladrillo unladrillo; //Creamos el ladrillo donde cargaremos cada ladrillo

		/*
		 * Recorremos todo el array muro cargando cada ladrillo
		 */
		for(int i=0;i<10;++i){
			for(int j=0;j<10;++j){
				unladrillo=Arkanoid.muro[j][i];
				if(Arkanoid.muro[j][i]!=null){

					/*
					 * Primero guardamos todas las distancias que necesitamos medir, las distancias
					 * a cada arista del ladrillo.
					 */

					/*
					 * Guardamos la distancia desde la arista superior del ladrillo a la pelota
					 */
					double distanciaAristaSuperior=distancia(unladrillo.esquinaSuperiorIzquierdaX,unladrillo.esquinaSuperiorIzquierdaY, unladrillo.esquinaSuperiorDerechaX, unladrillo.esquinaSuperiorDerechaY);
					/*
					 * Guardamos la distancia desde la arista inferior del ladrillo a la pelota
					 */
					double distanciaAristaInferior=distancia(unladrillo.esquinaInferiorIzquierdaX,unladrillo.esquinaInferiorIzquierdaY, unladrillo.esquinaInferiorDerechaX, unladrillo.esquinaInferiorDerechaY);
					/*
					 * Guardamos la distancia desde la arista izquierda del ladrillo a la pelota
					 */
					double distanciaAristaIzquierda=distancia(unladrillo.esquinaSuperiorIzquierdaX,unladrillo.esquinaSuperiorIzquierdaY, unladrillo.esquinaInferiorIzquierdaX, unladrillo.esquinaInferiorIzquierdaY);
					/*
					 * Guardamos la distancia desde la arista derecha del ladrillo a la pelota
					 */
					double distanciaAristaDerecha=distancia(unladrillo.esquinaSuperiorDerechaX,unladrillo.esquinaSuperiorDerechaY, unladrillo.esquinaInferiorDerechaX, unladrillo.esquinaInferiorDerechaY);


					if(distanciaAristaSuperior<=this.ancho/2){
						rebote(6); //Rebota la pelota
						unladrillo.eliminar(j,i,this); //Intentamos eliminar el ladrillo
					}
					if(distanciaAristaInferior<=this.ancho/2){
						rebote(7); //Rebota la pelota
						unladrillo.eliminar(j,i,this); //Intentamos eliminar el ladrillo
					}
					if(distanciaAristaIzquierda<=this.ancho/2){
						rebote(8); //Rebota la pelota
						unladrillo.eliminar(j,i,this); //Intentamos eliminar el ladrillo
					}
					if(distanciaAristaDerecha<=this.ancho/2){
						rebote(9); //Rebota la pelota
						unladrillo.eliminar(j,i,this); //Intentamos eliminar el ladrillo
					}


				}

			}
		}


		this.setX(this.x+DeltaX*velocidad); //Realizamos la suma de las X
		this.setY(this.y+DeltaY*velocidad); //Realizamos la suma de las Y
		centroX=this.x+(this.ancho/2); //Actualizamos el centro
		centroY=this.y+(this.alto/2);  //Actualizamos el centro
	}


	/**
	 * El metodo stop lo que hace es cambiar la posicion de la bola segun la posicion de la barra.
	 * Lo situa en el centro de la barra. Con los vectores de salida predeterminados.
	 */
	public void stop(){
		this.x=Arkanoid.jugador.getX()+Arkanoid.jugador.ancho/2; //Cambiamos la coordenada X
		this.y=Arkanoid.jugador.getY()-this.alto;//Cambiamos la coordenada Y
		this.DeltaX=1;
		this.DeltaY=-1;
	}

	/**
	 * Cambia la imagen de la bola depened si esta en modo rebote o no
	 * @param modo
	 */
	public void setModoRebote(boolean modo){
		rebota=modo;
		setNombreSprite("bolaazul.gif"); //cambiamos de imagen
	}




	/**
	 * Devuelve la distancia entre el centro de la bola el segmento determinado por los puntos:
	 * A(AX,AY) y B(BX,BY).
	 * 
	 * La distancia que da es desde el centro de la pelota, para saber si esta en contacto con la
	 * pelota abra que comparar esta distancia con el radio de la bola.
	 * 
	 * 
	 * @param AX
	 * @param AY
	 * @param BX
	 * @param BY
	 * @return
	 */
	public double distancia(int AX, int AY,int BX,int BY){
		double distancia=0;

		/*
		 * Calculamos la distancia de un punto al segmento determinado por los puntos
		 * 
		 * Nos basamos en la formula de:
		 * 
		 * http://luisrey.wordpress.com/2008/07/06/distancia-punto-1/
		 *  
		 */
		double u=((this.centroX-AX)*(BX-AX)+(this.centroY-AY)*(BY-AY))/(Math.pow((BX-AX), 2)+Math.pow((BY-AY), 2));
		/*
		 * Esto quiere decir que el punto esta perpendicular al segmento, por lo cual con usar la ecuacion
		 * de distancia a una recta nos vale.
		 */

		if(u>=0 && u<=1){
			distancia=(((BX-AX)*(this.centroY-AY))-(BY-AY)*(this.centroX-AX))/(Math.sqrt(Math.pow((BX-AX), 2)+Math.pow((BY-AY),2)));
		}
		else if(u>1){
			/*
			 * Hacemos el calculo de la distancia euclidea segun la formula
			 */
			distancia=Math.sqrt((Math.pow(this.centroX-BX,2))+(Math.pow(this.centroY-BY,2)));
		}
		else if(u<0){
			/*
			 * Hacemos el calculo de la distancia euclidea segun la formula
			 */
			distancia=Math.sqrt((Math.pow(this.centroX-AX,2))+(Math.pow(this.centroY-AY,2)));
		}

		return Math.abs(distancia);
	}

	/**
	 * Calcula el DeltaX y DeltaY segun los tipos de rebotes definidos en cada zona:
	 * 
	 * 1    2    3    4   5
	 *  -  ---  xxx  ---  -
	 * |                   |
	 *  -  ---  xxx  ---  -
	 *  
	 *  
	 *  Tambien calcula el rebote segun con que tipo de arista rebote, esto se utiliza
	 *  para los rebotes con los ladrillos y con los margenes del juego.
	 *  
	 *  Con los Ladrillos, REBOTE EXTERIOR:
	 *  
	 *            6
	 *     ----------------
	 *  8 |                | 9
	 *     ----------------
	 *            7 
	 *  
	 *  
	 *  
	 *  
	 *  
	 * @param i
	 */
	public void rebote(int i){

		if(i==1){
			/*
			 * Zona 1: El rebote sera con mas angulo de lo normal, aparte de eso siempre va a salir
			 * en direccion a la izquierda, venga de donde venga.
			 */
			if(DeltaX==2){
				DeltaX=2;
			}else DeltaX=-2;
			DeltaY=-1;
		}else if(i==2){
			/*
			 * Zona 2: El rebote sera el comun, angulo estandar hacia la izquierda.
			 */
			DeltaX=-1;
			DeltaY=-1;
		}else if(i==3){
			/*
			 * Zona 3: El rebote sera hacia arriba, un angulo de 90�.
			 */
			if(DeltaX<=0){
				DeltaX=-1;
			}else DeltaX=1;
			DeltaY=-2;
		}else if(i==4){
			/*
			 * Zona 4: El rebote sera el comun, angulo estandar hacia la derecha.
			 */
			DeltaX=1;
			DeltaY=-1;
		}else if(i==5){
			/*
			 * Zona 5: El rebote sera con mas angulo de lo normal, aparte de eso siempre va a salir
			 * en direccion a la izquierda, venga de donde venga.
			 */
			if(DeltaX==2){
				DeltaX=-2;
			}else DeltaX=2;
			DeltaY=-1;
		}else{
			if(rebota){
				if(i==6){
					/*
					 * Zona 6: El rebote que realiza golpeando desde el exterior a una arista
					 * situada en la parte superior.
					 */
					if(DeltaY==1){
						DeltaY=-1;
					}
					else if(DeltaY==2){
						DeltaY=-2;
					}
				}else if(i==7){
					/*
					 * Zona 7: El rebote que realiza golpeando desde el exterior a una arista
					 * situada en la parte inferior.
					 */
					if(DeltaY==-1){
						DeltaY=1;
					}
					else if(DeltaY==-2){
						DeltaY=2;
					}
				}else if(i==8){
					/*
					 * Zona 8: El rebote que realiza golpeando desde el exterior a una arista
					 * situada en la parte izquierda.
					 */
					if(DeltaX==0){
						DeltaX=-2;
					}
					else if(DeltaX==1){
						DeltaX=-1;
					}
					else if(DeltaX==2){
						DeltaX=-2;
					}
				}else if(i==9){
					/*
					 * Zona 9: El rebote que realiza golpeando desde el exterior a una arista
					 * situada en la parte derecha.
					 */
					if(DeltaX==0){
						DeltaX=2;
					}
					else if(DeltaX==-1){
						DeltaX=1;
					}
					else if(DeltaX==-2){
						DeltaX=2;
					}

				}
			}
			
		}
		
		if(Menu.efectosactivos){ //Si tenemos los efectos activos
			new Toca("sonido1.wav").start(); //Aplicamos sonido al rebote (sonido1.wav);
		}
		
		/*
		 * Si el rebote es con la barra tendremos que actualizar el valor de la hora en la que
		 * reboto la pelota con la barra, esta parte se usa mas que nada para que cuando la demo
		 * este jugando sepa cuanto tiempo lleva la pelota sin rebotar con la barra.
		 */
		if(i>=1 && i<=5){
			Arkanoid.jugador.setUltimoRebote(Arkanoid.horas, Arkanoid.minutos, Arkanoid.segundos);
		}



	}


}
