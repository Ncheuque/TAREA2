package backend;

import java.util.List;

/**
 * Clase base para preguntas
 * @author Nicolas Cheuque
 */
public abstract class Pregunta {
    protected int id;
    protected String texto;
    protected BloomLevel nivelBloom;
    protected int tiempoEstimado;
    protected TipoPregunta tipo;
    
    public Pregunta(int id, String texto, BloomLevel nivelBloom, int tiempoEstimado, TipoPregunta tipo) {
        this.id = id;
        this.texto = texto;
        this.nivelBloom = nivelBloom;
        this.tiempoEstimado = tiempoEstimado;
        this.tipo = tipo;
    }
    
    public int getId() { return id; }
    public String getTexto() { return texto; }
    public BloomLevel getNivelBloom() { return nivelBloom; }
    public int getTiempoEstimado() { return tiempoEstimado; }
    public TipoPregunta getTipo() { return tipo; }
    
    public abstract boolean verificarRespuesta(String respuesta);
    public abstract List<String> getOpciones();
    public abstract String getRespuestaCorrecta();
}
