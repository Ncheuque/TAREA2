package backend;

import java.util.List;
import java.util.ArrayList;

/**
 * Pregunta de opción múltiple
 * @author Nicolas Cheuque
 */
public class PreguntaOpcionMultiple extends Pregunta {
    private List<String> opciones;
    private int opcionCorrecta;
    
    public PreguntaOpcionMultiple(int id, String texto, BloomLevel nivelBloom, 
                                 int tiempoEstimado, List<String> opciones, int opcionCorrecta) {
        super(id, texto, nivelBloom, tiempoEstimado, TipoPregunta.OPCION_MULTIPLE);
        this.opciones = new ArrayList<>(opciones);
        this.opcionCorrecta = opcionCorrecta;
    }
    
    @Override
    public boolean verificarRespuesta(String respuesta) {
        try {
            return Integer.parseInt(respuesta) == opcionCorrecta;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    @Override
    public List<String> getOpciones() {
        return new ArrayList<>(opciones);
    }
    
    @Override
    public String getRespuestaCorrecta() {
        return String.valueOf(opcionCorrecta);
    }
}
