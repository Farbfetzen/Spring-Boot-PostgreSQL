# Spring-Boot-PostgreSQL

A project to learn Docker and PostgreSQL with Spring Boot.

## Usage

Start the app: `mvn spring-boot:run`

Connect to the database at port 8081, for example with pgAdmin.
Login data:
- Database: postgres
- Username: postgres
- Password: password

List all users at <http://localhost:8080/users>.

Create a new user by sending a PUT request to the same address with an email and a name in the request body.
Example: `{"email":"jane@example.org","name":"jane doe"}`

Modify a user by sending a POST request to the same address with the id as a URL parameter.
This is for testing how Spring Data JDBC updates the version field.
Example: `<http://localhost:8080/users?id=3dc65f6e-c3e4-4628-9a3d-d7608406d1fc>`

## TODO

- Maybe use Testcontainers to automatically set up and shut down a database for testing.
