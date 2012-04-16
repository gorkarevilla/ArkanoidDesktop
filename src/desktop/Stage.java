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

import java.awt.image.ImageObserver;

/**
 * Se Encarga de almacenar las constantes de los tama�os y tiene un metodo para acceder al cache
 * de sprites
 * 
 *
 */
public interface Stage extends ImageObserver{

	public static final int WIDTH =  480; //Ancho ventana(X)
	public static final int HEIGHT = 640; //Largo ventana(Y)
	public static final int SPEED = 10; //Velocidad de refresco de la pantalla

	/*
	 * Posicion incial de la ventana
	 */
	public static final int PosX=300; //Posicion de las X donde creamos la pantalla
	public static final int PosY=100; //Posicion de las Y donde creamos la pantalla

	/*
	 * Inicio del rectangulo de juego
	 */
	public final int InicX=20; //Inicio de X
	public final int InicY=20; //Inicio de Y

	/*
	 *  Margenes:
	 */
	public final int margen=20; //Margen entre la zona de juego y el marcador
	public final int largo=40;   //Altura del marcador

	/*
	 * Fin del rectangulo de juego
	 */
	public final int FinX=WIDTH-InicX-InicX-10; //Fin de las X, el 10 es para que quede centrado
	public final int FinY=HEIGHT-largo-InicY-InicY-margen-20; //Fin de las Y, el 20 es para que quede centrado

	/*
	 * Zonas de escritura
	 */
	public final int primeralinea=InicY+FinY+margen+15; //Primera linea para escribir
	public final int segundalinea=InicY+FinY+margen+33; //Segunda linea para escribir

	public final int primeracolumna=InicX+5; //Primera columna donde empezara el texto
	public final int segundacolumna=InicX+150; //Segunda columna donde empezara el texto
	public final int terceracolumna=InicX+325;


	/*
	 * Posicion de las Letras del menu
	 */
	public final int menuMargenFila=350;
	public final int menuEspacioFilas=10+20;
	public final int menuPrimeraFila=menuMargenFila;
	public final int menuSegundaFila=menuMargenFila+menuEspacioFilas;
	public final int menuTerceraFila=menuMargenFila+menuEspacioFilas*2;
	public final int menuCuartaFila=menuMargenFila+menuEspacioFilas*3;
	public final int menuQuintaFila=menuMargenFila+menuEspacioFilas*4;
	public final int menuSextaFila=menuMargenFila+menuEspacioFilas*5;
	public final int menuSeptimaFila=menuMargenFila+menuEspacioFilas*6;
	public final int menuOctavaFila=menuMargenFila+menuEspacioFilas*7;

	/*
	 * Posicion de donde empiezan los creditos
	 */
	public final int menuCreditos=menuMargenFila-100;



	/**
	 * Metodo para acceder a la cache 
	 * @return
	 */
	public SpriteCache getSpriteCache();

}
