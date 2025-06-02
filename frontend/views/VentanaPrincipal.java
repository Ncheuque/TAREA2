package frontend.views;

import backend.services.GestorPreguntas;
import backend.events.*;
import backend.models.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class VentanaPrincipal extends JFrame implements EventListener<PruebaEvent> {
    private GestorPreguntas gestor;
    private JLabel lblCantidadItems;
    private JLabel lblTiempoTotal;
    private JButton btnCargarArchivo;
    private JButton btnIniciarPrueba;
    private JPanel panelInfo;
    
    public VentanaPrincipal() {
        this.gestor = new GestorPreguntas();
        this.gestor.addListener(this);
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setTitle("Sistema de Gestión de Preguntas - Taxonomía de Bloom");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void initializeComponents() {
        lblCantidadItems = new JLabel("Cantidad de ítemes: -");
        lblTiempoTotal = new JLabel("Tiempo total estimado: -");
        btnCargarArchivo = new JButton("Cargar Archivo de Preguntas");
        btnIniciarPrueba = new JButton("Iniciar Prueba");
        
        btnIniciarPrueba.setEnabled(false);
        
        panelInfo = new JPanel();
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información de la Prueba"));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Panel superior con título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(70, 130, 180));
        JLabel lblTitulo = new JLabel("Sistema de Gestión de Preguntas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        
        // Panel central con información
        panelInfo.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panelInfo.add(new JLabel("Basado en la Taxonomía de Bloom"), gbc);
        
        gbc.gridy = 1;
        panelInfo.add(lblCantidadItems, gbc);
        
        gbc.gridy = 2;
        panelInfo.add(lblTiempoTotal, gbc);
        
        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnCargarArchivo);
        panelBotones.add(btnIniciarPrueba);
        
        add(panelTitulo, BorderLayout.NORTH);
        add(panelInfo, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        btnCargarArchivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarArchivo();
            }
        });
        
        btnIniciarPrueba.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarPrueba();
            }
        });
    }
    
    private void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            gestor.cargarPreguntasDesdeArchivo(archivo);
        }
    }
    
    private void iniciarPrueba() {
        VentanaPrueba ventanaPrueba = new VentanaPrueba(gestor);
        ventanaPrueba.setVisible(true);
        this.setVisible(false);
    }
    
    @Override
    public void onEvent(PruebaEvent event) {
        SwingUtilities.invokeLater(() -> {
            switch (event.getTipo()) {
                case PRUEBA_CARGADA:
                    @SuppressWarnings("unchecked")
                    List<Pregunta> preguntas = (List<Pregunta>) event.getData();
                    lblCantidadItems.setText("Cantidad de ítemes: " + preguntas.size());
                    lblTiempoTotal.setText("Tiempo total estimado: " + 
                        gestor.getTiempoTotalEstimado() + " minutos");
                    btnIniciarPrueba.setEnabled(true);
                    JOptionPane.showMessageDialog(this, 
                        "Archivo cargado exitosamente.\n" +
                        "Preguntas encontradas: " + preguntas.size(),
                        "Carga Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    break;
                
                case ERROR:
                    JOptionPane.showMessageDialog(this, 
                        event.getMensaje(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                    
                case PREGUNTA_CAMBIADA:
                case RESPUESTA_GUARDADA:
                case PRUEBA_FINALIZADA:
                case ESTADISTICAS_CALCULADAS:
                    // Estos eventos no se manejan en la ventana principal
                    break;
            }
        });
    }
}
