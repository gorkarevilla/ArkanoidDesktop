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
 * Son los disparos que se utilizan a la hora de tener la habilidad de disparo obtenida
 * de los bonus.
 *
 */
public class Disparo extends Actor {


	public static final int VELOCIDAD_MISIL=5;

	/**
	 * Constructora de la clase disparo
	 * 
	 * @param stage
	 */
	public Disparo(Stage stage){
		super(stage);
		setNombreSprite("MISILI.gif");
	}

	/**
	 * Metodo que realiza el lanzamiento de un misil, se le pasa el numero que ocupa en la lista
	 * de disparos.
	 * 
	 * @param num
	 */
	public void start(int num) {
		/*
		 * Avanzamos una posicion hacia arriba
		 */
		setY(getY()-VELOCIDAD_MISIL);

		/*
		 * Si toca el techo que las quite los disparos
		 */
		if (this.getY()==Stage.InicY){
			Arkanoid.disparos.remove(num);
		}


		/*
		 * ROMPE LOS LADRILLOS
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
					 * Comprobamos si el ladrillo que estamos comprobando tiene que rebotar con la pelota por la 
					 * alguna de las dos bases
					 */
					if(this.y<=unladrillo.getY()+unladrillo.alto && this.y>=unladrillo.getY()){
						if(this.x>=unladrillo.getX() && this.x<unladrillo.getX()+unladrillo.ancho){
							unladrillo.eliminar(j,i,null); //Intentamos eliminar el ladrillo
							Arkanoid.disparos.remove(num);//Destruimos el misil
						}
					}


				} 

			}
		}
	}
}
