# Mi Formación CTMA — Guía 06: Persistencia Local y DataStore

## Descripción del Incremento
En este incremento se evolucionó la aplicación para soportar la persistencia de datos local, asegurando que las actividades y preferencias del usuario no se borren al cerrar y reabrir la app.

## Decisiones de Diseño y Arquitectura
- **Room Database**: Se configuró para manejar el almacenamiento estructurado de actividades (`ActividadEntity`) y competencias (`CompetenciaEntity`) bajo una relación 1:N.
- **Single Source of Truth**: El repositorio local (`RoomActividadRepository`) actúa como la única fuente de verdad, exponiendo flujos reactivos (`Flow`) directo desde la base de datos hacia la UI.
- **Mapeadores de Datos**: Se implementaron en la capa de datos para separar por completo los modelos de base de datos de las entidades puras del dominio, evitando contaminación arquitectónica.
- **Preferences DataStore**: Utilizado mediante `PreferenciasRepository` para almacenar filtros y configuraciones ligeras elegidas por el usuario.
- **Evolución del Esquema**: Configuración de la base de datos iniciando con la Versión 1 y una migración explícita a la **Versión 2** que agrega la columna `completada` de forma segura.

---

## Criterios de Aceptación Cumplidos

| Criterio / Elemento | Estado | Observación |
| :--- | :---: | :--- |
| **Persistencia Completa (PA-01)** | Cumple | Los registros no se borran al reiniciar o cerrar la aplicación. |
| **Actualización Reactiva (PA-02)** | Cumple | Uso de `Flow` en Room que notifica automáticamente a la UI en cada cambio. |
| **Manejo Seguro de IDs (PA-03)** | Cumple | Las consultas de identificadores inexistentes están controladas. |
| **Evolución y Migración 1 ➔ 2 (PA-06)** | Cumple | `MIGRATION_1_2` programada de manera síncrona sin borrado destructivo. |
