# SIGEB — Sistema de Gestión de Biblioteca

Proyecto académico que busca automatizar los procesos de una biblioteca: registro de usuarios y libros, búsqueda, préstamos y devoluciones, para mejorar la atención y la administración del catálogo.

## Sprint 1

En este sprint se implementaron las clases principales del sistema siguiendo el diagrama UML y se preparó una demostración por consola.

El proyecto utiliza Java y almacena los datos en memoria durante la ejecución.

## Requisitos

- JDK 17 o superior.
- Una terminal o un editor compatible con Java.
- Si se utiliza Visual Studio Code: Extension Pack for Java.

No se requieren bibliotecas externas.

## Estructura del proyecto

| Ubicación | Contenido |
|---|---|
| src/ | Clases Java y PruebaSIGEB.java |
| SIGEB_UML.png | Diagrama de clases |
| README.md | Descripción e instrucciones |
| out/ | Archivos compilados; se genera al compilar |

## Cómo ejecutar

### 1. Abrir el proyecto

Descarga y descomprime el proyecto o clona el repositorio.

Abre una terminal dentro de la carpeta `sigeb`, donde se encuentran `src` y `README.md`.

### 2. Comprobar Java

Ejecuta:

```bash
java -version
javac -version
```

Ambos comandos deben mostrar la versión instalada. Si `javac` no está disponible, comprueba que tengas instalado un JDK completo y configurado en PATH.

### 3. Compilar

Desde la raíz del proyecto:

```bash
javac -encoding UTF-8 -d out src/*.java
```

Este comando compila las clases de `src` y guarda los archivos `.class` en `out`.

Si no aparecen errores, la compilación finalizó correctamente.

### 4. Ejecutar la prueba

```bash
java -cp out PruebaSIGEB
```

La consola mostrará ejemplos de usuarios, catálogo, préstamos, penalizaciones y devoluciones.

Cada vez que se modifique el código, se debe volver a compilar antes de ejecutar.

## Ejecutar en Visual Studio Code

1. Abre la carpeta `sigeb` con **Archivo > Abrir carpeta**.
2. Comprueba que tengas instalado el JDK y Extension Pack for Java.
3. Abre `src/PruebaSIGEB.java`.
4. Pulsa **Run Java** sobre el método `main`.
5. Revisa los resultados en la terminal.

También puedes compilar y ejecutar con los comandos anteriores desde la terminal integrada.

## Demostración por consola

La clase `PruebaSIGEB` realiza estas operaciones:

1. Crea un lector y un administrador.
2. Crea un libro con autor, categoría y ejemplar.
3. Muestra la cantidad de ejemplares disponibles.
4. Crea un préstamo con un plazo de siete días.
5. Marca el ejemplar como prestado.
6. Calcula el retraso de una devolución realizada dos días después del límite.
7. Ejecuta el cálculo y la resolución de una penalización.
8. Registra la devolución y marca el ejemplar como disponible.
9. Comprueba que el préstamo esté cerrado y sin detalles pendientes.

El ejemplo utiliza fechas fijas para obtener resultados reproducibles.

Los cambios de estado del ejemplar se realizan explícitamente en la prueba. Los mensajes de penalización indican que se ejecutaron los métodos; el monto y el estado de resolución no se consultan porque todavía no tienen métodos públicos de acceso.

## Solución de problemas

| Problema | Solución |
|---|---|
| No se reconoce `javac` | Instalar un JDK y configurar su carpeta `bin` en PATH. |
| No se encuentra `src/*.java` | Ubicar la terminal en la raíz del proyecto, donde está `src`. |
| No se encuentra la clase `PruebaSIGEB` | Comprobar el nombre del archivo, compilar y ejecutar con `java -cp out PruebaSIGEB`. |
| Se indica que falta el método `main` | Ejecutar `PruebaSIGEB`, que contiene el punto de entrada de la demostración. |

## Próximo sprint

- Implementar Biblioteca y AplicacionSIGEB.
- Integrar los módulos de los tres integrantes.
- Aplicar los límites de préstamos y bloqueos por penalización.
- Coordinar automáticamente préstamos, devoluciones y estados de ejemplares.
- Implementar la interfaz gráfica.

## Diagrama UML

![Diagrama UML de SIGEB](SIGEB_UML.png)
