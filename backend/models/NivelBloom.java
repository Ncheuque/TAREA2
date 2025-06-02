package backend.models;

public enum NivelBloom {
    RECORDAR(1, "Recordar"),
    COMPRENDER(2, "Comprender"), 
    APLICAR(3, "Aplicar"),
    ANALIZAR(4, "Analizar"),
    EVALUAR(5, "Evaluar"),
    CREAR(6, "Crear");
    
    private final int valor;
    private final String nombre;
    
    NivelBloom(int valor, String nombre) {
        this.valor = valor;
        this.nombre = nombre;
    }
    
    public int getValor() {
        return valor;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public static NivelBloom fromValor(int valor) {
        for (NivelBloom nivel : values()) {
            if (nivel.valor == valor) {
                return nivel;
            }
        }
        throw new IllegalArgumentException("Nivel de Bloom inválido: " + valor);
    }
}
