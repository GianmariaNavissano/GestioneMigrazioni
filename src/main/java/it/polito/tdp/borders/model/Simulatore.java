package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulatore {
	
	//Modello
	private Graph<Country, DefaultEdge> grafo;
	
	
	//Tipi di evento -> coda prioritaria
	private PriorityQueue<Evento> queue;
	
	//Parametri della simulazione (input)
	private int N_MIGRANTI = 1000;
	private Country partenza;
	
	//Valori di output
	private int T = -1;
	private Map<Country, Integer> stanziali;
	
	
	public void init(Country country, Graph<Country, DefaultEdge> grafo) {
		this.partenza = country;
		this.grafo = grafo;
		
		//stato iniziale
		this.T = 1;
		this.stanziali = new HashMap<>();
		for(Country c : this.grafo.vertexSet()) {
			stanziali.put(c, 0);
		}
		
		this.queue = new PriorityQueue<Evento>();
		this.queue.add(new Evento(T, partenza, N_MIGRANTI));
	}
	
	public void run() {
		while(!queue.isEmpty()) {
			Evento e = this.queue.poll();
			this.T = e.getT();
			int nPersone = e.getN();
			Country stato = e.getCountry();
			
			//ottengo i vicini di stato
			List<Country> vicini = Graphs.neighborListOf(this.grafo, stato);
			
			int migrantiPerStato = (nPersone/2)/vicini.size();
			
			if(migrantiPerStato > 0) {
				//le persone si possono muovere
				for(Country confinante : vicini) {
					queue.add(new Evento(e.getT()+1, confinante, migrantiPerStato));
				}
			}
			
			int stanziali = nPersone - (int)(nPersone/2);
			this.stanziali.put(stato, this.stanziali.get(stato)+stanziali);
			
		}
	}
	
	public Map<Country, Integer> getStanziali(){
		return this.stanziali;
	}
	
	public int getT() {
		return this.T;
	}
}
