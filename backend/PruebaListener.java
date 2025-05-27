package backend;

/**
 * Interfaz para el patrón Observer
 * Define el contrato para los objetos que quieren recibir notificaciones
 * del backend sobre eventos de la prueba
 * 
 * @author Nicolas Cheuque
 * @version 1.0
 */
public interface PruebaListener {
    /**
     * Método llamado cuando ocurre un evento en el backend
     * @param evento Tipo de evento ocurrido
     * @param data Datos asociados al evento (puede ser null)
     */
    void onEventoOcurrido(TipoEvento evento, Object data);
}
