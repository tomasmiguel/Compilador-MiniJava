package Excepciones;


@SuppressWarnings("serial")
public class claseDeclaradaInvalida extends Exception {
	public claseDeclaradaInvalida(String clase, int linea, int columna){
		super("Error Semantico: La clase '"+clase+"' ya existe y no puede existir otra con el mismo nombre, en linea "+linea+" y columna "+columna+" del archivo fuente");
	}
}