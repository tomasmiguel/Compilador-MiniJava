package Excepciones;



	@SuppressWarnings("serial")
	public class AsignacionNoValidaMetodo extends Exception {
		public AsignacionNoValidaMetodo(String metodo, int linea,int columna){
			super("Error Semantico: el metodo "+metodo+" no puede ser el lado izquierdo de una asignacion, en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}
