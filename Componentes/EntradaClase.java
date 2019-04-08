package Componentes;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Excepciones.claseNoDeclarada;
import Excepciones.constructorExistente;
import Excepciones.metodoDeclaradoInvalido;
import Excepciones.nombreVariableInvalida;
import Excepciones.parametroDeclaradoInvalido;
import Excepciones.redefinicionMetodoInvalido;
import Tipos_Datos.Tipo;

public class EntradaClase {
	private String nombre;
	private String herencia;
	private Token token;
	private HashMap<String,EntradaVarInst> entradaVar; //atributos
	private HashMap<String,EntradaMetodo> entradaMetodo;
	private HashMap<String,EntradaCtor> entradaCtor;
	private boolean consolidado;
	private boolean hayCtor;
	private List<EntradaMetodo> metodosDyn;
	private int maximoOffsetMetodo;
	private int maximoOffsetVarInst; //la 0 es para la VT
	
	//private List<NodoAsignacion> listaAsigInline;
	
	
	
	
	public EntradaClase(String n, Token t) {
		nombre= n;
		herencia=null;
		token=t;
		consolidado= hayCtor= false;
		entradaVar= new HashMap<String,EntradaVarInst>();
		entradaMetodo= new HashMap<String,EntradaMetodo>();
		entradaCtor= new HashMap<String, EntradaCtor>();
		metodosDyn= new ArrayList<EntradaMetodo>();
		maximoOffsetMetodo= 0;
		maximoOffsetVarInst= 1; 				//la 0 es para la VT
		
		//listaAsigInline= new ArrayList<NodoAsignacion>();
	}
	
	public EntradaVarInst esVarDeclarada(String idVar) {
		return entradaVar.get(idVar);
	}
	
	public boolean hayCtor() {
		return hayCtor;
	}
	
	public void setHerencia(String h) {
		herencia=h;
	}
	public int getMaximoOffsetMetodo() {
		return maximoOffsetMetodo;
	}
	
	public int getMaximoOffsetVarInst() {
		return maximoOffsetVarInst;
	}
	
	//public void setMaximoOffsetVarInst() {}
	
	
	public void setConsolidado() {
		consolidado=true;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getHerencia() {
		return herencia;
	}
	
	public int getTokenLinea() {
		return token.getNroLinea();
	}
	
	public int getTokenColumna() {
		return token.getNroColumna();
	}
	
	public HashMap<String, EntradaCtor> getEntradaCtor(){
		return entradaCtor;
	}
	
	
	
	public void generar() throws Exception {
		//genero la VT de la clase
		Analizador_Sintactico.salida.generar(".DATA", "genero los datos estaticos para la clase "+this.nombre);
		Analizador_Sintactico.salida.generar("VT_"+nombre+":"," ");
		
		boolean esDinamico=false;
		
		//para cada metodo, genero la etiqueta dw al codigo
		for (EntradaMetodo m: entradaMetodo.values()) {
			if (m.getModificador().equals("dynamic")) {
				Analizador_Sintactico.salida.gen_DW("DW "+m.getEtiqueta(),"offset: "+m.getOffsetMetodo(),m.getOffsetMetodo());
				esDinamico=true;
			}
		}
		Analizador_Sintactico.salida.agregar_DW();
		
		//si NO hay ningun metodo dinamico -> VT con NOP
		if (! esDinamico)
			Analizador_Sintactico.salida.generar("NOP","no hago nada");
		
		//genero codigo para todos los metodos
		Analizador_Sintactico.salida.generar(".CODE","seccion codigo de la clase "+this.nombre);
		
		List<EntradaPar> listaParams;
		for(EntradaMetodo m: entradaMetodo.values()) {
			//metodo actual es m
			Analizador_Sintactico.TS.setMetodoActual(m);
			
			//SETEO el offset de sus parametros
			listaParams= m.getEntradaParametros();
			
			for(EntradaPar p: listaParams) {
				//seteo offset de p
				
				//si es dinamico m -> offset desde 3
				if (m.getModificador().equals("dynamic")) {
					p.setOffset((listaParams.size() +4)  - p.getUbicacion());
				}
				else
				//si es estatico m -> offset desde 2
					p.setOffset((listaParams.size() +3)  - p.getUbicacion());
			}
			
			//genero el codigo del cuerpo de ese metodo
			m.generar();
		}	
		
		
		
		//genero codigo para todos los constructores
		for(EntradaCtor c: entradaCtor.values()) {
			//metodo actual es m
			Analizador_Sintactico.TS.setMetodoActual(c);
			
			//SETEO el offset de sus parametros
			listaParams= c.getEntradaParametros();
			
			for(EntradaPar p: listaParams) {
				//seteo offset de p
				p.setOffset(listaParams.size() +4  - p.getUbicacion());	// +4 porque el ctor tiene this
			}	
			
			//genero el codigo de ese metodo
			c.generar();
		}	
	}
	
	
	
	
//agrego un metodo si su nombre es unico, o difiere en su aridad -> recibo <metodo>$<aridad>
	public void agregarMetodo(EntradaMetodo m, List<EntradaPar> parametros, String metNom) throws Exception {
				
		EntradaMetodo met= entradaMetodo.get(m.getNombre());
		//si mi metodo ya existe en esta clase (con esa aridad) -> error
		if (met!=null)
				 throw new metodoDeclaradoInvalido(metNom, m.getToken().getNroLinea(), m.getToken().getNroColumna());
		
		//sino (tiene distinto nombre y por ende distinta aridad) -> lo agrego, chequeando sus parametros
		entradaMetodo.put(m.getNombre(), m);
		
		for (EntradaPar p: parametros) {
			//si el metodo no tiene parametros con ese nombre -> agrego el nuevo parametro p
			if (m.esVarMetodoDeclarado(p.getNombre()) == null) {
				m.agregarParametro(p.getNombre(),p.getTipo(),p.getUbicacion(), p.getToken());
			}
			// si ya existe ese nombre -> error
			else
				throw new parametroDeclaradoInvalido(p.getNombre(), p.getToken().getNroLinea(), p.getToken().getNroColumna());
		}//fin for
		
	}
	
	
	
	
//agrega un nuevo ctor, si tiene aridad distinta a alguno existente.	-> recibo <constructor>. La aridad la chequeo a mano
//ya tengo verificado que Ctor se llama igual a la clase.	
	public void agregarCtor(EntradaCtor c, List<EntradaPar> parametros) throws Exception{
		//tengo el mapeo <int, Ctor> en tabla entradaCtor, donde int es el nombre del Ctor, y 
		// esta determinado por la cantidad de parametros
		EntradaCtor ctor = entradaCtor.get(parametros.size()+"");
		
		//si mi ctor ya existe, veo si tiene la misma aridad. Si es asi -> error, Sino lo agrego
		if (ctor!= null) {
			//misma cant de parametros con algun constructor existente -> error
			for (EntradaCtor eC: entradaCtor.values()) {
			
				if (eC.getEntradaParametros().size() == parametros.size())
					throw new constructorExistente(c.getNombre(),c.getToken().getNroLinea(), c.getToken().getNroColumna());
			}
		}
		//sino, si tiene distinta aridad o no existia Ctor -> lo agrego, chequeando sus parametros
		// <K,V> donde K es cant de parametros y V es el constructor c a agregar
		entradaCtor.put(parametros.size()+"", c);
		//pongo la bandera en true
		hayCtor=true;
		
		for (EntradaPar p: parametros) {
			//si el Ctor no tiene parametros con ese nombre -> agrego el nuevo parametro p
			if (c.esVarMetodoDeclarado(p.getNombre())== null) {
				c.agregarParametro(p.getNombre(),p.getTipo(),p.getUbicacion(), p.getToken());
			}
			//sino, ya existe ese nombre de parametro -> error
			else
				throw new parametroDeclaradoInvalido(p.getNombre(), p.getToken().getNroLinea(), p.getToken().getNroColumna());
		}
	}
	
	
	//agrego var de inst
	public void agregarVar(EntradaVarInst v ) {
		//le asigno un offset a la variable nueva (1,2,3, etc)
		v.setOffset(maximoOffsetVarInst);
		
		
		//aumento el offset
		//maximoOffsetVarInst++;
		//agrego la variable a mi tabla de variables de instancia
		entradaVar.put(v.getNombre(), v);
	}
	
	public HashMap<String, EntradaMetodo> getEntradaMetodo(){
		return entradaMetodo;
	}
	
	public HashMap<String, EntradaVarInst> getEntradaVar(){
		return entradaVar;
	}
	
	//retorna true si tipo no es un tipo primitivo (incluido arreglos) o String
	private boolean noTipoPrimitivo(String tipo) {
		if (tipo.equals("arregloBoolean")|| tipo.equals("arregloChar")|| tipo.equals("arregloInt")|| tipo.equals("boolean")|| 
				tipo.equals("char")|| tipo.equals("int")|| tipo.equals("string")|| tipo.equals("void"))
				return false;
		else
				return true;	
	}
	
	
	public void chequearDeclaraciones() throws Exception{
		
		//Chequeo cada var de instancia de mi clase:		
		//para cada var de inst de mi clase
		for (EntradaVarInst v: entradaVar.values()) {
				//si es de tipo idClase,
				if (noTipoPrimitivo(v.getTipo().getTipo())) {
						//ahora si veo si esa clase esta declarada
						// si no es asi -> error
						if (Analizador_Sintactico.TS.esClaseDeclarada(v.getTipo().getTipo())==null)
							throw new claseNoDeclarada(v.getTipo().getTipo(), v.getToken().getNroLinea(), v.getToken().getNroColumna());
				}	
		}//fin for
		
		
		
		//Chequeo cada Ctor de mi clase
		for (EntradaCtor c: entradaCtor.values()) {	
			//chequeo parametros del Ctor...
			//para cada parametro de mi metodo
			for (EntradaPar p: c.getEntradaParametros()) {
				//si es de tipo idClase, chequeo que ese idClase exista
				if (noTipoPrimitivo(p.getTipo().getTipo())) {
						// si no es asi -> error
						if (Analizador_Sintactico.TS.esClaseDeclarada(p.getTipo().getTipo())==null)
							throw new claseNoDeclarada(p.getTipo().getTipo(), p.getToken().getNroLinea(), p.getToken().getNroColumna());
				}
			}//fin for
		}//fin for
		
		
		
		
		//Chequeo metodos de mi clase:
		//para cada metodo de mi clase
		for (EntradaMetodo m: entradaMetodo.values()) {
			
			// si no es un tipo de dato primitivo, chequeo que esa clase/tipo de dato  exista
			if (noTipoPrimitivo(m.getTipoRetorno().getTipo())) {
					//si no es clase declarada -> error
					if (Analizador_Sintactico.TS.esClaseDeclarada(m.getTipoRetorno().getTipo())==null)
						throw new claseNoDeclarada(m.getTipoRetorno().getTipo(), m.getToken().getNroLinea(), m.getToken().getNroColumna());		
			}//fin if
			
			//para cada parametro de mi metodo
			for (EntradaPar p: m.getEntradaParametros()) {
					//si es de tipo idClase, chequeo que ese idClase exista
					if (noTipoPrimitivo(p.getTipo().getTipo())) {
						if (Analizador_Sintactico.TS.esClaseDeclarada(p.getTipo().getTipo())==null)
							throw new claseNoDeclarada(p.getTipo().getTipo(), p.getToken().getNroLinea(), p.getToken().getNroColumna());	
					}
			}//fin for p
			
			if (m.getModificador().equals("dynamic"))
				metodosDyn.add(m);
		
		}//fin for m
		
		
	}//fin chequearDeclaraciones
	
	
	
	
	
	public void consolidar() throws Exception {
		//si estoy consolidado -> no hago nada
		if (consolidado) {
			return ;
		}

		//si NO estoy consolidado, llamo a consolidar a mi ancestro
		EntradaClase ancestro=Analizador_Sintactico.TS.getTabla().get(herencia);
		ancestro.consolidar();
		
		//Y ahora me consolido yo:
		
	//Consolido var de inst
		// creo lista auxiliar para agregar las variables heredadas al final del recorrido
		List<EntradaVarInst> varsParaAgregar= new ArrayList<EntradaVarInst>();
	
		
		//para cada variable de Inst de mi clase ancestro
		for (EntradaVarInst vAncestro: ancestro.getEntradaVar().values()) {
			
			//para cada variable de Inst  de mi clase
			for (EntradaVarInst v: entradaVar.values()) {
			
				//si tengo mismo nombre de var de inst que mi ancestro -> error, sin importar la visibilidad
				if (vAncestro.getNombre().equals(v.getNombre())) {
					throw new nombreVariableInvalida(v.getNombre(), v.getToken().getNroLinea(), v.getToken().getNroColumna());
				}
				//sino, agrego a la lista auxiliar esa var de inst (sea publico o privado)
				
				varsParaAgregar.add(vAncestro);
				//la cual ya tiene un offset fijado
				
			}//fin for de mi clase
			
			//Si mi clase no tiene ninguna variable de inst -> solo la agrego (caso particular)
			if (this.getEntradaVar().values().size() == 0) {
				varsParaAgregar.add(vAncestro);
				//la cual ya tiene un offset fijado
				
			}
			
		}
		
		

		
		
		
		
		//seteo offset de mis propias var de instancia a partir del maximo offset de mi ancestro
		int offsetVar= ancestro.getMaximoOffsetVarInst() ;
		for (EntradaVarInst v: entradaVar.values()) {
			v.setOffset(offsetVar);
			offsetVar++;
		}
		
		this.maximoOffsetVarInst= offsetVar;
		
		
		//luego de setear los offset de mis propias var de Inst, ya puedo
		//agregar ahora las variables heredadas a mi clase
		for(EntradaVarInst vHeredada: varsParaAgregar) {
				this.agregarVar(vHeredada);
			}
		
		
	
		
	//Consolido metodos
		//maximo offset sera el del padre (si es object o system es 0)
				if ( !this.nombre.equals("Object") &&  !this.nombre.equals("System"))
					maximoOffsetMetodo= Analizador_Sintactico.TS.getTabla().get(herencia).getMaximoOffsetMetodo() ;
		
		
		// creo lista auxiliar para agregar los metodos heredados al final del recorrido
		List<EntradaMetodo> metParaAgregar= new ArrayList<EntradaMetodo>();
		
		//para cada metodo de mi clase ancestro
		for (EntradaMetodo mAncestro: ancestro.getEntradaMetodo().values()) {
				
			//para cada metodo de mi clase
			
			//si mAncestro esta en this -> lo obtengo y evaluo redefinicion
			if (this.getEntradaMetodo().containsKey(mAncestro.getNombre())) {
				EntradaMetodo m= this.getEntradaMetodo().get(mAncestro.getNombre());
				
				//mismo nombre y por ende misma aridad de parametros -> chequeo posible redefinicion
					
					//comparo que tengan mismo Modificador
					if (mAncestro.getModificador().equals(m.getModificador())) {
						//comparo que tengan mismo tipo de dato de retorno
						if (mAncestro.getTipoRetorno().getTipo().equals(m.getTipoRetorno().getTipo())) {
						
							//si es asi, evaluo sus parametros -> <cantidad,Tipo> de cada parametro					
							evaluarRedefinicion(m.getEntradaParametros(), mAncestro.getEntradaParametros(), m.getToken());
							
							//como no hubo error, la redefinicion fue correcta, 
							//y este metodo ancestro no es heredado
							
							
						}
						else
							//si tienen mismo nombre/aridad, pero distinto tipo de ret -> error
							throw new redefinicionMetodoInvalido("Error el redefinir un metodo en la clase "+this.getNombre()+", se esperaba tipo de retorno '"+mAncestro.getTipoRetorno().getTipo()+"' y recibi '"+m.getTipoRetorno().getTipo()+"'",m.getToken().getNroLinea(), m.getToken().getNroColumna());
					}
					else
						//si tienen mismo nombre/aridad, pero distinto tipo de ret -> error
						throw new redefinicionMetodoInvalido("Error el redefinir un metodo en la clase "+this.getNombre()+", se esperaba modificador '"+mAncestro.getModificador()+"' y recibi '"+m.getModificador()+"'", m.getToken().getNroLinea(), m.getToken().getNroColumna());
			}//fin if
			
			//si el metodo ancestro no esta en this -> lo heredo
			else {
				//si tiene distinto nombre, solo lo agrego (heredo) a la lista auxiliar
				metParaAgregar.add(mAncestro);
			}
				
			
			//Si mi clase no tiene ningun metodo -> evaluo redefinicion
			if (this.getEntradaMetodo().values().size() == 0) {
				metParaAgregar.add(mAncestro);
			}
		
		}//fin forAncestro
		
		//agrego ahora los metodos heredados a mi clase
		for(EntradaMetodo mHeredado: metParaAgregar) {
			entradaMetodo.put(mHeredado.getNombre(), mHeredado);
		}
		
		
		//clase consolidada exitosamente
		consolidado=true;
		
		for (int i=0; i< metodosDyn.size();i++) {
			
			//obtengo clase padre
			EntradaClase padre= Analizador_Sintactico.TS.getTabla().get(this.herencia);
			
			
			//si esta en el padre -> uso el offset del metodo padre
			EntradaMetodo metPadre= padre.getEntradaMetodo().get(metodosDyn.get(i).getNombre());
			if (metPadre != null) {
				metodosDyn.get(i).setOffsetMetodo(metPadre.getOffsetMetodo());
				
			}
			//si no esta en el padre, el offset es mi variable
			else {
				metodosDyn.get(i).setOffsetMetodo(maximoOffsetMetodo);
				maximoOffsetMetodo++;
			}
		}
		
	}
	
	
	
	//Evalua los elementos de l y lAncestro uno a uno, si alguno difiere en tipo -> error
	private void evaluarRedefinicion(List<EntradaPar> l, List<EntradaPar> lAncestro, Token tok) throws Exception {
		// comparo sus tipos
		Iterator<EntradaPar> l1 = l.iterator();
		Iterator<EntradaPar> l2 = lAncestro.iterator(); //l2 tiene igual longitud que l1
		Tipo tipo,tipoAncestro;
		
		//mientras haya un siguiente -> itero
		while (l1.hasNext()) {
			tipo= l1.next().getTipo();
			tipoAncestro= l2.next().getTipo();
			
			//si tienen distinto tipo -> error
			if ( ! tipo.getTipo().equals(tipoAncestro.getTipo()))
				throw new redefinicionMetodoInvalido("Error el redefinir un metodo en la clase "+this.getNombre()+", se esperaba tipo de parametro '"+tipoAncestro.getTipo()+"' y recibi '"+tipo.getTipo()+"'", tok.getNroLinea(), tok.getNroColumna());
			
			//sino, sigo comprobando
		}
	}//fin evaluarRedefinicion
	
	
	
	
	public void chequearSentencias() throws Exception{
		//Hacer...logro Inline
		//chequeo atributos/varInst
		//for (EntradaVarInst v: entradaVar.values()) {
			
		//}
	
		
		//chequeo cuerpo ctores
		for (EntradaCtor c: entradaCtor.values()) {
			Analizador_Sintactico.TS.setMetodoActual(c);
			c.chequearSentencias();
		}
		
		//chequeo cuerpo metodos
		for(EntradaMetodo m: entradaMetodo.values()) {
			Analizador_Sintactico.TS.setMetodoActual(m);
			m.chequearSentencias();
		}	
	}
	
	
	

	
	
	public void imprimirAST(){
		System.out.println("#Atributos: ");
		for(EntradaVarInst entry: entradaVar.values()) {
			System.out.print("+ ");
			entry.imprimirAST();
			System.out.println("");
		}
		System.out.println("");
		System.out.println("#Constructores: ");
		for(EntradaCtor entry: entradaCtor.values()) {
			entry.imprimirAST();
		}
		System.out.println("");
		
		System.out.println("#Metodos: ");
		for(EntradaMetodo entry: entradaMetodo.values()) {
			entry.imprimirAST();
		}
		System.out.println("");
	}

	
	
}
