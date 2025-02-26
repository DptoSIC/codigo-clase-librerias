package es.otro;

import java.util.Arrays;
import java.util.Collection;

public class Partido extends Evento {
	private Participante local, visitante;
	private int golesLocal, golesVisitante;

	public Participante getLocal() {
		return local;
	}

	public Participante getVisitante() {
		return visitante;
	}
	
	@Override
	public Collection<Participante> getParticipantes() {
		return Arrays.asList(getLocal(), getVisitante());
	}

	public int getGolesLocal() {
		return golesLocal;
	}

	public int getGolesVisitante() {
		return golesVisitante;
	}

	public int getDiferenciaGoles() {
		return Math.abs(getGolesLocal() - getGolesVisitante());
	}
	
	public Participante ganador() {
		return getGolesLocal() == getGolesVisitante() ? null
													  : getGolesLocal() > getGolesVisitante() ? getLocal() : getVisitante();
	}
	
	public Partido(Participante local, Participante visitante, int golesLocal, int golesVisitante) {
		super();
		this.local = local;
		this.visitante = visitante;
		this.golesLocal = golesLocal;
		this.golesVisitante = golesVisitante;
	}

	@Override
	public String getResultado() {
		return getGolesLocal() + "-" + getGolesVisitante();
	}
	
	@Override
	public String toString() {
		return local.getNombre() + " " + getResultado() + " " + visitante.getNombre() + " gana " + ganador();
	}
}
