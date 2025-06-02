package frontend.views;

import backend.services.GestorPreguntas;
import backend.models.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaRevision extends JFrame {
    private GestorPreguntas gestor;
    private VentanaResultados ventanaResultados;
    private int preguntaActual;
    private JLabel lblNumeroPregunta;
    private JLabel lblTiempo;
    private JLabel lblNivelBloom;
    private JTextArea txtPregunta;
    private JPanel panelRespuestas;
    private JButton btnAnterior;
    private JButton btnSiguiente;
    private JButton btnVolverResultados;
    private JLabel lblResultado;
    
    public VentanaRevision(GestorPreguntas gestor, VentanaResultados ventanaResultados) {
        this.gestor = gestor;
        this.ventanaResultados = ventanaResultados;
        this.preguntaActual = 0;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setTitle("Revisión de Respuestas - Taxonomía de Bloom");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Cargar primera pregunta
        actualizarPregunta();
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
        btnVolverResultados = new JButton("Volver a Resultados");
        
        lblResultado = new JLabel();
        lblResultado.setFont(new Font("Arial", Font.BOLD, 16));
        lblResultado.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Panel superior con información
        JPanel panelSuperior = new JPanel(new GridLayout(4, 1));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelSuperior.add(lblNumeroPregunta);
        panelSuperior.add(lblTiempo);
        panelSuperior.add(lblNivelBloom);
        panelSuperior.add(lblResultado);
        
        // Panel central con pregunta
        JPanel panelPregunta = new JPanel(new BorderLayout());
        panelPregunta.setBorder(BorderFactory.createTitledBorder("Pregunta"));
        JScrollPane scrollPregunta = new JScrollPane(txtPregunta);
        scrollPregunta.setPreferredSize(new Dimension(0, 150));
        panelPregunta.add(scrollPregunta, BorderLayout.CENTER);
        
        // Panel de respuestas
        panelRespuestas.setBorder(BorderFactory.createTitledBorder("Respuestas"));
        
        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnAnterior);
        panelBotones.add(btnSiguiente);
        panelBotones.add(btnVolverResultados);
        
        add(panelSuperior, BorderLayout.NORTH);
        add(panelPregunta, BorderLayout.CENTER);
        add(panelRespuestas, BorderLayout.EAST);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        btnAnterior.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (preguntaActual > 0) {
                    preguntaActual--;
                    actualizarPregunta();
                }
            }
        });
        
        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (preguntaActual < gestor.getPreguntas().size() - 1) {
                    preguntaActual++;
                    actualizarPregunta();
                }
            }
        });
        
        btnVolverResultados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventanaResultados.setVisible(true);
                VentanaRevision.this.dispose();
            }
        });
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                ventanaResultados.setVisible(true);
                VentanaRevision.this.dispose();
            }
        });
    }
    
    private void actualizarPregunta() {
        Pregunta pregunta = gestor.getPreguntas().get(preguntaActual);
        
        // Actualizar información de la pregunta
        lblNumeroPregunta.setText("Pregunta " + (preguntaActual + 1) + 
            " de " + gestor.getPreguntas().size());
        lblTiempo.setText("Tiempo estimado: " + pregunta.getTiempoEstimado() + " minutos");
        lblNivelBloom.setText("Nivel de Bloom: " + pregunta.getNivelBloom().getNombre());
        txtPregunta.setText(pregunta.getTexto());
        
        // Verificar si la respuesta es correcta
        Object respuestaUsuario = gestor.getRespuestaUsuario(pregunta.getId());
        boolean esCorrecta = respuestaUsuario != null && pregunta.validarRespuesta(respuestaUsuario);
        
        if (respuestaUsuario == null) {
            lblResultado.setText("No respondida");
            lblResultado.setForeground(Color.ORANGE);
        } else if (esCorrecta) {
            lblResultado.setText("✓ Respuesta Correcta");
            lblResultado.setForeground(new Color(0, 150, 0));
        } else {
            lblResultado.setText("✗ Respuesta Incorrecta");
            lblResultado.setForeground(Color.RED);
        }
        
        // Actualizar opciones de respuesta
        actualizarOpcionesRespuesta(pregunta, respuestaUsuario);
        
        // Actualizar estado de botones
        btnAnterior.setEnabled(preguntaActual > 0);
        btnSiguiente.setEnabled(preguntaActual < gestor.getPreguntas().size() - 1);
    }
    
    private void actualizarOpcionesRespuesta(Pregunta pregunta, Object respuestaUsuario) {
        panelRespuestas.removeAll();
        
        if (pregunta instanceof PreguntaOpcionMultiple) {
            PreguntaOpcionMultiple pom = (PreguntaOpcionMultiple) pregunta;
            panelRespuestas.setLayout(new GridLayout(pom.getOpciones().size(), 1));
            
            for (int i = 0; i < pom.getOpciones().size(); i++) {
                JLabel lblOpcion = new JLabel(pom.getOpciones().get(i));
                lblOpcion.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                
                // Marcar respuesta del usuario
                if (respuestaUsuario != null && respuestaUsuario.equals(i)) {
                    lblOpcion.setOpaque(true);
                    if (i == pom.getOpcionCorrecta()) {
                        lblOpcion.setBackground(new Color(200, 255, 200)); // Verde claro
                        lblOpcion.setText("✓ " + lblOpcion.getText());
                    } else {
                        lblOpcion.setBackground(new Color(255, 200, 200)); // Rojo claro
                        lblOpcion.setText("✗ " + lblOpcion.getText());
                    }
                } else if (i == pom.getOpcionCorrecta()) {
                    // Marcar respuesta correcta
                    lblOpcion.setOpaque(true);
                    lblOpcion.setBackground(new Color(200, 255, 200)); // Verde claro
                    lblOpcion.setText("✓ " + lblOpcion.getText() + " (Correcta)");
                }
                
                panelRespuestas.add(lblOpcion);
            }
            
        } else if (pregunta instanceof PreguntaVerdaderoFalso) {
            PreguntaVerdaderoFalso pvf = (PreguntaVerdaderoFalso) pregunta;
            panelRespuestas.setLayout(new GridLayout(2, 1));
            
            JLabel lblVerdadero = new JLabel("Verdadero");
            JLabel lblFalso = new JLabel("Falso");
            
            lblVerdadero.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            lblFalso.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            // Marcar respuesta del usuario
            if (respuestaUsuario != null) {
                boolean respuestaBoolean = (Boolean) respuestaUsuario;
                if (respuestaBoolean) {
                    lblVerdadero.setOpaque(true);
                    if (pvf.getRespuestaCorrectaBoolean()) {
                        lblVerdadero.setBackground(new Color(200, 255, 200));
                        lblVerdadero.setText("✓ Verdadero");
                    } else {
                        lblVerdadero.setBackground(new Color(255, 200, 200));
                        lblVerdadero.setText("✗ Verdadero");
                    }
                } else {
                    lblFalso.setOpaque(true);
                    if (!pvf.getRespuestaCorrectaBoolean()) {
                        lblFalso.setBackground(new Color(200, 255, 200));
                        lblFalso.setText("✓ Falso");
                    } else {
                        lblFalso.setBackground(new Color(255, 200, 200));
                        lblFalso.setText("✗ Falso");
                    }
                }
            }
            
            // Marcar respuesta correcta
            if (pvf.getRespuestaCorrectaBoolean() && (respuestaUsuario == null || !((Boolean) respuestaUsuario))) {
                lblVerdadero.setOpaque(true);
                lblVerdadero.setBackground(new Color(200, 255, 200));
                lblVerdadero.setText("✓ Verdadero (Correcta)");
            } else if (!pvf.getRespuestaCorrectaBoolean() && (respuestaUsuario == null || ((Boolean) respuestaUsuario))) {
                lblFalso.setOpaque(true);
                lblFalso.setBackground(new Color(200, 255, 200));
                lblFalso.setText("✓ Falso (Correcta)");
            }
            
            panelRespuestas.add(lblVerdadero);
            panelRespuestas.add(lblFalso);
        }
        
        panelRespuestas.revalidate();
        panelRespuestas.repaint();
    }
}
