package frontend.views;

import backend.services.GestorPreguntas;
import backend.models.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Map;

public class VentanaResultados extends JFrame {
    private GestorPreguntas gestor;
    private EstadisticasPrueba estadisticas;
    private JLabel lblPorcentajeTotal;
    private JTable tablaEstadisticas;
    private JButton btnRevisarRespuestas;
    private JButton btnNuevaPrueba;
    
    public VentanaResultados(GestorPreguntas gestor, EstadisticasPrueba estadisticas) {
        this.gestor = gestor;
        this.estadisticas = estadisticas;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        cargarEstadisticas();
        
        setTitle("Resultados de la Prueba - Taxonomía de Bloom");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void initializeComponents() {
        lblPorcentajeTotal = new JLabel();
        lblPorcentajeTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblPorcentajeTotal.setHorizontalAlignment(SwingConstants.CENTER);
        
        String[] columnas = {"Categoría", "Tipo", "Porcentaje"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaEstadisticas = new JTable(modelo);
        tablaEstadisticas.setRowHeight(25);
        
        btnRevisarRespuestas = new JButton("Revisar Respuestas");
        btnNuevaPrueba = new JButton("Nueva Prueba");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Panel superior con título y porcentaje total
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Resultados de la Prueba");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);
        panelSuperior.add(lblPorcentajeTotal, BorderLayout.CENTER);
        
        // Panel central con tabla de estadísticas
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createTitledBorder("Estadísticas Detalladas"));
        
        JScrollPane scrollTabla = new JScrollPane(tablaEstadisticas);
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        
        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnRevisarRespuestas);
        panelBotones.add(btnNuevaPrueba);
        
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        btnRevisarRespuestas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaRevision ventanaRevision = new VentanaRevision(gestor, VentanaResultados.this);
                ventanaRevision.setVisible(true);
                VentanaResultados.this.setVisible(false);
            }
        });
        
        btnNuevaPrueba.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
                ventanaPrincipal.setVisible(true);
                VentanaResultados.this.dispose();
            }
        });
    }
    
    private void cargarEstadisticas() {
        DecimalFormat df = new DecimalFormat("#.##");
        
        // Mostrar porcentaje total
        lblPorcentajeTotal.setText(String.format("Puntuación Total: %.2f%% (%d/%d respuestas correctas)",
            estadisticas.getPorcentajeTotal(),
            estadisticas.getRespuestasCorrectas(),
            estadisticas.getTotalPreguntas()));
        
        // Cargar datos en la tabla
        DefaultTableModel modelo = (DefaultTableModel) tablaEstadisticas.getModel();
        modelo.setRowCount(0);
        
        // Agregar estadísticas por nivel de Bloom
        Map<NivelBloom, Double> porcentajesPorNivel = estadisticas.getPorcentajePorNivel();
        for (Map.Entry<NivelBloom, Double> entrada : porcentajesPorNivel.entrySet()) {
            modelo.addRow(new Object[]{
                "Nivel de Bloom",
                entrada.getKey().getNombre(),
                df.format(entrada.getValue()) + "%"
            });
        }
        
        // Agregar estadísticas por tipo de pregunta
        Map<String, Double> porcentajesPorTipo = estadisticas.getPorcentajePorTipo();
        for (Map.Entry<String, Double> entrada : porcentajesPorTipo.entrySet()) {
            modelo.addRow(new Object[]{
                "Tipo de Pregunta",
                entrada.getKey(),
                df.format(entrada.getValue()) + "%"
            });
        }
    }
}
