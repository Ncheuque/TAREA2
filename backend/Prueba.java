package backend;

import java.util.List;
import java.util.ArrayList;

/**
 * Clase que representa una prueba completa
 * Contiene una colección de preguntas y maneja información general de la prueba
 * 
 * @author Nicolas Cheuque
 * @version 1.0
 */
public class Prueba {
    private List<Pregunta> preguntas;   // Lista de preguntas de la prueba
    private String nombre;              // Nombre de la prueba
    private int tiempoTotal;            // Tiempo total estimado en minutos
    
    /**
     * Constructor de la prueba
     * @param nombre Nombre de la prueba
     */
    public Prueba(String nombre) {
        this.nombre = nombre;
        this.preguntas = new ArrayList<>();
        this.tiempoTotal = 0;
    }
    
    /**
     * Agrega una pregunta a la prueba y actualiza el tiempo total
     * @param pregunta Pregunta a agregar
     */
    public void agregarPregunta(Pregunta pregunta) {
        preguntas.add(pregunta);
        tiempoTotal += pregunta.getTiempoEstimado();
    }
    
    /**
     * Obtiene una copia de las preguntas de la prueba
     * @return Lista de preguntas (copia defensiva)
     */
    public List<Pregunta> getPreguntas() {
        return new ArrayList<>(preguntas);
    }
    
    /**
     * Obtiene el nombre de la prueba
     * @return Nombre de la prueba
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Obtiene el tiempo total estimado de la prueba
     * @return Tiempo total en minutos
     */
    public int getTiempoTotal() {
        return tiempoTotal;
    }
    
    /**
     * Obtiene la cantidad de ítems (preguntas) en la prueba
     * @return Número de preguntas
     */
    public int getCantidadItems() {
        return preguntas.size();
    }
}
