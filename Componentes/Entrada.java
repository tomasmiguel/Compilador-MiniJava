package Componentes;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


// Genera el buffer de lectura a partir del path recibido
public class Entrada {
	private BufferedReader br;
	
	
	//Constructor
	public Entrada() {
		br=null;
	}
		
	//Setters
	public void cargarInput(String archEntrada) {
			try {
				br = new BufferedReader(new FileReader(archEntrada));
			} catch (FileNotFoundException e) {
				System.out.println("File Error: No se pudo cargar el buffer para lectura del archivo Input. Posiblemente el archivo no existe o se encuentra en otro directorio\n");
				System.exit(20);
			}
	}
	public void cerrarArchivoInput() {
		try {
			br.close();
		} catch (IOException e) {
			System.out.println("File Error: No se pudo cerrar el archivo Input.\n");
		}
	}
	
	//Getters
	public BufferedReader getEntrada() {
		return br;
	}
	
	
}
