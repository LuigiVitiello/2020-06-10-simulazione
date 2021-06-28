package it.polito.tdp.imdb.model;

public class Evento implements Comparable<Evento> {

	public enum Type{
		SCELTA_CASUALE,
		VICINO,
		PAUSA
	}

	public Evento(Type tipo, int giorno) {
		super();
		this.tipo = tipo;
		this.attore = attore;
		this.giorno = giorno;
	}
	private Type tipo;
	private Actor attore;
	private int giorno;
	
	public Type getTipo() {
		return tipo;
	}
	public void setTipo(Type tipo) {
		this.tipo = tipo;
	}
	public int getGiorno() {
		return giorno;
	}
	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}
	public Actor getAttore() {
		return attore;
	}
	public void setAttore(Actor attore) {
		this.attore = attore;
	}
	@Override
	public int compareTo(Evento e) {
		
		return Integer.compare(this.giorno, e.giorno);
	}
	
}
