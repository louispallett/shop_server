# Shop Server in Spring

This application is a simple shop server built with Java Spring and PostgreSQL, designed to manage users, products, transactions, and stock. It also demonstrates best practices like password hashing, JWT authentication, and CRUD operations.

> ⚠️ This project is intended for educational purposes and personal learning, rather than production use.

### Features
- User Management
    - Registration with secure password hashing
    - Login with JWT-based authentication
    - Role-based access control (basic example)
- Shop Management
    - CRUD operations for products, stock, and transactions
    - Data aggregation for analytics (e.g., sales totals, stock levels)
- Security
    - Passwords are stored securely using hashing
    - JSON Web Tokens (JWTs) for stateless authentication
- Database
    - PostgreSQL backend for persistent data storage
    - Schema includes tables for users, products, transactions, and stock

### Technology Stack
- Backend: Java Spring Boot
- Database: PostgreSQL
- Security: Spring Security + JWT
- Build Tool: Maven

### Getting Started
#### Prerequisites
- Java 21+ installed
- PostgreSQL database running
- Maven for building the project

You could potentially use Graven, but it may require some tweaking.

#### Setup
1. Clone the repository:
```sh 
$ git clone https://github.com/louispallett/shop_server.git
$ cd shop_server
```

2. Configure your environment variables in application.properties:
```
spring.application.name=shop_server
server.port=8080
spring.mvc.throw-exception-if-no-handler-found=true
spring.web.resources.add-mappings=false
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
jwt.secret=${JWT_SECRET}
jwt.expiration=36000000
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

I strongly suggest running this in [IntelliJ](https://flathub.org/en/apps/com.jetbrains.IntelliJ-IDEA-Community). That way you can configure a Spring configuration to run and set the environment variables listed above - these are:

- DB_URL: The address of your database.
- DB_USERNAME: The PostgreSQL user accessing the database.
- DB_PASSWORD: The password of the PostgreSQL user accessing the database.
- JWT_SECRET: The key to sign and verify JSON Web Tokens.

IntelliJ allows you to easily set these so they aren't dangerously hardcoded in.

3. Build and run the application:

You can either do this directly:
```sh 
$ ./mvnw spring-boot:run
```

Or, I suggest again using IntelliJ, by creating a Spring configuration.

The server will start on http://localhost:8080.

### Usage

You can send CRUD operations via `curl`:

_Create a User_
```sh 
$ curl -X POST -H "Content-Type: application/json" -d '{"firstName": "John", "lastName": "Doe", "email": "john.doe@example.com", "password": "a_strong_password"}' http://localhost:8080/api/user/create
{"firstName":"John","lastName":"Doe","email":"john.doe@example.com","dateCreated":"2026-05-15T11:45:42.725987360Z","id":5,"superuser":false}
```

_Login_
```sh 
$ curl -X POST -H "Content-Type: application/json" -d '{"email": "john.doe@example.com", "password": "Hello123!"}' http://localhost:8080/api/user/login
{"token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTc3ODg1ODYwMywiZXhwIjoxNzc4ODk0NjAzfQ.GlNWQ7PPY2t0ezPpEjV5Kw4FwauL4EMTIkNtjWPV8ek"}
```

I personally like to use [jq](https://jqlang.org/download/) to make things look a little nicer.

_Fetch all users_
```sh 
$ curl http://localhost:8080/api/user/get-all | jq
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100   139    0   139    0     0   5452      0 --:--:-- --:--:-- --:--:--  5560
[
  {
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "dateCreated": "2026-05-15T11:45:42.725987Z",
    "id": 5,
    "superuser": false
  }
]
```

_Fetch a single user by email_
```sh 
$ curl http://localhost:8080/api/user/get\?email\=john.doe@example.com | jq
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100   137    0   137    0     0  18601      0 --:--:-- --:--:-- --:--:-- 19571
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "dateCreated": "2026-05-15T11:45:42.725987Z",
  "id": 5,
  "superuser": false
}
```

Alternatively, you can use an application like Postman, although that requires registration.
