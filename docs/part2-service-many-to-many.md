# Part 2 — Adopción Many-to-Many (Shared Custody): Estado de la capa Service

## 1. Propósito del documento

Este documento resume el estado de la parte correspondiente a la capa `Service` del ejercicio **Part 2** y las condiciones necesarias para la integración del resto de módulos. Adicionalmente, sirve como documentación del proyecto.

## 2. Contexto

El ejercicio evoluciona la adopción de perros desde una relación uno-a-uno (`adoptDog` de la Parte 1) hacia una relación **muchos-a-muchos con custodia compartida**. La lógica de negocio reside en `Service.java`; la interacción de usuario, en `Controller.java` y `Utils.java`.

Reglas de negocio contempladas:

- Una persona puede tener como máximo **3 perros activos**.
- Un perro puede tener como máximo **2 dueños activos**.
- No se permite un par (persona, perro) activo duplicado.
- Solo pueden adoptar personas adultas (`age >= 18`).
- El importe de adopción debe ser `>= 0`.
- `returnDog` finaliza únicamente el vínculo de esa persona con ese perro.
- `DogAdoption` se registra como entidad de unión con estado `ACTIVE` / `RETURNED`; el historial se conserva.

## 3. Alcance completado (capa Service)

Se han implementado o refactorizado los siguientes métodos en
`src/main/java/org/example/service/Service.java`:

| Método | Descripción |
|---|---|
| `adoptDog(Person, Dog, int, Repository)` | Valida la cadena de 7 reglas (existencia, edad, fee, duplicado activo, límite de perros por persona, límite de dueños por perro), crea el registro `ACTIVE` y mantiene ambos lados de la relación. |
| `returnDog(Person, Dog, Repository)` | Localiza el registro `ACTIVE` del par exacto, retira el vínculo de ambas colecciones y marca el registro como `RETURNED`, añadiendo `" - RETURNED"` al contrato. El registro permanece en el historial. |
| `getDogsByOwner(Person, Repository)` | Devuelve los perros activos de una persona. |
| `getOwnersByDog(Dog, Repository)` | Devuelve los dueños activos de un perro. |
| `deletePerson(...)` | Cascada: retira la persona de cada perro y marca como `RETURNED` las adopciones activas relacionadas, sin eliminar registros. |
| `deleteDog(...)` | Cascada simétrica sobre cada dueño. |

Todos los métodos respetan el patrón `ResultOperation` empleado en la Parte 1 y en `buyCar`.

## 4. Modelo provisional (stubs)

Con el fin de permitir el desarrollo y las pruebas de la capa `Service` antes de disponer del modelo definitivo, se han creado **versiones provisionales** de las clases de modelo. Se ha optado por un enfoque *superset*: se conserva la interfaz anterior y se añade la nueva.

| Clase | Se conserva | Se añade |
|---|---|---|
| `Person` | miembro `dog`, `getDog()` / `setDog()` | `List<Dog> dogs` con `getDogs()` / `setDogs()` |
| `Dog` | miembro `owner`, `getOwner()` / `setOwner()` | `List<Person> owners` con `getOwners()` / `setOwners()` |
| `DogAdoption` | constructor de 5 argumentos (asigna `status = "ACTIVE"`) | campo `status`, constructor de 6 argumentos, `getStatus()` / `setStatus()` |

Las versiones provisionales están diseñadas para ser **sustituidas** por las definitivas. Su carácter *superset* garantiza que el proyecto compile y que los flujos previos (CRUD, `buyCar`, adopción 1:1) continúen operativos durante la transición.

Ubicación: `src/main/java/org/example/model/` (`Person.java`, `Dog.java`, `DogAdoption.java`).

## 5. Verificación

- Resultado de la batería de pruebas: **66 pruebas, 0 fallos, 0 errores**.
- `AdoptDogServiceTest`: **32 pruebas** que cubren casos de éxito, las 7 validaciones, límites (3 perros / 2 dueños), devolución parcial, re-adopción tras devolución, cascada en borrado, regresión de `buyCar` y seguridad frente a recursión en `toString()`.
- Comando de ejecución: `mvnw.cmd test` (requiere Java 21; no requiere Maven instalado).

## 6. Estado en control de versiones

- Rama: `service-many-to-many-stubs`.
- Contiene la lógica de `Service`, sus pruebas y las versiones provisionales de modelo. Los stubs se conciben como un estado temporal de trabajo, pendiente de sustitución por el modelo definitivo.

## 7. Requisitos para la integración

Dependencias necesarias para que la integración resulte inmediata:

- Modelo definitivo de `Person`, `Dog` y `DogAdoption` conforme al contrato del apartado 8.
- (Opcional) Ayudas en `Repository`: `getDogAdoptionsByPersonId(String)`, `getDogAdoptionsByDogId(String)`, además de `clear()` / `isEmpty()` para escenarios de prueba y recarga.
- `Controller` / `Utils`: menú ampliado y selección por ID.
- `DataSeeder`: al menos un perro con dos dueños activos y una persona con dos perros activos.

## 8. Contrato de API esperado (modelo y repositorio)

Se recomienda que el modelo definitivo exponga, como mínimo, las siguientes firmas, dado que la capa `Service` y sus pruebas dependen de ellas:

| Tipo | Miembro esperado |
|---|---|
| `Person` | `List<Dog> getDogs()` |
| `Dog` | `List<Person> getOwners()` |
| `DogAdoption` | `String getStatus()` / `void setStatus(String)` |
| `DogAdoption` | constructor `(Person, Dog, Date, int, String, String)` |
| `Repository` | `ArrayList<DogAdoption> getAllDogAdoptions()` |

Valores de estado: se emplean los literales `"ACTIVE"` y `"RETURNED"` del enunciado.

## 9. Integración del modelo definitivo

1. Sustituir los tres ficheros de modelo provisionales por los definitivos.
2. Actualizar `Controller`, `Utils` y `DataSeeder` al nuevo contrato.
3. Ejecutar `mvnw.cmd test`.
4. Si se respeta el contrato del apartado 8, `Service.java` y `AdoptDogServiceTest.java` no requieren cambios.

## 10. Recomendaciones y sugerencias

Las siguientes observaciones se plantean como propuestas abiertas, sujetas a la planificación que el equipo considere oportuna:

- Mantener los nombres de método indicados en el contrato del apartado 8 facilita que la integración sea directa y evita ajustes adicionales.
- Resulta conveniente que las mutaciones de las listas de relación se realicen exclusivamente desde la capa `Service`, conforme al requisito de estilo del enunciado.
- Se sugiere conservar el historial de `DogAdoption` (marcar como `RETURNED` en lugar de eliminar registros), lo que da soporte a la trazabilidad exigida.
- Conviene consensuar el uso de los literales `"ACTIVE"` / `"RETURNED"`. En caso de optarse por el `enum` opcional del enunciado, resultaría útil comunicarlo para alinear la capa `Service`.
- Se recomienda mantener el estado de las adopciones como fuente de verdad para determinar si un par sigue activo, complementando el estado de las colecciones.
- La incorporación de la ayuda `clear()` / `isEmpty()` en `Repository` simplifica los escenarios de recarga de datos y de prueba.

## 11. Nota sobre disponibilidad

En caso de que alguna de las partes restantes no estuviera disponible en la fecha prevista, las versiones provisionales de modelo permiten mantener el proyecto compilable y ejecutable, de modo que el trabajo pueda continuar sin bloquearse. Una vez disponibles las versiones definitivas, bastará con sustituir los ficheros indicados en el apartado 9.
