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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Esta sera la base de todos los objetos que anyadamos al Arkanoid, tiene las variables mas generales
 * que son comunes a todos los objetos, como su posicion o la imagen que tiene cada uno.
 *
 */
public class Actor {
	protected int x,y; 					//Coordenadas X,Y de los objetos
	protected int ancho;  				//Ancho del objeto (Weight)
	protected int alto; 				//Alto del objeto (Height)
	protected String nombreSprite; 		//Nombre de la imagen 
	protected Stage stage;				//Pantalla en la que esta
	protected SpriteCache spriteCache;	//Almancena la imagen que se usara para el objeto

	/*
	 * Direccion del Vector, marca la direccion de la bola:
	 *        |	
	 * (-1,1) | (1,1)
	 *        |
	 * -------+--------
	 *        |
	 * (-1,-1)| (1,-1)
	 *        |       
	 */
	protected int DeltaX=1;
	protected int DeltaY=-1;

	/**
	 * Clase constructora del Actor, se le pasa por paramentro el interface sobre
	 * el que se trabaja con sus constantes.
	 * 
	 * Tambien almacena la imagen que se le quiere dar al objeto
	 * 
	 * @param stage
	 */
	public Actor(Stage stage){
		this.stage = stage;
		spriteCache = stage.getSpriteCache();
	}

	/**
	 * Se encarga de dibujar el Actor en las posiciones X,Y.
	 * Utilizamos el metodo de SpriteCache para tener mejor rendimiento cargando imagenes. Tambien
	 * le definimos donde queremos que se carge. 
	 * 
	 * @param g
	 */
	public void paint(Graphics2D g){
		g.drawImage(spriteCache.getSprite(nombreSprite),x,y,stage);
	}

	/**
	 * Devuelve la posicion X
	 * 
	 * @return
	 */
	public int getX(){
		return x;
	}

	/**
	 * Cambia la posicion X tiene que estar dentro del rango de la [InicX,FinX]
	 * @param i
	 */
	public void setX(int i){
		if(i>Stage.FinX+Stage.InicX-ancho)x=Stage.FinX+Stage.InicX-ancho;
		else if(i<Stage.InicX)x=Stage.InicX;
		else x=i;

	}

	/**
	 * Devuelve la posicion Y
	 * 
	 * @return
	 */
	public int getY(){
		return y;
	}

	/**
	 * Cambia la posicion Y
	 * @param i
	 */
	public void setY(int i){
		if(i>Stage.FinY+Stage.InicY-alto)y=Stage.FinY+Stage.InicY-alto;
		else if(i<Stage.InicY)y=Stage.InicY;
		else y=i;
	}

	/**
	 * Asigna el valor a DeltaX
	 * 
	 * @param i
	 */
	public void setDeltaX(int i){
		this.DeltaX=i;
	}

	/**
	 * Devuelve el vector DeltaX
	 * 
	 * @return
	 */
	public int getDeltaX(){
		return DeltaX;
	}

	/**
	 * Asigna el valor a DeltaY
	 * 
	 * @param i
	 */
	public void setDeltaY(int i){
		this.DeltaY=i;
	}

	/**
	 * Devuelve el vector DeltaY
	 * 
	 * @return
	 */
	public int getDeltaY(){
		return DeltaY;
	}

	/**
	 * Devuelve el nombre de la imagen
	 * @return
	 */
	public String getNombreSprite(){
		return nombreSprite;
	}

	/**
	 * 
	 * @param string
	 */
	public void setNombreSprite(String string) { 
		nombreSprite = string;
		BufferedImage image = spriteCache.getSprite(nombreSprite);
		alto = image.getHeight();
		ancho = image.getWidth();
	}

	/**
	 * Devuelve el Alto del objeto
	 * @return
	 */
	public int getAlto(){
		return alto;
	}

	/**
	 * Devuelve el Ancho del objeto
	 * @return
	 */
	public int getAncho(){
		return ancho;
	}

	/**
	 * Asigna el Alto deseado
	 * 
	 * @param i
	 */
	public void setAlto(int i){
		alto = i;
	}

	/**
	 * Asigna el Ancho deseado
	 * @param i
	 */
	public void setAncho(int i){
		ancho = i;
	}

	/**
	 * Comienza a funcionar el Objeto
	 *
	 */
	public void start(){

	}





}
