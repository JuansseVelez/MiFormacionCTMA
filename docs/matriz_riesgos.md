# Matriz de Riesgos y Controles — Mi Formación CTMA (Guía 09)

| ID | Riesgo Identificado | Impacto | Control Implementado | Estado |
|---|---|---|---|:---:|
| **R-01** | Fuga o exposición de tokens de autorización en consola/Logcat | Alto | `AuthInterceptor` no registra valores de cabecera `Authorization` en texto plano. | ✅ Verificado |
| **R-02** | Almacenamiento binario masivo de imágenes (Bitmap/Base64) en Room | Crítico | `EvidenciaEntity` guarda únicamente la URI String y metadatos sin binarios. | ✅ Verificado |
| **R-03** | Intercepción de tráfico HTTP en red por falta de cifrado | Alto | Desactivación de `usesCleartextTraffic` y uso exclusivo de HTTPS en producción. | ✅ Verificado |
| **R-04** | Solicitud excesiva/intrusiva de permisos sobre la galería completa | Medio | Implementación de `PickVisualMedia` (Photo Picker) sin pedir permisos broad. | ✅ Verificado |
| **R-05** | Exposición de archivos temporales mediante `file://` URIs | Alto | Uso estricto de `FileProvider` con `content://` URIs seguras y permisos delimitados. | ✅ Verificado |
| **R-06** | Pérdida de evidencia local ante fallos de sincronización de red | Alto | Gestión de estados (`LOCAL`, `FALLIDA`) conservando la URI local en Room. | ✅ Verificado |
| **R-07** | Solicitud inoportuna de permisos de notificaciones sin contexto | Bajo | Petición condicional de `POST_NOTIFICATIONS` (Android 13+) únicamente tras acción del usuario. | ✅ Verificado |
| **R-08** | Inyección de archivos maliciosos mediante tipo MIME o tamaño excesivo | Medio | Validaciones previas de tipo MIME (solo imágenes) y límites de tamaño en cliente. | ✅ Verificado |
