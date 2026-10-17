Plan de implementación

1\. Modelos (model/)

Person.java

\- Añadir campo private Dog dog;.

\- Añadir getter y setter (getDog(), setDog(Dog dog)).

\- No modificar toString() (no imprime el perro, con lo que evitamos recursividad).

Dog.java

\- Añadir campo private Person owner;.

\- Añadir getter y setter (getOwner(), setOwner(Person owner)).

\- Corregir toString(): no debe imprimir el objeto owner completo (riesgo de recursividad infinita con Person.toString() si Person también imprimiera el perro). Debe mostrar solo una referencia ligera (p.ej. ownerId o ownerName). Seguir el mismo criterio que usa CarTransaction.toString() (no expande objetos profundamente cuando hay relación bidireccional).

DogAdoption.java (nuevo)

Crear en src/main/java/org/example/model/DogAdoption.java con la misma estructura que CarTransaction:

\- private String id (generado con UUID.randomUUID().toString() en constructor vacío y en constructor completo)

\- private Person adopter

\- private Dog dog

\- private Date date

\- private int fee

\- private String contract

\- Constructor vacío + constructor completo (Person adopter, Dog dog, Date date, int fee, String contract)

\- Getters/setters para todos los campos

\- toString() que muestre los campos. Tener cuidado para no provocar recursividad (evitar imprimir adopter o dog completos si sus toString() cambian en el futuro; mostrar IDs o nombres suficientes).

2\. Repository (repository/Repository.java)

Añadir:

\- private ArrayList<DogAdoption> dogAdoptions; e inicializarlo en el constructor (this.dogAdoptions = new ArrayList<>()).

\- public void addDogAdoption(DogAdoption adoption)

\- public DogAdoption getDogAdoptionById(String id) (búsqueda por id, devuelve null si no existe)

\- public ArrayList<DogAdoption> getAllDogAdoptions() (devuelve la lista)

\- public boolean removeDogAdoption(String id) (usar removeIf igual que el resto)

Seguir exactamente el patrón usado para carTransactions.

3\. Service (service/Service.java)

3.1 Caso de uso adoptDog

Añadir método:

public static ResultOperation adoptDog(Person adopter, Dog dog, int fee, Repository repo)

Validaciones en este orden estricto (copiando el estilo de buyCar):

1\. adopter existe en repo.getAllPeople() → si no, fallo "Adopter not found in repository." o mensaje claro (seguir coherencia con buyCar que valida existencia). El enunciado lista las reglas 1–6; usar mensajes tal cual: 3. "Adopter already owns a dog.", 4. "Dog already has an owner.", 5. "Adopter must be an adult.", 6. "Fee cannot be negative.". Para 1–2 validar existencia (seguir patrón de buyCar: comprueba que están en el repositorio).

2\. dog existe en repo.getAllDogs()

3\. adopter.getDog() == null

4\. dog.getOwner() == null

5\. adopter.getAge() >= 18

6\. fee >= 0

En cada fallo: crear ResultOperation, devolver result.setter(false, "<mensaje>", "DogAdoption", System.currentTimeMillis()) (sin payload). No imprimir desde Service. Seguir exactamente el patrón de buyCar (Service solo retorna ResultOperation; Controller es quien lo imprime).

Éxito:

\- Establecer relación bidireccional: adopter.setDog(dog); y dog.setOwner(adopter); (siempre en este orden o simplemente asegurar consistencia; ambos lados deben quedar sincronizados).

\- Crear DogAdoption adoption = new DogAdoption(adopter, dog, new Date(), fee, "Dog adopted for " + fee);

\- repo.addDogAdoption(adoption);

\- Devolver result.setter(true, "Dog adopted successfully.", "DogAdoption", System.currentTimeMillis(), adoption)

3.2 Métodos de lectura (solo lectura)

Añadir:

\- public static ResultOperation getDogAdoptionById(String id, Repository repo)

\- public static ResultOperation getAllDogAdoptions(Repository repo)

Seguir el patrón exacto de getCarTransactionById / getAllCarTransactions (validaciones de id vacío cuando aplique, búsqueda, retorno con payload o fallo, tipo "DogAdoption").

3.3 Cascada en borrados

Modificar los métodos existentes para mantener consistencia bidireccional:

\- deletePerson(String id, Repository repo): antes de eliminar, si p.getDog() != null, hacer p.getDog().setOwner(null) (quitar propietario al perro) y opcionalmente p.setDog(null). Esto evita dejar perros con owner huérfano/incorrecto.

\- deleteDog(String id, Repository repo): antes de eliminar, si d.getOwner() != null, hacer d.getOwner().setDog(null) (quitar perro al adoptante).

Mismo enfoque que deleteCar (limpia la referencia del propietario). No cambia el resto de lógica.

4\. Utils (utils/Utils.java)

\- Añadir método public static void dogAdoptionMenu() que muestre solo lectura:

\===== DOG ADOPTIONS (READ ONLY) =====

1\. List all adoptions

2\. Find adoption by ID

3\. Back

\- Modificar mainMenu() para añadir las nuevas opciones (tal como indica el enunciado):

\===== MAIN MENU =====

1\. Person operations

2\. Dog operations

3\. Car operations

4\. Buy a car (person to person)

5\. View car transactions (read only)

6\. Adopt a dog (person <- dog)

7\. View dog adoptions (read only)

8\. Quit

5\. Controller (controller/Controller.java)

\- Añadir submenu private static void dogAdoptionSubMenu(Scanner scan, Repository repo) (solo lectura): gestiona opciones 1–3 llamando a Service (getAllDogAdoptions, getDogAdoptionById), capturando e imprimiendo siempre el ResultOperation (seguir patrón v4: guardar resultado y System.out.println("resultOperation: " + res) o equivalente, igual que resto de submenús).

\- Modificar run() para añadir casos case "6": (Adopt a dog) y case "7": (View dog adoptions). No romper los existentes.

\- Implementar opción 6 "Adopt a dog": el usuario debe elegir por ID (no hardcodear listas). Pasos:

1\. String pid = Utils.askString(scan, "Enter person ID: ");

2\. String did = Utils.askString(scan, "Enter dog ID: ");

3\. int fee = Utils.askInt(scan, "Enter adoption fee: ");

4\. Resolver objetos: Person adopter = repo.getPersonById(pid.trim()); y Dog dog = repo.getDogById(did.trim()); (pueden ser null; Service.adoptDog validará existencia)

5\. Llamar Service.adoptDog(adopter, dog, fee, repo) y capturar e imprimir el ResultOperation resultante (igual que el resto de acciones del Controller).

Importante: no reutilizar lógica tipo runBuyCarDemo (ese es un demo con selección implícita). Aquí la selección es explícita por ID.

6\. DataSeeder (utils/DataSeeder.java) – recomendado (no obligatorio pero útil)

Para facilitar pruebas del caso “ya tiene dueño”:

\- Crear/sembrar al menos 2 perros ya adoptados. Para ello: crear perros, crear adoptantes adultos, establecer relación bidireccional y crear sus DogAdoption correspondientes (añadirlos a dogAdoptions del repo).

\- Mantener el total/estructura coherente con lo ya existente. Asegurar que los adoptantes tengan age >= 18.

7\. Puntos de atención (evitar lo que no cumple)

\- Evitar recursividad en toString(): Dog.toString() nunca debe llamar a owner.toString() (ni incluir objeto completo). Person.toString() no debe incluir dog completo. DogAdoption.toString() tener cuidado con referencias cíclicas.

\- No meter lógica de negocio en Controller/Utils: Controller solo pide datos, resuelve IDs (lectura directa del repo) y delega en Service. Validaciones van todas a Service.

\- DogAdoption solo lectura desde UI: no crear menús para crear/editar/borrar adopciones. Solo listar/buscar por ID (tal como pide el enunciado).

\- Consistencia bidireccional SIEMPRE: al asignar/quitar dueño/perro, hacerlo en ambos lados. En adoptDog (asignar) y en deletes (limpiar).

\- No romper código existente: buyCar, CRUD Person/Car/Dog, CarTransaction y tests actuales deben seguir funcionando. Seguir exactamente el contrato de ResultOperation (tipo "DogAdoption" para las nuevas operaciones).

\- Cumplir con v4: Controller debe capturar e imprimir todos los ResultOperation (también los nuevos casos 6 y 7).

8\. Criterios de aceptación (verificación rápida)

\- Person.dog + getters/setters

\- Dog.owner + getters/setters + toString() sin recursividad

\- DogAdoption model creado (igual estructura que CarTransaction)

\- Repository con dogAdoptions + 4 métodos

\- Service.adoptDog con 6 validaciones, bidireccional, crea DogAdoption, retorna ResultOperation

\- Service read-only DogAdoption (getById, getAll)

\- deletePerson/deleteDog con cascada (consistencia)

\- Utils: dogAdoptionMenu + mainMenu actualizado

\- Controller: opción 6 adopta por ID, opción 7 submenu read-only, imprime ResultOperation

\- UI no permite crear/editar/borrar DogAdoption

\- buyCar y CRUD existentes siguen intactos