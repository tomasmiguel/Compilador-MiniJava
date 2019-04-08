package Excepciones;


	@SuppressWarnings("serial")
	public class ErrorTiposAsignacion extends Exception {
			public ErrorTiposAsignacion(String tipoI,String tipoD,int linea,int columna){
				super("Error Semantico: tipos no compatibles en la expresion de asignacion, se esperaba algun subtipo de "+tipoI+" y se recibio tipo "+tipoD+", en linea "+linea+" y columna "+columna+" del archivo fuente");
			}
	}
