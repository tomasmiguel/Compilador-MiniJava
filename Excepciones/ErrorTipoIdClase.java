package Excepciones;


	@SuppressWarnings("serial")
	public class ErrorTipoIdClase extends Exception {
		public ErrorTipoIdClase(String nombre,int linea,int columna){
			super("Error Semantico: la entidad que invoca a '"+nombre+"' no es un objeto, en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}