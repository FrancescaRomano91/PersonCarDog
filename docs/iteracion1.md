Creado JUnit 5 para el proyecto:
\- pom.xml: actualizado a JUnit Jupiter + maven-surefire-plugin.
\- AppTest: migrado a JUnit 5.
\- ServiceTest: 20 tests (CRUD Person/Car/Dog, transacciones, buyCar con validaciones).
\- RepositoryTest: 8 tests (add/get/remove para todas las entidades).
Resultado: BUILD SUCCESS. Total 29 tests ejecutados sin fallos (AppTest 1 + ServiceTest 20 + RepositoryTest 8).