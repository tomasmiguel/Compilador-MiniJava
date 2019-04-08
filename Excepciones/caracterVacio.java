package Excepciones;


@SuppressWarnings("serial")
public class caracterVacio extends Exception {
	public caracterVacio(int linea,int columna, String lexema){
		super("Error lexico: el literal caracter vacio '' NO es un literal valido, en linea "+linea+" y columna "+columna+" del archivo fuente se ingreso: "+lexema);
	}
}
