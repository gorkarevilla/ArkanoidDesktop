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

import java.io.FileInputStream;
import sun.audio.AudioPlayer;

/**
 * 
 * Se encarga de reproducir los sonidos que le pases por parametro. Modo de uso:
 * 
 * new Toca("cancion.ext").start();
 *
 */
public class Toca extends Thread{

	/*
	 * Aqui guardaremos la cancion reproduciendose en este momento
	 */
	public String cancion;
	public AudioPlayer reproductor;

	public Toca(String sonido){
		cancion=sonido;
	}

	/**
	 * Con esto ejecutamos la reproduccion del archivo. La cancion tiene que estar dentro
	 * de la carpeta snd. Que cuelga directamente de donde tenemos el proyecto.
	 */
	@SuppressWarnings("static-access")
	public void run() {
		try {
			while (true) {
				//AudioPlayer.player.start(new FileInputStream("snd/"+cancion));
				//AudioPlayer.player.run();
				reproductor.player.start(new FileInputStream("snd/"+cancion));
				reproductor.player.run();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public void parar(){
		try{
			reproductor.player.interrupt();
		}catch(Exception e){
			e.printStackTrace();
		}
	}


}
