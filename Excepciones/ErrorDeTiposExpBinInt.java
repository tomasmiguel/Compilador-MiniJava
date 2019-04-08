package Excepciones;


	@SuppressWarnings("serial")
	public class ErrorDeTiposExpBinInt extends Exception {
			public ErrorDeTiposExpBinInt(String op,int linea,int columna){
				super("Error Semantico: tipos no compatibles en la expresion binaria con operador "+op+" (deben ser de tipo entero), en linea "+linea+" y columna "+columna+" del archivo fuente");
			}
	}