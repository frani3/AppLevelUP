# LEVEL-UP GAMER — App Móvil 📱

## Descripción del Proyecto
Aplicación nativa Android desarrollada en Kotlin con Jetpack Compose que traslada la experiencia de la tienda Level-UP Gamer a un entorno móvil. Corresponde a la entrega de la Evaluación Parcial 2 de Desarrollo de Aplicaciones Móviles (DSY1105) y tiene como objetivo adaptar la plataforma web a un flujo nativo con arquitectura MVVM, persistencia local y uso de capacidades del dispositivo.

## Integrantes
- Francisca Barrera
- Patricio Zapata

## Características Implementadas (Sección Crítica)
La app cubre los criterios de la rúbrica combinando interfaz Compose, casos de uso y persistencia local:

- **Interfaz y navegación clara:** `AppNavGraph.kt` orquesta Navigation Compose con transiciones personalizadas entre Splash, autenticación, catálogo y perfil. La barra inferior y los accesos rápidos en `LandingPageScreen.kt` mantienen la navegación consistente.
- **Formularios validados:** `LoginScreen.kt`, `RegistroScreen.kt` y `EditProfileScreen.kt` consumen ViewModels (`LoginViewModel`, `RegistroViewModel`, `EditProfileViewModel`) que validan cada campo, muestran mensajes de error contextual y ofrecen feedback visual (íconos de estado, snackbars, loaders).
- **Arquitectura MVVM:** La carpeta `data/` contiene Room, DataStore y repositorios; `domain/` agrupa modelos y casos de uso; `presentation/` concentra screens Compose y ViewModels. Los casos de uso (p. ej. `ValidateUserLoginUseCase`, `CreateOrderUseCase`) encapsulan la lógica entre capa de datos y UI.
- **Persistencia de datos local:** `AppDatabase.kt` expone Room para productos, usuarios, carrito y métodos de pago con datos iniciales en `LocalSeedData.kt`. `SessionPreferencesDataSource`, `FavoritePreferencesDataSource` y `NotificationPreferencesDataSource` usan DataStore para recordar sesión, favoritos y configuración de notificaciones.
- **Acceso a recursos nativos:**
	- Notificaciones: el manifiesto pide `POST_NOTIFICATIONS` y `NotificationsViewModel` sincroniza preferencias locales para campañas y alertas en `NotificationsScreen.kt`.
	- Cámara: `ProfileScreen.kt` integra `FileProvider` y `rememberLauncherForActivityResult` para capturar o actualizar la foto de perfil con permiso `CAMERA`.
	- Búsqueda por voz: `SearchScreen.kt` habilita reconocimiento de voz solicitando `RECORD_AUDIO` y disparando `RecognizerIntent`.
- **Animaciones funcionales:** Las transiciones declarativas (`forwardEnter`, `modalEnter`) en `AppNavGraph.kt` suavizan cambios de pantalla, mientras `LandingViewModel` anima el carrusel de promociones con auto-scroll.
- **Funcionalidades de la tienda:**
	- Descuento DUOC: `RegistroViewModel` detecta correos `@duoc.cl` y `UserRepositoryImpl` marca el beneficio vitalicio (`hasLifetimeDiscount`).
	- Sistema de puntos/XP: `NotificationFeedViewModel` publica recompensas de fidelización (XP) que se muestran en la bandeja de notificaciones.
	- Carrito persistente: `CartRepositoryImpl` combina Room y casos de uso de carrito para conservar ítems por usuario.
	- Favoritos sincronizados: `FavoritesRepositoryImpl` mantiene IDs favoritos en DataStore por sesión (anónimo, correo o id).
	- Checkout completo: `CheckoutScreen.kt`, `AddPaymentMethodScreen.kt` y `AddAddressScreen.kt` trabajan con los repositorios de pagos y direcciones para simular órdenes, cupones y envíos.

## Vistas Implementadas (Screens)
### Autenticación
- `SplashScreen`, `LoginScreen`, `RegistroScreen`, `ForgotPasswordScreen`.

### Tienda y catálogo
- `LandingPageScreen`, `CatalogScreen`, `CategoryScreen`, `ProductListScreen`, `ProductDetailScreen`, `SearchScreen`, `FavoritesScreen`, `AddProductScreen`.

### Carrito y checkout
- `CartScreen`, `CheckoutScreen`, `OrderSuccessScreen`.

### Usuario y perfil
- `ProfileScreen`, `EditProfileScreen`, `AccountScreen`, `NotificationsScreen`, `NotificationSettingsScreen`.

### Gestión de datos personales
- `PaymentMethodsScreen`, `AddPaymentMethodScreen`, `AddressScreen`, `AddAddressScreen`.

## Estructura del Proyecto
```text
app/src/main/java/com/applevelup/levepupgamerapp
├─ data/              # Room (dao/entity), DataStore y repositorios concretos
│  ├─ local/          # Base de datos y seeds iniciales
│  ├─ prefs/          # DataStore para sesión, favoritos y notificaciones
│  └─ repository/     # Implementaciones que combinan fuentes locales
├─ domain/            # Modelos, repositorios contractuales y casos de uso
├─ presentation/      # UI Compose, navegación y ViewModels MVVM
├─ utils/             # Utilidades (formato RUN, seguridad, etc.)
├─ LevelUpApplication.kt   # Inicializa Room y expone DataStore via singleton
└─ MainActivity.kt         # Host de la composición y del grafo de navegación
```

## Herramientas y Colaboración
- **Control de versiones:** GitHub (ramas, issues y pull requests para rastrear trabajo).
- **Planificación:** Trello para tableros de sprint, backlog y seguimiento de la entrega DSY1105.

## Cómo Ejecutar el Proyecto
1. Clonar el repositorio:

	 ```powershell
	 git clone https://github.com/frani3/AppLevelUP.git
	 cd AppLevelUP
	 ```
2. Abrir la carpeta raíz (`AppLevelUP`) en Android Studio.
3. Permitir que Gradle sincronice dependencias usando el wrapper incluido (`gradlew`).
4. Ejecutar desde Android Studio en un emulador Pixel API 33+ o dispositivo físico con modo desarrollador.

## Integración LevelUp API

- La carpeta `data/network` contiene la configuración de Retrofit/OkHttp (incluye interceptores de autorización y logging) más los DTO necesarios para login, catálogo, estadísticas, regiones y direcciones.
- `data/local/levelup` y `data/local/dao` almacenan caches de productos, categorías, regiones y direcciones con metadatos de expiración y DAOs observables para MVVM.
- `domain/repository/levelup`, `domain/usecase/levelup` y `presentation/viewmodel/levelup` encapsulan los contratos, casos de uso y ViewModels reutilizables, mientras `LevelUpDependencyContainer` ofrece instancias listas para inyección.
- Revisa `infoAPI.md` para entender los endpoints, y `API_INTEGRATION_CHECKLIST.md` para seguir el estado de la integración móvil.
- **Pruebas sugeridas:** iniciar sesión, refrescar catálogo (`products/categories/regions`), consultar `users/me`, crear/editar/eliminar direcciones y verificar `levelup` stats para el usuario autenticado.

## Troubleshooting
- Datos que no se actualizan: en el emulador ve a Ajustes > Apps > Level-UP Gamer > Storage y selecciona “Clear cache” y “Clear data”.
- Crashes al iniciar: usa `Build > Clean Project` seguido de `Build > Rebuild Project` para regenerar clases Kapt/Room.
- Errores de sincronización Gradle: valida la versión de Android Gradle Plugin y del JDK en `File > Settings > Build Tools > Gradle`.