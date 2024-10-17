#  Prueba de Codigo 

Hay que construir una aplicacion (REST API) que tiene un unico endpoint para consultar precios de productos en una tienda.  

En los requisitos se dice que hay que usar h2 en memoria para proporcionar datos de ejemplo, yo me he tomado la libertad de usar
una base de datos real (postgres) en memoria usando docker. La configuracion de la base de datos esta en el fichero `compose.yaml`.

## Codigo 

El codigo se basa en `Project Reactor` que permite escribir codigo non-blocking con la consiguiente mejora en rendimiento.
 
Tambien he usado lombok para conseguir un codigo mas limpio y facil de mantener/modificar/leer.

El codigo sigue una arquitectura hexagonal, con una capa de dominio, una de aplicacion y una de infraestructura.

Durante mi carrera profesional siempre me ha gustado trabajar con pruebas en todos los niveles, unitarias, integracion y end to end. 

Esta prueba de codigo no es una excepcion, he escrito pruebas unitarias y de integracion para asegurarme de que el codigo funciona.
El test unitario es bastante sencillo, pero queria mostrar el uso de Mockito y de como puede ser usado en un proyecto con Spring Boot.

Respesto a las pruebas de integracion, se usan `TestContainers` para levantar una base de datos postgres y un `Component` para crear la
base de datos y poblarla con datos de ejemplo.

Los test end to end, tb usan Docker y crean la base de datos y le insertan datos de pruebas. Las pruebas las he hecho con k6 ya que es muy sencillo de utilizar y 
muy potente, proporcionando una interesante grafica (con Grafana e InfluxDB). 
Creo que el codigo end to end (incluso pruebas de performance) deben estar en un repositorio separado, pero en este caso por 
simplicidad las he incluido en el mismo repositorio.

Para ejecutar los tests end to end, en el diretorio testing,
```bash
./load-search.sh
```
Este comando ejecuta un docker compose que tiene, 
 1. Grafana, InfluxDB y k6 para las pruebas
 2. PriceApi, que es el microservicio que se esta probando. Compila y empaqueta el codigo en un container de docker que se ejecuta en esta prueba
 3. Postgresql, que es la base de datos que se usa para las pruebas

Los resultado se pudes en en la consola o en el navegador: http://localhost:3000/d/k6/k6-load-testing-results?orgId=1&refresh=5s


Para poblar la base de datos me gusta utilizar herramientas como Liquibase, Slick o Flyway para gestionar las migraciones de la base
de datos. Pero ahora mismo no conozco ninguna herramienta que trabaje con codigo reactivo, y como es una prueba de concepto, he usado algo muy rudimentario pero que funciona en estos casos sencillos. 


Para acceder a Swagger/OpenAPI: http://127.0.0.1:8080/webjars/swagger-ui/index.html

# Nota
Tengo el portatil con teclado americano y no tengo la tecla de la ñ, y los acentos.

# Pruebas rapidas de API
Para poder probar la API de forma sencilla y rapida se han incluido unos scritps en la carpeta `requests` que se ejecutar de forma integrada en el IDE que utilizo (Intellij Idea).
Podria hacerse para otras hrrramientas como PostMan de forma similar, pero por simplicidad lo he hecho asi.  


# Ejecucion local

## Docker
Para ejecutar el microservicio en docker en local.

```bash
./gradle clean build
docker build  . -t josetaboada/price-api
```

Para ejecutar en modo local/produccion (fake):
Hay que tener una instancia de postgres corriendo y configurado en el application-prod.properties. 
Una opcion rapida y sencilla es lanzar los test end to end que dejar lanzado un postgres en docker y esta pre-configurado en la aplicacion (application-prod.properties).
```bash
docker run -e SPRING_PROFILES_ACTIVE=prod  josetaboada/price-api
```

# Integracion con GitHub Actions
Hay dos actions que se pueden ejecutar manualmente en el repositorio, una para hacer un build (con tests) y otro para lanzar los test de integracion.

En el action del load test se puede ver las estadisticas de la prueba.

En el action del build solo se ejecutan los tests con docker. 

Para poder hacer "merge" en el repo hay que pasar los tests de integracion (y por tanto el build).
