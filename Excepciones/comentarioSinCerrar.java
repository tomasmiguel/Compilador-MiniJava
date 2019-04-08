package Excepciones;


@SuppressWarnings("serial")
public class comentarioSinCerrar extends Exception {
	public comentarioSinCerrar(int linea,int columna){
		super("Error lexico: comentario /* sin su correspondiente */, en linea "+linea+" y columna "+columna+" del archivo fuente");
	}
}
