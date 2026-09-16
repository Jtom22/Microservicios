# Microservicios

Proyecto de aprendizaje con dos servicios Spring Boot y comunicación asíncrona preparada con Kafka.

## Servicios

| Servicio | Puerto | Responsabilidad |
| --- | ---: | --- |
| `auth-service` | 8081 | Registro, autenticación y autorización basada en JWT. |
| `stock-service` | 8082 | Inventario, movimientos de stock y consumo de pedidos creados. |

`stock-service` usa una organización hexagonal: el dominio define los puertos y reglas; REST, Kafka y MongoDB son adaptadores de infraestructura.

## Infraestructura local

Docker Compose inicia MongoDB para cada servicio, un broker Kafka en modo KRaft y Kafka UI.

```bash
cp .env.example .env
docker compose up -d
```

En Windows PowerShell puedes crear el archivo así:

```powershell
Copy-Item .env.example .env
```

Completa los valores de `.env` antes de iniciar los servicios. Este archivo y los volúmenes de MongoDB no se versionan.

| Recurso | Dirección |
| --- | --- |
| MongoDB de autenticación | `localhost:27017` |
| MongoDB de stock | `localhost:27018` |
| Kafka | `localhost:9092` |
| Kafka UI | `http://localhost:8085` |

## Ejecutar y probar

Cada servicio incluye Maven Wrapper:

```powershell
./auth-service/mvnw.cmd spring-boot:run
./stock-service/mvnw.cmd spring-boot:run
./stock-service/mvnw.cmd test
```

## Evento Kafka previsto

Cuando exista `order-service`, publicará en el topic `order-created`. `stock-service` lo consume y descuenta el inventario, registrando un movimiento `DECREASE` por producto.

```json
{
  "orderId": "order-123",
  "customerId": "customer-10",
  "items": [{ "productId": "product-1", "quantity": 2 }],
  "createdAt": "2026-09-16T10:30:00Z"
}
```

El productor `order-service` todavía no está implementado; por tanto, Kafka está preparado como integración futura.
