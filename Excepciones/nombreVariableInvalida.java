package Excepciones;


@SuppressWarnings("serial")
public class nombreVariableInvalida extends Exception {
	public nombreVariableInvalida(String nombreVar, int linea, int columna){
		super("Error Semantico: El nombre de la variable '"+nombreVar+"' ya existe en la misma clase o en alguna clase heredada, error en linea "+linea+" y columna "+columna+" del archivo fuente");
	}
}
