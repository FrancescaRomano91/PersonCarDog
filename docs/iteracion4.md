Tests creados y esqueletos mínimos añadidos.
Estado actual:
\- Compilación: OK (tests compilan)
\- Repository tests (DogAdoptionRepositoryTest): 5/5 pasan (todos OK)
\- Service tests (AdoptDogServiceTest): 17 tests → 7 fallos + 1 error, 9 pasan
\- Pasan: fail\_adopterNotInRepo, fail\_dogNotInRepo, fail\_notAdult, fail\_feeNegative, getDogAdoptionById\_notFound, getDogAdoptionById\_emptyId, getAllDogAdoptions\_empty, dogToString\_noInfiniteRecursion, buyCar\_stillWorks\_afterAdoptionLogic (9)
\- Fallan/error: adoptDog\_success, adoptDog\_success\_feeZero, fail\_adopterAlreadyOwnsDog (segundo intento debe fallar), fail\_dogAlreadyHasOwner (segundo intento debe fallar), getAllDogAdoptions\_multiple (espera 2), deletePerson\_clearsDogOwnerRelation, deleteDog\_clearsPersonDogRelation, getDogAdoptionById\_found (adoption null porque adoptDog aún no implementado)
Los que pasan son los que solo usan el esqueleto mínimo que ya existe (getDogAdoptionById/getAllDogAdoptions básicos, y Repository ya funciona). Los que fallan son los que requieren lógica real en adoptDog + cascade en deletePerson/deleteDog.

Ahora implemento adoptDog correctamente en Service con las 6 validaciones, bidireccionalidad, creación de DogAdoption y guardado en repo.