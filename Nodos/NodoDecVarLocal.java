package Nodos;


import java.util.ArrayList;
import java.util.List;

import Componentes.Analizador_Sintactico;
import Componentes.EntradaVarInst;
import Componentes.EntradaVarLocal;
import Componentes.Token;
import Componentes.Unidad;
import Excepciones.claseNoDeclarada;
import Excepciones.parametroDeclaradoInvalido;
import Tipos_Datos.Tipo;
import Tipos_Datos.TipoIdClase;


public class NodoDecVarLocal extends NodoSentencia {
	protected List<EntradaVarInst> listaVars; //mis var locales
	protected Tipo tipoLista;
	protected NodoBloque bloque;
	protected Unidad unidad;
	protected List<EntradaVarLocal> listaAux;
	
	
	public NodoDecVarLocal(List<EntradaVarInst> l, Token tok, Tipo tipo, NodoBloque b) {
		super(tok);
		listaVars= l;
		tipoLista= tipo;
		bloque=b;
		listaAux= new ArrayList<EntradaVarLocal>();
	}

	public List<EntradaVarInst> getListaVars(){
		return listaVars;
	}
	
	
	public void chequear() throws Exception {
		//de listaVars uso nombre y tipo para crear varMetodo
		
		//si mis variables son de tipo idClase veo si esta ese tipo en la TS
		if (tipoLista instanceof TipoIdClase)
			//si no esta -> error
			if (Analizador_Sintactico.TS.esClaseDeclarada(tipoLista.getTipo()) == null)
				throw new claseNoDeclarada(tipoLista.getTipo(),token.getNroLinea(),token.getNroColumna());
		
		unidad= Analizador_Sintactico.TS.getUnidadActual();
		

		//para cada elemento v de mi listaVars -> TS.getUnidadActual.agregarVarLocal(v.nombre y v.tipo) a varsParams
		for (EntradaVarInst v: listaVars) {
			//agrego mi var local a varsParams de Unidad
			
			if (unidad.getEntradaVarsParams().get(v.getNombre()) !=null)
				throw new parametroDeclaradoInvalido(v.getNombre(),v.getToken().getNroLinea(),v.getToken().getNroColumna());
				
			
			EntradaVarLocal entradaAux= new EntradaVarLocal(v.getNombre(), v.getTipo(),v.getToken());
					
			bloque.agregarVariableLocal(entradaAux.getNombre(),entradaAux);
			
				
			unidad.getEntradaVarsParams().put(entradaAux.getNombre(), entradaAux);
			//unidad.agregarVarLocal(v.getNombre(),tipoLista,token);	
			//agrego la nueva var local a las variables de mi bloque
			

			listaAux.add(entradaAux);
			//bloque.agregarVariableLocal(entradaAux);	
		}
	}

	
	public void generar() throws Exception{
		//para cada elemento v de mi listaVars -> TS.getUnidadActual.agregarVarLocal(v.nombre y v.tipo) a varsParams
		for (EntradaVarLocal v: listaAux) {
			
			v.setOffset((unidad.offsetVarLocal * -1));
			//agrego mi var local a varsParams de Unidad
			//unidad.agregarVarLocal(v.getNombre(),tipoLista,token);	
			//
			
			bloque.agregarVariableLocal(v.getNombre(),v);
			unidad.getEntradaVarsParams().put(v.getNombre(), v);
			
			
			unidad.offsetVarLocal++;
		}
		//unidad.setOffsetMetodo(listaAux.size());
		//bloque.setCantVarsLocales(listaVars.size());
		
		//reservo memoria para mis var locales -> rmem
		Analizador_Sintactico.salida.generar("RMEM "+listaAux.size(),"reservo memoria para las var locales de la unidad");
	}
	
	
	
	
	
	public void imprimir(int n){
		tabs(n); 
		//System.out.println("Declaracion variable de bloque "+bloque);
		for (EntradaVarInst v: listaVars) {
			tabs(n+1); System.out.println("   Tipo: "+tipoLista+"");
			tabs(n+1); System.out.println("   Nombre: "+v.getNombre()+"");
		}
	}
	
}
