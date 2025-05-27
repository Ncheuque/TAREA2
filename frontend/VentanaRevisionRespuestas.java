package frontend;

import backend.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaRevisionRespuestas extends JFrame {
    private GestorPrueba gestor;
    private EstadisticasPrueba estadisticas;
    private List<Pregunta> preguntas;
    private int preguntaActual;
    
    private JPanel panelEstadisticas;
    private JPanel panelRevision;
    private CardLayout cardLayout;
    
    // Componentes para vista de estadísticas
    private JTextArea txtEstadisticas;
    private JButton btnRevisarRespuestas;
    
    // Componentes para vista de revisión
    private JLabel lblNumeroPregunta;
    private JTextArea txtPregunta;
    private JPanel panelOpciones;
    private JButton btnAnteriorRevision;
    private JButton btnSiguienteRevision;
    private JButton btnVolverEstadisticas;
    
    public VentanaRevisionRespuestas(GestorPrueba gestor, EstadisticasPrueba estadisticas) {
        this.gestor = gestor;
        this.estadisticas = estadisticas;
        this.preguntas = gestor.getPruebaActual().getPreguntas();
        this.preguntaActual = 0;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setTitle("Revisión de Respuestas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        mostrarEstadisticas();
    }
    
    private void initializeComponents() {
        cardLayout = new CardLayout();
        
        // Componentes para estadísticas
        txtEstadisticas = new JTextArea();
        txtEstadisticas.setEditable(false);
        txtEstadisticas.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        btnRevisarRespuestas = new JButton("Revisar Respuestas Detalladamente");
        
        // Componentes para revisión
        lblNumeroPregunta = new JLabel();
        txtPregunta = new JTextArea();
        txtPregunta.setEditable(false);
        txtPregunta.setWrapStyleWord(true);
        txtPregunta.setLineWrap(true);
        txtPregunta.setBackground(getBackground());
        
        panelOpciones = new JPanel();
        
        btnAnteriorRevision = new JButton("← Anterior");
        btnSiguienteRevision = new JButton("Siguiente →");
        btnVolverEstadisticas = new JButton("Volver al Resumen");
    }
    
    private void setupLayout() {
        setLayout(cardLayout);
        
        // Panel de estadísticas
        panelEstadisticas = new JPanel(new BorderLayout());
        
        JScrollPane scrollEstadisticas = new JScrollPane(txtEstadisticas);
        scrollEstadisticas.setBorder(BorderFactory.createTitledBorder("Resumen de Resultados"));
        
        JPanel panelBotonEstadisticas = new JPanel(new FlowLayout());
        panelBotonEstadisticas.add(btnRevisarRespuestas);
        
        panelEstadisticas.add(scrollEstadisticas, BorderLayout.CENTER);
        panelEstadisticas.add(panelBotonEstadisticas, BorderLayout.SOUTH);
        
        // Panel de revisión
        panelRevision = new JPanel(new BorderLayout());
        
        JPanel panelSuperiorRevision = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperiorRevision.add(lblNumeroPregunta);
        
        JPanel panelPreguntaRevision = new JPanel(new BorderLayout());
        panelPreguntaRevision.setBorder(BorderFactory.createTitledBorder("Pregunta"));
        panelPreguntaRevision.add(new JScrollPane(txtPregunta), BorderLayout.CENTER);
        
        panelOpciones.setBorder(BorderFactory.createTitledBorder("Opciones"));
        
        JPanel panelInferiorRevision = new JPanel(new FlowLayout());
        panelInferiorRevision.add(btnAnteriorRevision);
        panelInferiorRevision.add(btnSiguienteRevision);
        panelInferiorRevision.add(btnVolverEstadisticas);
        
        panelRevision.add(panelSuperiorRevision, BorderLayout.NORTH);
        panelRevision.add(panelPreguntaRevision, BorderLayout.CENTER);
        panelRevision.add(panelOpciones, BorderLayout.EAST);
        panelRevision.add(panelInferiorRevision, BorderLayout.SOUTH);
        
        add(panelEstadisticas, "ESTADISTICAS");
        add(panelRevision, "REVISION");
    }
    
    private void setupEventHandlers() {
        btnRevisarRespuestas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                preguntaActual = 0;
                mostrarRevisionPregunta();
                cardLayout.show(getContentPane(), "REVISION");
            }
        });
        
        btnAnteriorRevision.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (preguntaActual > 0) {
                    preguntaActual--;
                    mostrarRevisionPregunta();
                }
            }
        });
        
        btnSiguienteRevision.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (preguntaActual < preguntas.size() - 1) {
                    preguntaActual++;
                    mostrarRevisionPregunta();
                }
            }
        });
        
        btnVolverEstadisticas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(getContentPane(), "ESTADISTICAS");
            }
        });
    }
    
    private void mostrarEstadisticas() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("RESUMEN DE RESULTADOS\n");
        sb.append("=====================\n\n");
        
        sb.append(String.format("Porcentaje total de respuestas correctas: %.1f%%\n\n", 
                  estadisticas.getPorcentajeTotal()));
        
        sb.append("RESULTADOS POR NIVEL DE BLOOM:\n");
        sb.append("------------------------------\n");
        for (BloomLevel nivel : BloomLevel.values()) {
            int total = estadisticas.getTotalPorBloom().get(nivel);
            if (total > 0) {
                double porcentaje = estadisticas.getPorcentajeCorrectasPorBloom(nivel);
                int correctas = estadisticas.getCorrectasPorBloom().get(nivel);
                sb.append(String.format("%-12s: %d/%d (%.1f%%)\n", 
                          nivel.getNombre(), correctas, total, porcentaje));
            }
        }
        
        sb.append("\nRESULTADOS POR TIPO DE PREGUNTA:\n");
        sb.append("--------------------------------\n");
        for (TipoPregunta tipo : TipoPregunta.values()) {
            int total = estadisticas.getTotalPorTipo().get(tipo);
            if (total > 0) {
                double porcentaje = estadisticas.getPorcentajeCorrectasPorTipo(tipo);
                int correctas = estadisticas.getCorrectasPorTipo().get(tipo);
                sb.append(String.format("%-20s: %d/%d (%.1f%%)\n", 
                          tipo.getNombre(), correctas, total, porcentaje));
            }
        }
        
        txtEstadisticas.setText(sb.toString());
        txtEstadisticas.setCaretPosition(0);
    }
    
    private void mostrarRevisionPregunta() {
        if (preguntaActual < 0 || preguntaActual >= preguntas.size()) {
            return;
        }
        
        Pregunta pregunta = preguntas.get(preguntaActual);
        RespuestaUsuario respuestaUsuario = gestor.getRespuestaUsuario(pregunta.getId());
        
        // Actualizar número de pregunta
        lblNumeroPregunta.setText("Pregunta " + (preguntaActual + 1) + " de " + preguntas.size());
        
        // Actualizar texto de pregunta
        String estadoRespuesta = respuestaUsuario != null && respuestaUsuario.isEsCorrecta() ? 
                                "✓ CORRECTA" : "✗ INCORRECTA";
        Color colorEstado = respuestaUsuario != null && respuestaUsuario.isEsCorrecta() ? 
                           Color.GREEN : Color.RED;
        
        txtPregunta.setText(pregunta.getTexto() + "\n\n" +
                           "Nivel de Bloom: " + pregunta.getNivelBloom().getNombre() + "\n" +
                           "Tiempo estimado: " + pregunta.getTiempoEstimado() + " minutos\n\n" +
                           "Estado: " + estadoRespuesta);
        
        // Limpiar opciones anteriores
        panelOpciones.removeAll();
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
        
        // Crear opciones con indicadores visuales
        List<String> opciones = pregunta.getOpciones();
        String respuestaCorrecta = pregunta.getRespuestaCorrecta();
        String respuestaUsuarioStr = respuestaUsuario != null ? respuestaUsuario.getRespuesta() : null;
        
        for (int i = 0; i < opciones.size(); i++) {
            JLabel lblOpcion = new JLabel();
            String indiceStr = String.valueOf(i);
            
            // Determinar el color y el prefijo
            if (indiceStr.equals(respuestaCorrecta)) {
                lblOpcion.setText("✓ " + opciones.get(i) + " (Respuesta Correcta)");
                lblOpcion.setForeground(new Color(0, 150, 0)); // Verde más oscuro
                lblOpcion.setFont(lblOpcion.getFont().deriveFont(Font.BOLD));
            } else if (indiceStr.equals(respuestaUsuarioStr)) {
                lblOpcion.setText("✗ " + opciones.get(i) + " (Su Respuesta)");
                lblOpcion.setForeground(new Color(200, 0, 0)); // Rojo más oscuro
                lblOpcion.setFont(lblOpcion.getFont().deriveFont(Font.BOLD));
            } else {
                lblOpcion.setText("  " + opciones.get(i));
                lblOpcion.setForeground(Color.BLACK);
            }
            
            panelOpciones.add(lblOpcion);
            panelOpciones.add(Box.createVerticalStrut(8));
        }
        
        // Agregar información si no se respondió
        if (respuestaUsuario == null) {
            JLabel lblNoRespondida = new JLabel("⚠ Pregunta no respondida");
            lblNoRespondida.setForeground(new Color(255, 140, 0)); // Naranja
            lblNoRespondida.setFont(lblNoRespondida.getFont().deriveFont(Font.BOLD));
            panelOpciones.add(Box.createVerticalStrut(10));
            panelOpciones.add(lblNoRespondida);
        }
        
        // Actualizar estado de botones
        btnAnteriorRevision.setEnabled(preguntaActual > 0);
        btnSiguienteRevision.setEnabled(preguntaActual < preguntas.size() - 1);
        
        panelOpciones.revalidate();
        panelOpciones.repaint();
    }
}
