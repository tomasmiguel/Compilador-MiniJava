package Excepciones;



	@SuppressWarnings("serial")
	public class AccesoVarInexistente extends Exception {
		public AccesoVarInexistente(String clase, String nombre,int linea,int columna){
			super("Error Semantico: la entidad '"+nombre+"' a la que intenta acceder no existe o no es visible desde clase '"+clase+"', en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}