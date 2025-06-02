package backend.models;

import java.util.List;
import java.util.ArrayList;

public class PreguntaOpcionMultiple extends Pregunta {
    private List<String> opciones;
    private int opcionCorrecta;
    
    public PreguntaOpcionMultiple(int id, String texto, NivelBloom nivelBloom, 
                                 int tiempoEstimado, List<String> opciones, 
                                 int opcionCorrecta, int anio) {
        super(id, texto, nivelBloom, tiempoEstimado, anio);
        this.opciones = new ArrayList<>(opciones);
        this.opcionCorrecta = opcionCorrecta;
    }
    
    public List<String> getOpciones() {
        return new ArrayList<>(opciones);
    }
    
    public void setOpciones(List<String> opciones) {
        this.opciones = new ArrayList<>(opciones);
    }
    
    public int getOpcionCorrecta() {
        return opcionCorrecta;
    }
    
    public void setOpcionCorrecta(int opcionCorrecta) {
        this.opcionCorrecta = opcionCorrecta;
    }
    
    @Override
    public String getTipo() {
        return "Opción Múltiple";
    }
    
    @Override
    public boolean validarRespuesta(Object respuesta) {
        if (respuesta instanceof Integer) {
            return ((Integer) respuesta).equals(opcionCorrecta);
        }
        return false;
    }
    
    @Override
    public Object getRespuestaCorrecta() {
        return opcionCorrecta;
    }
}
