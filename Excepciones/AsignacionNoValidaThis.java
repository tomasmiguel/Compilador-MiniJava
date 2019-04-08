package Excepciones;



	@SuppressWarnings("serial")
	public class AsignacionNoValidaThis extends Exception {
		public AsignacionNoValidaThis(String metodo, int linea,int columna){
			super("Error Semantico: la utilizacion de 'this' en metodo estatico "+metodo+" no es valida, en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}

