# Mi Formación CTMA — Guía 09: Capacidades del Dispositivo y Seguridad

## Descripción del Incremento
En este incremento de la Guía 9 se evolucionó la aplicación integrando el soporte para evidencia fotográfica de compromisos mediante el Photo Picker nativo del sistema y la cámara, asegurando un almacenamiento local ligero en Room, permisos bajo el principio de mínimo privilegio y resiliencia de red HTTPS.

## Decisiones de Diseño y Arquitectura
- **Persistencia Ligera (`EvidenciaEntity`)**: Se almacena únicamente la URI y los metadatos de la imagen (tipo MIME, tamaño y estado `LOCAL`, `SUBIENDO`, `SINCRONIZADA`, `FALLIDA`), prohibiendo estrictamente el guardado de binarios (Bitmap o Base64) en Room.
- **Migración de Base de Datos v2 ➔ v3**: Implementación de `MIGRATION_2_3` para agregar la tabla `EvidenciaEntity` de forma segura.
- **Mínimo Privilegio (Photo Picker)**: Integración de `PickVisualMedia` para seleccionar imágenes de forma nativa sin requerir permisos amplios sobre la galería del usuario.
- **Captura Segura con `FileProvider`**: Configuración de `FileProvider` con subdirectorios delimitados (`res/xml/file_paths.xml`) generando `content://` URIs seguras en lugar de exponer `file://` URIs.
- **Permisos Contextuales y Seguridad**: Notificaciones con permiso `POST_NOTIFICATIONS` condicional (Android 13+), desactivación de tráfico en texto claro (`usesCleartextTraffic="false"`) y auditoría de Logcat.

---

## Criterios de Aceptación Cumplidos

| Criterio / Elemento | Estado | Observación |
| :--- | :---: | :--- |
| **Photo Picker Privado (CA 01)** | ✅ Cumple | Selección de imagen mediante `PickVisualMedia` sin requerir permisos de galería. |
| **Captura Segura Content URI (CA 03)** | ✅ Cumple | `FileProvider` genera content URIs delimitadas sin exponer file URIs. |
| **Persistencia de Evidencias (CA 05)** | ✅ Cumple | `EvidenciaEntity` restaura los metadatos y la vista previa al reabrir la app. |
| **Resiliencia ante Fallos (CA 06)** | ✅ Cumple | Ante errores de subida, la evidencia se conserva en estado `FALLIDA` con opción de reintentar. |
| **Control de Notificaciones (CA 07)** | ✅ Cumple | Permiso `POST_NOTIFICATIONS` solicitado de forma voluntaria y condicional en Android 13+. |
| **Tráfico HTTPS Estricto (CA 09)** | ✅ Cumple | Tráfico HTTP en texto plano desactivado (`usesCleartextTraffic="false"`). |
