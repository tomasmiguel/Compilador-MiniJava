package Excepciones;


	@SuppressWarnings("serial")
	public class ErrorReturn extends Exception {
		public ErrorReturn(String nombre,String tipo,String esperado, int linea,int columna){
			super("Error Semantico: el tipo de retorno "+tipo+" en el metodo '"+nombre+"' es invalido (se esperaba tipo "+esperado+"), en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
	}