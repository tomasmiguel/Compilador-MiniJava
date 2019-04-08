package Excepciones;


	@SuppressWarnings("serial")
	public class InvocacionInvalida extends Exception {
		public InvocacionInvalida(String nombre,int linea,int columna){
			super("Error Semantico: el metodo '"+nombre+"' es dinamico y no puede ser invocado desde un contexto estatico, en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}
