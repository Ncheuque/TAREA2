package backend.models;

public class PreguntaVerdaderoFalso extends Pregunta {
    private boolean respuestaCorrecta;
    
    public PreguntaVerdaderoFalso(int id, String texto, NivelBloom nivelBloom,
                                 int tiempoEstimado, boolean respuestaCorrecta, int anio) {
        super(id, texto, nivelBloom, tiempoEstimado, anio);
        this.respuestaCorrecta = respuestaCorrecta;
    }
    
    public boolean getRespuestaCorrectaBoolean() {
        return respuestaCorrecta;
    }
    
    public void setRespuestaCorrecta(boolean respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }
    
    @Override
    public String getTipo() {
        return "Verdadero/Falso";
    }
    
    @Override
    public boolean validarRespuesta(Object respuesta) {
        if (respuesta instanceof Boolean) {
            return ((Boolean) respuesta).equals(respuestaCorrecta);
        }
        return false;
    }
    
    @Override
    public Object getRespuestaCorrecta() {
        return respuestaCorrecta;
    }
}
