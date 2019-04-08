package Excepciones;


@SuppressWarnings("serial")
public class ErrorDeTiposExpUn extends Exception {
		public ErrorDeTiposExpUn(String op,int linea,int columna){
			super("Error Semantico: tipo no compatible en la expresion unaria "+op+", en linea "+linea+" y columna "+columna+" del archivo fuente");
		}
}
