# Prompt Maestro para Generación de Juego Android ("Más Allá de la Z")

**Rol:** Actúa como un Arquitecto de Software Senior en Android y experto en Kotlin.

**Objetivo:** Generar el código fuente completo, robusto y listo para producción de un juego móvil llamado "Más Allá de la Z".

**Contexto del Juego:** Es un juego tipo puzzle de deslizamiento (estilo 2048) en una cuadrícula 4x4.

**Diferencia clave:** Es infinito. Los valores son letras (A..Z, AA..AZ, BA..ZZ...).

**Mecánica de Spawn:** Para evitar bloqueos, las nuevas fichas que aparecen dependen de la ficha más baja presente en el tablero.

### Stack Tecnológico (Estricto):

*   **Lenguaje:** Kotlin.
*   **UI:** Jetpack Compose (Material 3). Usar Composables estándar con animaciones de `Modifier.animateContentSize` o similares. No usar Canvas complejo innecesariamente.
*   **Inyección de Dependencias:** Hilt.
*   **Persistencia:** Room Database (Guardado automático tras cada movimiento).
*   **Arquitectura:** Clean Architecture (Capas: presentacion, dominio, datos) + MVVM.
*   **Calidad:** Principios SOLID, Clean Code.

### Requisitos No Funcionales (Críticos):

*   **Idioma:** TODO el código (clases, variables, métodos, KDoc) debe estar en ESPAÑOL.
*   **Estabilidad:** La app debe ser "Crash-Free". Usa `runCatching` y manejo de estados seguro.
*   **Producción:** Incluir configuración para `proguard-rules.pro` (reglas para Hilt/Room/Modelos) y un `.gitignore` robusto.

---

## Especificaciones Técnicas Detalladas

### 1. Capa de Dominio (Lógica de Negocio)

**Clase `ConversorLetras` (Algoritmo Obligatorio):** Debes implementar la siguiente lógica exacta para convertir enteros a letras (Base 26 Biyectiva):

```kotlin
// Lógica a implementar:
fun obtenerEtiqueta(valor: Int): String {
    val sb = StringBuilder()
    var n = valor
    while (n > 0) {
        n-- 
        sb.insert(0, (('A'.code + (n % 26)).toChar()))
        n /= 26
    }
    return sb.toString()
}
```

**Clase `ReglasJuego`:**

*   **Tablero:** Array o Lista de listas de enteros (4x4). 0 representa vacío.
*   **Regla de Fusión:** Si dos fichas adyacentes tienen el mismo valor X, se unen creando una ficha X + 1 (y sumando puntuación).
*   **Regla de Spawn (Generación):**
    *   Calcular `minVal` = el valor más bajo presente en el tablero (ignorando ceros/vacíos).
    *   Generar nueva ficha: 90% probabilidad de ser `minVal`, 10% probabilidad de ser `minVal + 1`.
    *   Esto asegura que el juego sea infinito y estratégico.

### 2. Capa de Datos (Room)

**Entidad `PartidaEntidad`:**

*   Debe guardar: `id` (siempre 1), `tableroJson` (String), `puntuacion` (Long), `record` (Long).
*   Usa `@TypeConverter` o serialización manual simple para guardar la matriz del tablero como String en la base de datos.
*   **Importante:** Asegurar que los datos se guardan de forma atómica.

### 3. Capa de Presentación (Compose)

**`ViewModelJuego`:**

*   Exponer un `UiState` (data class) con: `tablero` (Lista de objetos Ficha), `puntuacion`, `record`, `gameOver`.
*   Las acciones (Swipe Arriba, Abajo, etc.) deben procesarse en el ViewModel y actualizar el estado.

**UI (`PantallaJuego`):**

*   Diseño limpio con Material 3.
*   **Colores:** Generar un color dinámico basado en el valor de la ficha (ej: `Color.HSV(valor * 10, ...)` o una lista predefinida) para que visualmente se note el progreso.
*   Mostrar "A", "Z", "AA" usando el `ConversorLetras`.

### 4. Archivos de Configuración

**`proguard-rules.pro`:**

*   Añadir reglas `@Keep` para las entidades de Room y ViewModels para evitar problemas con R8 en release.

**`.gitignore`:**

*   Estándar de Android + JetBrains.

---

**Instrucciones de Salida:** Genera el código fichero por fichero, especificando la ruta del paquete (ej: `com.juego.masallaz.dominio.modelos`). No omitas implementaciones clave. Prioriza la seguridad contra cierres inesperados.
