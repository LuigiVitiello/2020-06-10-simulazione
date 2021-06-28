package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	private ImdbDAO dao;
	
	private Graph<Actor, DefaultWeightedEdge> grafo;   //queste tre servono quasi sempre
	private Map<Integer, Actor> idMap;         
	private List<Adiacenza> adiacenze;				//crea la classe adiacenze
	private ConnectivityInspector<Actor, DefaultWeightedEdge> ci;
	/*private List<> best;    //queste due sono solitamente necessarie per la ricorsione
	private int max;
*/
	 /* public List<String> get(){         ci sono sempre metodi cosi
		  return  dao.get();
		  }
	*/
	
	private Simulatore sim;
	
	public Model() {
		this.dao = new ImdbDAO();
		this.sim = new Simulatore();
	}
	public List<String> getGeneri(){
		return this.dao.getGeneri();
	}
	
	public Graph<Actor, DefaultWeightedEdge> getGrafo(){
		return this.grafo;
	}
			
	public void creaGrafo(String genere) {         //spesso è richiesto un parametro da controller es: anno, colore
		
		grafo = new SimpleWeightedGraph<Actor,DefaultWeightedEdge>(DefaultWeightedEdge.class);

		//aggiungo i vertici

		this.idMap = new HashMap<Integer, Actor>(); 
		dao.listAllActors(idMap);//inizializzo la mappa 
		List<Actor> act = dao.getVertici(genere,idMap);//qui serve un metodo dao per riempire la mappa
		Graphs.addAllVertices(grafo, act);
		
		//aggiungo gli archi
		
		adiacenze= dao.getAdiacenze(genere, idMap);//metodo dao per archi
		
		for(Adiacenza a: adiacenze) {
             if(this.grafo.getEdge(a.getA1(), a.getA2()) ==null) {
                 Graphs.addEdge(grafo, a.getA1(), a.getA2(),(double) a.getPeso());
             }
		
		}
		//stampo a console i numeri 
		System.out.println( "#Vertici "+this.getNVertici()+" "+"#archi= "+this.getNArchi());
		ci = new ConnectivityInspector<>(grafo);
}
	

	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}

    public int getNArchi(){
        return this.grafo.edgeSet().size();
    }
    
    public Set<Actor> getVertici(){
    	return this.grafo.vertexSet();
    }
	
    public List<Actor> raggiungibili(Actor act){
    	List<Actor> result = new ArrayList<>();
    	for(Actor a : ci.connectedSetOf(act)) {
    		result.add(a);
    	}
    	result.remove(act);
    	Collections.sort(result);
    	System.out.println( result.size());
    	return result;
    }


    public int simula(int ng) { 
 	    this.sim.init(ng, this.grafo);
    	this.sim.run();
    	
    	return sim.getNPause();
    }
    
    public List<Actor> getIntervist (){
    	return sim.getIntervistati();
    }
}
