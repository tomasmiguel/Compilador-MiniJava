package Excepciones;


@SuppressWarnings("serial")
public class constructorExistente extends Exception {
	public constructorExistente(String ctor, int linea, int columna){
		super("Error Semantico: El constructor '"+ctor+"' ya existe (con esa cantidad de argumentos) en esa clase, error en linea "+linea+" y columna "+columna+" del archivo fuente");
	}
}
