# Gestión Académica - Sistema de Control Estudiantil

## 📱 Descripción del Sistema

Aplicación Android desarrollada en Java que permite gestionar información académica de estudiantes, materias e inscripciones. El sistema implementa persistencia de datos utilizando Room Database sobre SQLite.

## ✨ Funcionalidades Principales

### 1. Gestión de Estudiantes
- ✅ Agregar nuevos estudiantes con información completa (nombre, apellido, carnet, email, teléfono)
- ✅ Editar información de estudiantes existentes
- ✅ Eliminar estudiantes del sistema
- ✅ Buscar estudiantes por nombre, apellido o carnet
- ✅ Visualizar listado completo de estudiantes

### 2. Gestión de Materias
- ✅ Registrar nuevas materias con código, nombre, créditos y descripción
- ✅ Modificar información de materias
- ✅ Eliminar materias del sistema
- ✅ Listar todas las materias disponibles

### 3. Gestión de Inscripciones
- ✅ Inscribir estudiantes en materias
- ✅ Registrar periodo académico (semestre y año)
- ✅ Registrar fecha de inscripción
- ✅ Visualizar inscripciones con información completa (estudiante, materia, periodo)
- ✅ Eliminar inscripciones

### 4. Sistema de Calificaciones (Base de Datos)
- ✅ Estructura preparada para registrar calificaciones por inscripción
- ✅ Soporte para diferentes tipos de evaluación (parcial, final, trabajos)
- ✅ Cálculo de porcentajes y promedios

## 🏗️ Arquitectura y Tecnologías

### Base de Datos
- **Room Database** con 4 tablas relacionadas:
  - `estudiantes`: Información personal de estudiantes
  - `materias`: Catálogo de materias disponibles
  - `inscripciones`: Relación estudiante-materia (relación N:M)
  - `calificaciones`: Notas por inscripción (relación 1:N con inscripciones)

### Componentes Utilizados
- ✅ **Room Database**: Persistencia de datos con SQLite
- ✅ **ViewBinding**: Enlace de vistas en Activities, Fragments y Adapters
- ✅ **RecyclerView**: Listas eficientes con Adapters personalizados
- ✅ **Navigation Component**: Navegación entre fragmentos
- ✅ **BottomNavigationView**: Navegación principal de la app
- ✅ **Material Toolbar**: Barra de herramientas personalizada
- ✅ **Material Design Components**: Diseño moderno y consistente

### Patrones y Buenas Prácticas
- ✅ Patrón Repository para acceso a datos
- ✅ Operaciones de base de datos en hilos secundarios
- ✅ Uso de callbacks para operaciones asíncronas
- ✅ ViewBinding para todas las vistas
- ✅ Material Design Guidelines

## 📊 Diagrama de Base de Datos
┌─────────────────┐         ┌──────────────────┐
│  Estudiantes    │         │    Materias      │
├─────────────────┤         ├──────────────────┤
│ id (PK)         │         │ id (PK)          │
│ nombre          │         │ codigo           │
│ apellido        │         │ nombre           │
│ carnet          │         │ creditos         │
│ email           │         │ descripcion      │
│ telefono        │         └──────────────────┘
└─────────────────┘                 │
│                          │
│                          │
└────────┬─────────────────┘
│
┌────────▼─────────┐
│  Inscripciones   │
├──────────────────┤
│ id (PK)          │
│ estudianteId(FK) │
│ materiaId (FK)   │
│ semestre         │
│ anio             │
│ fechaInscripcion │
└──────────────────┘
│
│ 1:N
┌────────▼─────────┐
│  Calificaciones  │
├──────────────────┤
│ id (PK)          │
│ inscripcionId(FK)│
│ tipoEvaluacion   │
│ nota             │
│ porcentaje       │
│ fecha            │
│ observaciones    │
└──────────────────┘
## 🛠️ Requisitos del Sistema

- **Android Studio**: Arctic Fox o superior
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Lenguaje**: Java
- **Gradle**: 8.0+

## 📦 Dependencias Principales
```gradle
// Room Database
implementation 'androidx.room:room-runtime:2.6.1'
annotationProcessor 'androidx.room:room-compiler:2.6.1'

// Navigation Component
implementation 'androidx.navigation:navigation-fragment:2.7.6'
implementation 'androidx.navigation:navigation-ui:2.7.6'

// Material Design
implementation 'com.google.android.material:material:1.11.0'

// RecyclerView
implementation 'androidx.recyclerview:recyclerview:1.3.2'
