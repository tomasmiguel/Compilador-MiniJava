package Excepciones;


@SuppressWarnings("serial")
public class redefinicionMetodoInvalido extends Exception {
	public redefinicionMetodoInvalido(String cadena, int linea, int columna){
		super("Error Semantico: "+cadena+", en linea "+linea+" y columna "+columna+" del archivo fuente");
	}
}