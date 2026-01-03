# 🏨 Sistema de Gestión Hotelera - INSTRUCCIONES DE USO

## 🚀 Cómo Ejecutar el Proyecto

### Opción 1: Desde Eclipse (Recomendado)

1. **Abrir el proyecto** en Eclipse
2. **Verificar el Build Path**:
   - Click derecho en el proyecto → Properties → Java Build Path
   - En la pestaña "Libraries", asegúrate de que `sqlite-jdbc-3.51.1.0 (1).jar` esté agregado
   - Si no está, click en "Add JARs" y selecciona el archivo JAR de la carpeta `src/BD/`

3. **Ejecutar**:
   - Navega a `src/BD/INTERFAZ.java`
   - Click derecho → Run As → Java Application
   - ¡El sistema se abrirá automáticamente!

### Opción 2: Desde Línea de Comandos

```bash
# Navegar al directorio del proyecto
cd C:\Users\Legion\Desktop\ProyectoMartes

# Compilar (si tienes Java en PATH)
javac -cp "src/BD/sqlite-jdbc-3.51.1.0 (1).jar" -d bin src/BD/*.java

# Ejecutar
java -cp "bin;src/BD/sqlite-jdbc-3.51.1.0 (1).jar" BD.INTERFAZ
```

---

## 📱 Navegación por el Sistema

### 1️⃣ Dashboard (Inicio)
- **Qué verás**: Estadísticas generales del hotel
- **Acciones**:
  - Ver total de habitaciones
  - Ver porcentaje de ocupación actual
  - Ver total de huéspedes
  - Click en "🔄 Actualizar" para refrescar datos

### 2️⃣ Habitaciones
- **Qué verás**: Tarjetas con todas las habitaciones
- **Acciones**:
  - **Filtrar**: Click en "Todas", "Disponibles" u "Ocupadas"
  - **Ver detalles**: Click en cualquier habitación
  - **Actualizar**: Click en "🔄 Actualizar"

### 3️⃣ Huéspedes
- **Qué verás**: Tabla con todos los huéspedes registrados
- **Acciones**:
  - **Agregar nuevo**: Click en "➕ Agregar Huésped"
    - Llenar formulario (Nombre y Documento son obligatorios)
    - Click en "Guardar"
  - **Editar**: Seleccionar fila + Click en "✏️ Editar"
  - **Eliminar**: Seleccionar fila + Click en "🗑️ Eliminar" + Confirmar
  - **Actualizar**: Click en "🔄 Actualizar"

### 4️⃣ Recepción
- **Qué verás**: Panel con estadísticas rápidas
- **Acciones**:
  - Ver habitaciones disponibles/ocupadas
  - Ver huéspedes actuales
  - Ver reservas activas
  - Acceso rápido a acciones comunes

### 5️⃣ Reservas (NUEVO)
- **Qué verás**: Tabla con todas las reservas
- **Acciones**:
  - **Nueva reserva**: Click en "➕ Nueva Reserva"
    - Llenar datos del huésped
    - Seleccionar habitación
    - Indicar fechas de entrada/salida
    - Especificar precio
    - Click en "Guardar"
  - **Filtrar**: Usar el dropdown de estado
  - **Actualizar**: Click en "🔄 Actualizar"

---

## 🎨 Características Principales

### ✨ Interfaz Moderna
- **Topbar** con búsqueda, reloj en tiempo real y notificaciones
- **Sidebar** con navegación intuitiva y diseño profesional
- **Colores** profesionales y consistentes
- **Animaciones** suaves al pasar el mouse

### 🗄️ Base de Datos
- **Conexión automática** a `BDHotel.db`
- **Creación automática** de tablas si no existen
- **Datos persistentes** - todo se guarda automáticamente

### 🔧 Funcionalidades
- ✅ Ver estadísticas en tiempo real
- ✅ Agregar, editar y eliminar huéspedes
- ✅ Filtrar habitaciones por estado
- ✅ Crear y gestionar reservas
- ✅ Actualizar datos con un click

---

## ⚠️ Notas Importantes

1. **Base de Datos**: El archivo `BDHotel.db` debe estar en `src/BD/`
2. **JAR de SQLite**: El archivo `sqlite-jdbc-3.51.1.0 (1).jar` debe estar en el Build Path
3. **Primera ejecución**: Si no hay datos, las estadísticas mostrarán 0
4. **Errores**: Si hay problemas de conexión, verifica que el JAR esté correctamente configurado

---

## 🐛 Solución de Problemas

### Error: "ClassNotFoundException: org.sqlite.JDBC"
**Solución**: Agregar el JAR de SQLite al Build Path
1. Click derecho en el proyecto → Properties
2. Java Build Path → Libraries → Add JARs
3. Seleccionar `sqlite-jdbc-3.51.1.0 (1).jar`

### Error: "No se puede conectar a la base de datos"
**Solución**: Verificar que `BDHotel.db` existe en `src/BD/`

### La ventana no se ve completa
**Solución**: La ventana es de 1400x850px. Ajusta la resolución de tu pantalla o maximiza la ventana

---

## 📞 Archivos Modificados/Creados

### ✅ Archivos NUEVOS:
- `DatabaseManager.java` - Gestor de base de datos
- `Reserva.java` - Modelo de reservas
- `PanelReservas.java` - Panel de gestión de reservas

### ✏️ Archivos MODIFICADOS:
- `INTERFAZ.java` - Interfaz principal modernizada
- `PanelDashboard.java` - Dashboard con datos reales
- `PanelHabitaciones.java` - Filtros funcionales
- `PanelHuespedes.java` - CRUD completo
- `PanelRecepcion.java` - Estadísticas reales

---

## 🎯 ¡Listo para Usar!

El sistema está completamente funcional y listo para gestionar tu hotel. Todas las funcionalidades están conectadas a la base de datos y los datos se guardan automáticamente.

**¡Disfruta tu nuevo sistema de gestión hotelera! 🏨✨**
