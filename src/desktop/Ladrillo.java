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
 * Son los ladrillos que incluiremos dentro del array muro, tendran diferentes colores, y su
 * colocacion depende del archivo de entrada. Tambien tienen distintas puntuaciones.
 *
 */
public class Ladrillo extends Actor{

	/*
	 * Serial para el extends
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	/*
	 * Color y valor del ladrillo
	 */
	public int color;
	public int valor;

	/*
	 * Los puntos de las cuatro esquinas del ladrillo:
	 * 
	 * +--------+
	 * |        |
	 * +--------+
	 * 
	 */
	//Superior izquierda
	protected int esquinaSuperiorIzquierdaX=this.getX();
	protected int esquinaSuperiorIzquierdaY=this.getY();
	//Superior derecha
	protected int esquinaSuperiorDerechaX=this.getX()+this.getAncho();
	protected int esquinaSuperiorDerechaY=this.getY();
	//Inferior izquierda
	protected int esquinaInferiorIzquierdaX=this.getX();
	protected int esquinaInferiorIzquierdaY=this.getY()+this.getAlto();
	//Inferior derecha
	protected int esquinaInferiorDerechaX=this.getX()+this.getAncho();
	protected int esquinaInferiorDerechaY=this.getY()+this.getAlto();
	
	
	/**
	 * Constructor de los ladrillos. Hay que especificar donde queremos anyadirla
	 * 
	 * @param stage
	 */
	public Ladrillo(Stage stage){
		super(stage);
		setNombreSprite("ladrillogris.gif");
		valor=0;
		
	}

	/**
	 * Actualiza el valor de todos los puntos del ladrillo
	 */
	public void start(){
		actualizarPuntos();
		
	}

	/**
	 * Convierte el valor i del array en la posicion X de la pantalla
	 * 
	 * @param i
	 * @return
	 */	
	public int convertirX(int i){
		int posicion=Stage.InicX;
		while(i>0){
			posicion=posicion+ancho;
			--i;
		}

		return posicion;
	}

	/**
	 * Convierte el valor j del array en la posicion Y de la pantalla, teniendo en cuenta que la
	 * primera posicion comienza 50 pixels mas abajo de la posicion de inicio (Para dejar un margen
	 * arriba).
	 *  
	 * @param j
	 * @return
	 */
	public int convertirY(int j){
		int posicion=Stage.InicY+50;
		while(j>0){
			posicion=posicion+alto;
			--j;
		}

		return posicion;
	}

	/**
	 * Asigna el color que queremos al ladrillo
	 * 
	 * @param i
	 */
	public void setColor(int i){
		color=i;
		setImagen(i);
	}
	

	/**
	 * Actualiza el valor de todos los puntos del ladrillo
	 *
	 */
	public void actualizarPuntos(){
		//Superior izquierda
		esquinaSuperiorIzquierdaX=this.getX();
		esquinaSuperiorIzquierdaY=this.getY();
		//Superior derecha
		esquinaSuperiorDerechaX=this.getX()+this.getAncho();
		esquinaSuperiorDerechaY=this.getY();
		//Inferior izquierda
		esquinaInferiorIzquierdaX=this.getX();
		esquinaInferiorIzquierdaY=this.getY()+this.getAlto();
		//Inferior derecha
		esquinaInferiorDerechaX=this.getX()+this.getAncho();
		esquinaInferiorDerechaY=this.getY()+this.getAlto();
	}

	/**
	 * Asigna el valor que queremos al ladrillo
	 * 
	 * @param i
	 */
	public void setValor(int i){
		valor=i;
	}

	/**
	 * Eliminar este ladrillo. Para ello tiene que estar dentro del muro y se le tienen que pasar
	 * los parametros j y i. 
	 * 
	 * @param i
	 * @param j
	 */
	public void eliminar(int j,int i, Bola pelota){
		boolean eliminarla=false;


		if(pelota==null){
			if(this.color!=1){
				eliminarla=true;
			}
		}
		else if(pelota.rebota && this.color!=1){
			eliminarla=true;
		}
		else if(!pelota.rebota){
			eliminarla=true;
		}
		//Cuando no es un ladrillo gris se puede romper.
		if(eliminarla){
			Arkanoid.muro[j][i]=null;
			
			Bonus premio = Arkanoid.premios[j][i];
			if(premio!=null){
				premio.activo=true;
			}
			
			
			/*
			 * Actualizamos el marcador
			 */
			Arkanoid.marcador=Arkanoid.marcador+this.valor;
			/*
			 * Comprobamos que no haya acabado el juego
			 */
			if(comprobarFin()){
				//System.out.println("Se acabo el nivel");
				Arkanoid.nuevonivel=true;
			}
		}//else System.out.println("Es un Ladrillo GRIS!");
	}



	/**
	 * Comprueba si el nivel se acaba a acabado. Devuelve true si se acabo y false en caso
	 * contrario
	 * 
	 * @return
	 */
	public boolean comprobarFin(){
		boolean fin=true;
		Ladrillo unladrillo;
		for(int i=0;i<10;++i){
			for(int j=0;j<10;++j){
				if(Arkanoid.muro[j][i]!=null){
					unladrillo=Arkanoid.muro[j][i];
					if(unladrillo.color!=1){
						fin=false;
					}
				}
			}
		}
		return fin;
	}

	/**
	 * Cambia la imagen del ladrillo, como parametro se le pasa el color:
	 * 
	 * 1-> Ladrillo Gris
	 * 2-> Ladrillo Azul
	 * 3-> Ladrillo Amarillo
	 * 4-> Ladrillo Morado
	 * 5-> Ladrillo Naranja
	 * 6-> Ladrillo Rojo
	 * 7-> Ladrillo Verde
	 *
	 * @param i
	 */
	public void setImagen(int i){
		if(i==1){
			setNombreSprite("ladrillogris.gif");
			setValor(0);
		}else if(i==2){
			setNombreSprite("ladrilloazul.gif");
			setValor(10);
		}else if(i==3){
			setNombreSprite("ladrilloamarillo.gif");
			setValor(20);
		}else if(i==4){
			setNombreSprite("ladrillomorado.gif");
			setValor(50);
		}else if(i==5){
			setNombreSprite("ladrillonaranja.gif");
			setValor(100);
		}else if(i==6){
			setNombreSprite("ladrillorojo.gif");
			setValor(150);
		}else if(i==7){
			setNombreSprite("ladrilloverde.gif");
			setValor(200);
		}else System.out.println("Error de color!");
		
		actualizarPuntos();
	}

}
