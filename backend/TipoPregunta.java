package backend;

/**
 * Tipos de preguntas soportados
 * @author Nicolas Cheuque
 */
public enum TipoPregunta {
    OPCION_MULTIPLE("Opción Múltiple"),
    VERDADERO_FALSO("Verdadero/Falso");
    
    private final String nombre;
    
    TipoPregunta(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNombre() { return nombre; }
}
