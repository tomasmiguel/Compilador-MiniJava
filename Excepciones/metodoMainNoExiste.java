package Excepciones;


@SuppressWarnings("serial")
public class metodoMainNoExiste extends Exception {
	public metodoMainNoExiste(){
		super("Error Semantico: No existe metodo static void main() en ninguna clase declarada del archivo fuente");
	}
}
