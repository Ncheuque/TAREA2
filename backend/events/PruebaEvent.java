package backend.events;

public class PruebaEvent {
    public enum Tipo {
        PRUEBA_CARGADA,
        PREGUNTA_CAMBIADA,
        RESPUESTA_GUARDADA,
        PRUEBA_FINALIZADA,
        ESTADISTICAS_CALCULADAS,
        ERROR
    }
    
    private Tipo tipo;
    private Object data;
    private String mensaje;
    
    public PruebaEvent(Tipo tipo, Object data, String mensaje) {
        this.tipo = tipo;
        this.data = data;
        this.mensaje = mensaje;
    }
    
    public PruebaEvent(Tipo tipo, Object data) {
        this(tipo, data, null);
    }
    
    public PruebaEvent(Tipo tipo) {
        this(tipo, null, null);
    }
    
    // Getters
    public Tipo getTipo() { return tipo; }
    public Object getData() { return data; }
    public String getMensaje() { return mensaje; }
}
