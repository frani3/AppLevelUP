# Checklist de integración API LevelUp

Sigue esta lista para documentar el progreso de la integración en la app Android. Marca cada casilla a medida que completes cada paso.

## Configuración de red y autenticación
- [x] Configurar Retrofit con OkHttp, GsonConverterFactory, timeout y logging interceptor.
- [x] Añadir interceptor personalizado para enviar `Authorization: Bearer <token>` cuando haya sesión.
- [x] Definir un cliente de red reutilizable en `data/network`.

## Endpoints móviles
- [x] Crear interfaces Retrofit para login y register.
- [x] Crear interfaz para `users/me` y level-up stats.
- [x] Crear interfaces para catálogo (`products`, `products/{codigo}`, `categories`).
- [x] Crear interfaces para regiones y direcciones del usuario (CRUD).

## Modelos y mapeos
- [x] Crear DTOs Kotlin que reflejen las respuestas JSON del backend.
- [x] Definir modelos de dominio en `domain/model/levelup`.
- [x] Crear entidades de Room para productos, categorías, regiones y direcciones.
- [x] Generar mappers entre DTO, dominio y entidades locales.

## Persistencia local (Room)
- [x] Crear DAOs para productos, categorías, regiones y direcciones.
- [x] Definir la base de datos Room y su proveedor.
- [x] Añadir lógica de expiración local al cache para saber cuándo refrescar datos.

## Repositorios y Single Source of Truth
- [x] Implementar repositorios que combinen Room y Retrofit respetando el patrón Single Source of Truth.
- [x] Manejar errores y estados con `LevelUpResult`/`LevelUpResource`.
- [x] Implementar almacenamiento del token/session para reintentos autorizados.

## Capa de dominio
- [x] Crear UseCases para login, register, fetchProducts, fetchProductDetail, fetchCategories, fetchRegiones, fetchUserProfile, fetchLevelUpStats, address CRUD.

## Presentación (MVVM)
- [x] Crear ViewModels que expongan `StateFlow`/`SharedFlow` para cada operación relevante.
- [x] Inyectar los UseCases en los ViewModels y manejar estados de carga/éxito/error.

## Documentación y pruebas
- [x] Verificar que `infoAPI.md` y `README.md` explican la misma versión de los endpoints y cómo usar el token.
- [x] Añadir notas de cómo probar cada caso (login, fetch de catálogo, CRUD de direcciones, etc.).
