package backend.models;

public class RespuestaUsuario {
    private int preguntaId;
    private Object respuesta;
    private boolean esCorrecta;
    
    public RespuestaUsuario(int preguntaId, Object respuesta, boolean esCorrecta) {
        this.preguntaId = preguntaId;
        this.respuesta = respuesta;
        this.esCorrecta = esCorrecta;
    }
    
    // Getters y setters
    public int getPreguntaId() { return preguntaId; }
    public void setPreguntaId(int preguntaId) { this.preguntaId = preguntaId; }
    
    public Object getRespuesta() { return respuesta; }
    public void setRespuesta(Object respuesta) { this.respuesta = respuesta; }
    
    public boolean isEsCorrecta() { return esCorrecta; }
    public void setEsCorrecta(boolean esCorrecta) { this.esCorrecta = esCorrecta; }
}
