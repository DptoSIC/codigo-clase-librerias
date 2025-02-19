package es.mde;

import java.util.Collection;

public abstract class Evento {

	abstract public Collection<Participante> getParticipantes();

	abstract public String getResultado();

}