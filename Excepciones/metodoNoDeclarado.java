package Excepciones;


	@SuppressWarnings("serial")
	public class metodoNoDeclarado extends Exception {
		public metodoNoDeclarado(String clase,String nombre,int aridad, int linea,int columna){
			super("Error Semantico: el metodo '"+nombre+"' con aridad "+aridad+" no existe en la clase "+clase+", en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}
