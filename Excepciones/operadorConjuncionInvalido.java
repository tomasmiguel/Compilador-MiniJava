package Excepciones;


@SuppressWarnings("serial")
public class operadorConjuncionInvalido extends Exception {
	public operadorConjuncionInvalido(int linea, int columna){
		super("Error lexico: operador & invalido, en linea "+linea+" y columna "+columna+" del archivo fuente. Tal vez quiso decir &&");
	}
}