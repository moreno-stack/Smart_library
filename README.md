# SmartLibrary – Taller Bloque 5
**De clases aisladas a objetos que colaboran**
Integrantes: _(completar)_

---

## Actividad 1 – Diagnóstico de relaciones

| Elementos | ¿Qué relación existe en el problema? | ¿Existencia independiente? | Decisión inicial |
|---|---|---|---|
| Estudiante – Préstamo | Un estudiante realiza préstamos; ambos se conocen y colaboran. | Sí. El estudiante existe sin préstamos, y un préstamo no es "parte" del estudiante. | Asociación |
| Libro – Ejemplar | Un libro (obra) tiene varios ejemplares físicos (R10). | Parcial: el ejemplar solo tiene sentido como copia *de un* libro, pero es un objeto físico con identidad propia. | Agregación (supuesto abajo) |
| Préstamo – Renovación | El préstamo registra su historial de renovaciones (R11). | No. Una renovación solo describe un cambio de fecha de *ese* préstamo. | Composición |
| Usuario – Estudiante | El estudiante es un usuario de la biblioteca (R12). | No aplica (es una relación de tipo, no de parte). | Generalización |
| Usuario – Bibliotecario | El bibliotecario es un usuario de la biblioteca (R12). | No aplica. | Generalización |

**Preguntas de discusión**
1. **No.** Que dos clases se relacionen solo indica que colaboran; "ser parte de" exige una relación conceptual todo–parte (Estudiante–Préstamo son asociados, no partes).
2. Usamos el texto de los requisitos: R10 (un ejemplar ↔ un único libro), R11 (las renovaciones se registran *dentro* del préstamo y no tienen vida propia) y R12 (usuarios que comparten datos y tienen información particular).
3. **Libro–Ejemplar**: por los nombres parece "contiene", pero el símbolo correcto depende de la regla de ciclo de vida, que el enunciado no fija; por eso hay que declarar un supuesto.

---

## Actividad 2 – Asociación, agregación y composición

Las relaciones y sus justificaciones se describen en la siguiente tabla.

| Relación | Tipo seleccionado | Justificación basada en el dominio |
|---|---|---|
| Estudiante – Prestamo | **Asociación** (1 – 0..*) | Un estudiante puede no tener préstamos y seguir existiendo; un préstamo finalizado tampoco "pertenece" al estudiante como parte. Solo necesitan conocerse. El código mantiene la referencia desde `Prestamo` hacia `Estudiante`. |
| Prestamo – Ejemplar | **Asociación** (0..* – 1) | El ejemplar existe antes y después del préstamo. A lo largo del tiempo un ejemplar tiene muchos préstamos; en un instante dado solo uno activo (regla de negocio a validar en Préstamos, fuera del alcance del taller). Un préstamo apunta a un único ejemplar. |
| Libro – Ejemplar | **Agregación** (1 – 1..*) | Hay un todo–parte conceptual (R10), pero **supuesto:** el ejemplar es un bien físico con código de inventario propio y su historial de préstamos debe conservarse aunque el registro bibliográfico se reorganice o fusione. Por eso la parte tiene ciclo de vida independiente. Se usa 1..* porque un libro del catálogo se registra con al menos un ejemplar. |
| Prestamo – Renovacion | **Composición** (1 – 0..*) | La renovación solo existe como entrada del historial de *un* préstamo (R11); si el préstamo deja de existir, sus renovaciones pierden significado. Solo Prestamo las crea. En código se refleja así: la lista es privada, `renovar` crea la `Renovacion` internamente y `getRenovaciones()` devuelve una vista no modificable. |

**Supuesto alterno (por si el profesor lo pregunta):** si se declara que al eliminar un libro del catálogo se eliminan sus ejemplares, Libro–Ejemplar pasaría a **composición**. Cambia el supuesto de ciclo de vida, por lo tanto cambia el símbolo.

---

## Actividad 3 – Herencia: ¿es-un o solo se parecen?

1. **¿Estudiante es un Usuario?** Sí. En SmartLibrary el estudiante es una persona que usa la biblioteca y puede usarse donde se espera un usuario (préstamos, identificación, contacto) sin romper el significado.
2. **¿Bibliotecario es un Usuario?** Sí, por R12: el enunciado los trata explícitamente como usuarios del sistema con identificación, nombre y correo. Es un usuario con un rol distinto (gestiona el sistema).
3. **¿Bastan los atributos repetidos?** No. La repetición de `identificacion`, `nombre` y `correo` es solo la *pista*. La justificación es que R12 nombra a "usuario de biblioteca" como un concepto real del sistema y ambas clases cumplen la relación «es-un». Si solo compartieran atributos, bastaría con composición o con un objeto de datos.
4. **Ejemplo de superclase débil:** `Ejemplar` y `Prestamo` podrían compartir `fecha` y `codigo`; crear `Registro` como superclase solo para evitar repetirlos no expresa nada del dominio, un préstamo no *es un* ejemplar, y acoplaría dos conceptos que cambian por razones distintas.
5. **Decisión:** se acepta la generalización. `Usuario` es **abstracta** (no se instancia un "usuario genérico") y Estudiante y Bibliotecario la especializan.
   **Limitación reconocida:** si una misma persona pudiera ser estudiante *y* bibliotecario a la vez (p. ej., un estudiante auxiliar), la herencia sería rígida y habría que modelar roles. Supuesto del taller: las dos categorías son excluyentes.

---

## Actividad 4 – Interfaz `Notificable`

**¿Quién la implementa?** `Estudiante`, porque R13 dice que *algunos* usuarios reciben notificaciones y el caso del taller (aviso de renovación) es del estudiante. `Bibliotecario` **no** la implementa (supuesto documentado). Tampoco `Usuario`: si la superclase la implementara, *todos* los usuarios quedarían obligados, contradiciendo R13.

| Pregunta | Respuesta |
|---|---|
| ¿Qué promete una clase que implementa Notificable? | Que tiene la operación `notificar(String mensaje)` y que puede invocarse con un mensaje de texto para comunicarlo a su destinatario. |
| ¿La interfaz define cómo se envía el mensaje? | No. No define canal (consola, correo, SMS), formato, reintentos ni si devuelve confirmación. Cada clase decide (aquí, `Estudiante` lo simula en consola). |
| ¿Puede una clase heredar de Usuario e implementar Notificable? | Sí. Java permite una sola superclase pero varias interfaces: `Estudiante extends Usuario implements Notificable`. |

**Qué garantiza y qué NO especifica el contrato:** garantiza la existencia de la capacidad `notificar(String)`. No especifica la implementación, el medio, el momento, ni qué pasa si el mensaje es nulo o la entrega falla.

**Interfaz ≠ superclase:** una superclase responde "¿qué *es* esto?" y aporta estado y comportamiento heredado (relación «es-un»). Una interfaz responde "¿qué *puede hacer* esto?" y es un contrato sin estado. Clases no relacionadas por herencia pueden cumplir la misma interfaz, y una clase puede cumplir varias.

---

## Resumen de relaciones del modelo

Las relaciones del modelo quedan documentadas en las actividades 2, 3 y 4. `Usuario` se especializa en `Estudiante` y `Bibliotecario`; `Estudiante` implementa `Notificable`; `Libro` agrupa ejemplares; y cada `Prestamo` conserva sus renovaciones. `Reserva` se considera en la vista funcional (R14), pero no se implementa como clase Java en este taller.

---

## Actividad 5 – Implementación Java
Clases Java en la raíz del proyecto: `Notificable`, `Usuario`, `Estudiante`, `Bibliotecario`, `Libro`, `Ejemplar`, `Renovacion`, `Prestamo` y `Main`.

Correspondencia UML → código:
- **Herencia:** `Estudiante extends Usuario`, `Bibliotecario extends Usuario`.
- **Realización:** `Estudiante implements Notificable`.
- **Asociación:** `Prestamo` tiene referencias a `Estudiante` y `Ejemplar`.
- **Agregación:** `Libro` guarda `List<Ejemplar>` que se crean fuera y se agregan con `agregarEjemplar`.
- **Composición:** `Prestamo` crea y guarda las `Renovacion` (lista privada, vista no modificable).
- **Regla protegida:** `Prestamo.renovar` lanza `IllegalArgumentException` si la nueva fecha no es estrictamente posterior a la vigente; en ese caso **no** modifica la fecha ni agrega renovaciones.

**Compilar y ejecutar:**
```bash
javac -d out *.java
java -cp out Main
```

**Evidencia de ejecución** (archivo `evidencia_ejecucion.txt`):
```
=== PRUEBA 1: renovación válida ===
[NOTIFICACIÓN a Ana Torres <ana@univ.edu.co>] Su préstamo fue renovado.
Nueva fecha prevista: 2026-10-15
Cantidad de renovaciones: 1
Fecha anterior conservada: 2026-10-08

=== PRUEBA 2: renovación inválida (fecha anterior a la vigente) ===
Rechazada correctamente: La nueva fecha (2026-10-10) debe ser posterior a la fecha prevista vigente (2026-10-15).
Fecha prevista sigue en: 2026-10-15
Cantidad de renovaciones sigue en: 1

=== PRUEBA 3: renovación inválida (fecha igual) ===
Rechazada correctamente: La nueva fecha (2026-10-15) debe ser posterior a la fecha prevista vigente (2026-10-15).
```
**Qué debe ocurrir ante una renovación inválida:** se rechaza con excepción, el préstamo queda exactamente como estaba (misma fecha, mismo número de renovaciones) y no se envía notificación de éxito.

---

## Actividad 6 – Vista funcional (componentes)

La siguiente tabla resume los componentes funcionales, sus clases y la información que intercambian.

| Componente | Clases relacionadas | ¿De qué otro componente necesita información? ¿Para qué? |
|---|---|---|
| Gestión de Usuarios | `Usuario`, `Estudiante`, `Bibliotecario`, `Notificable` | De ninguno para su función básica: es fuente de datos de personas. |
| Gestión de Catálogo | `Libro`, `Ejemplar` | De ninguno para su función básica: es fuente de datos de obras y copias. |
| Gestión de Préstamos | `Prestamo`, `Renovacion` | **Usuarios**: saber quién presta y notificarle. **Catálogo**: identificar el ejemplar prestado. |
| Gestión de Reservas | `Reserva` | **Usuarios**: quién reserva y a quién avisar. **Catálogo**: qué libro se reserva. **Préstamos**: si hay ejemplares disponibles o prestados. |

`Reserva` no se modeló en detalle porque el taller no lo pide; se ubica aquí por el enunciado (R14). No se incluyen microservicios, APIs, bases de datos ni despliegue.

---

## Conclusión (≤150 palabras)
Cuando dejamos de mirar clases aisladas, el diseño deja de ser una lista de atributos y pasa a expresar reglas del dominio: quién conoce a quién, qué depende del ciclo de vida de qué y qué puede hacer cada objeto. Elegir entre asociación, agregación y composición obliga a citar un requisito; la herencia deja de ser un atajo para reutilizar atributos y exige una relación «es-un»; y la interfaz `Notificable` separa lo que un objeto promete de cómo lo cumple. Además, las multiplicidades y la regla de renovación en `Prestamo` hacen que el modelo sea verificable en código, y los componentes permiten leer el sistema por funciones y no solo por clases.

---

## Lista de verificación
- [x] No usé composición solo porque una clase contiene otra (Libro–Ejemplar es agregación y se justificó).
- [x] Relaciones justificadas desde el dominio.
- [x] La herencia expresa «es-un» y se reconoce su limitación.
- [x] Diferencia herencia vs. interfaz explicada.
- [x] Multiplicidades coherentes con R10–R12.
- [x] El código refleja el UML.
- [x] `Prestamo` protege la regla de renovación.
- [x] La vista de componentes se organiza por funciones y no como microservicios.
- [x] No se adelantó GRASP, SOLID ni patrones.
