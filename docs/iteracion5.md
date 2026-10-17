Estado actual

\- Rama: dog-adoption-francesca (commits hechos con josejavacifovioleta@gmail.com)

\- Commits ordenados:

1\. aaf355e - test: add adopt-dog unit tests and minimal scaffolding (DogAdoption, Person.dog/Dog.owner, Repository, Service stubs)

2\. 83a254a - feat: implement adoptDog + cascada deletePerson/deleteDog (hace pasar tests)

3\. 4f7e127 - docs: add Adopt\_a\_Dog.md + iteraciones

4\. df67c93 - feat: add ResultOperation.java

\- Tests: 51/51 pasan (BUILD SUCCESS). AdoptDogServiceTest 17/17, DogAdoptionRepositoryTest 5/5. Código existente intacto.

Adopt\_a\_Dog.md - estado y qué queda por reparar/cumplir

He leído el ejercicio. Con lo hecho ya cubrimos gran parte (modelo, repo, Service adoptDog + lectura + cascada, tests). Falta lo de UI/Utils/Controller y DataSeeder opcional recomendado.

Lo que YA cumple (implementado + tests)

\- Person tiene dog + getters/setters

\- Dog tiene owner + getters/setters + toString() sin recursividad (usa ownerName)

\- DogAdoption creado con misma estructura que CarTransaction

\- Repository almacena/recupera DogAdoption (add/get/getAll/remove) - tests pasan

\- Service.adoptDog con 6 validaciones, bidireccional, crea DogAdoption, retorna ResultOperation

\- Service solo lectura para DogAdoption (getById, getAll)

\- deletePerson/deleteDog mantienen consistencia bidireccional

\- buyCar y CRUD existentes siguen funcionando (tests verdes)

\- Dog.toString() sin recursividad

Lo que FALTA por cumplir (del enunciado)

\- Utils.java: añadir dogAdoptionMenu() (read-only: List all, Find by ID, Back)

\- Utils.java: actualizar mainMenu() para añadir opción 6 "Adopt a dog (person <- dog)" y 7 "View dog adoptions (read only)" (total 8 opciones, Quit pasa a 8)

\- Controller.java: añadir submenu dogAdoptionSubMenu() (read-only) - llama a Service, captura e imprime ResultOperation

\- Controller.java: en run() añadir case "6" para adoptar (pedir person ID, dog ID, fee por Utils.askString/askInt, resolver vía repo.getPersonById/repo.getDogById, llamar Service.adoptDog y capturar e imprimir ResultOperation). Opción 7 abre submenu.

\- Controller.java: adoptar por ID (explícito), no hardcodear como demo

\- DataSeeder (recomendado): sembrar al menos 2 perros ya adoptados con adoptantes adultos para poder probar regla "already has an owner"

Sugerencia de commits por pasos

1\. feat: add dog adoption menu and update main menu in Utils

2\. feat: add dog adoption submenu and wire adoptDog in Controller (by ID)

3\. feat: seed adopted dogs in DataSeeder (optional)

Para push: necesitas permisos en el repo remoto. Opciones:

\- Crear repo en GitHub con tu cuenta (josejavacifovioleta/PersonCarDog) y pushear (o tener colaborador).

\- O usar gh auth login con tu cuenta de GitHub (token/HTTPS) desde la CLI y cambiar remote al repo correcto.

Dime qué paso quieres hacer primero (Utils/Controller/DataSeeder) y te digo el commit exacto a hacer.