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

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * Esta clase se encaga de realizar los dibujos de las imagenes, usa un HasMap para optimizar la carga
 * de imagenes y solo cargarlas una vez.
 *
 */
public class SpriteCache {

	/*
	 * Esto nos ayudara a optimizar la carga de imagenes, para de estar forma solo
	 * cargar una imagen en cada buffer y moverla. Sin necesidad de cargarla a cada refresco
	 * ya que saturaba mucho el ordenador.
	 */
	private HashMap<String, BufferedImage> sprites;

	/** 
	 * Creamos el HashMap para asociar el nombre con cada imagen y almacenarlo en una lista
	 * de esta forma cuando queramos volver a leer la imagen no lo cargara desde archivo sino desde
	 * la lista.
	 */
	public SpriteCache() {
		sprites = new HashMap<String, BufferedImage>();
	}


	/**
	 * Carga la imagen de la lista, en caso de que no este la carga desde el archivo y la inserta
	 * en el hashmap para no tener que volver a leer desde archivo.
	 * 
	 * @param nombre
	 * @return
	 */
	public BufferedImage getSprite(String nombre) {
		BufferedImage img = (BufferedImage)sprites.get(nombre);
		if (img == null) {
			img = loadImage("img/"+nombre);
			sprites.put(nombre,img);

		}
		return img;
	}


	/**
	 * Intenta cargar la imagen de la direccion, en caso de que no la carge finaliza el programa y
	 * da un error.
	 * 
	 * @param nombre
	 * @return
	 */
	public BufferedImage loadImage(String nombre) {
		URL url=null;
		try {
			url = getClass().getClassLoader().getResource(nombre);
			return ImageIO.read(url);
		} catch (Exception e) {
			System.out.println("No se pudo cargar la imagen " + nombre +" de "+url);
			System.out.println("El error fue : "+e.getClass().getName()+" "+e.getMessage());
			System.exit(0);
			return null;
		}
	} 
}
