package backend;

import java.util.*;
import java.io.*;

/**
 * Gestor principal del sistema
 * @author Nicolas Cheuque
 */
public class GestorPrueba {
    private List<Pregunta> preguntas;
    private Map<Integer, String> respuestasUsuario;
    private List<Observer> observers;
    
    public GestorPrueba() {
        this.preguntas = new ArrayList<>();
        this.respuestasUsuario = new HashMap<>();
        this.observers = new ArrayList<>();
    }
    
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    
    private void notificar(String evento, Object data) {
        for (Observer obs : observers) {
            obs.actualizar(evento, data);
        }
    }
    
    public boolean cargarArchivo(String ruta) {
        try {
            preguntas.clear();
            respuestasUsuario.clear();
            
            BufferedReader reader = new BufferedReader(new FileReader(ruta));
            String linea;
            int id = 1;
            
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;
                
                String[] partes = linea.split("\\|");
                if (partes.length < 5) continue;
                
                String tipo = partes[0].trim();
                String texto = partes[1].trim();
                int bloom = Integer.parseInt(partes[2].trim());
                int tiempo = Integer.parseInt(partes[3].trim());
                
                BloomLevel nivel = BloomLevel.fromNivel(bloom);
                
                if ("OM".equals(tipo)) {
                    String[] opciones = partes[4].split(";");
                    int correcta = Integer.parseInt(partes[5].trim());
                    
                    List<String> listaOpciones = Arrays.asList(opciones);
                    preguntas.add(new PreguntaOpcionMultiple(id++, texto, nivel, tiempo, listaOpciones, correcta));
                    
                } else if ("VF".equals(tipo)) {
                    boolean correcta = "V".equals(partes[4].trim());
                    preguntas.add(new PreguntaVerdaderoFalso(id++, texto, nivel, tiempo, correcta));
                }
            }
            reader.close();
            
            notificar("CARGADO", preguntas.size());
            return true;
            
        } catch (Exception e) {
            notificar("ERROR", e.getMessage());
            return false;
        }
    }
    
    public void guardarRespuesta(int idPregunta, String respuesta) {
        respuestasUsuario.put(idPregunta, respuesta);
    }
    
    public Map<String, Double> calcularEstadisticas() {
        Map<String, Double> stats = new HashMap<>();
        Map<BloomLevel, Integer> correctasPorBloom = new HashMap<>();
        Map<BloomLevel, Integer> totalPorBloom = new HashMap<>();
        Map<TipoPregunta, Integer> correctasPorTipo = new HashMap<>();
        Map<TipoPregunta, Integer> totalPorTipo = new HashMap<>();
        
        // Inicializar contadores
        for (BloomLevel nivel : BloomLevel.values()) {
            correctasPorBloom.put(nivel, 0);
            totalPorBloom.put(nivel, 0);
        }
        for (TipoPregunta tipo : TipoPregunta.values()) {
            correctasPorTipo.put(tipo, 0);
            totalPorTipo.put(tipo, 0);
        }
        
        int totalCorrectas = 0;
        
        for (Pregunta p : preguntas) {
            String respuesta = respuestasUsuario.get(p.getId());
            boolean correcta = respuesta != null && p.verificarRespuesta(respuesta);
            
            totalPorBloom.put(p.getNivelBloom(), totalPorBloom.get(p.getNivelBloom()) + 1);
            totalPorTipo.put(p.getTipo(), totalPorTipo.get(p.getTipo()) + 1);
            
            if (correcta) {
                correctasPorBloom.put(p.getNivelBloom(), correctasPorBloom.get(p.getNivelBloom()) + 1);
                correctasPorTipo.put(p.getTipo(), correctasPorTipo.get(p.getTipo()) + 1);
                totalCorrectas++;
            }
        }
        
        // Calcular porcentajes
        stats.put("TOTAL", preguntas.size() > 0 ? (totalCorrectas * 100.0 / preguntas.size()) : 0.0);
        
        for (BloomLevel nivel : BloomLevel.values()) {
            int total = totalPorBloom.get(nivel);
            double porcentaje = total > 0 ? (correctasPorBloom.get(nivel) * 100.0 / total) : 0.0;
            stats.put("BLOOM_" + nivel.name(), porcentaje);
        }
        
        for (TipoPregunta tipo : TipoPregunta.values()) {
            int total = totalPorTipo.get(tipo);
            double porcentaje = total > 0 ? (correctasPorTipo.get(tipo) * 100.0 / total) : 0.0;
            stats.put("TIPO_" + tipo.name(), porcentaje);
        }
        
        return stats;
    }
    
    public List<Pregunta> getPreguntas() { return preguntas; }
    public String getRespuesta(int id) { return respuestasUsuario.get(id); }
    public int getTiempoTotal() { 
        return preguntas.stream().mapToInt(Pregunta::getTiempoEstimado).sum(); 
    }
    
    public interface Observer {
        void actualizar(String evento, Object data);
    }
}
