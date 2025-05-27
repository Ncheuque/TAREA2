package backend;

/**
 * Enumeración que define los tipos de eventos que puede generar el backend
 * Utilizado en el patrón Observer para comunicación con el frontend
 * 
 * @author Nicolas Cheuque
 * @version 1.0
 */
public enum TipoEvento {
    PRUEBA_CARGADA,         // Se cargó exitosamente una prueba desde archivo
    RESPUESTA_GUARDADA,     // Se guardó una respuesta del usuario
    PRUEBA_FINALIZADA,      // La prueba fue finalizada y se calcularon estadísticas
    ERROR                   // Ocurrió un error que debe ser mostrado al usuario
}
