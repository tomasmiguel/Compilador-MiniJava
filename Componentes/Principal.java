package Componentes;
public class Principal {
	public static void main (String args[]){
		
		Entrada entrada=null;
		
		if (args.length<=1 || args.length>2) {
			System.out.println("\n Cantidad de parametros ingresados incorrecta\nSintaxis esperada:  <program_name><In_file> <Out_file> \n ");
			System.exit(10);
		}
		String archEntrada= args[0];
		String archSalida= args[1];
		
		
		
		entrada= new Entrada();
		entrada.cargarInput(archEntrada);
		
		//aSin crea al analizador Lexico, y le pasa por parametro la entrada 
		Analizador_Sintactico aSin= new Analizador_Sintactico(entrada);
		
		try {
			//comienzo el analisis sintactico
			aSin.start(archSalida);
		
			///si llegue aca es porque alcance el fin de archivo....
			
			//entrada.escribir(cadena);
			
			System.out.println("\nNo se detectaron errores lexicos, sintacticos, ni semanticos    \n");
			
		} catch (Exception e) {
			// capturo excepciones lexicas o sintacticas y muestro el mensaje
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		// cierro el archivo de entrada
		entrada.cerrarArchivoInput();
	}//fin main	
}//fin Principal