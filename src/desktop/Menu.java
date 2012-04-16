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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;

/**
 * 
 * Esta clase es la que se encarga de manejar toda la parte del menu del juego.
 *
 * NOTA para cuando crees nuevos botones: El tipo de letra utilizado para el texto es la: mostwasted
 */
public class Menu {

	/*
	 * Aqui almacenamos el Arkanoid en el que estamos jugando
	 */
	public Arkanoid juego;

	/*
	 * Activamos o desactivamos la musica
	 */
	public static boolean musicaactiva=false;
	
	/*
	 * Activamos o desactivamos los sonidos de los efectos
	 */
	public static boolean efectosactivos=false;
	
	/*
	 * Creamos el buffer para refrescar la pantalla
	 */
	private BufferStrategy strategy;

	/*
	 * Esta activo cuando estemos en el menu normal, donde estan todas las opciones.
	 */
	public boolean modogeneral=true;

	/*
	 * Esta activo cuando estemos viendo los creditos
	 */
	public boolean modocreditos=false;

	/*
	 * Esta activo cuando estemos viendo los records
	 */
	public boolean modorecords=false;

	/*
	 * Esta activo cuando estemos viendo las opciones
	 */
	public boolean modoopciones=false;

	/*
	 * Este es el modo de escritura, estara activo cuando estemos mostrando algun tipo de mensaje
	 * en el menu.
	 * 
	 * De momento en deshuso
	 */
	public boolean modoprint=false;
	
	/*
	 * Esta activo cuando se puede continuar la partida.
	 */
	public boolean activarcontinuar=false;
	
	/*
	 * Aqui guardamos el texto que queremos mostrar cuando estemos en modo print
	 */
	public String texto="";
	
	/*
	 * En el menu indica que tenemos selecionado, por defecto la primera
	 */
	public int seleccion=2;

	/*
	 * Controla si hay que pedir nombre al usuario o no
	 */
	public boolean preguntarnombre=true;

	/**
	 * Clase constructora del Menu
	 * 
	 * @param juego
	 */
	public Menu(Arkanoid juego){
		this.juego=juego;
	}


	/**
	 * Se encarga de realizar las actualizaciones en el Menu
	 * 
	 * Activa el continuar cuando la pelota este en juego
	 *
	 */
	public void actualizarMenu(){
		Bola pelota = Arkanoid.bolas.get(0);
		if(pelota.parada){
			activarcontinuar=false;
		}else activarcontinuar=true;
	}


	/**
	 * Cuando estemos en modomenu esto pintara en toda la pantalla el menu, con las opciones.
	 *
	 */
	public void paintMenu(){
		
		/*
		 * Tenemos que cargar el mismo "dibujador" de pantalla que el del juego por lo tanto
		 * el que usemos lo igualamos al del Arkanoid.
		 */
		strategy=juego.strategy;

		Graphics2D g = (Graphics2D)strategy.getDrawGraphics();

		/*
		 * Pintamos el fondo del menu en toda la pantalla
		 */
		Marco menu = new Marco(juego);
		menu.setNombreSprite("menu/ImagenMenu.jpg");
		menu.setX(0);
		menu.setY(0);
		menu.paint(g);

		/*
		 * Ahora comprobaremos en que modo estamos y pintaremos segun donde estemos situados. 
		 * Tenemos dos imagenes, una para cuando esta seleccionada que sera de color verde. Las
		 * imagenes son las que empiezan por W.
		 */
		if(modogeneral){
			/*
			 * Pintamos las opciones del menu general:
			 */
			/*
			 * Continuar:
			 */
			if(activarcontinuar){
				Marco continuar = new Marco(juego);
				continuar.setNombreSprite("menu/continuar.gif");
				//Este if controla cuando se iluminan las letras al pasar el raton por encima
				if(seleccion==1){
					continuar.setNombreSprite("menu/Wcontinuar.gif"); 
				}
				continuar.setX(Stage.primeracolumna);
				continuar.setY(Stage.menuPrimeraFila);
				continuar.paint(g);
			}
			/*
			 * Juego Nuevo:
			 */
			Marco juegonuevo = new Marco(juego);
			juegonuevo.setNombreSprite("menu/juegonuevo.gif");
			
			if(seleccion==2){
				juegonuevo.setNombreSprite("menu/Wjuegonuevo.gif"); 
			}
			juegonuevo.setX(Stage.primeracolumna);
			juegonuevo.setY(Stage.menuSegundaFila);
			juegonuevo.paint(g);
			/*
			 * Modo Demo:
			 */
			Marco mododemo = new Marco(juego);
			mododemo.setNombreSprite("menu/mododemo.gif");
			if(seleccion==3){
				mododemo.setNombreSprite("menu/Wmododemo.gif");
			}
			mododemo.setX(Stage.primeracolumna);
			mododemo.setY(Stage.menuTerceraFila);
			mododemo.paint(g);
			/*
			 * Opciones:
			 */
			Marco opciones = new Marco(juego);
			opciones.setNombreSprite("menu/opciones.gif");
			if(seleccion==4){
				opciones.setNombreSprite("menu/Wopciones.gif");
			}
			opciones.setX(Stage.primeracolumna);
			opciones.setY(Stage.menuCuartaFila);
			opciones.paint(g);
			/*
			 * High Scores:
			 */
			Marco highscores = new Marco(juego);
			highscores.setNombreSprite("menu/highscores.gif");
			if(seleccion==5){
				highscores.setNombreSprite("menu/Whighscores.gif");
			}
			highscores.setX(Stage.primeracolumna);
			highscores.setY(Stage.menuQuintaFila);
			highscores.paint(g);
			/*
			 * Creditos:
			 */
			Marco creditos = new Marco(juego);
			creditos.setNombreSprite("menu/creditos.gif");
			if(seleccion==6){
				creditos.setNombreSprite("menu/Wcreditos.gif");
			}
			creditos.setX(Stage.primeracolumna);
			creditos.setY(Stage.menuSextaFila);
			creditos.paint(g);
			/*
			 * Salir:
			 */
			Marco salir = new Marco(juego);
			salir.setNombreSprite("menu/salir.gif");
			if(seleccion==7){
				salir.setNombreSprite("menu/Wsalir.gif");
			}
			salir.setX(Stage.primeracolumna);
			salir.setY(Stage.menuSeptimaFila);
			salir.paint(g);
		}else if(modoopciones){
			/*
			 * Pintamos las Opciones
			 */
			/*
			 * Pinta unicamente musica
			 */
			Marco musica = new Marco(juego);
			musica.setNombreSprite("menu/musica.gif");
			musica.setX(Stage.primeracolumna);
			musica.setY(Stage.menuPrimeraFila);
			musica.paint(g);
			/*
			 * Para la opcion del Si en musica
			 */
			Marco sonidoSi = new Marco(juego);
			sonidoSi.setNombreSprite("menu/si.gif");
			if(seleccion==2){
				sonidoSi.setNombreSprite("menu/Wsi.gif");
			}
			sonidoSi.setX(Stage.primeracolumna);
			sonidoSi.setY(Stage.menuSegundaFila);
			sonidoSi.paint(g);
			/*
			 * Para la opcion del No en musica
			 */
			Marco sonidoNo = new Marco(juego);
			sonidoNo.setNombreSprite("menu/no.gif");
			if(seleccion==3){
				sonidoNo.setNombreSprite("menu/Wno.gif");
			}
			sonidoNo.setX(Stage.primeracolumna);
			sonidoNo.setY(Stage.menuTerceraFila);
			sonidoNo.paint(g);		
			/*
			 * Pinta el punto que marca la seleccion actual
			 */
			Marco puntosonido = new Marco(juego);
			puntosonido.setNombreSprite("menu/punto.gif");
			if(musicaactiva){
				puntosonido.setY(sonidoSi.getY());
			}else puntosonido.setY(sonidoNo.getY());
			puntosonido.setX(Stage.primeracolumna-30);
			puntosonido.paint(g);
			
			
			/*
			 * Pinta unicamente efectos
			 */
			Marco efectos = new Marco(juego);
			efectos.setNombreSprite("menu/efectos.gif");
			efectos.setX(Stage.primeracolumna);
			efectos.setY(Stage.menuCuartaFila);
			efectos.paint(g);
			/*
			 * Para la opcion del Si en efectos
			 */
			Marco efectosSi = new Marco(juego);
			efectosSi.setNombreSprite("menu/si.gif");
			if(seleccion==5){
				efectosSi.setNombreSprite("menu/Wsi.gif");
			}
			efectosSi.setX(Stage.primeracolumna);
			efectosSi.setY(Stage.menuQuintaFila);
			efectosSi.paint(g);
			/*
			 * Para la opcion del No en efectos
			 */
			Marco efectosNo = new Marco(juego);
			efectosNo.setNombreSprite("menu/no.gif");
			if(seleccion==6){
				efectosNo.setNombreSprite("menu/Wno.gif");
			}
			efectosNo.setX(Stage.primeracolumna);
			efectosNo.setY(Stage.menuSextaFila);
			efectosNo.paint(g);
			/*
			 * Pinta el punto que marca la seleccion actual
			 */
			Marco puntoefectos = new Marco(juego);
			puntoefectos.setNombreSprite("menu/punto.gif");
			if(efectosactivos){
				puntoefectos.setY(efectosSi.getY());
			}else puntoefectos.setY(efectosNo.getY());
			puntoefectos.setX(Stage.primeracolumna-30);
			puntoefectos.paint(g);
			
			
			/*
			 * Pintamos el boton para volver al menu principal Salir:
			 */
			Marco salir = new Marco(juego);
			salir.setNombreSprite("menu/salir.gif");
			if(seleccion==7){
				salir.setNombreSprite("menu/Wsalir.gif");
			}
			salir.setX(Stage.primeracolumna);
			salir.setY(Stage.menuSeptimaFila);
			salir.paint(g);




		}else if(modorecords){
			/*
			 * Pintamos los Records
			 */
			/*
			 * Creamos un objeto de la clase records para recoger los datos y le pasamos por parametro
			 * cualquier cosa, ya que no vamos a escribir nada.
			 */
			Records record = new Records(juego.nombre,Arkanoid.marcador,Arkanoid.horas,Arkanoid.minutos,Arkanoid.segundos);
			/*
			 * Guardamos dentro del array los datos que cogemos de records
			 * que estaran almacenados en el archivo
			 */
			String[][] resultados = record.getDatos();
			g.setFont(new Font("Forte", Font.BOLD, 15));
			/*
			 * Pintamos los records en la pantalla
			 */
			for(int i=0;i<record.getMaxRecords();++i){
				g.setColor(Color.red);
				g.drawString(String.valueOf(i+1)+".",Stage.primeracolumna-20,Stage.menuPrimeraFila-50+20*i);
				g.setColor(Color.blue);
				if(resultados[1][i]!=null){
					g.drawString(resultados[0][i],Stage.primeracolumna,Stage.menuPrimeraFila-50+20*i);
					g.drawString(resultados[1][i],Stage.segundacolumna,Stage.menuPrimeraFila-50+20*i);
				}

			}



			/*
			 * Pintamos el boton para volver al menu principal Salir:
			 */
			Marco salir = new Marco(juego);
			salir.setNombreSprite("menu/salir.gif");
			if(seleccion==7){
				salir.setNombreSprite("menu/Wsalir.gif");
			}
			salir.setX(Stage.primeracolumna);
			salir.setY(Stage.menuSeptimaFila);
			salir.paint(g);

		}else if(modocreditos){
			/*
			 * Pintamos los Creditos
			 */
			Marco creditos = new Marco(juego);
			creditos.setNombreSprite("menu/menucreditos.gif");
			creditos.setX(Stage.primeracolumna);
			creditos.setY(Stage.menuCreditos);
			creditos.paint(g);
			/*
			 * Pintamos el boton para volver al menu principal Salir:
			 */
			Marco salir = new Marco(juego);
			salir.setNombreSprite("menu/salir.gif");
			if(seleccion==7){
				salir.setNombreSprite("menu/Wsalir.gif");
			}
			salir.setX(Stage.primeracolumna);
			salir.setY(Stage.menuSeptimaFila);
			salir.paint(g);

			/*
			 * De momento esta parte no se usa.
			 */
		}else if(modoprint){
			g.setFont(new Font("Forte", Font.BOLD, 15));
			g.setColor(Color.blue);
			g.drawString(this.texto,Stage.primeracolumna,Stage.menuPrimeraFila);
			
			/*
			 * High Scores:
			 */
			Marco highscores = new Marco(juego);
			highscores.setNombreSprite("menu/highscores.gif");
			if(seleccion==5){
				highscores.setNombreSprite("menu/Whighscores.gif");
			}
			highscores.setX(Stage.primeracolumna);
			highscores.setY(Stage.menuCuartaFila);
			highscores.paint(g);
			
			/*
			 * Pintamos el boton para volver al menu principal Salir:
			 */
			Marco salir = new Marco(juego);
			salir.setNombreSprite("menu/salir.gif");
			if(seleccion==7){
				salir.setNombreSprite("menu/Wsalir.gif");
			}
			salir.setX(Stage.primeracolumna);
			salir.setY(Stage.menuSeptimaFila);
			salir.paint(g);
			
		}

		/*
		 * Mostramos lo pintado
		 */
		strategy.show();

	}
	
	/**
	 * Este metodo se encarga de pintar un texto en el menu con un boton para salir.
	 * 
	 * Un vez pulses en salir te mandara a la pantalla principal.
	 * 
	 * @param texto
	 */
	public void println(String texto){
		Arkanoid.modomenu=true;
		Arkanoid.mododemo=false;
		modogeneral=false;
		modocreditos=false;
		modoopciones=false;
		modorecords=false;
		modoprint=true;
		
		this.texto=texto;
	}







	public void mouseClicked(MouseEvent arg0) {	
		/*
		 * Este seria el menu normal, sin entrar en ninguna opcion
		 */
		if(modogeneral){
			if(seleccion==1){
				/*
				 * Tiene que estar visible el continuar
				 */
				if(activarcontinuar){
					if(Arkanoid.modomenu){
						Arkanoid.modomenu=false;
					}else Arkanoid.modomenu=true;
				}
			}
			//Juego Nuevo
			if(seleccion==2){
				Arkanoid.mododemo=false; //Desactivamos el modo demo
				Arkanoid.modomenu=false; //Desactivamos el modo menu
				juego.nivel=juego.nivelinicial;
				Arkanoid.jugar=true;
				Arkanoid.vidas=Arkanoid.vidasinicial;
				Arkanoid.segundos=0;
				Arkanoid.minutos=0;
				Arkanoid.horas=0;
				Arkanoid.marcador=0;
				Arkanoid.inicio=false;
				juego.borrarNivel();
				juego.ArrancarMundo();

				/*
				 * Preguntamos el nombre del jugador
				 */
				if(preguntarnombre){
					 juego.nombre = juego.dialog.readLine("Inserte su nombre para empezar a jugar: ");
					 if(juego.nombre.equals("")){
						 juego.nombre="-";
					 }
					 preguntarnombre=false;
				 }

			}
			//Modo Demo
			else if(seleccion==3){
				Arkanoid.modomenu=false;
				Arkanoid.mododemo=true;
				juego.nivel=juego.nivelinicial;
				Arkanoid.vidas=Arkanoid.vidasinicial;
				Arkanoid.segundos=0;
				Arkanoid.minutos=0;
				Arkanoid.horas=0;
				Arkanoid.marcador=0;
				Arkanoid.inicio=true;
				juego.nombre="demo";
				preguntarnombre=true;
				juego.borrarNivel();
				juego.ArrancarMundo();

			}
			//Opciones
			else if(seleccion==4){
				modogeneral=false;
				modoopciones=true;

			}
			//High Scores
			else if(seleccion==5){
				modogeneral=false;
				modorecords=true;
			}
			//Creditos
			else if(seleccion==6){
				modogeneral=false;
				modocreditos=true;
			}
			//Salir
			else if(seleccion==7){
				System.exit(0);
			}
		}else if(modoopciones){

			/*
			 * Si estamos en modoOpciones, si clickea en Si, la cancion empezara de nuevo y si le da que no se parar�
			 */
			if(seleccion==2){ //opcion Si

				//System.out.println("Ejecutar�a sonido");//Ejecutar sonido
				if(!Menu.musicaactiva){
					try{
						Arkanoid.musica.start(); //Aplicamos sonido general
					}catch(Exception e){
						
					}
					
					musicaactiva=true;
				}
				

			}else if(seleccion==3){ //opcion No

				//System.out.println("Deber�a parase el sonido");//Paramos sonido
				Arkanoid.musica.parar(); //De momento no funciona
				musicaactiva=false;
				
			}else if(seleccion==5){
				efectosactivos = true;
				
			}else if(seleccion==6){
				efectosactivos = false;
			}
		}


		/*
		 * En los creditos en los records y en opciones tenemos un boton que es comun y que
		 * realiza la misma opcion para los tres, el boton salir. Que lo que hace es volver al menu.
		 */
		if(modocreditos || modorecords || modoopciones || modoprint){
			if(seleccion==7){
				modogeneral=true;
				modocreditos=false;
				modorecords=false;
				modoopciones=false;
				modoprint=false;
			}
		}
		
		/*
		 * En el modo print pondremos para que pueda ir directamente a ver los high scores si quiere
		 */
		if(modoprint){
			if(seleccion==5){
				modogeneral=false;
				modocreditos=false;
				modorecords=true;
				modoopciones=false;
				modoprint=false;
			}
		}
		

	}

	/**
	 * Seleccion marca en que linea estamos situados, por lo tanto determinamos los rangos
	 * y le indicamos en seleccion cual es el que tenemos seleccionado. Para que cuando hagamos
	 * click sepamos donde debe de ir. 
	 * 
	 * @param arg0
	 */
	public void mouseMoved(MouseEvent arg0) {

		if(arg0.getY()>=Stage.menuPrimeraFila && arg0.getY()<Stage.menuSegundaFila){
			seleccion=1;
		}
		else if(arg0.getY()>=Stage.menuSegundaFila && arg0.getY()<Stage.menuTerceraFila){
			seleccion=2;
		}
		else if(arg0.getY()>=Stage.menuTerceraFila && arg0.getY()<Stage.menuCuartaFila){
			seleccion=3;
		}
		else if(arg0.getY()>=Stage.menuCuartaFila && arg0.getY()<Stage.menuQuintaFila){
			seleccion=4;
		}
		else if(arg0.getY()>=Stage.menuQuintaFila && arg0.getY()<Stage.menuSextaFila){
			seleccion=5;
		}
		else if(arg0.getY()>=Stage.menuSextaFila && arg0.getY()<Stage.menuSeptimaFila){
			seleccion=6;
		}		
		else if(arg0.getY()>=Stage.menuSeptimaFila && arg0.getY()<Stage.menuOctavaFila){
			seleccion=7;
		}else seleccion=0;

	}
	
	/**
	 * Perminete moverte por el menu usando los cursores 
	 * 
	 * @param e
	 */
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_DOWN){
			if(modogeneral){
				if(seleccion<7){
					++seleccion;
				}else{
					if(activarcontinuar){
						seleccion=1;
					}else seleccion=2;
				}
			}
			else if(modorecords || modocreditos){
				seleccion=7;
			}
			else if(modoopciones){
				/*
				 * En modo opciones se mueve del:
				 * 2
				 * 3
				 * 5
				 * 6
				 * 7
				 */
				if(seleccion>=7){
					seleccion=2;
				}
				else if(seleccion==2 || seleccion==5 || seleccion==6){
					++seleccion;
				}
				else if(seleccion==3){
					seleccion=seleccion+2;
				}
				else if(seleccion<2){
					seleccion=2;
				}
				else if(seleccion!=2 && seleccion!=3 && seleccion!=5 && seleccion!=6){
					seleccion=1;
				}
				
			}

		}
		else if(e.getKeyCode()==KeyEvent.VK_UP){
			if(modogeneral){
				if(activarcontinuar){
					if(seleccion>1){
						--seleccion;
					}else seleccion=7;
				}else{
					if(seleccion>2){
						--seleccion;
					}else seleccion=7;

				}
			}
			else if(modorecords || modocreditos){
				seleccion=7;
			}
			else if(modoopciones){
				/*
				 * En modo opciones se mueve del:
				 * 2
				 * 3
				 * 5
				 * 6
				 * 7
				 */
				if(seleccion==7 || seleccion == 6 || seleccion==3 || seleccion==4){
					seleccion--;
				}
				else if(seleccion!=2 && seleccion!=3 && seleccion!=5 && seleccion!=6){
					seleccion=7;
				}else if(seleccion==5){
					seleccion=3;
				}else if(seleccion==2){
					seleccion=7;
				}
				
			}

		}
		else if(e.getKeyCode()==KeyEvent.VK_ENTER){
			mouseClicked(null);
		}
		else if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
			if(activarcontinuar){
				Arkanoid.modomenu=false;
			}
		}


	}









}
