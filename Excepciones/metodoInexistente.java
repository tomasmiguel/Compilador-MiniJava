package Excepciones;


	@SuppressWarnings("serial")
	public class metodoInexistente extends Exception {
		public metodoInexistente(String nombre,int linea,int columna){
			super("Error Semantico: el metodo '"+nombre+"' invocado no existe con ese tipo de parametros, en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}
