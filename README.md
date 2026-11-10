# Taller grupal: automatización de pruebas con Postman + Newman

API REST de biblioteca (Spring Boot) y su suite de pruebas automatizadas.

| Componente | Archivo |
|---|---|
| Colección principal (21 peticiones, 4 carpetas) | `postman/Biblioteca.postman_collection.json` |
| Colección data-driven (9 escenarios) | `postman/Biblioteca-DataDriven.postman_collection.json` |
| Datos de prueba | `postman/data/libros-validaciones.csv` |
| Entorno | `postman/local.postman_environment.json` |
| Pipeline CI | `.github/workflows/api-tests.yml` |

## Requisitos
Java 17, Maven 3.9+, Node 18+ y Postman (opcional, para la parte gráfica).

## Ejecución local
```bash
mvn spring-boot:run          # terminal 1: API en http://localhost:8080
npm install                  # terminal 2
npm test                     # corre ambas colecciones
npm run test:api             # solo la colección principal
npm run test:data            # solo la data-driven
```
Reportes HTML en `reports/biblioteca.html` y `reports/data-driven.html`.

## En Postman
1. Import → seleccionar los 3 archivos JSON de `postman/`.
2. Elegir el entorno "Biblioteca - Local" (arriba a la derecha).
3. Clic derecho sobre la colección → Run collection → Run.
4. Para la data-driven: Run → Select File → `libros-validaciones.csv`.

API key de prueba: `biblioteca-2026` (cabecera `X-API-KEY`).
