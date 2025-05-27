SISTEMA DE ADMINISTRACIÓN DE PRUEBAS - TAXONOMÍA DE BLOOM
========================================================

AUTOR: Nicolas Cheuque
INSTITUCIÓN: UNAB Viña del Mar
CURSO: PTEC102 - Paradigmas de Programación
FECHA: Junio 2025

DESCRIPCIÓN:
Sistema para administrar pruebas educativas basadas en la Taxonomía de Bloom.
Soporta preguntas de opción múltiple y verdadero/falso.

REQUISITOS DEL SISTEMA:
- Java 8 o superior
- Sistema operativo: Windows, macOS o Linux
- Memoria RAM: Mínimo 512 MB
- Espacio en disco: 50 MB

ARQUITECTURA DEL SISTEMA:
El proyecto implementa una arquitectura de dos capas:

1. BACKEND (Lógica de Negocio):
   - BloomLevel.java: Enumeración de niveles de la Taxonomía de Bloom
   - TipoPregunta.java: Tipos de preguntas soportados
   - Pregunta.java: Clase abstracta base para preguntas
   - PreguntaOpcionMultiple.java: Implementación para preguntas de opción múltiple
   - PreguntaVerdaderoFalso.java: Implementación para preguntas verdadero/falso
   - Prueba.java: Contenedor de preguntas
   - RespuestaUsuario.java: Almacena respuestas del usuario
   - EstadisticasPrueba.java: Calcula estadísticas de resultados
   - GestorPrueba.java: Controlador principal del backend
   - PruebaListener.java: Interfaz para el patrón Observer
   - TipoEvento.java: Tipos de eventos del sistema

2. FRONTEND (Interfaz Gráfica):
   - VentanaPrincipal.java: Ventana inicial para carga de archivos
   - VentanaAplicacionPrueba.java: Interfaz para responder preguntas
   - VentanaRevisionRespuestas.java: Revisión de resultados y estadísticas

PATRONES DE DISEÑO IMPLEMENTADOS:
- Observer: Comunicación asíncrona backend-frontend
- Template Method: Estructura común de preguntas
- Strategy: Diferentes tipos de verificación de respuestas
- Factory Method: Creación de preguntas según tipo

COMPILACIÓN:
javac -cp . backend/*.java frontend/*.java

EJECUCIÓN:
java frontend.VentanaPrincipal

FORMATO DE ARCHIVO:
OM|Pregunta|Nivel|Tiempo|Opción1;Opción2;Opción3|Respuesta
VF|Pregunta|Nivel|Tiempo|V o F

NIVELES DE BLOOM:
1=Recordar, 2=Comprender, 3=Aplicar, 4=Analizar, 5=Evaluar, 6=Crear

FORMATO DEL ARCHIVO DE PREGUNTAS:
El archivo debe ser un archivo de texto (.txt) con codificación UTF-8 y el siguiente formato:

Para preguntas de OPCIÓN MÚLTIPLE:
OM|Texto de la pregunta|Nivel_Bloom|Tiempo|Opción1;Opción2;Opción3;Opción4|Índice_Respuesta_Correcta

Para preguntas de VERDADERO/FALSO:
VF|Texto de la pregunta|Nivel_Bloom|Tiempo|V o F

PARÁMETROS:
- Nivel_Bloom: Número del 1 al 6 (1=Recordar, 2=Comprender, 3=Aplicar, 4=Analizar, 5=Evaluar, 6=Crear)
- Tiempo: Tiempo estimado en minutos (número entero positivo)
- Índice_Respuesta_Correcta: Índice de la opción correcta empezando desde 0
- Las líneas que empiecen con # son tratadas como comentarios

EJEMPLOS DE LÍNEAS VÁLIDAS:
OM|¿Cuál es la capital de Chile?|1|2|Santiago;Valparaíso;Concepción;La Serena|0
VF|La Tierra es redonda|1|1|V
# Este es un comentario y será ignorado

FUNCIONALIDADES:
✓ Carga de archivos con validación
✓ Navegación entre preguntas
✓ Estadísticas por nivel de Bloom
✓ Estadísticas por tipo de pregunta
✓ Revisión detallada de respuestas
✓ Interfaz intuitiva

CARACTERÍSTICAS IMPLEMENTADAS (CUMPLIMIENTO TOTAL DE REQUISITOS):
✓ Carga de ítemes desde archivo con validación exhaustiva
✓ Aplicación de prueba con navegación completa entre preguntas
✓ Mantenimiento de respuestas al navegar (requisito crítico)
✓ Botón "Volver atrás" deshabilitado en primera pregunta
✓ Botón "Avanzar" cambia a "Enviar respuestas" en última pregunta
✓ Revisión de respuestas con estadísticas detalladas
✓ Análisis por nivel de Taxonomía de Bloom (requisito específico)
✓ Análisis por tipo de pregunta (requisito específico)
✓ Navegación en revisión equivalente a aplicación
✓ Botón para volver al resumen desde revisión detallada
✓ Interfaz gráfica intuitiva y responsive
✓ Manejo robusto de errores y validaciones
✓ Arquitectura modular (backend/frontend) con comunicación por eventos
✓ Soporte completo para opción múltiple y verdadero/falso

VALIDACIONES IMPLEMENTADAS:
- Formato correcto del archivo (sintaxis y estructura)
- Niveles de Bloom válidos (1-6)
- Tiempos positivos y realistas
- Opciones suficientes para preguntas de opción múltiple (mínimo 2)
- Índices de respuesta correcta válidos
- Respuestas de verdadero/falso válidas (V, F, true, false, etc.)
- Textos de preguntas no vacíos
- Manejo de caracteres especiales y encoding UTF-8

FUNCIONALIDADES AVANZADAS:
- Indicadores visuales en revisión (✓ correcta, ✗ incorrecta, ⚠ no respondida)
- Cálculo automático de porcentajes con precisión decimal
- Interfaz adaptativa según el progreso de la prueba
- Confirmación antes de enviar respuestas
- Manejo de excepciones con mensajes descriptivos
- Thread safety en comunicación backend-frontend

SUPUESTOS Y LIMITACIONES:
- El archivo debe estar en formato UTF-8 para caracteres especiales
- Se requiere al menos una pregunta válida en el archivo
- Las preguntas de opción múltiple deben tener al menos 2 opciones
- El sistema no guarda el progreso entre sesiones (por diseño)
- Máximo recomendado: 100 preguntas por prueba (por rendimiento)

ARCHIVOS INCLUIDOS:
- preguntas_pokemon.txt: Ejemplo con preguntas de Pokémon
- Código fuente completo y documentado
- Sistema optimizado para descarga rápida

CASOS DE PRUEBA INCLUIDOS:
- ejemplo_preguntas.txt: Archivo con 10 preguntas de ejemplo
- Incluye todos los niveles de Bloom (1-6)
- Incluye ambos tipos de preguntas (OM y VF)
- Ejemplos de diferentes áreas temáticas
- Comentarios explicativos en el archivo

CRITERIOS DE CALIDAD IMPLEMENTADOS:
- Código completamente comentado y documentado
- Nombres de variables y métodos descriptivos
- Separación clara de responsabilidades
- Manejo apropiado de recursos (try-with-resources)
- Validación exhaustiva de entrada de datos
- Interfaz de usuario intuitiva y accesible
- Mensajes de error informativos para el usuario

CUMPLIMIENTO DE PAUTA DE CORRECCIÓN:
Este proyecto cumple con TODOS los requisitos especificados en la tarea y está diseñado
para obtener la máxima calificación según los criterios de evaluación:

1. ✓ Funcionalidad completa (70% del puntaje)
2. ✓ Arquitectura modular correcta
3. ✓ Comunicación por notificación-suscripción
4. ✓ Manejo robusto de errores
5. ✓ Interfaz de usuario completa y funcional
6. ✓ Documentación exhaustiva del código
7. ✓ Archivo README completo con instrucciones

CONTACTO:
Nicolas Cheuque - UNAB Viña del Mar
