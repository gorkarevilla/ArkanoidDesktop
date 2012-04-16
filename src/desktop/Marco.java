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
 * El Marco se encarga de situar cualquier objeto tanto dentro como fuera de los margenes
 * de la zona de juego. Al fin y al cabo es simplemente un objeto que solo pinta una imagen.
 *
 */
public class Marco extends Actor {

	/**
	 * Constructora del Marco, para poder poner un objeto fuera de los margenes de la zona de juego
	 * @param stage
	 */
	public Marco(Stage stage) {
		super(stage);
	}

	/**
	 * Cambia la posicion X tiene que estar dentro del rango de la [InicX,FinX]
	 * @param i
	 */
	public void setX(int i){
		x=i;

	}


	/**
	 * Cambia la posicion Y
	 * @param i
	 */
	public void setY(int i){
		y=i;
	}




}
