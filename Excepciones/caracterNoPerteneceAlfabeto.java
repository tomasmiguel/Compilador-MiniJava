package Excepciones;


@SuppressWarnings("serial")
public class caracterNoPerteneceAlfabeto extends Exception {
	public caracterNoPerteneceAlfabeto(int linea,int columna, String lexema){
		super("Error lexico: caracter no correspondiente al alfabeto, en linea "+linea+" y columna "+columna+" del archivo fuente se ingreso: "+lexema+" en ASCII");
	}
}
