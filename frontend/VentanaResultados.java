package frontend;

import backend.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * Ventana de resultados y revisión
 * @author Nicolas Cheuque
 */
public class VentanaResultados extends JFrame {
    private GestorPrueba gestor;
    private List<Pregunta> preguntas;
    private int preguntaActual = 0;
    private boolean modoRevision = false;
    
    private CardLayout cardLayout;
    private JPanel panelEstadisticas, panelRevision;
    private JTextArea txtEstadisticas;
    private JLabel lblNumero;
    private JTextArea txtPregunta;
    private JPanel panelOpciones;
    private JButton btnRevisar, btnAnterior, btnSiguiente, btnVolver;
    
    public VentanaResultados(GestorPrueba gestor) {
        this.gestor = gestor;
        this.preguntas = gestor.getPreguntas();
        
        setTitle("Resultados de la Prueba");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initComponents();
        mostrarEstadisticas();
    }
    
    private void initComponents() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        
        // Panel de estadísticas
        panelEstadisticas = new JPanel(new BorderLayout());
        txtEstadisticas = new JTextArea();
        txtEstadisticas.setEditable(false);
        txtEstadisticas.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        btnRevisar = new JButton("Revisar Respuestas");
        btnRevisar.addActionListener(e -> {
            modoRevision = true;
            preguntaActual = 0;
            mostrarRevision();
            cardLayout.show(getContentPane(), "REVISION");
        });
        
        panelEstadisticas.add(new JScrollPane(txtEstadisticas), BorderLayout.CENTER);
        panelEstadisticas.add(btnRevisar, BorderLayout.SOUTH);
        
        // Panel de revisión
        panelRevision = new JPanel(new BorderLayout());
        
        lblNumero = new JLabel();
        panelRevision.add(lblNumero, BorderLayout.NORTH);
        
        JPanel panelCentral = new JPanel(new BorderLayout());
        txtPregunta = new JTextArea();
        txtPregunta.setEditable(false);
        txtPregunta.setWrapStyleWord(true);
        txtPregunta.setLineWrap(true);
        panelCentral.add(new JScrollPane(txtPregunta), BorderLayout.CENTER);
        
        panelOpciones = new JPanel();
        panelCentral.add(panelOpciones, BorderLayout.EAST);
        panelRevision.add(panelCentral, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel();
        btnAnterior = new JButton("← Anterior");
        btnSiguiente = new JButton("Siguiente →");
        btnVolver = new JButton("Volver al Resumen");
        
        btnAnterior.addActionListener(e -> {
            preguntaActual--;
            mostrarRevision();
        });
        
        btnSiguiente.addActionListener(e -> {
            preguntaActual++;
            mostrarRevision();
        });
        
        btnVolver.addActionListener(e -> {
            cardLayout.show(getContentPane(), "ESTADISTICAS");
        });
        
        panelBotones.add(btnAnterior);
        panelBotones.add(btnSiguiente);
        panelBotones.add(btnVolver);
        panelRevision.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelEstadisticas, "ESTADISTICAS");
        add(panelRevision, "REVISION");
    }
    
    private void mostrarEstadisticas() {
        Map<String, Double> stats = gestor.calcularEstadisticas();
        StringBuilder sb = new StringBuilder();
        
        sb.append("RESULTADOS DE LA PRUEBA\n");
        sb.append("======================\n\n");
        sb.append(String.format("Porcentaje total: %.1f%%\n\n", stats.get("TOTAL")));
        
        sb.append("POR NIVEL DE BLOOM:\n");
        sb.append("------------------\n");
        for (BloomLevel nivel : BloomLevel.values()) {
            double porcentaje = stats.get("BLOOM_" + nivel.name());
            sb.append(String.format("%-12s: %.1f%%\n", nivel.getNombre(), porcentaje));
        }
        
        sb.append("\nPOR TIPO DE PREGUNTA:\n");
        sb.append("--------------------\n");
        for (TipoPregunta tipo : TipoPregunta.values()) {
            double porcentaje = stats.get("TIPO_" + tipo.name());
            sb.append(String.format("%-20s: %.1f%%\n", tipo.getNombre(), porcentaje));
        }
        
        txtEstadisticas.setText(sb.toString());
    }
    
    private void mostrarRevision() {
        Pregunta p = preguntas.get(preguntaActual);
        String respuestaUsuario = gestor.getRespuesta(p.getId());
        boolean correcta = respuestaUsuario != null && p.verificarRespuesta(respuestaUsuario);
        
        lblNumero.setText("Pregunta " + (preguntaActual + 1) + " de " + preguntas.size());
        
        String estado = correcta ? "✓ CORRECTA" : "✗ INCORRECTA";
        txtPregunta.setText(p.getTexto() + "\n\nNivel: " + p.getNivelBloom().getNombre() + 
                           "\nTiempo: " + p.getTiempoEstimado() + " min\n\nEstado: " + estado);
        
        // Mostrar opciones con indicadores
        panelOpciones.removeAll();
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
        
        List<String> opciones = p.getOpciones();
        String respuestaCorrecta = p.getRespuestaCorrecta();
        
        for (int i = 0; i < opciones.size(); i++) {
            JLabel lbl = new JLabel();
            String indice = String.valueOf(i);
            
            if (indice.equals(respuestaCorrecta)) {
                lbl.setText("✓ " + opciones.get(i) + " (Correcta)");
                lbl.setForeground(Color.GREEN);
                lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            } else if (indice.equals(respuestaUsuario)) {
                lbl.setText("✗ " + opciones.get(i) + " (Su respuesta)");
                lbl.setForeground(Color.RED);
                lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            } else {
                lbl.setText("  " + opciones.get(i));
            }
            
            panelOpciones.add(lbl);
            panelOpciones.add(Box.createVerticalStrut(5));
        }
        
        btnAnterior.setEnabled(preguntaActual > 0);
        btnSiguiente.setEnabled(preguntaActual < preguntas.size() - 1);
        
        panelOpciones.revalidate();
        panelOpciones.repaint();
    }
}
