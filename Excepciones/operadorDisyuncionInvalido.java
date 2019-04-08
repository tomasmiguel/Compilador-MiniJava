package Excepciones;


@SuppressWarnings("serial")
public class operadorDisyuncionInvalido extends Exception {
	public operadorDisyuncionInvalido(int linea,int columna){
		super("Error lexico: operador | invalido, en linea "+linea+" y columna "+columna+" del archivo fuente. Tal vez quiso decir ||");
	}
}
