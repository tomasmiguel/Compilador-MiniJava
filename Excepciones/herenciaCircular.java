package Excepciones;


@SuppressWarnings("serial")
public class herenciaCircular extends Exception {
	public herenciaCircular(String clase, int linea, int columna){
		super("Error Semantico: La clase '"+clase+"' tiene herencia circular (hereda de si misma), en linea "+linea+" y columna "+columna+" del archivo fuente");
	}
}
