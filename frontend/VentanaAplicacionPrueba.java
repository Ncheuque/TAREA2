package frontend;

import backend.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaAplicacionPrueba extends JFrame implements PruebaListener {
    private GestorPrueba gestor;
    private List<Pregunta> preguntas;
    private int preguntaActual;
    
    private JLabel lblNumeroPregunta;
    private JTextArea txtPregunta;
    private JPanel panelOpciones;
    private ButtonGroup grupoOpciones;
    private JButton btnAnterior;
    private JButton btnSiguiente;
    private JButton btnEnviarRespuestas;
    
    public VentanaAplicacionPrueba(GestorPrueba gestor) {
        this.gestor = gestor;
        this.gestor.addListener(this);
        this.preguntas = gestor.getPruebaActual().getPreguntas();
        this.preguntaActual = 0;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Agregar listener para el cierre de ventana
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int confirmacion = JOptionPane.showConfirmDialog(
                    VentanaAplicacionPrueba.this,
                    "¿Está seguro de que desea salir? Se perderán las respuestas no guardadas.",
                    "Confirmar Salida",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirmacion == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        
        setTitle("Aplicación de Prueba");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        mostrarPregunta();
    }
    
    private void initializeComponents() {
        lblNumeroPregunta = new JLabel();
        txtPregunta = new JTextArea();
        txtPregunta.setEditable(false);
        txtPregunta.setWrapStyleWord(true);
        txtPregunta.setLineWrap(true);
        txtPregunta.setBackground(getBackground());
        
        panelOpciones = new JPanel();
        grupoOpciones = new ButtonGroup();
        
        btnAnterior = new JButton("← Volver Atrás");
        btnSiguiente = new JButton("Avanzar a la Siguiente →");
        btnEnviarRespuestas = new JButton("Enviar Respuestas");
        
        btnEnviarRespuestas.setVisible(false);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Panel superior con número de pregunta
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(lblNumeroPregunta);
        
        // Panel central con pregunta
        JPanel panelPregunta = new JPanel(new BorderLayout());
        panelPregunta.setBorder(BorderFactory.createTitledBorder("Pregunta"));
        panelPregunta.add(new JScrollPane(txtPregunta), BorderLayout.CENTER);
        
        // Panel de opciones
        panelOpciones.setBorder(BorderFactory.createTitledBorder("Opciones"));
        
        // Panel inferior con botones de navegación
        JPanel panelInferior = new JPanel(new FlowLayout());
        panelInferior.add(btnAnterior);
        panelInferior.add(btnSiguiente);
        panelInferior.add(btnEnviarRespuestas);
        
        add(panelSuperior, BorderLayout.NORTH);
        add(panelPregunta, BorderLayout.CENTER);
        add(panelOpciones, BorderLayout.EAST);
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        btnAnterior.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (preguntaActual > 0) {
                    guardarRespuestaActual();
                    preguntaActual--;
                    mostrarPregunta();
                }
            }
        });
        
        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarRespuestaActual();
                if (preguntaActual < preguntas.size() - 1) {
                    preguntaActual++;
                    mostrarPregunta();
                }
            }
        });
        
        btnEnviarRespuestas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarRespuestaActual();
                int confirmacion = JOptionPane.showConfirmDialog(
                    VentanaAplicacionPrueba.this,
                    "¿Está seguro de que desea enviar sus respuestas? Esta acción no se puede deshacer.",
                    "Confirmar Envío",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirmacion == JOptionPane.YES_OPTION) {
                    gestor.finalizarPrueba();
                }
            }
        });
    }
    
    private void mostrarPregunta() {
        if (preguntaActual < 0 || preguntaActual >= preguntas.size()) {
            return;
        }
        
        Pregunta pregunta = preguntas.get(preguntaActual);
        
        // Actualizar número de pregunta
        lblNumeroPregunta.setText("Pregunta " + (preguntaActual + 1) + " de " + preguntas.size());
        
        // Actualizar texto de pregunta
        txtPregunta.setText(pregunta.getTexto() + "\n\n" +
                           "Nivel de Bloom: " + pregunta.getNivelBloom().getNombre() + "\n" +
                           "Tiempo estimado: " + pregunta.getTiempoEstimado() + " minutos");
        
        // Limpiar opciones anteriores
        panelOpciones.removeAll();
        grupoOpciones = new ButtonGroup();
        
        // Crear opciones según el tipo de pregunta
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
        
        List<String> opciones = pregunta.getOpciones();
        for (int i = 0; i < opciones.size(); i++) {
            JRadioButton radioButton = new JRadioButton(opciones.get(i));
            radioButton.setActionCommand(String.valueOf(i));
            grupoOpciones.add(radioButton);
            panelOpciones.add(radioButton);
            panelOpciones.add(Box.createVerticalStrut(5));
        }
        
        // Restaurar respuesta anterior si existe
        RespuestaUsuario respuestaAnterior = gestor.getRespuestaUsuario(pregunta.getId());
        if (respuestaAnterior != null) {
            String respuesta = respuestaAnterior.getRespuesta();
            for (AbstractButton button : java.util.Collections.list(grupoOpciones.getElements())) {
                if (button.getActionCommand().equals(respuesta)) {
                    button.setSelected(true);
                    break;
                }
            }
        }
        
        // Actualizar estado de botones
        btnAnterior.setEnabled(preguntaActual > 0);
        
        if (preguntaActual == preguntas.size() - 1) {
            btnSiguiente.setVisible(false);
            btnEnviarRespuestas.setVisible(true);
        } else {
            btnSiguiente.setVisible(true);
            btnEnviarRespuestas.setVisible(false);
        }
        
        panelOpciones.revalidate();
        panelOpciones.repaint();
    }
    
    private void guardarRespuestaActual() {
        if (preguntaActual < 0 || preguntaActual >= preguntas.size()) {
            return;
        }
        
        Pregunta pregunta = preguntas.get(preguntaActual);
        ButtonModel seleccionado = grupoOpciones.getSelection();
        
        if (seleccionado != null) {
            String respuesta = seleccionado.getActionCommand();
            gestor.guardarRespuesta(pregunta.getId(), respuesta);
        }
    }
    
    @Override
    public void onEventoOcurrido(TipoEvento evento, Object data) {
        SwingUtilities.invokeLater(() -> {
            if (evento == TipoEvento.PRUEBA_FINALIZADA) {
                EstadisticasPrueba estadisticas = (EstadisticasPrueba) data;
                VentanaRevisionRespuestas ventanaRevision = new VentanaRevisionRespuestas(gestor, estadisticas);
                ventanaRevision.setVisible(true);
                this.dispose();
            }
        });
    }
}
