package es.mde;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id" })
//@JsonIgnoreProperties({ "nombre" })
public class Participante {
	private String id;
	private String nombre;

	@JsonProperty("identificador")
	@JsonAlias("id")
	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Participante() {}
	
	public Participante(String id, String nombre) {
		setId(id);
		setNombre(nombre);
	}

	@Override
	public String toString() {
		return "#" + id + " " + nombre;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Participante other = (Participante) obj;
		return Objects.equals(id, other.id);
	}


	
	
}
