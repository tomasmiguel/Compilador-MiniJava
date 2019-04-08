package Excepciones;


	@SuppressWarnings("serial")
	public class ErrorDeTiposExpBinBoolean extends Exception {
			public ErrorDeTiposExpBinBoolean(String op,int linea,int columna){
				super("Error Semantico: tipos no compatibles en la expresion binaria con operador "+op+" (deben ser de tipo boolean), en linea "+linea+" y columna "+columna+" del archivo fuente");
			}
	}