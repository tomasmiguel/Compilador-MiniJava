package Excepciones;



@SuppressWarnings("serial")
public class metodoDeclaradoInvalido extends Exception {
	public metodoDeclaradoInvalido(String metodo, int linea, int columna){
		super("Error Semantico: El metodo '"+metodo+"' ya existe con esa aridad en esa clase, error en linea "+linea+" y columna "+columna+" del archivo fuente");
	}
}
