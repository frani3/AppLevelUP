# LEVEL-UP GAMER — App Móvil 📱

Versión móvil de la tienda online Level-UP Gamer, desarrollada nativamente para Android con Kotlin en Android Studio. Este proyecto es la entrega para la **Evaluación Parcial 2 de Desarrollo de Aplicaciones Móviles (DSY1105)**.

El objetivo es adaptar la lógica y funcionalidades de la tienda web a una experiencia móvil fluida y funcional, cumpliendo con los estándares de desarrollo móvil, incluyendo persistencia de datos local, arquitectura MVVM y uso de recursos nativos.

👥 **Integrantes**

  * Francisca Barrera
  * Patricio Zapata

-----

### ✅ **Características Implementadas**

Este proyecto integra los aprendizajes clave de la asignatura, enfocándose en:

  * **Interfaz y Navegación Clara:** Se implementaron las vistas principales de la tienda, asegurando una navegación intuitiva y coherente entre ellas.
  * **Formularios Validados:** Todos los formularios (Registro, Login, Perfil) incluyen validaciones por campo con retroalimentación visual (íconos y mensajes de error) para una mejor experiencia de usuario.
  * **Arquitectura MVVM:** El proyecto sigue el patrón de arquitectura **Model-View-ViewModel**, separando la lógica de la interfaz para un código más mantenible y escalable.
  * **Persistencia de Datos Local:** Se utiliza una base de datos **Room** (o SharedPreferences) para gestionar localmente los datos de productos, usuarios y el carrito de compras, reemplazando el `LocalStorage` de la versión web.
  * **Acceso a Recursos Nativos:** La aplicación integra funcionalidades nativas del dispositivo para mejorar la experiencia.
      * **Almacenamiento Interno:** Gestión de la base de datos local.
      * **Notificaciones:** Alertas para eventos clave (ej. registro exitoso, compra realizada).
  * **Animaciones Funcionales:** Se han añadido animaciones sutiles para dar fluidez a las transiciones y retroalimentación visual a las acciones del usuario.
  * **Funcionalidades de la Tienda:**
      * **Descuento DUOC:** Se mantiene el descuento automático del 20% para correos `@duoc.cl`.
      * **Sistema de Puntos "Level-Up":** Visualización del nivel de usuario en su perfil.

-----

### 📲 **Vistas Implementadas (Screens)**

  * **Splash Screen:** Pantalla de bienvenida con animación.
  * **Autenticación:**
      * Inicio de Sesión (`login.kt`)
      * Registro (`registro.kt`)
  * **Tienda:**
      * Inicio / Index (`index.kt`)
      * Categorías (`categorias.kt`)
      * Carrito de Compras (`carrito.kt`)
  * **Usuario:**
      * Perfil de Usuario (`perfil.kt`)

-----

### 🗂️ **Estructura del Proyecto**

El proyecto está organizado siguiendo las mejores prácticas de desarrollo en Android, bajo el patrón MVVM:

```
/app
  /src
    /main
      /java
        /com/levelupgamer
          /data         # Model: Repositorios, Base de Datos (Room), API Client (si aplica)
          /ui           # View: Actividades, Fragmentos y Composables
            /auth
            /store
            /profile
          /viewmodel    # ViewModels para cada vista
          /util         # Clases de ayuda, constantes, etc.
```

-----

### 🛠️ **Herramientas y Colaboración**

  * **Control de Versiones:** El código fuente está gestionado en **GitHub**, utilizando un flujo de ramas para el desarrollo de funcionalidades (`main`, `develop`, `feature/...`).
  * **Planificación:** La gestión de tareas y la planificación del sprint se realizan en **Trello**, asegurando una distribución clara del trabajo en equipo.

-----

### 🚀 **Cómo Ejecutar el Proyecto**

1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/tu-usuario/proyecto-levelup-app.git
    ```
2.  **Abrir en Android Studio:**
      * Abre Android Studio.
      * Selecciona `Open an existing project`.
      * Navega a la carpeta clonada y ábrela.
3.  **Ejecutar la aplicación:**
      * Espera a que Gradle sincronice el proyecto.
      * Selecciona un emulador o conecta un dispositivo físico.
      * Presiona el botón `Run 'app'`.

-----

### 🧪 **Troubleshooting**

  * **Los datos no cargan al iniciar:** Ve a la configuración de la app en el emulador/dispositivo y selecciona "Limpiar caché" y "Limpiar datos". Esto forzará a la app a sembrar los datos iniciales de nuevo.
  * **La app crashea al iniciar:** Asegúrate de que todas las dependencias de Gradle se hayan descargado correctamente y realiza un `Build > Clean Project` y `Build > Rebuild Project`.
