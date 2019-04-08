package Excepciones;


@SuppressWarnings("serial")
public class metodoMainExistenteEnClase extends Exception {
	public metodoMainExistenteEnClase(int linea, int columna){
		super("Error Semantico: El metodo static void main() ya existe y no puede declararse otro con mismo modificador, tipo de retorno, nombre y aridad, en linea "+linea+" y columna "+columna+" del archivo fuente");
	}
}
