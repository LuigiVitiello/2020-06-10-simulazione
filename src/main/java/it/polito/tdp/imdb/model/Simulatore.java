package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.sun.glass.ui.CommonDialogs.Type;

public class Simulatore {

	//modello del mondo 
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private List<Actor> daIntervistare; //spesso serve qualche lista dei vertici o di qualcos altro
	//altre variabili che possono servire, dipendono dal tema d'esame
	Actor piuVicino;
	int numAttoriIntervistati;
	List<Actor> intervistati;
	//coda degli eventi
	private PriorityQueue<Evento> queue;
	
	//parametri in input
	int n;
	//parametri in output
	int nPause;
	//spesso sono dei contatori 
	int nM;
	int nF;
	
	public void init(int ng,Graph<Actor, DefaultWeightedEdge> g) {
		this.grafo=g;
		this.n=ng;
		
		this.daIntervistare= new ArrayList<>();
		for (Actor v : this.grafo.vertexSet())  //possibile inizializzazione di liste o mappe
			this.daIntervistare.add(v);
		this.queue= new PriorityQueue<Evento>();
		//inizializzazione delle varie variabili messe nel modello del mondo
		this.piuVicino=null;
		//inizializzazione a 0 o vuote dei parametri in input
		nPause =0;
		nM=0;
		nF=0;
		this.numAttoriIntervistati=0;
		Evento e= new Evento(Evento.Type.SCELTA_CASUALE,1);	
		this.queue.add(e);
		this.intervistati = new ArrayList<>();
		//inseriti++;
		}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Evento e = queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Evento e) {
		int day= e.getGiorno();
		//prendo i vari attributi dell'evento e, li salvo in delle variabili
		
		if(day>this.n) //possibile caso terminale
			return;
		
		switch(e.getTipo()) {
		case SCELTA_CASUALE:
			int scelto= (int) (Math.random()*this.daIntervistare.size());
			Actor a = this.daIntervistare.get(scelto-1);
			intervistati.add(a);
			this.daIntervistare.remove(a);
			if(a.getGender()=="M") {
				this.nM++;
				this.nF=0;
			}
			else {
				this.nF++;
				this.nM=0;
			}
			//poi schedulo
			if(this.nF==2 || this.nM==2) {
				this.nF=0;
				this.nM=0;
				int prob=(int) (Math.random()*100); //a volte *lista.size() se voglio prendere un numero casuale da 1 a lista.size()
				if (prob<90)
					this.queue.add(new Evento(Evento.Type.PAUSA, day+1) );
				else {
					int probab= (int) (Math.random()*100);
					if(probab<60) {
						this.queue.add( new Evento(Evento.Type.SCELTA_CASUALE,day+1));
					}
					else {
						this.piuVicino = this.attorePiuVicino(a);
						if (piuVicino==null)
							this.queue.add( new Evento(Evento.Type.SCELTA_CASUALE,day+1));
						else {
							this.queue.add( new Evento(Evento.Type.VICINO,day+1));
						}
					}
				}
			}
			else {
				int probab= (int) (Math.random()*100);
				if(probab<60) {
					this.queue.add( new Evento(Evento.Type.SCELTA_CASUALE,day+1));
				}
				else {
					this.piuVicino = this.attorePiuVicino(a);
					if (piuVicino==null)
						this.queue.add( new Evento(Evento.Type.SCELTA_CASUALE,day+1));
					else {
						this.queue.add( new Evento(Evento.Type.VICINO,day+1));
					}
				}
			}
			this.numAttoriIntervistati++;
			break;
		case VICINO:
			Actor ac = this.piuVicino;
			intervistati.add(ac);
			this.daIntervistare.remove(ac);
			if(ac.getGender()=="M") {
				this.nM++;
				this.nF=0;
			}
			else {
				this.nF++;
				this.nM=0;
			}
			//poi schedulo
			if(this.nF==2 || this.nM==2) {
				this.nF=0;
				this.nM=0;
				int prob=(int) (Math.random()*100);
				if (prob<90)
					this.queue.add( new Evento(Evento.Type.PAUSA,day+1));
				else {
					int probab= (int) (Math.random()*100);
					if(probab<60) {
						this.queue.add( new Evento(Evento.Type.SCELTA_CASUALE,day+1));
					}
					else {
						this.piuVicino = this.attorePiuVicino(ac);
						if (piuVicino==null)
							this.queue.add( new Evento(Evento.Type.SCELTA_CASUALE,day+1));
						else {
							this.queue.add( new Evento(Evento.Type.VICINO,day+1));
						}
					}
				}
			}
			else {
				int probab= (int) (Math.random()*100);
				if(probab<60) {
					this.queue.add( new Evento(Evento.Type.SCELTA_CASUALE,day+1));
				}
				else {
					this.piuVicino = this.attorePiuVicino(ac);
					if (piuVicino==null)
						this.queue.add( new Evento(Evento.Type.SCELTA_CASUALE,day+1));
					else {
						this.queue.add( new Evento(Evento.Type.VICINO,day+1));
					}
				}
			}
			this.numAttoriIntervistati++;
			break;
		case PAUSA:
			this.nPause++;
			this.queue.add( new Evento(Evento.Type.SCELTA_CASUALE,day+1));
			break;
		}
		
	}
	
	private Actor attorePiuVicino(Actor actor) { //metodo che puo servire negli switch
		int max=0;
		Actor best=null;
		for (Actor a : Graphs.neighborListOf(grafo, actor)) {
			int grado= grafo.degreeOf(a);
			if (grado>max && this.daIntervistare.contains(a)) {
				max=grado;
				best=a;
			}
		}
		return best;
	}


	//metodi getter per i parametri in output
	

	public int getNPause() {
		return this.nPause;
	}
	
	public List<Actor> getIntervistati(){
		return this.intervistati;
	}
}
