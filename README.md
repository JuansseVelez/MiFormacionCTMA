# Mi Formación CTMA — Guía 08: Servicios Web REST, Caché y Resiliencia

## Descripción del Incremento
En este incremento de la Guía 8 se integró la comunicación con servicios web mediante **Retrofit** y **OkHttp**, implementando una estrategia **Offline-First** donde Room se mantiene como la única fuente de verdad y se garantiza que los fallos de red nunca corrompan ni vacíen el caché local.

## Decisiones de Diseño y Arquitectura
- **Estrategia Offline-First**: El repositorio (`RoomActividadRepository`) coordina las peticiones remotas (`RemoteActividadDataSource`) con el almacenamiento local (`FormacionDao`). Si la red falla, el caché local permanece intacto.
- **Separación de Modelos DTO**: Se introduce `ActividadDto` para el transporte de red, manteniéndolo completamente independiente de `ActividadEntity` (Room) y de los modelos puros de dominio.
- **Mapeadores de Red**: Funciones puras (`toEntity()`, `toDto()`) que aíslan la capa REST de la persistencia y de la interfaz de usuario.
- **Seguridad de Tokens (`TokenProvider`)**: Inyección dinámica de tokens de sesión mediante `AuthInterceptor` en OkHttp, evitando claves hardcodeadas en texto plano.
- **Resiliencia ante Errores**: Manejo robusto de excepciones de red (`IOException`, timeouts) con propagación limpia de `CancellationException`.

---

## Criterios de Aceptación Cumplidos

| Criterio / Elemento | Estado | Observación |
| :--- | :---: | :--- |
| **Sincronización Exitosa (CA-01)** | ✅ Cumple | Respuestas HTTP 200 actualizan Room de forma atómica y la UI se refresca reactivamente. |
| **Listas Vacías Válidas (CA-02)** | ✅ Cumple | Arreglos vacíos devueltos por el servidor se procesan correctamente sin confundirse con errores. |
| **Resiliencia ante Timeout (CA-03)** | ✅ Cumple | Ante timeouts con datos previos, el caché local permanece intacto. |
| **Cancelación Cooperativa (CA-08)** | ✅ Cumple | Al salir de la pantalla, las llamadas pendientes se cancelan sin excepciones huérfanas. |
