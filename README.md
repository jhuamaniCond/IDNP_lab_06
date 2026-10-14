# Laboratorio 06: Componentes UI Personalizados y Canvas en Jetpack Compose

Este proyecto corresponde a la práctica de laboratorio del curso de **Interacción Dispositivo-Nube / Desarrollo de Aplicaciones Móviles (IDNP)** en la **Universidad Nacional de San Agustín (UNSA)**.

El objetivo principal es el diseño e implementación de componentes de interfaz de usuario personalizados y reutilizables utilizando **Jetpack Compose**, **Canvas API**, gráficos 2D y animaciones en Android.

---

## 🚀 Características y Componentes Desarrollados

### 1. 🌊 `WaveComponent` (Fondo Ondulado Multinivel)
* **Descripción:** Componente gráfico decorativo para encabezados de pantalla.
* **Técnica:** Utiliza `Canvas` y dibujado de rutas (`Path`) mediante curvas Bézier cúbicas (`cubicTo`) para renderizar un diseño fluido de 3 capas superpuestas con distinta opacidad y tonos.

### 2. 📊 `CustomProgressBar` (Barra de Progreso Lineal Animada)
* **Descripción:** Barra de progreso con indicador de avance determinista (0% a 100%).
* **Técnica:** Incorpora franjas diagonales intercaladas en movimiento continuo generadas mediante `Brush.linearGradient` con `TileMode.Repeated` y animadas con `rememberInfiniteTransition`.

### 3. ⭕ `CustomCircularProgressBar` (Progreso Circular Determinista)
* **Descripción:** Indicador circular de progreso para representar porcentajes de carga de forma limpia.
* **Técnica:** Dibujado de arcos con `drawArc` sobre `Canvas`, configurando bordes redondeados (`StrokeCap.Round`) y cálculo de ángulos según el progreso.

### 4. 🌀 `CustomLoadCircleBar` (Indicador de Carga Indeterminado)
* **Descripción:** Spinner o rueda de carga animada con efecto estilo cometa.
* **Técnica:** Animación de rotación infinita a 360° combinada con un gradiente de barrido (`Brush.sweepGradient`) para crear una cola con atenuación progresiva de transparencia.

### 5. 📱 `ComponentsDesignPreview` (Pantalla de Demostración)
* **Descripción:** Pantalla principal que integra y previsualiza todos los componentes dentro de una estructura con desplazamiento vertical (`verticalScroll`).

---

## 🛠️ Tecnologías y Herramientas

* **Lenguaje:** [Kotlin](https://kotlinlang.org/)
* **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
* **Gráficos y Animaciones:** `androidx.compose.foundation.Canvas`, `rememberInfiniteTransition`, `graphicsLayer`
* **Android SDK Target:** 37
* **Android SDK Min:** 24 (Android 7.0 Nougat)
* **IDE:** Android Studio

---

## 📁 Estructura del Proyecto

```text
app/src/main/java/com/example/idnp_lab_06/
├── MainActivity.kt                      # Actividad principal con Scaffold y soporte edge-to-edge
├── ui/
│   ├── components/                      # Componentes gráficos personalizados reutilizables
│   │   ├── CustomCircularProgressBar.kt # Barra de progreso circular
│   │   ├── CustomLoadCircleBar.kt       # Indicador de carga estilo cometa
│   │   ├── CustomProgressBar.kt          # Barra de progreso lineal con franjas animadas
│   │   └── WaveComponent.kt             # Fondo con efecto de olas (Canvas + Path)
│   ├── screens/
│   │   └── ComponentsDesignPreview.kt   # Pantalla principal que integra los componentes
│   └── theme/                           # Paleta de colores, tipografía y tema de la aplicación
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
```

---

## ⚙️ Requisitos e Instalación

### Prerrequisitos
* Android Studio (versión reciente compatible con Compose y Kotlin).
* JDK 11 o superior.
* Dispositivo móvil o emulador Android con API 24 o superior.

### Pasos para ejecutar
1. **Clonar el repositorio:**
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd IDNP_lab_06
   ```
2. **Abrir el proyecto en Android Studio.**
3. **Sincronizar Gradle:** Deja que Android Studio descargue las dependencias mediante Gradle Version Catalogs (`libs.versions.toml`).
4. **Ejecutar la app:** Selecciona el módulo `app` y haz clic en **Run** (`Shift + F10`), o utiliza la vista previa de Compose (`@Preview`) dentro del editor para visualizar los componentes.

---

## 📝 Autor

* **Estudiante / Desarrollador:** 
* **Escuela Profesional de Ingeniería de Sistemas** - Universidad Nacional de San Agustín
