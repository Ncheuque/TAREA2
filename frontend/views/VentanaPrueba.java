package frontend.views;

import backend.services.GestorPreguntas;
import backend.events.*;
import backend.models.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrueba extends JFrame implements EventListener<PruebaEvent> {
    private GestorPreguntas gestor;
    private JLabel lblNumeroPregunta;
    private JLabel lblTiempo;
    private JLabel lblNivelBloom;
    private JTextArea txtPregunta;
    private JPanel panelRespuestas;
    private JButton btnAnterior;
    private JButton btnSiguiente;
    private ButtonGroup grupoRespuestas;
    
    public VentanaPrueba(GestorPreguntas gestor) {
        this.gestor = gestor;
        this.gestor.addListener(this);
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setTitle("Aplicación de Prueba - Taxonomía de Bloom");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Cargar primera pregunta
        gestor.irAPregunta(0);
    }
    
    private void initializeComponents() {
        lblNumeroPregunta = new JLabel();
        lblTiempo = new JLabel();
        lblNivelBloom = new JLabel();
        txtPregunta = new JTextArea();
        txtPregunta.setEditable(false);
        txtPregunta.setWrapStyleWord(true);
        txtPregunta.setLineWrap(true);
        txtPregunta.setFont(new Font("Arial", Font.PLAIN, 14));
        
        panelRespuestas = new JPanel();
        btnAnterior = new JButton("← Anterior");
        btnSiguiente = new JButton("Siguiente →");
        
        grupoRespuestas = new ButtonGroup();
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Panel superior con información
        JPanel panelSuperior = new JPanel(new GridLayout(3, 1));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelSuperior.add(lblNumeroPregunta);
        panelSuperior.add(lblTiempo);
        panelSuperior.add(lblNivelBloom);
        
        // Panel central con pregunta
        JPanel panelPregunta = new JPanel(new BorderLayout());
        panelPregunta.setBorder(BorderFactory.createTitledBorder("Pregunta"));
        JScrollPane scrollPregunta = new JScrollPane(txtPregunta);
        scrollPregunta.setPreferredSize(new Dimension(0, 150));
        panelPregunta.add(scrollPregunta, BorderLayout.CENTER);
        
        // Panel de respuestas
        panelRespuestas.setBorder(BorderFactory.createTitledBorder("Opciones de Respuesta"));
        
        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnAnterior);
        panelBotones.add(btnSiguiente);
        
        add(panelSuperior, BorderLayout.NORTH);
        add(panelPregunta, BorderLayout.CENTER);
        add(panelRespuestas, BorderLayout.EAST);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        btnAnterior.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarRespuestaActual();
                gestor.preguntaAnterior();
            }
        });
        
        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarRespuestaActual();
                if (gestor.esUltimaPregunta()) {
                    finalizarPrueba();
                } else {
                    gestor.siguientePregunta();
                }
            }
        });
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int opcion = JOptionPane.showConfirmDialog(
                    VentanaPrueba.this,
                    "¿Está seguro que desea salir? Se perderán las respuestas no guardadas.",
                    "Confirmar Salida",
                    JOptionPane.YES_NO_OPTION
                );
                if (opcion == JOptionPane.YES_OPTION) {
                    volverAVentanaPrincipal();
                }
            }
        });
    }
    
    private void actualizarPregunta(Pregunta pregunta) {
        if (pregunta == null) return;
        
        // Actualizar información de la pregunta
        lblNumeroPregunta.setText("Pregunta " + (gestor.getIndicePreguntaActual() + 1) + 
            " de " + gestor.getPreguntas().size());
        lblTiempo.setText("Tiempo estimado: " + pregunta.getTiempoEstimado() + " minutos");
        lblNivelBloom.setText("Nivel de Bloom: " + pregunta.getNivelBloom().getNombre());
        txtPregunta.setText(pregunta.getTexto());
        
        // Actualizar opciones de respuesta
        actualizarOpcionesRespuesta(pregunta);
        
        // Actualizar estado de botones
        btnAnterior.setEnabled(!gestor.esPrimeraPregunta());
        btnSiguiente.setText(gestor.esUltimaPregunta() ? "Finalizar Prueba" : "Siguiente →");
        
        // Cargar respuesta guardada si existe
        cargarRespuestaGuardada(pregunta);
    }
    
    private void actualizarOpcionesRespuesta(Pregunta pregunta) {
        panelRespuestas.removeAll();
        grupoRespuestas = new ButtonGroup();
        
        if (pregunta instanceof PreguntaOpcionMultiple) {
            PreguntaOpcionMultiple pom = (PreguntaOpcionMultiple) pregunta;
            panelRespuestas.setLayout(new GridLayout(pom.getOpciones().size(), 1));
            
            for (int i = 0; i < pom.getOpciones().size(); i++) {
                JRadioButton radio = new JRadioButton(pom.getOpciones().get(i));
                radio.setActionCommand(String.valueOf(i));
                grupoRespuestas.add(radio);
                panelRespuestas.add(radio);
            }
            
        } else if (pregunta instanceof PreguntaVerdaderoFalso) {
            panelRespuestas.setLayout(new GridLayout(2, 1));
            
            JRadioButton radioVerdadero = new JRadioButton("Verdadero");
            JRadioButton radioFalso = new JRadioButton("Falso");
            
            radioVerdadero.setActionCommand("true");
            radioFalso.setActionCommand("false");
            
            grupoRespuestas.add(radioVerdadero);
            grupoRespuestas.add(radioFalso);
            
            panelRespuestas.add(radioVerdadero);
            panelRespuestas.add(radioFalso);
        }
        
        panelRespuestas.revalidate();
        panelRespuestas.repaint();
    }
    
    private void cargarRespuestaGuardada(Pregunta pregunta) {
        Object respuestaGuardada = gestor.getRespuestaUsuario(pregunta.getId());
        if (respuestaGuardada == null) return;
        
        if (pregunta instanceof PreguntaOpcionMultiple && respuestaGuardada instanceof Integer) {
            int indice = (Integer) respuestaGuardada;
            Component[] componentes = panelRespuestas.getComponents();
            if (indice >= 0 && indice < componentes.length) {
                ((JRadioButton) componentes[indice]).setSelected(true);
            }
            
        } else if (pregunta instanceof PreguntaVerdaderoFalso && respuestaGuardada instanceof Boolean) {
            boolean valor = (Boolean) respuestaGuardada;
            Component[] componentes = panelRespuestas.getComponents();
            if (componentes.length >= 2) {
                if (valor) {
                    ((JRadioButton) componentes[0]).setSelected(true); // Verdadero
                } else {
                    ((JRadioButton) componentes[1]).setSelected(true); // Falso
                }
            }
        }
    }
    
    private void guardarRespuestaActual() {
        Pregunta preguntaActual = gestor.getPreguntaActual();
        if (preguntaActual == null) return;
        
        ButtonModel seleccionado = grupoRespuestas.getSelection();
        if (seleccionado == null) return;
        
        String comando = seleccionado.getActionCommand();
        Object respuesta = null;
        
        if (preguntaActual instanceof PreguntaOpcionMultiple) {
            respuesta = Integer.parseInt(comando);
        } else if (preguntaActual instanceof PreguntaVerdaderoFalso) {
            respuesta = Boolean.parseBoolean(comando);
        }
        
        if (respuesta != null) {
            gestor.guardarRespuesta(preguntaActual.getId(), respuesta);
        }
    }
    
    private void finalizarPrueba() {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro que desea finalizar la prueba?",
            "Finalizar Prueba",
            JOptionPane.YES_NO_OPTION
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            gestor.finalizarPrueba();
        }
    }
    
    private void volverAVentanaPrincipal() {
        VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
        ventanaPrincipal.setVisible(true);
        this.dispose();
    }
    
    @Override
    public void onEvent(PruebaEvent event) {
        SwingUtilities.invokeLater(() -> {
            switch (event.getTipo()) {
                case PREGUNTA_CAMBIADA:
                    Pregunta pregunta = (Pregunta) event.getData();
                    actualizarPregunta(pregunta);
                    break;
                
                case PRUEBA_FINALIZADA:
                    EstadisticasPrueba estadisticas = (EstadisticasPrueba) event.getData();
                    VentanaResultados ventanaResultados = new VentanaResultados(gestor, estadisticas);
                    ventanaResultados.setVisible(true);
                    this.dispose();
                    break;
                
                case ERROR:
                    JOptionPane.showMessageDialog(this, 
                        event.getMensaje(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                
                case PRUEBA_CARGADA:
                case RESPUESTA_GUARDADA:
                case ESTADISTICAS_CALCULADAS:
                    // Estos eventos no se manejan en la ventana de prueba
                    break;
            }
        });
    }
}
