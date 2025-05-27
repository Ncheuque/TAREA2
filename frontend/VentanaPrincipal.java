package frontend;

import backend.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Ventana principal del sistema
 * @author Nicolas Cheuque
 */
public class VentanaPrincipal extends JFrame implements GestorPrueba.Observer {
    private GestorPrueba gestor;
    private JLabel lblInfo;
    private JButton btnCargar, btnIniciar;
    
    public VentanaPrincipal() {
        gestor = new GestorPrueba();
        gestor.addObserver(this);
        
        setTitle("Sistema de Pruebas - Taxonomía de Bloom - Nicolas Cheuque");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel superior
        JPanel panelTop = new JPanel();
        btnCargar = new JButton("Cargar Archivo");
        btnCargar.addActionListener(e -> cargarArchivo());
        panelTop.add(btnCargar);
        
        // Panel central
        lblInfo = new JLabel("<html><center>No hay archivo cargado<br>Seleccione un archivo .txt</center></html>");
        lblInfo.setHorizontalAlignment(JLabel.CENTER);
        
        // Panel inferior
        btnIniciar = new JButton("Iniciar Prueba");
        btnIniciar.setEnabled(false);
        btnIniciar.addActionListener(e -> iniciarPrueba());
        
        add(panelTop, BorderLayout.NORTH);
        add(lblInfo, BorderLayout.CENTER);
        add(btnIniciar, BorderLayout.SOUTH);
    }
    
    private void cargarArchivo() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".txt");
            }
            public String getDescription() { return "Archivos de texto (*.txt)"; }
        });
        
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            gestor.cargarArchivo(fc.getSelectedFile().getAbsolutePath());
        }
    }
    
    private void iniciarPrueba() {
        new VentanaPrueba(gestor).setVisible(true);
        setVisible(false);
    }
    
    @Override
    public void actualizar(String evento, Object data) {
        SwingUtilities.invokeLater(() -> {
            if ("CARGADO".equals(evento)) {
                int cantidad = (Integer) data;
                lblInfo.setText("<html><center>Archivo cargado exitosamente<br>" +
                               "Preguntas: " + cantidad + "<br>" +
                               "Tiempo total: " + gestor.getTiempoTotal() + " min</center></html>");
                btnIniciar.setEnabled(true);
            } else if ("ERROR".equals(evento)) {
                JOptionPane.showMessageDialog(this, "Error: " + data, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
