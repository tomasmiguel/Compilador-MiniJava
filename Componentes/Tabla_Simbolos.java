package Componentes;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Excepciones.claseDeclaradaInvalida;
import Excepciones.claseNoDeclarada;
import Excepciones.herenciaCircular;
import Excepciones.metodoMainExistenteEnClase;
import Excepciones.nombreConstructorInvalido;
import Excepciones.nombreVariableInvalida;
import Nodos.NodoBloque;
import Nodos.NodoBloqueSystem;
import Tipos_Datos.TipoBoolean;
import Tipos_Datos.TipoChar;
import Tipos_Datos.TipoInt;
import Tipos_Datos.TipoString;
import Tipos_Datos.TipoVoid;


public class Tabla_Simbolos {
	private HashMap<String,EntradaClase> tabla;
	private EntradaClase claseActual;
	private EntradaMetodo miMain;
	private Unidad unidadActual;
	private NodoBloque bloqueActual;
	private boolean hayUnMain; //bandera -> true si hay un metodo estatico llamado main
	
	
	
	//contiene el par (idClase, entradaClase)
	public Tabla_Simbolos() throws Exception {
		tabla= new HashMap<String,EntradaClase>(16);
		claseActual=null;
		unidadActual=null;
		bloqueActual=null;
		
		crearClaseObjectYSystem();
		
		hayUnMain=false;
		miMain = null;
	}
	
	
	//recorre la tabla y devuelve una entrada valida si la clase se encuentra en la tabla. null en caso contrario.
	//asumo que idClase es distinto de Object o System
	public EntradaClase esClaseDeclarada(String idClase) {
		return tabla.get(idClase);
	}
	
	public void setClaseActual(EntradaClase c) {
		claseActual=c;
	}
	
	public void setMetodoActual(Unidad m) {
		unidadActual=m;
	}
	
	public void setBloqueActual(NodoBloque n) {
		bloqueActual=n;
	}
	
	public EntradaClase getClaseActual() {
		return claseActual;
	}
	
	public Unidad getUnidadActual() {
		return unidadActual;
	}
	
	public NodoBloque getBloqueActual() {
		return bloqueActual;
	}
	
	public HashMap<String,EntradaClase> getTabla() {
		return tabla;
	}
	
	public boolean hayUnMain() {
		return hayUnMain;
	}
	
	
	public Unidad getMain() {
		return miMain;
	}
	
	
	
	public EntradaClase agregarClase(String nombre, EntradaClase c) throws Exception {
		//si es una nueva clase
		if (tabla.get(nombre) == null) {
			tabla.put(nombre, c);
			claseActual=c;
		}
		else
		//si ya hay una clase con ese nombre -> error
			throw new claseDeclaradaInvalida(nombre, c.getTokenLinea(), c.getTokenColumna());
	return c;
	}
	
	
	
	//recibo metNom porque m ya tiene la aridad concatenada a su nombre
	public EntradaMetodo agregarMetodo(EntradaMetodo m, List<EntradaPar> parametros, String metNom)throws Exception{
		
		//chequeo si es STATIC VOID MAIN(), sin parametros. Si es asi pongo true la bandera -> solo acepto UN static void main() !!
		if (m.getNombre().equals("main$0") && m.getModificador().equals("static") && m.getTipoRetorno().getTipo().equals("void") &&
				parametros.size()==0) {
			if (!hayUnMain) {
				hayUnMain=true;
				//guardo el metodo static main 
				miMain = m;
			}
			else //error -> ya habia un static main declarado previamente
				throw new metodoMainExistenteEnClase(m.getToken().getNroLinea(), m.getToken().getNroColumna());
			
		}
		//sino agrego
		unidadActual= m;
		claseActual.agregarMetodo(m, parametros, metNom);
		
		m.setNombreAux(metNom);
		
	 return m;
	}
	
	
	//agrego un constructor 
	public void agregarCtor(EntradaCtor c, List<EntradaPar> parametros) throws Exception{
		//si se llama igual que la claseActual
		if (claseActual.getNombre().equals(c.getNombre())) {
				//lo creo y agrego sus parametros
				unidadActual= c;
				claseActual.agregarCtor(c, parametros);
		}//fin if
		else
			//el Ctor se llama distinto a la clase -> error
			throw new nombreConstructorInvalido(c.getNombre(),c.getToken().getNroLinea(), c.getToken().getNroColumna());
	
	}
	
	
	//agregar variable de inst/atributo a una clase
		public void agregarVariable(EntradaVarInst v)throws Exception{
			//si la variable actual no esta declarada, la agrego
			if (claseActual.esVarDeclarada(v.getNombre())==null)
				claseActual.agregarVar(v);
			//si existe variable con ese nombre -> error
			else
				throw new nombreVariableInvalida(v.getNombre(), claseActual.getTokenLinea(), claseActual.getTokenColumna());
		}
		
	
	
	
	private void crearClaseObjectYSystem() throws Exception {
		//clase Object
		EntradaClase clase= this.agregarClase("Object",new EntradaClase("Object", new Token("Object","Object",0,0)));
		
		this.agregarCtor(new EntradaCtor("Object", new Token("Object","Object",0,0)), new ArrayList<EntradaPar>());
		//Object no tiene metodos ni atributos, solo lo consolido
		clase.setConsolidado();
		
		//clase System
		clase= this.agregarClase("System", new EntradaClase("System", new Token("System","System",0,0)));
		this.setClaseActual(clase);
		//System hereda de Object
		claseActual.setHerencia("Object");
		//consolido
		clase.setConsolidado();
		
		
		//etapa5 -> agrego las traducciones
		NodoBloqueSystem nodo;
		
		//agrego los metodos de clase System
		this.setMetodoActual(this.agregarMetodo(new EntradaMetodo("read$0",new TipoInt(""),"static",null, clase),new ArrayList<EntradaPar>(),"read"));
		nodo= new NodoBloqueSystem(null, new Token("","",0,0));
		unidadActual.agregarCuerpo(nodo);
		
		this.setMetodoActual(this.agregarMetodo(new EntradaMetodo("printB$1",new TipoVoid(""),"static",null, clase),new ArrayList<EntradaPar>(),"printB"));
		unidadActual.agregarParametro("b", new TipoBoolean(""), 1,null);
		nodo= new NodoBloqueSystem(null, new Token("","",0,0));
		unidadActual.agregarCuerpo(nodo);
		
		this.setMetodoActual(this.agregarMetodo(new EntradaMetodo("printC$1",new TipoVoid(""),"static",null, clase),new ArrayList<EntradaPar>(),"printC"));
		unidadActual.agregarParametro("c", new TipoChar(""), 1,null);
		nodo= new NodoBloqueSystem(null, new Token("","",0,0));
		unidadActual.agregarCuerpo(nodo);
		
		this.setMetodoActual(this.agregarMetodo(new EntradaMetodo("printI$1",new TipoVoid(""),"static",null, clase),new ArrayList<EntradaPar>(),"printI"));
		unidadActual.agregarParametro("i", new TipoInt(""), 1,null);
		nodo= new NodoBloqueSystem(null, new Token("","",0,0));
		unidadActual.agregarCuerpo(nodo);
		
		this.setMetodoActual(this.agregarMetodo(new EntradaMetodo("printS$1",new TipoVoid(""),"static",null, clase),new ArrayList<EntradaPar>(),"printS"));
		unidadActual.agregarParametro("s", new TipoString(""), 1,null);
		nodo= new NodoBloqueSystem(null, new Token("","",0,0));
		unidadActual.agregarCuerpo(nodo);
		
		this.setMetodoActual(this.agregarMetodo(new EntradaMetodo("println$0",new TipoVoid(""),"static",null, clase),new ArrayList<EntradaPar>(),"println"));
		nodo= new NodoBloqueSystem(null, new Token("","",0,0));
		unidadActual.agregarCuerpo(nodo);
		
		this.setMetodoActual(this.agregarMetodo(new EntradaMetodo("printBln$1",new TipoVoid(""),"static",null, clase),new ArrayList<EntradaPar>(),"printBln"));
		unidadActual.agregarParametro("b", new TipoBoolean(""), 1,null);
		nodo= new NodoBloqueSystem(null, new Token("","",0,0));
		unidadActual.agregarCuerpo(nodo);
		
		this.setMetodoActual(this.agregarMetodo(new EntradaMetodo("printCln$1",new TipoVoid(""),"static",null, clase),new ArrayList<EntradaPar>(),"printCln"));
		unidadActual.agregarParametro("c", new TipoChar(""), 1,null);
		nodo= new NodoBloqueSystem(null, new Token("","",0,0));
		unidadActual.agregarCuerpo(nodo);
		
		this.setMetodoActual(this.agregarMetodo(new EntradaMetodo("printIln$1",new TipoVoid(""),"static",null, clase),new ArrayList<EntradaPar>(),"printIln"));
		unidadActual.agregarParametro("i", new TipoInt(""), 1,null);
		nodo= new NodoBloqueSystem(null, new Token("","",0,0));
		unidadActual.agregarCuerpo(nodo);
		
		this.setMetodoActual(this.agregarMetodo(new EntradaMetodo("printSln$1",new TipoVoid(""),"static",null, clase),new ArrayList<EntradaPar>(),"printSln"));
		unidadActual.agregarParametro("s", new TipoString(""), 1,null);
		nodo= new NodoBloqueSystem(null, new Token("","",0,0));
		unidadActual.agregarCuerpo(nodo);
		
	}
	
	
	
	
	//metodo auxiliar para chequear que no haya herencia circular
	private void noHerenciaAux(EntradaClase c, String herencia) throws Exception{
		//llegue a clase Object
		if (herencia.equals("Object")) {
			//todo ok
			return ;
		}
		
		//si heredo de mi mismo -> error
		String str= c.getNombre();
		if (str.equals(herencia))
			throw new herenciaCircular(str, claseActual.getTokenLinea(), claseActual.getTokenColumna());
		
		//si no esta declarado mi ancestro -> error
		if (this.esClaseDeclarada(c.getHerencia())==null)
			throw new claseNoDeclarada(c.getHerencia(), c.getTokenLinea(), c.getTokenColumna());
		//sino, llamo a otro ancestro
		noHerenciaAux(c, tabla.get(herencia).getHerencia());
	}
	
	
	//Chequeo de herencia NO circular
	//chequea que algun ancestro del cual una clase hereda, sea distinto de si mismo
	public void noHerenciaCircular() throws Exception{
		for (EntradaClase c: tabla.values()) {
			//verifico que no sea la clase Object. Si no es asi, realizo el chequeo
			if (c.getHerencia()!= null) {
				//si no esta declarado mi ancestro -> error
				if (this.esClaseDeclarada(c.getHerencia())==null)
					throw new claseNoDeclarada(c.getHerencia(), c.getTokenLinea(), c.getTokenColumna());
			
				noHerenciaAux(c, c.getHerencia());
			}
			//sino, era Object -> sigo iterando
		}
	}//fin noHerenciaCircular

	
	
	
	public void imprimirAST(){
		System.out.println("=======================");
		for(EntradaClase entry: tabla.values()) {
			if ( !  (entry.getNombre().equals("Object") || entry.getNombre().equals("System"))) {
				System.out.println(""+entry.getNombre()+" extends "+entry.getHerencia());
				entry.imprimirAST();
				System.out.println("=======================");	
			}
		}	
		System.out.println("=======================");	
	}
	
	
	
	
	
	
}//fin clase Tabla_Simbolos
