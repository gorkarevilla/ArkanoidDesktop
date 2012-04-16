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
 * 
 * Es el bonus que cae desde los ladrillos. Segun cada tipo tenemos un bonus diferente:
 * 
 * a: No hay bonus
 * b: Agranda la barra
 * c: Hace la barra mas pequenya
 * d: Bonus de 1000 puntos
 * e: Modo disparo
 * f: 1 vida mas
 * g: 3 Bolas.
 * h: Modo pelota lenta
 * i: Modo pelota rapida
 * j: No rebota la bola, y destroza todo lo que encuentre.
 *
 */
public class Bonus extends Actor{

	/*
	 * Almacena de que tipo es el bonus
	 */
	public char tipo;
	
	/*
	 * Marca si esta en movimiento o no, en caso de estar en movimiento bajara
	 */
	public boolean activo=false;
	
	
	/**
	 * Constructora de bonus.
	 * @param stage
	 */
	public Bonus(Stage stage) {
		super(stage);
		setTipo('b');
		setNombreSprite("bonus/b.gif"); //Le damos la imagen que queremos al bonus
	}
	
	/**
	 * Especifica de que tipo es el bonus, depende las letras seran unos u otros
	 * 
	 * @param tipo
	 */
	public void setTipo(char tipo){
		this.tipo=tipo;
		if(tipo=='b')setNombreSprite("bonus/b.gif");
		else if(tipo=='c')setNombreSprite("bonus/c.gif");
		else if(tipo=='d')setNombreSprite("bonus/d.gif");
		else if(tipo=='e')setNombreSprite("bonus/e.gif");
		else if(tipo=='f')setNombreSprite("bonus/f.gif");
		else if(tipo=='g')setNombreSprite("bonus/g.gif");
		else if(tipo=='h')setNombreSprite("bonus/h.gif");
		else if(tipo=='i')setNombreSprite("bonus/i.gif");
		else if(tipo=='j')setNombreSprite("bonus/j.gif");
		else System.out.println("El tipo de bonus "+tipo+" no existe");
	}
	
	/**
	 * Metodo que hace que funcionen los bonus.
	 * 
	 * @param j
	 * @param i
	 */
	public void start(int j, int i){
		if(activo){
			//Si esta activo el bonus tiene que bajar
			setY(getY()+1);
			if(getY() == Stage.FinY){ //Si llega el bonus al final sin que se coja el bonus que lo elimine
				this.activo=false;
				eliminar(j,i); // Eliminamos el bonus
			}
			coger(j,i); // O sino lo cogemos
		}
	}
	
	
	/**
	 * Eliminar este bonus. Se realiazara esta operacion cuando se haya cogido el bonus o cuando se
	 * haya salido del limite.
	 * 
	 * @param i
	 * @param j
	 */
	public void eliminar(int j,int i){
			Arkanoid.premios[j][i]=null;
	}
	
	/**
	 * Metodo que realiza cuando se coge cualquier bonus, en funcion de que tipo sea ese bonus
	 * da una habilidad u otra.
	 *
	 * @param i
	 * @param j
	 */	
	public void coger(int j, int i){
		Barra jugador=Arkanoid.jugador;
		boolean premiocogido=false;
		
		if(this.getX()+this.ancho>=jugador.getX() && this.getX()<=jugador.finbarra){
			if(this.y+this.alto>=jugador.getY() && this.getY()<=jugador.parteabajo){
				premiocogido=true;
			}
		}
		
		/*
		 * Agranda la Barra.
		 */
		if(premiocogido){
			if(tipo=='b'){	
				//System.out.println("Agrandamos la BARRAA");
				jugador.setModoDisparo(false);
				jugador.cambiartamano(4);
			}
			/*
			 * Hace mas pequenya la barra
			 */
			else if(tipo=='c'){
				//System.out.println("BARRA PEQUENYA");
				jugador.setModoDisparo(false);
				jugador.cambiartamano(1);
			}
			/*
			 * Bonus de 1000 puntos
			 */
			else if(tipo=='d'){
				//System.out.println("1000 PUNTOS");
				Arkanoid.marcador=Arkanoid.marcador+1000;
			}
			/*
			 * Modo disparo
			 */
			else if(tipo=='e'){
				//System.out.println("MODO DISPARO");
				jugador.setModoDisparo(true);
			}
			/*
			 * 1 vida mas
			 */
			else if(tipo=='f'){
				//System.out.println("1 VIDA");
				++Arkanoid.vidas;
			}
			/*
			 * 3 Bolas
			 * 
			 * De momento no se usa
			 */
			else if(tipo=='g'){
				//System.out.println("3 BOLAS");
				/*
				 * Creamos la pelota y la anyadimos al inventario de pelotas
				 */
				Bola pelota1 = new Bola(stage);
				pelota1.parada=false;
				pelota1.setX(jugador.comienzocentro);
				pelota1.setY(jugador.getY());
				Arkanoid.bolas.add(pelota1);
				/*
				 * Creamos la pelota y la anyadimos al inventario de pelotas
				 */
				Bola pelota2 = new Bola(stage);
				pelota2.parada=false;
				pelota2.setX(jugador.fincentro);
				pelota2.setY(jugador.getY());
				Arkanoid.bolas.add(pelota2);
			}
			/*
			 * Pelota mas lenta
			 * 
			 * De momento no usar!!
			 */
			else if(tipo=='h'){
				for (int u = 0; u < Arkanoid.bolas.size(); u++) {
					Bola pelota = Arkanoid.bolas.get(u);
					pelota.velocidad=1;
				}
			}
			/*
			 * Pelota mas rapida
			 * 
			 * De momento no usar!!
			 */
			else if(tipo=='i'){
				for (int u = 0; u < Arkanoid.bolas.size(); u++) {
					Bola pelota = Arkanoid.bolas.get(u);
					pelota.velocidad=3;
				}
			}
			/*
			 * La pelota no rebota con los ladrillos
			 */
			else if(tipo=='j'){
				for (int u = 0; u < Arkanoid.bolas.size(); u++) {
					Bola pelota = Arkanoid.bolas.get(u);
					pelota.setModoRebote(false);
				}
			}
			
			eliminar(j,i);
		}
	}
	
	
	
	
	
	/**
	 * Convierte el valor i del array en la posicion X de la pantalla
	 * 
	 * @param i
	 * @return
	 */	
	public int convertirX(int j,int i){
		int posicion=0;
		Ladrillo unladrillo = Arkanoid.muro[j][i];
		if(unladrillo!=null){
			posicion=unladrillo.getX()+this.ancho/4;
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
	public int convertirY(int j,int i){
		int posicion=0;
		Ladrillo unladrillo = Arkanoid.muro[j][i];
		if(unladrillo!=null){
			posicion=unladrillo.getY();
		}
		return posicion;
	}
	
	
}
