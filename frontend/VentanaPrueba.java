package frontend;

import backend.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Ventana para realizar la prueba
 * @author Nicolas Cheuque
 */
public class VentanaPrueba extends JFrame {
    private GestorPrueba gestor;
    private List<Pregunta> preguntas;
    private int preguntaActual = 0;
    
    private JLabel lblNumero;
    private JTextArea txtPregunta;
    private JPanel panelOpciones;
    private ButtonGroup grupo;
    private JButton btnAnterior, btnSiguiente;
    
    public VentanaPrueba(GestorPrueba gestor) {
        this.gestor = gestor;
        this.preguntas = gestor.getPreguntas();
        
        setTitle("Aplicación de Prueba");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(VentanaPrueba.this, 
                    "¿Salir sin terminar?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        
        initComponents();
        mostrarPregunta();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel superior
        lblNumero = new JLabel();
        add(lblNumero, BorderLayout.NORTH);
        
        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout());
        
        txtPregunta = new JTextArea();
        txtPregunta.setEditable(false);
        txtPregunta.setWrapStyleWord(true);
        txtPregunta.setLineWrap(true);
        txtPregunta.setBorder(BorderFactory.createTitledBorder("Pregunta"));
        panelCentral.add(new JScrollPane(txtPregunta), BorderLayout.CENTER);
        
        panelOpciones = new JPanel();
        panelOpciones.setBorder(BorderFactory.createTitledBorder("Opciones"));
        panelCentral.add(panelOpciones, BorderLayout.EAST);
        
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior
        JPanel panelBotones = new JPanel();
        btnAnterior = new JButton("← Anterior");
        btnSiguiente = new JButton("Siguiente →");
        
        btnAnterior.addActionListener(e -> {
            guardarRespuesta();
            preguntaActual--;
            mostrarPregunta();
        });
        
        btnSiguiente.addActionListener(e -> {
            guardarRespuesta();
            if (preguntaActual == preguntas.size() - 1) {
                finalizarPrueba();
            } else {
                preguntaActual++;
                mostrarPregunta();
            }
        });
        
        panelBotones.add(btnAnterior);
        panelBotones.add(btnSiguiente);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void mostrarPregunta() {
        Pregunta p = preguntas.get(preguntaActual);
        
        lblNumero.setText("Pregunta " + (preguntaActual + 1) + " de " + preguntas.size());
        txtPregunta.setText(p.getTexto() + "\n\nNivel: " + p.getNivelBloom().getNombre() + 
                           "\nTiempo: " + p.getTiempoEstimado() + " min");
        
        // Opciones
        panelOpciones.removeAll();
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
        grupo = new ButtonGroup();
        
        List<String> opciones = p.getOpciones();
        for (int i = 0; i < opciones.size(); i++) {
            JRadioButton rb = new JRadioButton(opciones.get(i));
            rb.setActionCommand(String.valueOf(i));
            grupo.add(rb);
            panelOpciones.add(rb);
        }
        
        // Restaurar respuesta anterior
        String respuestaAnterior = gestor.getRespuesta(p.getId());
        if (respuestaAnterior != null) {
            for (AbstractButton btn : java.util.Collections.list(grupo.getElements())) {
                if (btn.getActionCommand().equals(respuestaAnterior)) {
                    btn.setSelected(true);
                    break;
                }
            }
        }
        
        // Botones
        btnAnterior.setEnabled(preguntaActual > 0);
        btnSiguiente.setText(preguntaActual == preguntas.size() - 1 ? "Enviar Respuestas" : "Siguiente →");
        
        panelOpciones.revalidate();
        panelOpciones.repaint();
    }
    
    private void guardarRespuesta() {
        if (grupo.getSelection() != null) {
            String respuesta = grupo.getSelection().getActionCommand();
            gestor.guardarRespuesta(preguntas.get(preguntaActual).getId(), respuesta);
        }
    }
    
    private void finalizarPrueba() {
        if (JOptionPane.showConfirmDialog(this, "¿Enviar respuestas?", "Confirmar", 
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            new VentanaResultados(gestor).setVisible(true);
            dispose();
        }
    }
}
