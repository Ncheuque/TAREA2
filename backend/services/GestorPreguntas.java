package backend.services;

import backend.models.*;
import backend.events.*;
import java.util.*;
import java.io.*;

public class GestorPreguntas {
    private List<Pregunta> preguntas;
    private Map<Integer, Object> respuestasUsuario;
    private int preguntaActual;
    private List<backend.events.EventListener<PruebaEvent>> listeners;
    private EstadisticasPrueba estadisticas;
    
    public GestorPreguntas() {
        this.preguntas = new ArrayList<>();
        this.respuestasUsuario = new HashMap<>();
        this.preguntaActual = 0;
        this.listeners = new ArrayList<>();
        this.estadisticas = new EstadisticasPrueba();
    }
    
    public void addListener(backend.events.EventListener<PruebaEvent> listener) {
        listeners.add(listener);
    }
    
    public void removeListener(backend.events.EventListener<PruebaEvent> listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners(PruebaEvent event) {
        for (backend.events.EventListener<PruebaEvent> listener : listeners) {
            listener.onEvent(event);
        }
    }
    
    public void cargarPreguntasDesdeArchivo(File archivo) {
        try {
            preguntas.clear();
            respuestasUsuario.clear();
            preguntaActual = 0;
            
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                int id = 1;
                
                while ((linea = reader.readLine()) != null) {
                    if (linea.trim().isEmpty()) continue;
                    
                    Pregunta pregunta = parsearPregunta(linea, id++);
                    if (pregunta != null) {
                        preguntas.add(pregunta);
                    }
                }
            }
            
            if (preguntas.isEmpty()) {
                throw new IllegalArgumentException("No se encontraron preguntas válidas en el archivo");
            }
            
            notifyListeners(new PruebaEvent(PruebaEvent.Tipo.PRUEBA_CARGADA, preguntas));
            
        } catch (Exception e) {
            notifyListeners(new PruebaEvent(PruebaEvent.Tipo.ERROR, null, 
                "Error al cargar archivo: " + e.getMessage()));
        }
    }
    
    private Pregunta parsearPregunta(String linea, int id) {
        try {
            String[] partes = linea.split("\\|");
            if (partes.length < 6) return null;
            
            String tipo = partes[0].trim();
            String texto = partes[1].trim();
            int nivelBloom = Integer.parseInt(partes[2].trim());
            int tiempo = Integer.parseInt(partes[3].trim());
            int anio = Integer.parseInt(partes[4].trim());
            
            NivelBloom nivel = NivelBloom.fromValor(nivelBloom);
            
            if ("OM".equals(tipo)) {
                // Opción Múltiple: OM|texto|nivel|tiempo|año|opcion1;opcion2;opcion3|respuesta_correcta
                String[] opciones = partes[5].split(";");
                int respuestaCorrecta = Integer.parseInt(partes[6].trim());
                
                return new PreguntaOpcionMultiple(id, texto, nivel, tiempo, 
                    Arrays.asList(opciones), respuestaCorrecta, anio);
                    
            } else if ("VF".equals(tipo)) {
                // Verdadero/Falso: VF|texto|nivel|tiempo|año|respuesta_correcta
                boolean respuestaCorrecta = Boolean.parseBoolean(partes[5].trim());
                
                return new PreguntaVerdaderoFalso(id, texto, nivel, tiempo, 
                    respuestaCorrecta, anio);
            }
            
        } catch (Exception e) {
            System.err.println("Error parseando pregunta: " + linea + " - " + e.getMessage());
        }
        
        return null;
    }
    
    public void guardarRespuesta(int preguntaId, Object respuesta) {
        respuestasUsuario.put(preguntaId, respuesta);
        notifyListeners(new PruebaEvent(PruebaEvent.Tipo.RESPUESTA_GUARDADA, respuesta));
    }
    
    public void irAPregunta(int indice) {
        if (indice >= 0 && indice < preguntas.size()) {
            preguntaActual = indice;
            notifyListeners(new PruebaEvent(PruebaEvent.Tipo.PREGUNTA_CAMBIADA, 
                preguntas.get(preguntaActual)));
        }
    }
    
    public void siguientePregunta() {
        if (preguntaActual < preguntas.size() - 1) {
            irAPregunta(preguntaActual + 1);
        }
    }
    
    public void preguntaAnterior() {
        if (preguntaActual > 0) {
            irAPregunta(preguntaActual - 1);
        }
    }
    
    public void finalizarPrueba() {
        calcularEstadisticas();
        notifyListeners(new PruebaEvent(PruebaEvent.Tipo.PRUEBA_FINALIZADA, estadisticas));
    }
    
    private void calcularEstadisticas() {
        Map<NivelBloom, Integer> correctasPorNivel = new HashMap<>();
        Map<NivelBloom, Integer> totalPorNivel = new HashMap<>();
        Map<String, Integer> correctasPorTipo = new HashMap<>();
        Map<String, Integer> totalPorTipo = new HashMap<>();
        
        int totalCorrectas = 0;
        
        for (Pregunta pregunta : preguntas) {
            NivelBloom nivel = pregunta.getNivelBloom();
            String tipo = pregunta.getTipo();
            
            // Contar totales
            totalPorNivel.put(nivel, totalPorNivel.getOrDefault(nivel, 0) + 1);
            totalPorTipo.put(tipo, totalPorTipo.getOrDefault(tipo, 0) + 1);
            
            // Verificar si la respuesta es correcta
            Object respuestaUsuario = respuestasUsuario.get(pregunta.getId());
            if (respuestaUsuario != null && pregunta.validarRespuesta(respuestaUsuario)) {
                correctasPorNivel.put(nivel, correctasPorNivel.getOrDefault(nivel, 0) + 1);
                correctasPorTipo.put(tipo, correctasPorTipo.getOrDefault(tipo, 0) + 1);
                totalCorrectas++;
            }
        }
        
        // Calcular porcentajes por nivel
        Map<NivelBloom, Double> porcentajesPorNivel = new HashMap<>();
        for (NivelBloom nivel : totalPorNivel.keySet()) {
            int correctas = correctasPorNivel.getOrDefault(nivel, 0);
            int total = totalPorNivel.get(nivel);
            double porcentaje = total > 0 ? (correctas * 100.0 / total) : 0.0;
            porcentajesPorNivel.put(nivel, porcentaje);
        }
        
        // Calcular porcentajes por tipo
        Map<String, Double> porcentajesPorTipo = new HashMap<>();
        for (String tipo : totalPorTipo.keySet()) {
            int correctas = correctasPorTipo.getOrDefault(tipo, 0);
            int total = totalPorTipo.get(tipo);
            double porcentaje = total > 0 ? (correctas * 100.0 / total) : 0.0;
            porcentajesPorTipo.put(tipo, porcentaje);
        }
        
        // Actualizar estadísticas
        estadisticas.setPorcentajePorNivel(porcentajesPorNivel);
        estadisticas.setPorcentajePorTipo(porcentajesPorTipo);
        estadisticas.setTotalPreguntas(preguntas.size());
        estadisticas.setRespuestasCorrectas(totalCorrectas);
        estadisticas.setPorcentajeTotal(preguntas.size() > 0 ? 
            (totalCorrectas * 100.0 / preguntas.size()) : 0.0);
        
        notifyListeners(new PruebaEvent(PruebaEvent.Tipo.ESTADISTICAS_CALCULADAS, estadisticas));
    }
    
    // Getters
    public List<Pregunta> getPreguntas() {
        return new ArrayList<>(preguntas);
    }
    
    public Pregunta getPreguntaActual() {
        if (preguntaActual >= 0 && preguntaActual < preguntas.size()) {
            return preguntas.get(preguntaActual);
        }
        return null;
    }
    
    public int getIndicePreguntaActual() {
        return preguntaActual;
    }
    
    public Object getRespuestaUsuario(int preguntaId) {
        return respuestasUsuario.get(preguntaId);
    }
    
    public int getTiempoTotalEstimado() {
        return preguntas.stream().mapToInt(Pregunta::getTiempoEstimado).sum();
    }
    
    public boolean esPrimeraPregunta() {
        return preguntaActual == 0;
    }
    
    public boolean esUltimaPregunta() {
        return preguntaActual == preguntas.size() - 1;
    }
    
    public EstadisticasPrueba getEstadisticas() {
        return estadisticas;
    }
}
