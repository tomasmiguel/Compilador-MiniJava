package Excepciones;


@SuppressWarnings("serial")
public class ErrorDeTiposExpBin extends Exception {
		public ErrorDeTiposExpBin(String op,int linea,int columna){
			super("Error Semantico: tipos no compatibles en la expresion binaria con operador "+op+" (deben ser tipos relacionados por herencia), en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
}

