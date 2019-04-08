package Excepciones;


	@SuppressWarnings("serial")
	public class ctorNoDeclarado extends Exception {
		public ctorNoDeclarado(String nombre,int linea,int columna){
			super("Error Semantico: el constructor '"+nombre+"' invocado no existe con esa aridad o tipos de parametros, en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}