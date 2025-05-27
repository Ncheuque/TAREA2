package backend;

import java.util.List;
import java.util.Arrays;

/**
 * Pregunta de verdadero/falso
 * @author Nicolas Cheuque
 */
public class PreguntaVerdaderoFalso extends Pregunta {
    private boolean respuestaCorrecta;
    
    public PreguntaVerdaderoFalso(int id, String texto, BloomLevel nivelBloom, 
                                 int tiempoEstimado, boolean respuestaCorrecta) {
        super(id, texto, nivelBloom, tiempoEstimado, TipoPregunta.VERDADERO_FALSO);
        this.respuestaCorrecta = respuestaCorrecta;
    }
    
    @Override
    public boolean verificarRespuesta(String respuesta) {
        if (respuesta == null) return false;
        
        try {
            int respuestaInt = Integer.parseInt(respuesta);
            return (respuestaInt == 0) ? respuestaCorrecta : !respuestaCorrecta;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    @Override
    public List<String> getOpciones() {
        return Arrays.asList("Verdadero", "Falso");
    }
    
    @Override
    public String getRespuestaCorrecta() {
        return respuestaCorrecta ? "0" : "1";
    }
}
