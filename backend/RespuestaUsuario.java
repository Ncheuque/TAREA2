package backend;

/**
 * Clase que representa la respuesta de un usuario a una pregunta específica
 * Almacena la respuesta dada y si fue correcta o no
 * 
 * @author Nicolas Cheuque
 * @version 1.0
 */
public class RespuestaUsuario {
    private int idPregunta;         // ID de la pregunta respondida
    private String respuesta;       // Respuesta dada por el usuario
    private boolean esCorrecta;     // Indica si la respuesta es correcta
    
    /**
     * Constructor de la respuesta del usuario
     * @param idPregunta ID de la pregunta
     * @param respuesta Respuesta dada por el usuario
     * @param esCorrecta true si la respuesta es correcta
     */
    public RespuestaUsuario(int idPregunta, String respuesta, boolean esCorrecta) {
        this.idPregunta = idPregunta;
        this.respuesta = respuesta;
        this.esCorrecta = esCorrecta;
    }
    
    // Métodos getter
    public int getIdPregunta() { return idPregunta; }
    public String getRespuesta() { return respuesta; }
    public boolean isEsCorrecta() { return esCorrecta; }
    
    // Métodos setter para permitir modificaciones
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
    public void setEsCorrecta(boolean esCorrecta) { this.esCorrecta = esCorrecta; }
}
