# Microservices Example Stock Viewer

Microservices in this example includes:
- `db-service` - For interactive with a file based H2 database.
- `stock-service` - For pulling Stock Price from YahooFinance API.
- `eureka-service` - Service registry for registering all microservices.
- `api-gateway` - Proxy/API Gateway for all microservices (using Zuul).
