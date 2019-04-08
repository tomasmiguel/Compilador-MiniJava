package Excepciones;


	@SuppressWarnings("serial")
	public class AsignacionThisSolo extends Exception {
		public AsignacionThisSolo(String metodo, int linea,int columna){
			super("Error Semantico: la asignacion del metodo "+metodo+" no puede tener solo a 'this' como lado izquierdo, en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}
