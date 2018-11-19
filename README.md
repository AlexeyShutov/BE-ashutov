### Requirements:
- Docker Compose 18.06.0+
- JDK10+ (optional)
- Gradle 4.7+ (optional)

This application builds using docker multi-stage build, JDK and Gradle required only to run tests.

### Build and run:
1) _cd .../scloud-microservices_
2) _docker-compose up --build_

### Monitoring
Eureka and Zipkin servers are available from localhost mapped to default ports:
- http://localhost:8761/
- http://localhost:9411/

### Run test suite:
_cd .../scloud-microservices/**$SERVICE_NAME** gradle test_ <br />
_Catalog Service_ has data and redis instance depending integration tests (which are skipped at the build stage and supposed to manual run)

### Requests using Rest Client:

- Check product availability (inventory-service): <br />
**GET** http://localhost:8082/inventory/availability/b6c0b6bea69c722939585baeac73c13d,93e5272c51d8cce02597e3ce67b7ad0a,013e320f2f2ec0cf5b3ff5418d688528,505e6633d81f2cb7400c0cfa0394c427,d969a8542122e1331e304b09f81a83f6
- Product from catalog (catalog-service): <br />
**GET** http://localhost:8081/catalog/product/b6c0b6bea69c722939585baeac73c13d,93e5272c51d8cce02597e3ce67b7ad0a,013e320f2f2ec0cf5b3ff5418d688528,505e6633d81f2cb7400c0cfa0394c427,d969a8542122e1331e304b09f81a83f6
- Available Products (product service): <br />
**GET** http://localhost:8083/product/b6c0b6bea69c722939585baeac73c13d,93e5272c51d8cce02597e3ce67b7ad0a,013e320f2f2ec0cf5b3ff5418d688528,505e6633d81f2cb7400c0cfa0394c427,d969a8542122e1331e304b09f81a83f6 
- Request and force delay as parameter in seconds (product service): <br />
**GET** http://localhost:8083/product/heavy/013e320f2f2ec0cf5b3ff5418d688528,232aa40a479d349693a8e3e0fcc94403?delay=5
<br /> The service expected to be timed-out in 3s. Sending this request five times in a row during 20s will lead the circuit to be opened.
