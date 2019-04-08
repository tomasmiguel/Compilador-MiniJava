package Excepciones;


	@SuppressWarnings("serial")
	public class ErrorArregloEncadenado extends Exception {
		public ErrorArregloEncadenado(int linea,int columna){
			super("Error Semantico: el encadenado que tiene el arreglo no es valido (los arreglos son de tipos primitivos), en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}

