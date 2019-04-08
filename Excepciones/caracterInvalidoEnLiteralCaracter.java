package Excepciones;


@SuppressWarnings("serial")
public class caracterInvalidoEnLiteralCaracter extends Exception {
	public caracterInvalidoEnLiteralCaracter(int linea,int columna, String lexema){
		super("Error lexico: caracter NO valido en literal caracter, en linea "+linea+" y columna "+columna+" del archivo fuente se ingreso: "+lexema+" en ASCII");
	}
}
