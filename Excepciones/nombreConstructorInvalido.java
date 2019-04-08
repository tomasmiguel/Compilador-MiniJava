package Excepciones;


@SuppressWarnings("serial")
public class nombreConstructorInvalido extends Exception {
	public nombreConstructorInvalido(String nombreCtor, int linea, int columna){
		super("Error Semantico: El nombre del constructor '"+nombreCtor+"' es distinto al nombre de la clase que lo contiene, en linea "+linea+" y columna "+columna+" del archivo fuente");
	}
}
