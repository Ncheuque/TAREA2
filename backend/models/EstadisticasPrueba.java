package backend.models;

import java.util.Map;
import java.util.HashMap;

public class EstadisticasPrueba {
    private Map<NivelBloom, Double> porcentajePorNivel;
    private Map<String, Double> porcentajePorTipo;
    private double porcentajeTotal;
    private int totalPreguntas;
    private int respuestasCorrectas;
    
    public EstadisticasPrueba() {
        this.porcentajePorNivel = new HashMap<>();
        this.porcentajePorTipo = new HashMap<>();
    }
    
    // Getters y setters
    public Map<NivelBloom, Double> getPorcentajePorNivel() {
        return new HashMap<>(porcentajePorNivel);
    }
    
    public void setPorcentajePorNivel(Map<NivelBloom, Double> porcentajePorNivel) {
        this.porcentajePorNivel = new HashMap<>(porcentajePorNivel);
    }
    
    public Map<String, Double> getPorcentajePorTipo() {
        return new HashMap<>(porcentajePorTipo);
    }
    
    public void setPorcentajePorTipo(Map<String, Double> porcentajePorTipo) {
        this.porcentajePorTipo = new HashMap<>(porcentajePorTipo);
    }
    
    public double getPorcentajeTotal() { return porcentajeTotal; }
    public void setPorcentajeTotal(double porcentajeTotal) { this.porcentajeTotal = porcentajeTotal; }
    
    public int getTotalPreguntas() { return totalPreguntas; }
    public void setTotalPreguntas(int totalPreguntas) { this.totalPreguntas = totalPreguntas; }
    
    public int getRespuestasCorrectas() { return respuestasCorrectas; }
    public void setRespuestasCorrectas(int respuestasCorrectas) { this.respuestasCorrectas = respuestasCorrectas; }
}
