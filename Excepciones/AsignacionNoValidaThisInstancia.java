package Excepciones;


	@SuppressWarnings("serial")
	public class AsignacionNoValidaThisInstancia extends Exception {
		public AsignacionNoValidaThisInstancia(String metodo, int linea,int columna){
			super("Error Semantico: la utilizacion de atributos de instancia en metodo estatico "+metodo+" no es valida, en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}
