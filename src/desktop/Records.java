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

import java.io.*;


/**
 * Esta clase se encarga de manegar los records del Arkanoid, trabaja con el archivo resultados.txt
 * Lee los datos y escribe los datos nuevos. Hay que tener en cuenta que el archivo de entrada ya
 * se encuentra ordenado segun los resultados, siendo el primer valor el de mayor puntuacion.
 */
public class Records {

	/*
	 * Direccion del archivo donde guardamos las marcas
	 */
	private static String PATH="datos/resultados.txt";  

	/*
	 * Numero maximo de records
	 */
	private int maxrecords=10;

	/*
	 * Lista con los resultados cargados en memoria
	 */
	String[][]resultados=new String[2][maxrecords];


	private acm.io.IODialog dialog = new acm.io.IODialog(); //para crear dialogos

	public String nombre;
	public int resultado;
	public int posicion;



	/**
	 * Constructor de la clase records, se le pasa el nombre del jugador y su resultado.
	 * 
	 * @param nombrejugador
	 * @param marca
	 */
	public Records(String nombrejugador,int marca, int horas, int minutos, int segundos){
		nombre=nombrejugador;
		resultado=marca;
	}


	/**
	 * Realiza la comprobacion de los records y los almacena en caso de que sea record.
	 *
	 */
	public void start(){
		/*
		 * Leemos los datos y los cargamos en la memoria temporal
		 */
		leerDatos();
		/*
		 * Si el resultado obtenido es mejor que el ultimo tendremos que modificar el Archivo
		 */
		if(comprobarDatos()){
			modificarMemoria();
			escribirDatos();
		}
		/*
		 * Mostramos los resultados.
		 */
		//imprimirDatos();
	}






	/**
	 * Se encarga de leer los datos almacenados en el archivo
	 *
	 */
	public void leerDatos(){
		/*
		 * Creamos el input para leer el archivo
		 */
		try {

			System.setIn(new java.io.FileInputStream(new java.io.File(PATH)));
		} catch (java.io.FileNotFoundException e) {

			e.printStackTrace(); 
			System.exit(0);     
		}
		java.util.Scanner input = new java.util.Scanner(System.in);

		int i=0; //Almacena la posicion del record: 0-> Primero 1->Segundo...
		/*
		 * Esta while se encarga de cargar en resultados los datos del archivo
		 */
		while(input.hasNext() && i<maxrecords){
			/*
			 * Cargamos el nombre del jugador
			 */
			resultados[0][i]=input.next();
			/*
			 * Cargamos el resultado
			 */
			if(input.hasNextInt()){
				resultados[1][i]=input.next();
			}
			//System.out.println(resultados[0][i]+" "+resultados[1][i]);
			++i;

		}


	}

	/**
	 * Actualiza el archivo con los datos 
	 *
	 */
	public void escribirDatos(){
		File archivo= new File(PATH);
		PrintStream escribir=null;
		try {
			escribir = new PrintStream(archivo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		/*
		 * Recorremos todo lo que hay en la memoria y lo escribimos en el archivo
		 */
		for(int i=0;i<maxrecords;++i){
			if(resultados[0][i]!=null){
				escribir.println(resultados[0][i]+" "+resultados[1][i]);
			}
		}





	}

	/**
	 * Devuelve true si hay que modificar los datos, devuelve false si no se ha batido ningun record.
	 *
	 */
	public boolean comprobarDatos(){
		/*
		 * Recorremos todo el array y miramos aver si hay algun resultado mas pequenyo que el obtenido
		 * si es asi devolvemos true.
		 * En caso contrario false.
		 */
		for(int i=0;i<maxrecords;++i){
			//System.out.println(resultados[1][i]);
			if(resultados[1][i]==null){
				return true;
			}
			if(Integer.parseInt(resultados[1][i])<resultado){
				return true;
			}
		}
		return false;
	}


	/**
	 * Se encarga de almacenar en el array en la posicion adecuada el record nuevo y
	 * borrar el mas viejo.
	 *
	 */
	public void modificarMemoria(){

		boolean seguir=true; //Controla cuando se encuentra el sitio donde hay que registrarlo
		/*
		 * Empezamos a comprobar desde los valores mas altos.
		 */
		int i=0; 
		while(seguir){
			/*
			 * Si encontramos vacio es que se a acabado el archivo, entonces insertamos directamente
			 * el resultado y paramos la busqueda
			 */
			if(resultados[1][i]==null){
				seguir=false; //Paramos la busqueda
				resultados[0][i]=nombre; //Guardamos el nombre
				resultados[1][i]=Integer.toString(resultado); //Guardamos el resultado
				posicion=i; //Guardamos la posicion
			}

			/*
			 * Si es mayor el resultado obtenido que el que teniamos modificamos el array
			 */
			else if(Integer.parseInt(resultados[1][i])<=resultado){
				seguir=false; //Ya lo hemos encontrado
				posicion=i; //Guardamos la posicion en la que a quedado
				i=maxrecords-1; //Lo inicializamos en la ultima posicion del array(es la nueve porque el maximo son 10 por defecto)
				/*
				 * Ahora lo que vamos a hacer es empezar desde atras a mover todos los valores para asi no
				 * machacar ningun resultado.
				 * El penultimo pasara a ser el ultimo y asi sucesivamente hasta que llegemos a la posicion
				 * en la que hay que insertar el resultado obtenido.
				 */
				while(i>posicion){
					resultados[0][i]=resultados[0][i-1];
					resultados[1][i]=resultados[1][i-1];
					--i;
				}
				//Guardamos en la posicion adecuada la nueva marca.
				resultados[0][i]=nombre;
				resultados[1][i]=Integer.toString(resultado);

			}
			++i;	

		}

		/*
		 * Adaptamos la posicion para que sea mas visible, osea que el primero sea el 1 el segundo el 2...
		 */
		posicion=posicion+1;

		//Imprirmir la memoria
		/*for(int u=0;u<maxrecords;++u){
			System.out.println(resultados[0][u]+" "+resultados[1][u]);
		}*/

		//System.out.println("Posicion: "+posicion);

	}


	/**
	 * Se encarga de mostrar por pantalla los datos
	 *
	 */
	public void imprimirDatos(){
		/*
		 * Primero leemos los datos
		 */
		leerDatos();


		StringBuilder cadena = new StringBuilder();
		for(int i=0;i<maxrecords;++i){
			//System.out.println(resultados[0][i]);
			if(resultados[0][i]!=null){
				char retornocarro='\n';
				cadena.append(resultados[0][i].toString());
				cadena.append("--->");
				cadena.append(resultados[1][i].toString());
				cadena.append(retornocarro);
			}
		}
		dialog.println(cadena);


	}

	public String[][] getDatos(){
		/*
		 * Primero leemos los datos
		 */
		leerDatos();
		return resultados;

	}

	public int getMaxRecords(){
		return maxrecords;
	}


}
