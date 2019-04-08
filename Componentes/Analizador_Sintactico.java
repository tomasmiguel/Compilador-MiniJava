package Componentes;




import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Excepciones.ErrorSintactico;
import Excepciones.claseDeclaradaInvalida;
import Excepciones.claseNoDeclarada;
import Excepciones.metodoMainNoExiste;
import Nodos.NodoAcceso;
import Nodos.NodoAccesoArregloEncadenado;
import Nodos.NodoAccesoThis;
import Nodos.NodoAccesoVar;
import Nodos.NodoAccesoVarEncadenado;
import Nodos.NodoAsignacion;
import Nodos.NodoAsignacionDec;
import Nodos.NodoBloque;
import Nodos.NodoDecVarLocal;
import Nodos.NodoEncadenado;
import Nodos.NodoEncadenadoVacio;
import Nodos.NodoExpresion;
import Nodos.NodoExpresionBinaria;
import Nodos.NodoExpresionParentizada;
import Nodos.NodoExpresionUnaria;
import Nodos.NodoExpresionVacio;
import Nodos.NodoIf;
import Nodos.NodoIfElse;
import Nodos.NodoIfSolo;
import Nodos.NodoLiteral;
import Nodos.NodoLlamadaCtor;
import Nodos.NodoLlamadaCtorArreglo;
import Nodos.NodoLlamadaCtorClase;
import Nodos.NodoLlamadaMetodo;
import Nodos.NodoLlamadaMetodoEncadenado;
import Nodos.NodoLlamadaMetodoEstatico;
import Nodos.NodoOperando;
import Nodos.NodoPrimario;
import Nodos.NodoReturn;
import Nodos.NodoSentencia;
import Nodos.NodoSentenciaLlamada;
import Nodos.NodoSentenciaVacio;
import Nodos.NodoWhile;
import Tipos_Datos.Tipo;
import Tipos_Datos.TipoArregloBoolean;
import Tipos_Datos.TipoArregloChar;
import Tipos_Datos.TipoArregloInt;
import Tipos_Datos.TipoBoolean;
import Tipos_Datos.TipoChar;
import Tipos_Datos.TipoIdClase;
import Tipos_Datos.TipoInt;
import Tipos_Datos.TipoNull;
import Tipos_Datos.TipoString;
import Tipos_Datos.TipoVoid;


public class Analizador_Sintactico {
	private Token token;
	private String lexemaActual;
	private String idTokenActual;
	private Analizador_Lexico alex;
	private short parametroUbicacion;
	private String nombreSalida;
	
	public static Tabla_Simbolos TS;
	public static Generador salida;
	
	
	
	
//Constructor
	public Analizador_Sintactico(Entrada e) {		
		idTokenActual="";
		lexemaActual="";
		parametroUbicacion=1;
		alex= new Analizador_Lexico(e);
	}
	
	
	
//Setters
	public void start(String nombreS) throws Exception{
		nombreSalida=nombreS;
		//pido el primer Token
		match(""); 
		idTokenActual= token.getNombre();
		
		//creo la tabla de simbolos
		TS= new Tabla_Simbolos();
		
		//Llamo a Inicial
		Inicial();
		
	}
	
	
	private void match(String nombre) throws Exception{
		// si matchea pido el proximo Token
		if(nombre.equals(idTokenActual)) {
				token = alex.nextToken();
				idTokenActual= token.getNombre();
				lexemaActual= token.getLexema();
		}
		// sino error!
		//1) por fin de archivo
		else if (idTokenActual.equals("Fin archivo"))
			throw new ErrorSintactico("Error Sintactico: Se esperaba 'class', pero recibi un archivo vacio. \n");
		else
		//2) por error de matcheo	
			throw new ErrorSintactico("Error Sintactico en match: Se esperaba "+nombre+" y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
	}
	
	
	
	//chequeo semantico y consolidacion
	private void chequeoSemantico() throws Exception {
		//CHEQUEO SEMANTICO I
			//chequeo que existe un metodo estatico main en alguna clase, sino error
			if (!TS.hayUnMain())
				throw new metodoMainNoExiste();
			
			//para cada clase de mi TS, llamo a chequearDeclaraciones()
			for (EntradaClase c: TS.getTabla().values()) {
					//solo chequeamos si c es distinto de Object o System
					if ( (!c.getNombre().equals("Object")) && (!c.getNombre().equals("System")) ) {
							//Si del que heredo no existe -> error
							if (TS.esClaseDeclarada(c.getHerencia())==null)
									throw new claseNoDeclarada(c.getHerencia(), c.getTokenLinea(), c.getTokenColumna());
					
							//sino, chequeo cada clase
							c.chequearDeclaraciones();
					}//fin if
			}//fin for

		
		//Consolidacion
		Collection<EntradaClase> clases= TS.getTabla().values();	
			
		//variable de instancia con mismo nombre que algun ancestro
		for (EntradaClase c: clases)
			c.consolidar();
		
		
		//etapa 5
		salida= new Generador(nombreSalida);
		//
		
		
		
		//CHEQUEO SEMANTICO II
		//realizo chequeo de sentencias para cada clase
		for (EntradaClase c: clases) {
			TS.setClaseActual(c);
			c.chequearSentencias();
		}
		
		
		
		
		//traduzco a codigo intermedio
		for(EntradaClase c: clases) {
			TS.setClaseActual(c);
			c.generar();
		}	
		
		salida.generarRutinas();
		
		//cierro el archivo de salida del Generador
		salida.cerrarArchivoOutput();
	}//fin chequeoSemantico
	
	
	
	
	
//Metodos de mi Gramatica
	
	private void Inicial()throws Exception {
		Clase();
		MasClases();
		match("Fin archivo");
		
	//chequear herencia no circular
		TS.noHerenciaCircular();
				
	//realizo chequeo de declaraciones y consolido
		chequeoSemantico();
	}
	
	
	private void MasClases()throws Exception {
		if (idTokenActual.equals("class")) {
			Clase();
			MasClases();
		}
		else if (idTokenActual.equals("Fin archivo")){
			//epsilon
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba 'class' o 'fin de archivo', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private void Clase()throws Exception {
		match("class");
		
		//si la clase se llama Object o System, lanzo error Semantico
		if (lexemaActual.equals("Object") || lexemaActual.equals("System"))
			throw new claseDeclaradaInvalida(lexemaActual,token.getNroLinea(),token.getNroColumna());
		
		//sino, creo la clase con el lexema correspondiente
		EntradaClase clase= new EntradaClase(lexemaActual,token);
		//agrego la clase a la TS comprobando antes que no haya otra con mismo lexemaActual. Y setea la clase actual de la TS.
		TS.agregarClase(lexemaActual,clase);
		
		match("idClase");
		Herencia();
		match("{");
		Miembro();
		match("}");
		
		EntradaClase claseActual= TS.getClaseActual();
		//si recorri toda la clase y no hay Ctor, creo uno sin parametros	
		if (!claseActual.hayCtor())	
			TS.agregarCtor(new EntradaCtor(claseActual.getNombre(), token), new ArrayList<EntradaPar>());
	}
	
	
	private void Herencia()throws Exception {
		if (idTokenActual.equals("extends")) {
			match("extends");
			// seteo la clase padre
			TS.getClaseActual().setHerencia(lexemaActual);
			match("idClase");
		}
		//sino heredo de Object
		else if (idTokenActual.equals("{")) {
			 	//epsilon
				TS.getClaseActual().setHerencia("Object");
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba 'extends' o '{', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	private void Miembro()throws Exception {
		//atributo
		if (idTokenActual.equals("public") || idTokenActual.equals("private")) {
			Atributo();
			Miembro();
		}
		//Ctor
		else if (idTokenActual.equals("idClase")) {
			Ctor();
			Miembro();
		}
		//Metodo
		else if (idTokenActual.equals("static")|| idTokenActual.equals("dynamic")) {
			Metodo();
			Miembro();
		}
		else if (idTokenActual.equals("}")) {
			//epsilon
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba una sentencia, o '}', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private void Atributo()throws Exception {
		String visibilidad=	Visibilidad();
		Tipo tipoH = Tipo();
		
		List<EntradaVarInst> lista= new ArrayList<EntradaVarInst>();
		ListaDecVars(lista,visibilidad,tipoH);
		
		for(EntradaVarInst v: lista)
			TS.agregarVariable(v);
		
		NodoExpresion n= Inline();
		//
		///
		//
		//
		//
		//agregarAsignacionInline
		
		match(";");
	}
	
	private void Metodo()throws Exception {
		//recibo el modificador del metodo en modMet
		String modMet= FormaMetodo();
		//recibo el tipo del metodo en tipoH
		Tipo tipoMet= TipoMetodo();
		// guardo el nombre del metodo
		String metNom= lexemaActual;
		Token aux= token; // para no perder el token actual luego del match
		
		match("idMetVar");
		
		////creo lista y al volver obtengo los parametros de mi constructor
		List<EntradaPar> lista= new ArrayList<EntradaPar>();
		ArgsFormales(lista);
		
		//agrego metodo y mis parametros (si es que hay) 
		TS.agregarMetodo(new EntradaMetodo((metNom+"$"+lista.size()), tipoMet, modMet, aux, TS.getClaseActual() ), lista, metNom);
	
		TS.setBloqueActual(null);
			
		//desps proceso el bloque
		NodoBloque nb= Bloque();
		
		//vuelvo y asigno el bloque a la unidad actual. Ademas setea nb como bloqueActual en la TS
		TS.getUnidadActual().agregarCuerpo(nb);
	}
	
	
	private void Ctor()throws Exception {
		String nombreCtor= lexemaActual;
		Token aux= token;// para no perder el token actual luego del match
		match("idClase");
		//creo lista y al volver obtengo los parametros de mi constructor
		List<EntradaPar> lista= new ArrayList<EntradaPar>();
		ArgsFormales(lista);
		
		//agrego el nuevo Ctor, con la lista de parametros
		TS.agregarCtor(new EntradaCtor(nombreCtor,aux),lista); // (los controles los hago en TS y EntradaClase)
		
		TS.setBloqueActual(null);
		
		NodoBloque nb= Bloque();
		TS.getUnidadActual().agregarCuerpo(nb);
	}
	
	
	private void ArgsFormales(List<EntradaPar> lista)throws Exception {
		match("(");
		PregListaArgsFormales(lista);
		match(")");
	}
	
	
	private void PregListaArgsFormales(List<EntradaPar> lista)throws Exception {
		if (idTokenActual.equals("char") || idTokenActual.equals("boolean")|| idTokenActual.equals("int")|| 
				idTokenActual.equals("idClase")|| idTokenActual.equals("String")) {
			ListaArgsFormales(lista);
		}
		else if (idTokenActual.equals(")")) {
			//epsilon
		}
		else {		
			throw new ErrorSintactico("Error Sintactico: Se esperaba un tipo de dato, o ')', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
		parametroUbicacion=1;
	}
	
	
	private void ListaArgsFormales(List<EntradaPar> lista)throws Exception {
		ArgFormal(lista);
		parametroUbicacion++;
		F1(lista);
	}
	
	
	private void F1(List<EntradaPar> lista)throws Exception {
		if (idTokenActual.equals(",")) {
			match(",");
			ListaArgsFormales(lista);
		}
		else if (idTokenActual.equals(")")) {
			//epsilon
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba una ',' o ')', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private void ArgFormal(List<EntradaPar> lista) throws Exception {
		Tipo tipo= Tipo();
		lista.add(new EntradaPar(lexemaActual,tipo,parametroUbicacion, token));
		match("idMetVar");
	}
	
	
	private String FormaMetodo()throws Exception {
		String modific=null;
		modific= lexemaActual;
		if (idTokenActual.equals("static"))
			match("static");
		else if (idTokenActual.equals("dynamic"))
			match("dynamic");
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba una palabra clave ('static' o 'dynamic'), y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	 return modific;	
	}
	
	
	private String Visibilidad() throws Exception {
		String vis= lexemaActual;
		if (idTokenActual.equals("public"))
			match("public");
		else if (idTokenActual.equals("private"))
			match("private");
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba una palabra clave ('public' o 'private'), y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
		return vis;
	}
	
	
	private Tipo TipoMetodo()throws Exception {
		Tipo aux=null;
		if (idTokenActual.equals("void")) {
			aux= new TipoVoid("");
			match("void");
		}
		else if (idTokenActual.equals("boolean")|| idTokenActual.equals("char")|| idTokenActual.equals("int")|| 
					idTokenActual.equals("idClase")|| idTokenActual.equals("String")) {
			aux= Tipo();
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba 'void' o un tipo de dato, y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
		return aux;
	}
	
	
	private Tipo Tipo()throws Exception {
		Tipo tipo=null;
		if (idTokenActual.equals("boolean")){
			tipo= new TipoBoolean("");
			match("boolean");
			tipo= F10(tipo);
		} else if (idTokenActual.equals("char")) {
			tipo= new TipoChar("");
			match("char");
			tipo= F10(tipo);
		} else if (idTokenActual.equals("int")) {
			tipo= new TipoInt("");
			match("int");
			tipo= F10(tipo);
		} else if (idTokenActual.equals("idClase")) {
			tipo= new TipoIdClase(lexemaActual);
			match("idClase");
		}
		else if (idTokenActual.equals("String")) {
			tipo= new TipoString("");
			match("String");
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un tipo de dato, y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
		return tipo;
	}
		
	
	private Tipo F10(Tipo tipo)throws Exception {
		Tipo aux= null;
		String str= tipo.getTipo();
		
		if (idTokenActual.equals("[")) {
				//veo si es char, int o boolean
				if (str.equals("char"))
					aux= new TipoArregloChar("");
				else if (str.equals("int"))
					aux= new TipoArregloInt("");
				else if (str.equals("boolean"))
					aux= new TipoArregloBoolean("");
				
				match("[");
				match("]");
		}
		else if (idTokenActual.equals("idMetVar")){
			//epsilon
			aux= tipo;
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un '[', o un identificador de Metodo/Variable, y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
		return aux;
	}
	
	
	private NodoExpresion Inline () throws Exception{
		//Token tok= token;
		if (idTokenActual.equals("=")) {
			match("=");
			return Expresion(false);
	
		}
		//siguientes de inline
		else if (idTokenActual.equals(";")) {
			//epsilon
			return null;
		}
		else
			throw new ErrorSintactico("Error Sintactico: Se esperaba un '=', o ';', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
	}
	
	//cheq ndo Asign agregar lo de NodoDecVarLocal en NodoAsignacion
	private void TipoPrimitivo() throws Exception {
		if (idTokenActual.equals("char"))
			match("char");
		else if (idTokenActual.equals("int"))
			match("int");
		else if (idTokenActual.equals("boolean"))
			match("boolean");
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un tipo de dato primitivo, y recibi "+token.getLexema()+", en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private void ListaDecVars(List<EntradaVarInst> lista,String visibilidad,Tipo tipoH) throws Exception {
		
		lista.add(new EntradaVarInst(lexemaActual,tipoH,visibilidad, token));
		match("idMetVar");
		F2(lista,visibilidad,tipoH);
		
		//estoy "retornando" la lista en el parametro
	}
	
	
	private void F2(List<EntradaVarInst> lista, String visibilidad,Tipo tipoH) throws Exception {
		if (idTokenActual.equals(",")) {
			match(",");
			ListaDecVars(lista,visibilidad,tipoH);
		}
		

		else if (idTokenActual.equals(";")|| idTokenActual.equals("=")) {
			//epsilon
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un ',' o un ';', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private NodoBloque Bloque()throws Exception {
			match("{");
			
			//bloque actual es el padre de b
			NodoBloque b= new NodoBloque(TS.getBloqueActual(), token);
			
			//y ahora bloqueActual es el nuevo b
			
			TS.setBloqueActual(b);
			
			List<NodoSentencia> ls= new ArrayList<NodoSentencia>();
			PorSentencia(ls);
			
			for (NodoSentencia s: ls)
				b.agregarSentencia(s);
			
			//vuelvo a poner al padre como el actual
			TS.setBloqueActual(b.getBloquePadre());
			
			match("}");
			
			return b;
	}
	
	
	private void PorSentencia(List<NodoSentencia> ls) throws Exception {			
		if (idTokenActual.equals(";")|| idTokenActual.equals("idMetVar")|| idTokenActual.equals("this")|| 
				idTokenActual.equals("(")|| idTokenActual.equals("boolean")|| idTokenActual.equals("char")|| 
				idTokenActual.equals("int")|| idTokenActual.equals("idClase")|| idTokenActual.equals("String")|| 
				idTokenActual.equals("if")|| idTokenActual.equals("while")|| idTokenActual.equals("{")|| idTokenActual.equals("return")) {
			 
			ls.add(Sentencia());
			PorSentencia(ls);
			
		}
		else if (idTokenActual.equals("}")) {
			//epsilon
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba una sentencia, ';', '(', o '{', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n") ;
		}
	}
	
	
	private NodoSentencia Sentencia() throws Exception {
		if (idTokenActual.equals(";")) {
			Token tok= token;
			match(";");
			return new NodoSentenciaVacio(tok);
		}
		else if (idTokenActual.equals("idMetVar") || idTokenActual.equals("this")) {
			NodoAsignacion na = Asignacion();
			match(";");
			return na;
		}
		else if (idTokenActual.equals("(")) {
			NodoSentenciaLlamada ns= SentenciaLlamada(); 
			match(";");
			return ns;
		}
		else if (idTokenActual.equals("boolean")|| idTokenActual.equals("char")|| idTokenActual.equals("int")|| 
					idTokenActual.equals("idClase")|| idTokenActual.equals("String")) {
			
			Tipo tipo= Tipo();
			
			List<EntradaVarInst> lista= new ArrayList<EntradaVarInst>();
			Token tok=token;
			ListaDecVars(lista,"visibilidad", tipo);
			Token tokenIgual= token;
			NodoExpresion n= Inline();
			match(";");
			
			if (n==null)
				return new NodoDecVarLocal(lista,tok, tipo, TS.getBloqueActual());
			//sino 
			//nodoAsignacion especial
			return new NodoAsignacionDec(new NodoDecVarLocal(lista,tok, tipo, TS.getBloqueActual()), n,tokenIgual);
		}
		else if (idTokenActual.equals("if")) {
			match("if");
			match("(");
			Token tok=token;
			NodoExpresion ne= Expresion(false);
			match(")");
			NodoSentencia ns= Sentencia();
			return F3(ne,ns, tok);
		}	
		else if (idTokenActual.equals("while")) {
			Token tok= token;
			match("while");
			match("(");
			//
			NodoExpresion ne= Expresion(false);
			match(")");
			//
			NodoSentencia ns= Sentencia();
			return new NodoWhile(ne,ns, tok);
		}
		else if (idTokenActual.equals("{")) {
			return Bloque();
		}
		else if (idTokenActual.equals("return")) {
			Token tok=token;
			match("return");
			NodoExpresion ne= PregExpresion(tok,false);
			match(";");
			return new NodoReturn (ne,tok);
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba una sentencia, un tipo de dato, ';', '(', o '{', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private NodoIf F3(NodoExpresion ne, NodoSentencia ns, Token tok) throws Exception{
		if (idTokenActual.equals("else")) {
			match("else");
			NodoSentencia nsElse = Sentencia();
			return new NodoIfElse(ne,ns,nsElse, tok);
		}		
		else if(idTokenActual.equals("}") || idTokenActual.equals(";") || idTokenActual.equals("idMetVar") || idTokenActual.equals("this")|| 
				idTokenActual.equals("(")|| idTokenActual.equals("boolean")|| idTokenActual.equals("int")|| idTokenActual.equals("char")|| 
				idTokenActual.equals("String")|| idTokenActual.equals("idClase")|| idTokenActual.equals("if")|| idTokenActual.equals("while")||
				idTokenActual.equals("{")|| idTokenActual.equals("return")) {
			//epsilon
			return new NodoIfSolo(ne,ns,tok);
		}
		else {	
			throw new ErrorSintactico("Error Sintactico: Se esperaba una sentencia, un simbolo de puntuacion o un tipo de dato, y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private NodoAsignacion Asignacion() throws Exception {
		
		Token tok=null;
		NodoExpresion ld=null;
		boolean ladoIzq=true;
		NodoAcceso na= null;
		
		if (idTokenActual.equals("idMetVar")) {
		
			na= AccesoVar(ladoIzq);
			tok= token;
			match("=");
			ld= Expresion(false);
			return new NodoAsignacion(na,ld,tok); 
		}
		else if (idTokenActual.equals("this")) {
			na=	AccesoThis(ladoIzq);
			tok=token;
			match("=");
			ld= Expresion(false);
			return new NodoAsignacion(na,ld,tok); 
		}
	 return null;
	}
	
	
	private NodoSentenciaLlamada SentenciaLlamada() throws Exception{
		Token tok= token;
		match("(");
		NodoPrimario np= Primario(false);
		match(")");
		return new NodoSentenciaLlamada(np,tok);
	}	
	
	
	private NodoExpresion PregExpresion(Token tok,boolean ladoIzq) throws Exception{
		if (idTokenActual.equals("+")|| idTokenActual.equals("-")|| idTokenActual.equals("!") || idTokenActual.equals("null")||
				idTokenActual.equals("true")|| idTokenActual.equals("false") ||idTokenActual.equals("Entero")|| 
				idTokenActual.equals("Caracter")|| idTokenActual.equals("string") ||idTokenActual.equals("(")|| 
				idTokenActual.equals("this")|| idTokenActual.equals("idMetVar") ||idTokenActual.equals("idClase")|| 
				idTokenActual.equals("new")){
			return ExpOr(ladoIzq);
		}
		else if (idTokenActual.equals(";")) {
			//epsilon
			return new NodoExpresionVacio(tok);
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba una sentencia, un operador, o '(', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private NodoExpresion Expresion(boolean ladoIzq)throws Exception {
		return ExpOr(ladoIzq);
	}
	
	
	private NodoExpresion ExpOr(boolean ladoIzq) throws Exception{
		NodoExpresion ne= ExpAnd(ladoIzq);
		return R1(ne,ladoIzq);
	}
	
	
	private NodoExpresion R1(NodoExpresion ne,boolean ladoIzq) throws Exception {
		Token tok= token;
		if (idTokenActual.equals("||")) {
			match("||");
			NodoExpresion n= ExpAnd(ladoIzq);
			NodoExpresionBinaria neb= new NodoExpresionBinaria(ne,n,tok);
			return R1(neb,ladoIzq);
		}																					
		else if (idTokenActual.equals(";")|| idTokenActual.equals(")")|| idTokenActual.equals(",")|| idTokenActual.equals("]")) {
			//epsilon
			return ne;
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un '||', ';', ')', ',' o ']', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private NodoExpresion ExpAnd(boolean ladoIzq) throws Exception {
		NodoExpresion ne = ExpIg(ladoIzq);
		return R2(ne,ladoIzq);
	}
	
	
	private NodoExpresion R2(NodoExpresion ne,boolean ladoIzq)throws Exception {
		Token tok=token;
		if (idTokenActual.equals("&&")) {
			match("&&");
			NodoExpresion n= ExpIg(ladoIzq);
			NodoExpresionBinaria neb= new NodoExpresionBinaria(ne,n,tok);
			return R2(neb,ladoIzq);
		}
		else if (idTokenActual.equals(";")|| idTokenActual.equals(")")|| idTokenActual.equals("||")|| idTokenActual.equals(",")
					|| idTokenActual.equals("]")) {
			//epsilon
			return ne;
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un '&&', ';', ')', '||', ',' o un ']', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private NodoExpresion ExpIg(boolean ladoIzq) throws Exception{
		NodoExpresion ne= ExpComp(ladoIzq);
		return R3(ne,ladoIzq);
	}
	
	
	private NodoExpresion R3(NodoExpresion ne,boolean ladoIzq)throws Exception {
		if (idTokenActual.equals("==")|| idTokenActual.equals("!=")) {
			Token tok= token;
			OpIg();
			NodoExpresion n= ExpComp(ladoIzq);
			NodoExpresionBinaria neb= new NodoExpresionBinaria(ne,n,tok);
			return R3(neb,ladoIzq);
		}
		else if (idTokenActual.equals(";")|| idTokenActual.equals(")")|| idTokenActual.equals("&&") || 
					idTokenActual.equals("||")|| idTokenActual.equals(",")|| idTokenActual.equals("]")) {
			//epsilon
			return ne;
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un operador de comparacion/conjuncion/disyuncion, ';', ')', ',' o ']', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private NodoExpresion ExpComp(boolean ladoIzq) throws Exception {
		NodoExpresion ne= ExpAd(ladoIzq);
		return F4(ne,ladoIzq);
	}
	
	
	private NodoExpresion F4 (NodoExpresion ne,boolean ladoIzq) throws Exception{
		if (idTokenActual.equals("<=")|| idTokenActual.equals(">=")|| idTokenActual.equals("<") || idTokenActual.equals(">")) {
			Token tok=token;
			OpComp();
			NodoExpresion n= ExpAd(ladoIzq);
			return new NodoExpresionBinaria(ne,n,tok);
		}
		else if (idTokenActual.equals(";")||idTokenActual.equals("==")|| idTokenActual.equals("!=")|| idTokenActual.equals(")")|| 
				idTokenActual.equals("||")||  idTokenActual.equals("&&") || idTokenActual.equals(",")|| idTokenActual.equals("]")) {
			//epsilon
			return ne;
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un operador de comparacion/conjuncion/disyuncion, ';', ')', ',' o ']', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private NodoExpresion ExpAd(boolean ladoIzq) throws Exception {
		NodoExpresion ne= ExpMul(ladoIzq);
		return R4(ne,ladoIzq);
	}
	
	
	private NodoExpresion R4(NodoExpresion ne,boolean ladoIzq) throws Exception{
		if (idTokenActual.equals("+")|| idTokenActual.equals("-")) {
			Token tok= token;
			OpAd();
			NodoExpresion n= ExpMul(ladoIzq);
			NodoExpresionBinaria neb= new NodoExpresionBinaria(ne,n,tok);
			return R4(neb,ladoIzq);
		}
		if (idTokenActual.equals("<=")|| idTokenActual.equals(">=")|| idTokenActual.equals("<") || idTokenActual.equals(">") || 
					idTokenActual.equals(";")|| idTokenActual.equals("==") || idTokenActual.equals("!=") || idTokenActual.equals(")")|| 
					idTokenActual.equals("||")||  idTokenActual.equals("&&") || idTokenActual.equals(",")|| idTokenActual.equals("]")) {
			//epsilon
			return ne;
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba una sentencia, un operador de comparacion/disyuncion/conjuncion, o simbolo de puntuacion, y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private NodoExpresion ExpMul(boolean ladoIzq) throws Exception{
		NodoExpresion ne= ExpUn(ladoIzq);
		return R5(ne,ladoIzq);
	}
	
	
	private NodoExpresion R5(NodoExpresion ne,boolean ladoIzq) throws Exception{
		if (idTokenActual.equals("*")|| idTokenActual.equals("/")) {
			Token tok=token;
			OpMul();
			NodoExpresion n= ExpUn(ladoIzq);
			NodoExpresionBinaria neb= new NodoExpresionBinaria(ne,n,tok);
			return R5(neb,ladoIzq);
		}
		if (idTokenActual.equals("+")|| idTokenActual.equals("-") || idTokenActual.equals("<=")|| idTokenActual.equals(">=") || 
				idTokenActual.equals("<") || idTokenActual.equals(">") || idTokenActual.equals(";")|| idTokenActual.equals("==") || 
				idTokenActual.equals("!=") || idTokenActual.equals(")")|| idTokenActual.equals("||")|| idTokenActual.equals("&&") || 
				idTokenActual.equals(",")|| idTokenActual.equals("]")) {
			//epsilon
			return ne;
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba una sentencia, operador de comparacion/conjuncion/disyuncion, simbolo de puntuacion, '+' o '-', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private NodoExpresion ExpUn(boolean ladoIzq) throws Exception {
		if (idTokenActual.equals("+")|| idTokenActual.equals("-")|| idTokenActual.equals("!")) {
			Token tok=token;
			OpUn();
			NodoExpresion n= ExpUn(ladoIzq);
			
			//retorna NodoExpresionUnaria
			return new NodoExpresionUnaria(n,tok);			
		}
		else if (idTokenActual.equals("null")|| idTokenActual.equals("true")||idTokenActual.equals("false")|| 
					idTokenActual.equals("Entero")||idTokenActual.equals("Caracter")||idTokenActual.equals("string")||
					idTokenActual.equals("(")||idTokenActual.equals("this")||idTokenActual.equals("idMetVar")|| 
					idTokenActual.equals("idClase")|| idTokenActual.equals("new")) {
			//retorna nodoOperando
			return Operando(ladoIzq);
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba una sentencia, y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private void OpIg()throws Exception {
		if (idTokenActual.equals("=="))
			match("==");
		else if (idTokenActual.equals("!="))
			match("!=");
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un '==' o '!=', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private void OpComp()throws Exception {
		if (idTokenActual.equals("<"))
			match("<");
		else if (idTokenActual.equals(">"))
			match(">");
		else if (idTokenActual.equals("<="))
			match("<=");
		else if (idTokenActual.equals(">="))
			match(">=");
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un '<=', '>=', '<' o '>', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private void OpAd() throws Exception{
		if (idTokenActual.equals("+"))
			match("+");
		else if (idTokenActual.equals("-"))
			match("-");
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un '+' o '-', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private void OpUn() throws Exception{
		if (idTokenActual.equals("+"))
			match("+");
		else if (idTokenActual.equals("-"))
			match("-");
		else if (idTokenActual.equals("!"))
			match("!");
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un '+', '-' o '!', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private void OpMul()throws Exception {
		if (idTokenActual.equals("*"))
			match("*");
		else if (idTokenActual.equals("/"))
			match("/");
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un '*' o '/', y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private NodoOperando Operando(boolean ladoIzq)throws Exception {
		if (idTokenActual.equals("null")|| idTokenActual.equals("true")||idTokenActual.equals("false")||
					idTokenActual.equals("Entero")||idTokenActual.equals("Caracter")||idTokenActual.equals("string")){
			//return nodo literal
			return Literal();
		}
		else if (idTokenActual.equals("(")||idTokenActual.equals("this")||idTokenActual.equals("idMetVar")|| 
					idTokenActual.equals("idClase")|| idTokenActual.equals("new")) {
			//return nodo Primario
			return Primario(ladoIzq); //primario siempre esta del lado derecho ?????? o se ve mas adelante eso??
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba una sentencia, y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private NodoLiteral Literal()throws Exception {
		Tipo tipo;
		Token tok= token;
		if (idTokenActual.equals("null")) {
				match("null");
				tipo= new TipoNull("");
		}
		else if (idTokenActual.equals("true")) {
				match("true");
				tipo= new TipoBoolean("");
		}
		else if (idTokenActual.equals("false")) {
				match("false");
				tipo= new TipoBoolean("");
		}
		else if (idTokenActual.equals("Entero")) {
				match("Entero");
				tipo= new TipoInt("");
		}
		else if (idTokenActual.equals("Caracter")) {
				match("Caracter");
				tipo= new TipoChar("");
		}
		else if (idTokenActual.equals("string")) {
				match("string");
				tipo= new TipoString("");
		}	
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un literal, o una palabra clave, y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	 return new NodoLiteral(tipo, tok);
	}
	
	
			
	private NodoPrimario Primario(boolean ladoIzq) throws Exception {
		if (idTokenActual.equals("("))
			return  ExpresionParentizada(ladoIzq);
		
		else if (idTokenActual.equals("this"))
			return AccesoThis(ladoIzq);
		
		else if (idTokenActual.equals("idMetVar")) {
			Token tok=token;
			match("idMetVar");
			
			return F5(tok,ladoIzq);
		}
		else if (idTokenActual.equals("idClase"))
			return LlamadaMetodoEstatico(ladoIzq);
		
		else if (idTokenActual.equals("new")) {
			match("new");
			
			return F6(ladoIzq);
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un '(', o una sentencia, y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private NodoPrimario F5(Token tok,boolean ladoIzq) throws Exception{
		if (idTokenActual.equals("(")) {
			List<NodoExpresion> l= ArgsActuales(ladoIzq);
			NodoEncadenado ne= Encadenado(ladoIzq);
			return new NodoLlamadaMetodo(l, ne, tok,ladoIzq);
		}
		
		else if (idTokenActual.equals(",")|| idTokenActual.equals(".")|| idTokenActual.equals("[")|| idTokenActual.equals(")")|| idTokenActual.equals(";")|| 
						idTokenActual.equals("]")|| idTokenActual.equals("=")|| idTokenActual.equals("*") || idTokenActual.equals("/")|| 
						idTokenActual.equals("+") || idTokenActual.equals("-")||  idTokenActual.equals(">")|| idTokenActual.equals("<")||  
						idTokenActual.equals(">=")|| idTokenActual.equals("<=")||  idTokenActual.equals("==")|| idTokenActual.equals("!=")|| 
						idTokenActual.equals("&&")||  idTokenActual.equals("||")) {
			//lista vacia de argsActuales	
			NodoEncadenado ne=  Encadenado(ladoIzq);
			return new NodoAccesoVar(ne,tok, ladoIzq);
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un simbolo de puntuacion, o un operador, y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private NodoAcceso AccesoVar(boolean ladoIzq)throws Exception {
		Token tok=token;
		match("idMetVar");
		NodoEncadenado ne= Encadenado(ladoIzq);
		
		return new NodoAccesoVar(ne,tok, ladoIzq);
	}
	
	
	private NodoAcceso AccesoThis(boolean ladoIzq)throws Exception {
		Token tok= token;
		match("this");
		NodoEncadenado ne=  Encadenado(ladoIzq);
	
		return new NodoAccesoThis(ne, tok, ladoIzq);
	}
	

	private NodoExpresionParentizada ExpresionParentizada(boolean ladoIzq)throws Exception {
		Token tok=token;
		match("(");
		NodoExpresion ne= Expresion(ladoIzq);
		match(")");
		NodoEncadenado nen=  Encadenado(ladoIzq);
		
		return new NodoExpresionParentizada(ne,nen,tok);
	}
	
	
	
	private NodoLlamadaMetodoEstatico LlamadaMetodoEstatico(boolean ladoIzq)throws Exception {
		Token tokIdClase= token;
		match("idClase");
		match(".");
		NodoLlamadaMetodo n= LlamadaMetodo(ladoIzq);
		
		//NodoEncadenado ne= Encadenado(ladoIzq);
		return new NodoLlamadaMetodoEstatico(n.getEncadenado(),tokIdClase, n);
	}
	
	
	private NodoLlamadaMetodo LlamadaMetodo(boolean ladoIzq)throws Exception {
		Token tokIdMetVar= token;
		match("idMetVar");
		List<NodoExpresion> l= ArgsActuales(ladoIzq);
		NodoEncadenado ne= Encadenado(ladoIzq);
		return new NodoLlamadaMetodo(l,ne,tokIdMetVar,ladoIzq);
	}
	
	
	//compatible: lado der igual o subclase de lado izq
	private NodoLlamadaCtor F6(boolean ladoIzq)throws Exception {
		Token tok= token;
		
		if (idTokenActual.equals("idClase")) {
			match("idClase");
			List<NodoExpresion> l = ArgsActuales(ladoIzq);
			NodoEncadenado ne= Encadenado(ladoIzq);
			
			return new NodoLlamadaCtorClase(l,ne,tok);
		}
		else if (idTokenActual.equals("char")|| idTokenActual.equals("boolean")|| idTokenActual.equals("int")) {
			TipoPrimitivo();
			match("[");
			NodoExpresion nExp= Expresion(ladoIzq);
			match("]");
			NodoEncadenado ne= Encadenado(ladoIzq);
			
			return new NodoLlamadaCtorArreglo(nExp,ne,tok);
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un identificador de Clase, o un tipo de dato primitivo, y recibi "+token.getLexema()+", en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private List<NodoExpresion> ArgsActuales(boolean ladoIzq)throws Exception {
		match("(");
		List<NodoExpresion> l =new ArrayList<NodoExpresion>();
		//primeros de ListaExps -> primeros de Expresion
		if (    idTokenActual.equals("!") || idTokenActual.equals("+") || idTokenActual.equals("-") || idTokenActual.equals("null")|| 
				idTokenActual.equals("true")||idTokenActual.equals("false")|| idTokenActual.equals("Entero")||idTokenActual.equals("Caracter")||
				idTokenActual.equals("string") || idTokenActual.equals("(")|| idTokenActual.equals("this")||idTokenActual.equals("idMetVar")|| 
				idTokenActual.equals("idClase")|| idTokenActual.equals("new")) {
			
			ListaExps(l,ladoIzq);
			match(")");
			//al final devuelvo lista l
		}
		else if (idTokenActual.equals(",")|| idTokenActual.equals(".")|| idTokenActual.equals("[")|| idTokenActual.equals(")")|| idTokenActual.equals(";")|| 
				idTokenActual.equals("]")|| idTokenActual.equals("=")|| idTokenActual.equals("*") || idTokenActual.equals("/")|| 
				idTokenActual.equals("+") || idTokenActual.equals("-")||  idTokenActual.equals(">")|| idTokenActual.equals("<")||  
				idTokenActual.equals(">=")|| idTokenActual.equals("<=")||  idTokenActual.equals("==")|| idTokenActual.equals("!=")|| 
				idTokenActual.equals("&&")||  idTokenActual.equals("||"))
		{
			//epsilon
			match(")");
			//al final devuelvo lista vacia
		}
		else
			throw new ErrorSintactico("Error Sintactico: Se esperaba una sentencia, un operador o un simbolo de puntuacion, y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");	
	 return l;
	}
	
	
	private void ListaExps(List<NodoExpresion> l,boolean ladoIzq)throws Exception {
			l.add(Expresion(ladoIzq));
			F7(l,ladoIzq);
		
	}
	
	
	private void F7(List<NodoExpresion> l,boolean ladoIzq)throws Exception {
		if (idTokenActual.equals(",")) {
			match(",");
			ListaExps(l,ladoIzq);
		}
		else if (idTokenActual.equals(")")) {
			//epsilon
		}
	}
	
	
	private NodoEncadenado Encadenado(boolean ladoIzq)throws Exception {
		if (idTokenActual.equals(".")) {
			match(".");
			return F8(ladoIzq);
		}
		else if (idTokenActual.equals("["))
			return AccesoArregloEncadenado(ladoIzq);
		//siguientes de encadenado		
		else if (idTokenActual.equals(",")|| idTokenActual.equals("]")|| idTokenActual.equals(")")|| idTokenActual.equals("=")|| idTokenActual.equals("*") || 
				idTokenActual.equals("/")|| idTokenActual.equals("+") || idTokenActual.equals("-")|| idTokenActual.equals(">")|| 
				idTokenActual.equals("<")|| idTokenActual.equals(">=")|| idTokenActual.equals("<=")|| idTokenActual.equals("==")||
				idTokenActual.equals("!=")|| idTokenActual.equals("&&")||  idTokenActual.equals("||")||  idTokenActual.equals(";")){
			//epsilon
			return new NodoEncadenadoVacio(null, token, ladoIzq);
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba una sentencia, y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	private NodoEncadenado F8(boolean ladoIzq)throws Exception {
		Token tok=token; //guardo el nombre del metodo
		match("idMetVar");
		return F9(tok, ladoIzq);
	}
	
	
	private NodoEncadenado AccesoArregloEncadenado(boolean ladoIzq)throws Exception {
		Token tok= token;
		match("[");
		NodoExpresion ne= Expresion(ladoIzq);
		match("]");
		NodoEncadenado nen= Encadenado(ladoIzq);
		
		return new NodoAccesoArregloEncadenado(ne,nen, tok,ladoIzq);
	}
	
	
	private NodoEncadenado F9(Token tok,boolean ladoIzq)throws Exception {
		//es argsActuales()
		if (idTokenActual.equals("(")) {
			List<NodoExpresion> l= ArgsActuales(ladoIzq);
			NodoEncadenado ne= Encadenado(ladoIzq);
			return new NodoLlamadaMetodoEncadenado(l, ne, tok,ladoIzq);
		}
		//es encadenado
		else if (idTokenActual.equals(",")|| idTokenActual.equals(".")|| idTokenActual.equals("[")|| idTokenActual.equals(")")|| idTokenActual.equals(";")|| 
						idTokenActual.equals("]")|| idTokenActual.equals("=")|| idTokenActual.equals("*") || idTokenActual.equals("/")|| 
						idTokenActual.equals("+") || idTokenActual.equals("-")||  idTokenActual.equals(">")|| idTokenActual.equals("<")||  
						idTokenActual.equals(">=")|| idTokenActual.equals("<=")||  idTokenActual.equals("==")|| idTokenActual.equals("!=")|| 
						idTokenActual.equals("&&")||  idTokenActual.equals("||")) {
			
			//lista vacia de argsActuales	
			NodoEncadenado ne= Encadenado(ladoIzq);
			return new NodoAccesoVarEncadenado(ne, tok,ladoIzq);
		}
		else {
			throw new ErrorSintactico("Error Sintactico: Se esperaba un simbolo de puntuacion, o un operador, y recibi '"+token.getLexema()+"', en linea "+token.getNroLinea()+" y columna "+token.getNroColumna()+" del archivo fuente.\n");
		}
	}
	
	
	
	
}// fin Analizador_Sintactico
