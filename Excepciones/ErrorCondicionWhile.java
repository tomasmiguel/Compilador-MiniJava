package Excepciones;


	@SuppressWarnings("serial")
	public class ErrorCondicionWhile extends Exception {
		public ErrorCondicionWhile(int linea,int columna){
			super("Error Semantico: la condicion del while no es valida, en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}