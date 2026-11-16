This Spring Boot application uses both MVC and REST controllers. Thymeleaf templates are used for the Admin and Doctor dashboards, while REST APIs serve all other modules. The application interacts with two databases—MySQL (for patient, doctor, appointment, and admin data) and MongoDB (for prescriptions). All controllers route requests through a common service layer, which in turn delegates to the appropriate repositories. MySQL uses JPA entities while MongoDB uses document models.



1. Users interact with the application through Thymeleaf-based web dashboards or REST API clients, which receive JSON responses.
2. Thymeleaf and REST Controllers handle incoming requests, validate inputs, and direct them to the appropriate backend logic.
3. The Service Layer manages business logic, validations, and workflows, ensuring proper coordination between different application components.
4. The Repository Layer handles database operations using Spring Data JPA for MySQL and Spring Data MongoDB for MongoDB.
5. MySQL stores structured data such as users, doctors, and appointments, while MongoDB stores flexible documents such as prescriptions.
6. Retrieved database records are mapped into Java model classes using JPA entities (@Entity) and MongoDB documents (@Document).
7. The models are passed to Thymeleaf templates to generate dynamic HTML pages or converted into JSON responses for REST API clients.
