package Excepciones;


@SuppressWarnings("serial")
public class claseNoDeclarada extends Exception {
	public claseNoDeclarada(String clase,int linea, int columna){
		super("Error Semantico: La clase '"+clase+"' no fue declarada, en linea "+linea+" y columna "+columna+" del archivo fuente");
	}
}