# conversion-unidades

API REST con Spring Boot para convertir unidades de longitud, peso y temperatura, además de divisas con tasas de cambio configurables.

## Requisitos

- Java 25 o superior.
- No hace falta tener Maven instalado: el proyecto incluye el wrapper `./mvnw`.

## Ejecución

```bash
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

También puedes compilar y ejecutar el jar:

```bash
./mvnw clean package
java -jar target/conversion-unidades-0.0.1-SNAPSHOT.jar
```

## Endpoints

| Método | Ruta | Parámetros | Descripción |
| --- | --- | --- | --- |
| GET | `/unidades` | — | Lista las unidades disponibles por categoría. |
| GET | `/convertir/longitud` | `valor`, `de`, `a` | Convierte entre unidades de longitud. |
| GET | `/convertir/peso` | `valor`, `de`, `a` | Convierte entre unidades de peso. |
| GET | `/convertir/temperatura` | `valor`, `de`, `a` | Convierte entre unidades de temperatura. |
| GET | `/convertir/divisa` | `valor`, `de`, `a` | Convierte entre divisas configuradas. |

Los parámetros `de` y `a` no distinguen mayúsculas de minúsculas y se recortan los espacios sobrantes. Los resultados no se redondean; el formato queda a cargo del cliente.

### `GET /unidades`

```bash
curl http://localhost:8080/unidades
```

```json
{
  "longitud": ["cm", "ft", "in", "km", "m", "mi", "mm", "yd"],
  "peso": ["g", "kg", "lb", "mg", "oz", "t"],
  "temperatura": ["C", "F", "K"],
  "divisa": ["EUR", "MXN", "USD"]
}
```

### `GET /convertir/longitud`

Unidades soportadas: `mm`, `cm`, `m`, `km`, `in`, `ft`, `yd`, `mi`.

```bash
curl "http://localhost:8080/convertir/longitud?valor=10&de=km&a=mi"
```

```json
{
  "valorOriginal": 10.0,
  "unidadOrigen": "km",
  "resultado": 6.2137119223733395,
  "unidadDestino": "mi"
}
```

### `GET /convertir/peso`

Unidades soportadas: `mg`, `g`, `kg`, `t`, `oz`, `lb`.

```bash
curl "http://localhost:8080/convertir/peso?valor=1&de=kg&a=lb"
```

```json
{
  "valorOriginal": 1.0,
  "unidadOrigen": "kg",
  "resultado": 2.2046226218487757,
  "unidadDestino": "lb"
}
```

### `GET /convertir/temperatura`

Unidades soportadas: `C`, `F`, `K`.

```bash
curl "http://localhost:8080/convertir/temperatura?valor=100&de=C&a=F"
```

```json
{
  "valorOriginal": 100.0,
  "unidadOrigen": "C",
  "resultado": 212.0,
  "unidadDestino": "F"
}
```

### `GET /convertir/divisa`

Convierte entre las divisas definidas en la configuración. Por defecto: `USD`, `EUR` y `MXN`.

```bash
curl "http://localhost:8080/convertir/divisa?valor=100&de=USD&a=MXN"
```

```json
{
  "valorOriginal": 100,
  "unidadOrigen": "USD",
  "resultado": 1710,
  "unidadDestino": "MXN"
}
```

La conversión usa `BigDecimal` para evitar errores de coma flotante y admite conversiones cruzadas entre divisas que no son la base.

## Configuración de divisas

Las tasas se definen en `src/main/resources/application.yaml`. Cada tasa indica cuántas unidades de esa divisa equivalen a 1 unidad de la divisa base:

```yaml
divisas:
  base: USD
  tasas:
    USD: 1.0
    EUR: 0.92
    MXN: 17.10
```

Reglas que se validan al arrancar:

- `divisas.base` es obligatoria y debe existir en `divisas.tasas` con tasa `1`.
- Todas las tasas deben ser mayores que `0`.
- Los códigos se normalizan a mayúsculas automáticamente.

Para agregar una divisa basta con añadir su código y su tasa; aparecerá también en `GET /unidades`.

## Errores

Las unidades o divisas no soportadas, los parámetros faltantes y los valores con formato inválido devuelven `400 Bad Request` con un `ProblemDetail`:

```bash
curl -i "http://localhost:8080/convertir/longitud?valor=1&de=km&a=kg"
```

```json
{
  "detail": "Unidad 'kg' no soportada para longitud. Unidades válidas: cm, ft, in, km, m, mi, mm, yd",
  "instance": "/convertir/longitud",
  "status": 400,
  "title": "Bad Request"
}
```

## Tests

```bash
./mvnw test
```

Incluye tests de controller con `MockMvc` y tests de servicio para las conversiones, la validación de configuración y el manejo de unidades no soportadas.

## Estructura

```
src/main/java/com/angel/conversion_unidades
├── config        # Propiedades y validación de tasas de cambio
├── controller    # Endpoints REST
├── dto           # Respuestas de conversión
├── exception     # Manejo global de errores
└── service       # Lógica de conversión de unidades y divisas
```
