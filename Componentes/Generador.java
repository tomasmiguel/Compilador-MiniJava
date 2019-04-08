package Componentes;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Generador {
	private BufferedWriter bw;
	public static int contadorIf;
	public static int contadorWhile;
	public static int contadorString;
	private HashMap<Integer,String> listaDw;
	private HashMap<Integer,String> comentariosDw;
	
	
	//Constructor
		public Generador(String nombreSalida) throws IOException {
			bw= new BufferedWriter(new FileWriter(nombreSalida));
			contadorIf=contadorWhile= contadorString= 0;
			
			listaDw= new HashMap<Integer,String>();
			comentariosDw= new HashMap<Integer,String>();
			
			//genero codigo del main
			generar(".CODE"," ");
			generar("PUSH "+Analizador_Sintactico.TS.getMain().getEtiqueta()," ");
			generar("CALL"," ");
			generar("HALT"," ");
			
			//genero codigo de las rutinas
			//generarRutinas();
		}
		
	public void generarRutinas() {
		generar("simple_heap_init: "," ");
		generar("RET 0"," ");
		
		generar("simple_malloc: "," ");
		generar("LOADFP ","inicializa la unidad");
		generar("LOADSP "," ");
		generar("STOREFP ","fin inicializacion del RA");
		generar("LOADHL "," hl");
		generar("DUP ","hl");
		generar("PUSH 1","1");
		generar("ADD "," hl+1");
		generar("STORE 4","Guarda el resultado (un puntero a la primer celda de la región de memoria)");
		generar("LOAD 3"," Carga la cantidad de celdas a alojar (parámetro que debe ser positivo)");
		generar("ADD"," ");
		generar("STOREHL ","Mueve el heap limit (hl). Expande el heap");
		generar("STOREFP "," ");
		generar("RET 1","Retorna eliminando el parámetro");
		generar("NOP ","#####################################################");
	}	
		
		
	public void gen_DW(String instr, String comm, int offset) {
		listaDw.put(offset, instr);
		comentariosDw.put(offset, comm);
		
	}
	
	public void agregar_DW() {
		for (int i=0;i< listaDw.size();i++) {
			generar(listaDw.get(i),comentariosDw.get(i));
		}
		listaDw= new HashMap<Integer,String>();
		comentariosDw= new HashMap<Integer,String>();
	}
		
	//Setters
		public void generar(String instr, String comm) {
			try {
				bw.write(instr);
				bw.write("\t\t\t\t\t\t\t #"+comm);
				bw.newLine();
			} catch (IOException e) {
				System.out.println("File Error: No se pudo escribir en el archivo Output.\n");
			}
		}
		
		
		
		
		public void cerrarArchivoOutput() {
			try {
				bw.close();
			} catch (IOException e) {
				System.out.println("File Error: No se pudo cerrar el archivo Output.\n");
			}
		}
	
}
