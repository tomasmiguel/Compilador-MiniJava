package Excepciones;


	@SuppressWarnings("serial")
	public class ErrorCtorConReturn extends Exception {
		public ErrorCtorConReturn(String nombre, int linea,int columna){
			super("Error Semantico: el constructor '"+nombre+"' es invalido por tener un tipo de retorno, en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}
