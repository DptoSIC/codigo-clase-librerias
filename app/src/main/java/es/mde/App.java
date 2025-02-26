/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package es.mde;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import es.lanyu.commons.config.Propiedades;
import es.lanyu.commons.servicios.entidad.ServicioEntidad;
import es.lanyu.commons.servicios.entidad.ServicioEntidadImpl;
import es.lanyu.comun.evento.Partido;
import es.lanyu.comun.suceso.Suceso;
//import es.otro.Participante;
//import es.otro.Partido;
import es.lanyu.participante.Participante;

public class App {

//	static ServicioEntidad servicioEntidad;
	
    public static void main(String[] args) throws StreamWriteException, DatabindException, IOException {
//        Participante participante = new Participante(1, "Real Madrid");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//        File archivo = new File("./participante.json");
////        objectMapper.writeValue(archivo, participante);
//        String json = mapper.writeValueAsString(participante);
//        System.out.println(json);
//        Participante participanteLeido;
////        participanteLeido = objectMapper.readValue(archivo, Participante.class);
//        participanteLeido = mapper.readValue(json, Participante.class);
//        System.out.println(participanteLeido);
        
        
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.addMixIn(Participante.class, MixIns.Participantes.class);
//        mapper.addMixIn(Partido.class, MixIns.Partidos.class);
//        mapper.addMixIn(Partido.class, MixIns.Datable.class);
        mapper.addMixIn(Partido.class, MixIns.Datables.class);
        mapper.addMixIn(Suceso.class, MixIns.Datables.class);
        
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    	File participantesFile = new File("./participantes.json");
//    	List<Participante> participantes = new ArrayList<>();
    	ServicioEntidad servicioEntidad = new ServicioEntidadImpl();
    	try (BufferedReader br = new BufferedReader(new FileReader(participantesFile))) {
    		for (String linea : br.lines().toList()) {
    			if (!linea.startsWith("*")) {
//    				participantes.add(mapper.readValue(linea, Participante.class));
    				servicioEntidad.getGestorNombrables().addNombrable(Participante.class, mapper.readValue(linea, Participante.class));
    			}
    		}
        } catch (Exception e) {
    		e.printStackTrace(System.out);
		}
//    	Collection<Participante> participantes = servicioEntidad.getElementosRegistradosDe(Participante.class);
//    	participantes.forEach(System.out::println);
    	System.out.println("Total " + servicioEntidad.getElementosRegistradosDe(Participante.class).size() + " participantes.");
    	
//    	System.getProperties().list(System.out);
    	Properties propiedades = new Propiedades();
    	propiedades.load(new BufferedReader(new FileReader(new File("mapaCSV.properties"))));
    	Properties propiedadesInvertidas = new Propiedades();
    	propiedades.forEach((k, v) -> propiedadesInvertidas.put(v, k));
    	propiedadesInvertidas.list(System.out);
    	System.out.println("FIN LISTA\n");
    	
    	File liga = new File("./SP1.csv");
    	List<Partido> partidos = new ArrayList<>();
    	try (BufferedReader br = new BufferedReader(new FileReader(liga))) {
    		br.lines().map(l -> l.split(","))
    				  .map(a -> new Partido(getParticipante(a[3], servicioEntidad, propiedadesInvertidas),
    						  				getParticipante(a[4], servicioEntidad, propiedadesInvertidas)
//    						  				Integer.parseInt(a[5]),
//    						  				Integer.parseInt(a[6])
    						  ))
    				  .forEach(p -> partidos.add(p));
    	} catch (Exception e) {
    		e.printStackTrace(System.out);
		}
    	
    	mapper.writeValue(new File("./partidosSP1.json"), partidos);
    	
//    	File liga = new File("./partidos.json");
//    	List<Partido> partidos = new ArrayList<>();
//    	try (BufferedReader br = new BufferedReader(new FileReader(liga))) {
//    		br.lines().map(l -> lineaToPartido(l, mapper, servicioEntidad))
//    				  .forEach(p -> partidos.add(p));
//    	} catch (Exception e) {
//    		e.printStackTrace(System.out);
//		}
//    	partidos.forEach(System.out::println);
    	
    	
    	System.out.println("\n--- Gana RM por más de 2 ---\n");
    	Participante RM = getParticipante("Real Madrid", servicioEntidad, propiedades);
    	partidos.stream()
//    			.filter(p -> {
//    							boolean valido = false;
//	    						if (p.getLocal().equals(RM)) {
//	    							valido = p.getGolesLocal() - p.getGolesVisitante() > 2;
//	    						} else if (p.getVisitante() == RM) {
//	    							valido = p.getGolesLocal() - p.getGolesVisitante() < -2;
//	    						}
//    							return valido;
//	    					 })
    			.filter(p -> p.getGanador() == RM)// && p..getDiferenciaGoles() > 2)
    			.forEach(System.out::println);
    	

    }
    
    static Partido lineaToPartido(String linea, ObjectMapper mapper, ServicioEntidad servicioEntidad) {
    	Partido partido = null;
		try {
			partido = mapper.readValue(linea, Partido.class);
			partido.setServicioEntidad(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return partido;
    }
    
    static private Participante getParticipante(String nombre, Collection<Participante> participantes, Properties propiedades) {
    	return participantes.stream()
    						.filter(p -> propiedades.entrySet().stream()
    														   .filter(e -> nombre.equals(e.getValue()))
    														   .findFirst()
    														   .orElse(new Entry<Object, Object>() {
																
																	@Override
																	public Object setValue(Object value) { return value; }
																	
																	@Override
																	public Object getValue() { return nombre; }
																	
																	@Override
																	public Object getKey() { return nombre; }
																})
    														   .getKey().equals(p.getIdentificador()))
    						.findFirst()
    						.orElse(new Participante(nombre));
    }
    
    static private Participante getParticipante(String nombre, ServicioEntidad servicioEntidad, Properties propiedades) {
    	return servicioEntidad.getIdentificable(Participante.class, propiedades.getProperty(nombre));
    }
}
