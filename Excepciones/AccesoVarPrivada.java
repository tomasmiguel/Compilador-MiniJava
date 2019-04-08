package Excepciones;



	@SuppressWarnings("serial")
	public class AccesoVarPrivada extends Exception {
		public AccesoVarPrivada(String clase,String nombre,int linea,int columna){
			super("Error Semantico: la variable '"+nombre+"' a la que intenta acceder no es visible en clase "+clase+", en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}
