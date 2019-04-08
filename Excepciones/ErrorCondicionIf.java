package Excepciones;


@SuppressWarnings("serial")
public class ErrorCondicionIf extends Exception {
	public ErrorCondicionIf(int linea,int columna){
		super("Error Semantico: la condicion del if no es valida (debe ser de tipo booleana), en linea "+linea+" y columna "+columna+" del archivo fuente");
	}
}
