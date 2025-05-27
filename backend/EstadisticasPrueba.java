package backend;

import java.util.Map;
import java.util.HashMap;

/**
 * Clase que calcula y almacena las estadísticas de una prueba completada
 * Proporciona análisis por nivel de Bloom y por tipo de pregunta
 * 
 * @author Nicolas Cheuque
 * @version 1.0
 */
public class EstadisticasPrueba {
    // Mapas para estadísticas por nivel de Bloom
    private Map<BloomLevel, Integer> correctasPorBloom;     // Respuestas correctas por nivel
    private Map<BloomLevel, Integer> totalPorBloom;        // Total de preguntas por nivel
    
    // Mapas para estadísticas por tipo de pregunta
    private Map<TipoPregunta, Integer> correctasPorTipo;   // Respuestas correctas por tipo
    private Map<TipoPregunta, Integer> totalPorTipo;       // Total de preguntas por tipo
    
    // Estadísticas generales
    private int totalCorrectas;     // Total de respuestas correctas
    private int totalPreguntas;     // Total de preguntas en la prueba
    
    /**
     * Constructor que inicializa todos los contadores en cero
     */
    public EstadisticasPrueba() {
        correctasPorBloom = new HashMap<>();
        totalPorBloom = new HashMap<>();
        correctasPorTipo = new HashMap<>();
        totalPorTipo = new HashMap<>();
        
        // Inicializar contadores para todos los niveles de Bloom
        for (BloomLevel nivel : BloomLevel.values()) {
            correctasPorBloom.put(nivel, 0);
            totalPorBloom.put(nivel, 0);
        }
        
        // Inicializar contadores para todos los tipos de pregunta
        for (TipoPregunta tipo : TipoPregunta.values()) {
            correctasPorTipo.put(tipo, 0);
            totalPorTipo.put(tipo, 0);
        }
    }
    
    /**
     * Agrega una respuesta a las estadísticas
     * @param pregunta Pregunta respondida
     * @param esCorrecta true si la respuesta fue correcta
     */
    public void agregarRespuesta(Pregunta pregunta, boolean esCorrecta) {
        BloomLevel nivel = pregunta.getNivelBloom();
        TipoPregunta tipo = pregunta.getTipo();
        
        // Incrementar contadores totales
        totalPorBloom.put(nivel, totalPorBloom.get(nivel) + 1);
        totalPorTipo.put(tipo, totalPorTipo.get(tipo) + 1);
        totalPreguntas++;
        
        // Si la respuesta es correcta, incrementar contadores de correctas
        if (esCorrecta) {
            correctasPorBloom.put(nivel, correctasPorBloom.get(nivel) + 1);
            correctasPorTipo.put(tipo, correctasPorTipo.get(tipo) + 1);
            totalCorrectas++;
        }
    }
    
    /**
     * Calcula el porcentaje de respuestas correctas para un nivel de Bloom
     * @param nivel Nivel de Bloom a consultar
     * @return Porcentaje de respuestas correctas (0.0 - 100.0)
     */
    public double getPorcentajeCorrectasPorBloom(BloomLevel nivel) {
        int total = totalPorBloom.get(nivel);
        if (total == 0) return 0.0;
        return (correctasPorBloom.get(nivel) * 100.0) / total;
    }
    
    /**
     * Calcula el porcentaje de respuestas correctas para un tipo de pregunta
     * @param tipo Tipo de pregunta a consultar
     * @return Porcentaje de respuestas correctas (0.0 - 100.0)
     */
    public double getPorcentajeCorrectasPorTipo(TipoPregunta tipo) {
        int total = totalPorTipo.get(tipo);
        if (total == 0) return 0.0;
        return (correctasPorTipo.get(tipo) * 100.0) / total;
    }
    
    /**
     * Calcula el porcentaje total de respuestas correctas
     * @return Porcentaje total de respuestas correctas (0.0 - 100.0)
     */
    public double getPorcentajeTotal() {
        if (totalPreguntas == 0) return 0.0;
        return (totalCorrectas * 100.0) / totalPreguntas;
    }
    
    // Métodos getter para acceder a los mapas de estadísticas
    public Map<BloomLevel, Integer> getCorrectasPorBloom() { return correctasPorBloom; }
    public Map<BloomLevel, Integer> getTotalPorBloom() { return totalPorBloom; }
    public Map<TipoPregunta, Integer> getCorrectasPorTipo() { return correctasPorTipo; }
    public Map<TipoPregunta, Integer> getTotalPorTipo() { return totalPorTipo; }
    public int getTotalCorrectas() { return totalCorrectas; }
    public int getTotalPreguntas() { return totalPreguntas; }
}
