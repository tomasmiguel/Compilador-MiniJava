package Excepciones;


@SuppressWarnings("serial")
public class literalStringSinCerrar extends Exception {
	public literalStringSinCerrar(int linea,int columna, String lexema){
		super("Error lexico: literal String sin cerrar (sin \"), en linea "+linea+" y columna "+columna+" del archivo fuente");
	}
}