package Excepciones;


@SuppressWarnings("serial")
public class literalCaracterSinCerrar extends Exception {
	public literalCaracterSinCerrar(int linea,int columna){
		super("Error lexico: literal caracter sin cerrar (sin '), en linea "+linea+" y columna "+columna+" del archivo fuente");
	}
}
