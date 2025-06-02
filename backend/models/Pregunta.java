package backend.models;

public abstract class Pregunta {
    protected int id;
    protected String texto;
    protected NivelBloom nivelBloom;
    protected int tiempoEstimado;
    protected int anio;
    
    public Pregunta(int id, String texto, NivelBloom nivelBloom, int tiempoEstimado, int anio) {
        this.id = id;
        this.texto = texto;
        this.nivelBloom = nivelBloom;
        this.tiempoEstimado = tiempoEstimado;
        this.anio = anio;
    }
    
    // Getters
    public int getId() { return id; }
    public String getTexto() { return texto; }
    public NivelBloom getNivelBloom() { return nivelBloom; }
    public int getTiempoEstimado() { return tiempoEstimado; }
    public int getAnio() { return anio; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setTexto(String texto) { this.texto = texto; }
    public void setNivelBloom(NivelBloom nivelBloom) { this.nivelBloom = nivelBloom; }
    public void setTiempoEstimado(int tiempoEstimado) { this.tiempoEstimado = tiempoEstimado; }
    public void setAnio(int anio) { this.anio = anio; }
    
    public abstract String getTipo();
    public abstract boolean validarRespuesta(Object respuesta);
    public abstract Object getRespuestaCorrecta();
}
