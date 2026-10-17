no compilan los tests porque faltan las clases/métodos que usan (DogAdoption, Person.dog, Dog.owner, métodos en Repository y Service.adoptDog).

Para seguir el enfoque TDD que quieres (tests creados, luego ir solucionando poco a poco), lo más útil es crear esqueletos mínimos que permitan compilar los tests. Eso nos deja los tests en rojo (fallando por lógica) en lugar de tener errores de compilación.

Te propongo crear los esqueletos mínimos:

DogAdoption (modelo)

Añadir dog a Person, owner a Dog (campos + getters/setters + ajustar toString() de Dog para evitar recursividad)

Añadir métodos de DogAdoption en Repository

Añadir firmas/métodos vacíos necesarios en Service (adoptDog, getDogAdoptionById, getAllDogAdoptions) que devuelvan valores por defecto para que compilen (aunque los tests los harán fallar)