package Componentes;



import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import Excepciones.caracterInvalidoEnLiteralCaracter;
import Excepciones.caracterNoPerteneceAlfabeto;
import Excepciones.caracterVacio;
import Excepciones.comentarioSinCerrar;
import Excepciones.literalCaracterSinCerrar;
import Excepciones.literalStringSinCerrar;
import Excepciones.operadorConjuncionInvalido;
import Excepciones.operadorDisyuncionInvalido;

public class Analizador_Lexico {
	private BufferedReader br;
	private String lexemaActual;
	private String palabraClave;
	private byte estado;
	private int archNroLinea, archNroColumna;
	private int charActual;
	private char aux; // utilizado para guardar el caracter \ por si luego se lee el caracter n
	private boolean iterar;
	private int lineaInicioError;
	private HashMap<String, String> palabras_clave_map;
	
//Constructor con la clase Entrada para manejar el archivo
	public Analizador_Lexico(Entrada e) {
		br=e.getEntrada();
		lexemaActual="";
		palabraClave="";
		charActual=0;
		estado= 0;
		archNroColumna=1;
		archNroLinea=lineaInicioError=1;
		iterar=true;
		palabras_clave_map= new HashMap<String,String>(22); //capacidad inicial = 22
		aux=0;
		cargarTabla();
	}
	
	//Tabla de palabras clave
	private void cargarTabla() {
		palabras_clave_map.put("class","class");
		palabras_clave_map.put("extends","extends");
		palabras_clave_map.put("static","static");
		palabras_clave_map.put("dynamic","dynamic");
		palabras_clave_map.put("String","String");
		palabras_clave_map.put("boolean","boolean");
		palabras_clave_map.put("char","char");
		palabras_clave_map.put("int","int");
		palabras_clave_map.put("public","public");
		palabras_clave_map.put("private","private");
		palabras_clave_map.put("void","void");
		palabras_clave_map.put("null","null");
		palabras_clave_map.put("if","if");
		palabras_clave_map.put("else","else");
		palabras_clave_map.put("while","while");
		palabras_clave_map.put("return","return");
		palabras_clave_map.put("this","this");
		palabras_clave_map.put("new","new");
		palabras_clave_map.put("true","true");
		palabras_clave_map.put("false","false");
	}
	
	// Concatena el caracter actual a la cadena lexema, y lee si es posible el proximo caracter del archivo. Sino se muestra por 
	// pantalla un error de tipo IOException
	private void consumir(){
		try {
			lexemaActual+= (char)charActual;
			charActual= br.read();
		}
		catch(IOException e) {
			System.out.println("Error al leer un caracter, en linea "+archNroLinea+" y columna "+archNroColumna+", del archivo fuente.\n");
			System.exit(100);
		}
		
	}
	
	// Procesa el caracter leido por parametro y avanza a otro estado en caso de ser necesario. Si detecta un caracter 
	// no valido, lanza una excepcion de tipo caracterNoPerteneceAlfabeto
	private void estadoE0(int Actual)throws caracterNoPerteneceAlfabeto, IOException{
		//digito
		if (Actual >= 48 && Actual<=57) {
			consumir();
			estado=35;
		}
		//mayuscula
		else if (Actual >= 65 && Actual <=90) {
			consumir();
			estado=1;
		}
		//minuscula
		else if (Actual >= 97 && Actual <= 122) {
			consumir();
			estado=2;
		}
		// /
		else if (Actual == 47) {
			consumir();
			estado=3;
		}
		// *
		else if (Actual == 42){
			consumir();
			estado=6;
		}
		// (
		else if (Actual == 40){
			consumir();
			estado=8;
		}
		// )
		else if (Actual == 41){
			consumir();
			estado=9;
		}
		// {
		else if (Actual == 123){
			consumir();
			estado=10;
		}
		// }
		else if (Actual == 125){
			consumir();
			estado=11;
		}
		// ;
		else if (Actual == 59){
			consumir();
			estado=12;
		}
		// , 
		else if (Actual == 44){
			consumir();
			estado=13;
		}
		// .
		else if (Actual == 46){
			consumir();
			estado=14;
		}
		// [
		else if (Actual == 91){
			consumir();
			estado=15;
		}
		// ]
		else if (Actual == 93){
			consumir();
			estado=16;
		}
		// >
		else if (Actual == 62){
			consumir();
			estado=25;
		}
		// <
		else if (Actual == 60){
			consumir();
			estado=23;
		}
		// =
		else if (Actual == 61){
			consumir();
			estado=22;
		}
		// !
		else if (Actual == 33){
			consumir();
			estado=17;
		}
		// +
		else if (Actual == 43){
			consumir();
			estado=24;
		}
		// -
		else if (Actual == 45){
			consumir();
			estado=18;
		}
		// &
		else if (Actual == 38){
			consumir();
			estado=27;
		}
		// |
		else if (Actual == 124){
			consumir();
			estado=32;
		}
		// '
		else if (Actual == 39){
			consumir();
			estado=36;
		}
		// "
		else if (Actual == 34){
			consumir();
			estado=21;
		}
		// espacio, tab o salto linea							//windows
		else if (Actual == 32 || Actual == 9 || Actual == 10 || Actual == 13){
				//10 (enter) siempre esta
				if (Actual == 10) {
					archNroLinea++;
					archNroColumna=0;
				}

				charActual = br.read();
		}
		//fin archivo
		else if (Actual== -1) {
			estado=-1;
		}
		//otro tipo de caracteres -> error
		else {
			throw new caracterNoPerteneceAlfabeto(archNroLinea,archNroColumna,lexemaActual+"<"+charActual+">");
		}	
	}//fin metodo estadoE0
	
		
	
	//Devuelve proximo token encontrado en el archivo fuente. Lanza excepcion ante algun caso invalido.
	public Token nextToken()throws caracterInvalidoEnLiteralCaracter, caracterNoPerteneceAlfabeto,caracterVacio,comentarioSinCerrar,literalCaracterSinCerrar,literalStringSinCerrar,operadorConjuncionInvalido,operadorDisyuncionInvalido{
		try {
			lexemaActual="";
			
			//primera lectura
			if (charActual==0) {
				charActual= br.read();
			}
			
			while (iterar) {
				
				switch(estado) {    
			    	//fin archivo
			    	case(-1):{
			    		iterar=false;
			    		break;
			    	}
			    	//estado inicial
					case (0):{ 	
						estadoE0(charActual);
						break;
					}
					case(1):{ 
						//idClase: mayus, minus, digito o underscore
						if ((charActual >= 65 && charActual <= 90) || (charActual >= 97 && charActual <= 122) || (charActual >= 48 && charActual <= 57) || charActual==95) {
							consumir();
							break;
						}
						else {
							estado=0;
							palabraClave= palabras_clave_map.get(lexemaActual);
							if ( palabraClave != null)
								return new Token (palabraClave,lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
							else
								return new Token("idClase",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
						}
					}
					case(2):{ 
						// idMetVar: mayus, minus, digito o underscore
						if ((charActual >= 65 && charActual <= 90) || (charActual >= 97 && charActual <= 122) || (charActual >= 48 && charActual <= 57) || charActual==95) {
							consumir();
							break;
						}
						else {
							estado=0;
							palabraClave= palabras_clave_map.get(lexemaActual);
							if (palabraClave != null) {
								//es palabra clave
								return new Token (palabraClave,lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
							}
							else 
								//identificador de metodo o variable
								return new Token ("idMetVar",lexemaActual,archNroLinea,archNroColumna- lexemaActual.length());
						}
					}
					case(3):{ 
						// /
						if (charActual == 47) {
							consumir();
							estado=4;
							break;
						}
						// *
						else if (charActual == 42) {
								consumir();
								lineaInicioError= archNroLinea;
								estado= 5;
								break;
						}
						else{
							estado=0;
							return new Token("/",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
						}
					}
					case(4):{ 
						// ante // --> ignorar toda la linea
						while (charActual != '\n' && charActual != -1)
							charActual= br.read();
						
						estado=0;
						lexemaActual="";
						if (charActual != -1) {
							archNroLinea++;
							archNroColumna=0;
							charActual= br.read();
							break;
						}
						else {
							//fin archivo
							charActual=-1;
							break;
						}
					}
					case(5):{
						//ignorar todo hasta proximo * o fin archivo
						while ((charActual != '*') && (charActual != -1)){
							if (charActual ==10) {
								archNroLinea++;
								archNroColumna=0;
							}
							charActual= br.read();
							archNroColumna++;
						}
						//borro la / acumulada
						lexemaActual="";
						if (charActual == '*') {
							//veo si al * le sigue /
							archNroColumna++;
							if (br.read()=='/') {
								charActual= br.read();
								estado=0;
								break;
							}
							else {
								// seguir consumiendo en estado 5, hasta que encuentre */ o eof
								charActual= br.read();
								break;
							}
						}
						else{ //fin archivo -> error lexico
							throw new comentarioSinCerrar(lineaInicioError,archNroColumna);
						}
					}
					case(6):{ 
						estado=0;
						return new Token("*",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}
					case(8):{ 
						//solo aceptar, hasta e16
						estado=0;
						return new Token("(",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}
					case(9):{
						estado=0;
						return new Token(")",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}	
					case(10):{
						estado=0;
						return new Token("{",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}	
					case(11):{
						estado=0;
						return new Token("}",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}	
					case(12):{
						estado=0;
						return new Token(";",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}	
					case(13):{
						estado=0;
						return new Token(",",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}	
					case(14):{
						estado=0;
						return new Token(".",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}	
					case(15):{
						estado=0;
						return new Token("[",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}	
					case(16):{
						estado=0;
						return new Token("]",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}	
					case(17):{ 
						// =
						if (charActual == 61) {
							consumir();
							estado= 19;
							break;
						}
						else {
							estado=0;
							return new Token("!",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
						}
					}
					case(18):{
						estado=0;
						return new Token("-",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}
					case(19):{
						estado=0;
						return new Token("!=",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}
					case(20):{
						estado=0;
						return new Token("==",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}
					case(21):{
						// si es "
						 if (charActual == 34) {
								consumir();
								estado= 31;
								break;
							}
						 // si es \
						 else if (charActual== 92) {
							 //guardo el caracter barra por si el proximo caracter es n para imprimir salto de linea, o t para agregar el tab.
							 aux='\\';
							 charActual= br.read();
							 break;
						 }
						 // si es n, chequear que aux no sea \
						 else if (charActual == 110) {
							 // si aux es \, agrego salto de linea al String
							 if (aux==92) {
								 lexemaActual+="\n";
								 charActual= br.read();
								 aux=0;
								 break;
							 }
							 //sino concateno n normalmente, y me quedo leyendo el resto de la cadena.
							 else {
								 consumir();
								 break;
							 }
						 }
						// si es salto de linea o  algun caracter NO imprimible, distinto de tab (ASCII 9)
						else if (charActual == 10) {  
							estado=0;
							throw new literalStringSinCerrar(archNroLinea,archNroColumna,lexemaActual+=charActual);
						}
						else if ((charActual<32 && charActual != 9) || charActual >126){
							estado=0;
							throw new caracterNoPerteneceAlfabeto(archNroLinea,archNroColumna,lexemaActual+"<"+charActual+">");
						}	
						
						//si es t, concateno ademas el \ que tengo en aux. 
						else if (charActual == 116 && aux=='/'){
							lexemaActual+=aux;
							aux=0;
							consumir();
							break;
						}
						//cualquier otro digito imprimible (ASCII: del 32 al 126) menos ", salto de linea.
						else {
							consumir();
							break;
						}
					}
					case(22):{ 
						// =
						if (charActual == 61) {
							consumir();
							estado=20;
							break;
						}
						else {
							estado=0;
							return new Token("=",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
						}
					}
					case(23):{ 
						// =
						if (charActual == 61) {
							consumir();
							estado=34;
							break;
						}
						else {
							estado=0;
							return new Token("<",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
						}
					}
					case(24):{
						estado=0;
						return new Token("+",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}
					case(25):{ 
						// =
						if (charActual == 61) {
							consumir();
							estado= 39;
							break;
						}
						else {
							estado=0;
							return new Token(">",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
						}
					}
					case(26):{
						estado=0;
						return new Token("&&",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}
					case(27):{
						// &
						if (charActual== 38) {
							consumir();
							estado=26;
							break;
						}
						else { // error: algun otro caracter invalido
							estado=0;
							throw new operadorConjuncionInvalido(archNroLinea,archNroColumna);
						}
					}
					case(28):{ 
						// '
						if (charActual == 39) {
							consumir();
							estado=38;
							break;
						}
						else {
							estado=0;
							throw new literalCaracterSinCerrar(archNroLinea,archNroColumna);
						}
					}
					case(29):{
						// es t
						if (charActual == 116) {
							//agrego el caracter TAB '\t'
							lexemaActual+="\t";
							charActual= br.read();
							estado=28;
							break;
						}
						// es n y antes vino un \ -> enter
						else if (charActual == 110) {
							lexemaActual+='\n';
							charActual= br.read();
							estado=30;
							break;
						}
						//otro caracter de mi alfabeto (del 32 al 126 en ASCII)
						else if (charActual >= 32 && charActual<=126){
							consumir();
							estado=37;
							break;
						}
						else {
							//algun otro caracter no valido
							estado=0;
							throw new caracterNoPerteneceAlfabeto(archNroLinea,archNroColumna,lexemaActual+=charActual);
						}
					}
					case(30):{ 
						// '
						if (charActual == 39) {
							consumir();
							estado=41;
							break;
						}
						//otro caracter invalido
						else {
							throw new literalCaracterSinCerrar(archNroLinea,archNroColumna);		
						}
					}
					case(31):{
						// " o salto linea
						if (charActual == 34 || charActual== '\n') {
							estado=20;
							lexemaActual="";
							break;
						}
						else {
							estado=0;
							return new Token("string",lexemaActual,archNroLinea,archNroColumna- lexemaActual.length());
						}
					}
					case(32):{
						// |
						if (charActual== 124) {
							consumir();
							estado=33;
							break;
						}
						else {
							estado=0;
							throw new operadorDisyuncionInvalido(archNroLinea,archNroColumna);
						}
					}
					case(33):{
						estado=0;
						return new Token("||",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}
					case(34):{
						estado=0;
						return new Token("<=",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}
					case(35):{
						// digito 
						if (charActual >= 48 && charActual <= 57) {
							consumir();
							break;
						}
						else {
							estado=0;
							return new Token ("Entero",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
						}
					}
					case(36):{ 
						// si es \ no concateno aun, por si viene un \n
						if (charActual == 92) {
							charActual= br.read();
							estado=29;
							break;
						}
						// si es ' -> error, caracter vacio
						else if (charActual == '\'') {
							estado=0;
							throw new caracterVacio(archNroLinea,archNroColumna,lexemaActual+=charActual);
						}
						//otro caracter de mi alfabeto (del 32 al 126 en ASCII), menos \, salto de linea o '
						else if (charActual >= 32 && charActual<=126){
							consumir();
							estado=37;
							break;
						}
						else {
							estado=0;
							throw new caracterInvalidoEnLiteralCaracter(archNroLinea,archNroColumna,lexemaActual+"<"+charActual+">");
						}
					}
					case(37):{ // '
						if (charActual == 39) {
							consumir();
							estado= 40;
							break;
						}
						//otro caracter invalido
						else {
							throw new literalCaracterSinCerrar(archNroLinea,archNroColumna);
						}
					}
					case(38):{ //es \t (tab)
						estado=0;
						return new Token("Tab",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}
					case(39):{ // >=
						estado=0;
						return new Token(">=",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}
					case(40):{ // es un caracter valido
						estado=0;
						return new Token("Caracter",lexemaActual,archNroLinea,archNroColumna- lexemaActual.length());
					}
					case(41):{ // es \n (enter)
						estado=0;
						return new Token("Enter",lexemaActual,archNroLinea,archNroColumna-lexemaActual.length());
					}
					
				}//fin switch
				
				archNroColumna++;
				
			}//fin while true	
		}//fin try
		catch (IOException e) {
			System.out.println("Se produjo un error al intentar leer el archivo, en linea "+archNroLinea+" y columna "+archNroColumna+" del archivo Input");
			System.exit(110);
		}	
	
	//llegue a fin de archivo
	return new Token("Fin archivo","Fin archivo",archNroLinea,archNroColumna-2);	
	}//fin metodo nextToken
					
}//fin clase
