# Mi Formación CTMA — Guía 03: Interfaces Declarativas con Jetpack Compose

## Descripción del Incremento
En este incremento de la Semana 3 se implementó la interfaz gráfica declarativa para consultar los compromisos formativos del Aprendiz mediante Jetpack Compose y Material 3.

## Estructura de la Capa de Presentación
- **`ui/components/TarjetaActividad.kt`**: Componente reutilizable y sin estado (stateless) para renderizar la información de una actividad (título, descripción con soporte de nulos, prioridad y progreso).
- **`ui/screens/PantallaActividades.kt`**: Pantalla principal compuesta por un `Scaffold`, `TopAppBar` y un `LazyColumn` perezoso optimizado con claves estables (`key = { it.id }`).
- **`MainActivity.kt`**: Punto de entrada donde se instancia el repositorio en memoria y se proveen datos de prueba.

---

## Arquitectura y Persistencia (Semana 7)

### Inventario de Datos
| Elemento | Persistencia | Tipo |
| :--- | :--- | :--- |
| Actividades | Room (`ActividadEntity`) | Persistente (Observable Flow) |
| Competencias | Room (`CompetenciaEntity`) | Persistente |
| Filtro de Prioridad | DataStore | Persistente |
| Estado Formulario | `mutableStateOf` | Efímero |

### Diagrama de Arquitectura
```mermaid
graph TD
    UI[Jetpack Compose UI] --> VM[ViewModels: Actividades, Formulario]
    VM --> REP[Repositories: RoomActividad, Preferencias]
    REP --> DB[(Room Database)]
    REP --> DS[(Preferences DataStore)]
    FormApp[FormacionApp: DI Container] --> VM
    FormApp --> REP
```

### Decisiones Técnicas
- **ViewModel-Repository**: Se migró la lógica de `MainActivity` a ViewModels para mejorar la testabilidad y sobrevivir a cambios de configuración.
- **Singleton Manual**: Se utiliza `FormacionApp` como contenedor de dependencias simple para asegurar una única instancia de la base de datos.
- **Migraciones Controladas**: Se implementó una migración manual de v1 a v2 para añadir el campo `completada` sin pérdida de datos.
- **Pruebas Instrumentadas**: Se añadieron tests de integración para el DAO y validación de migración de esquema.

## Checklist de UX / Accesibilidad

| Criterio / Elemento | Estado | Observación / Hallazgo Corregido |
| :--- | :---: | :--- |
| **Manejo de Nulos** | Corregido | Se corrigió error de compilación en `descripcion` (`String?`) agregando un valor por defecto mediante operador Elvis (`?: "Sin descripción"`). |
| **Claves Estables en Listas** | Cumple | Se asignó `key = { actividad.id }` en `LazyColumn` para optimizar la recomposición y el rendimiento. |
| **Jerarquía Visual Material 3** | Cumple | Tipografías centralizadas (`titleMedium`, `bodyMedium`) y contraste adecuado según colores del tema. |
| **Estado Vacío** | Cumple | La pantalla valida cuando `actividades.isEmpty()` y despliega un mensaje explicativo al usuario. |
