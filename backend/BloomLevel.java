package backend;

/**
 * Niveles de la Taxonomía de Bloom
 * @author Nicolas Cheuque
 */
public enum BloomLevel {
    RECORDAR(1, "Recordar"),
    COMPRENDER(2, "Comprender"), 
    APLICAR(3, "Aplicar"),
    ANALIZAR(4, "Analizar"),
    EVALUAR(5, "Evaluar"),
    CREAR(6, "Crear");
    
    private final int nivel;
    private final String nombre;
    
    BloomLevel(int nivel, String nombre) {
        this.nivel = nivel;
        this.nombre = nombre;
    }
    
    public int getNivel() { return nivel; }
    public String getNombre() { return nombre; }
    
    public static BloomLevel fromNivel(int nivel) {
        for (BloomLevel bl : values()) {
            if (bl.nivel == nivel) return bl;
        }
        throw new IllegalArgumentException("Nivel inválido: " + nivel);
    }
}
