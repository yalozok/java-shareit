# ShareIt – Item Sharing Service

ShareIt is a RESTful web application that allows users to share items with each other. The platform supports item management, search functionality, booking requests, and commenting after usage.
## Technologies & Architecture
- Spring Boot – Backend framework
- Spring Data JPA – Data access layer
- DTO & MapStruct-style Mapping – Clean separation of layers
- PostgreSQL – Primary database
- H2 – In-memory DB for testing and development
- JUnit 5, Hamcrest, Mockito – Unit and integration testing with full coverage (`@WebMvcTest`, `@DataJpaTest`, `@SpringBootTest`)
- Docker & Docker Compose – Containerized multi-module architecture
- Lombok – Boilerplate reduction

## Modular Structure
1. Gateway Module
   - REST clients for User, Item, Booking, and Request services
   - Built with RestTemplate and configured via BaseClient

2. Server Module
   - Core business logic and data persistence
   - Exposes REST API endpoints
   - Handles validations, exceptions, and request mapping
