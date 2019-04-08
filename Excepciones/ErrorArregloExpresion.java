package Excepciones;


	@SuppressWarnings("serial")
	public class ErrorArregloExpresion extends Exception {
		public ErrorArregloExpresion(int linea,int columna){
			super("Error Semantico: la expresion usada en el arreglo no es de tipo entero, en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}
