package Excepciones;


	@SuppressWarnings("serial")
	public class ErrorTipoExpresion extends Exception {
		public ErrorTipoExpresion(int linea,int columna){
			super("Error Semantico: la expresion usada dentro del arreglo no es de tipo entera, en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}
