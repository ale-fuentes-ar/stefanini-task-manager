```
(\ (\
( • •)  
━∪∪━━━━ 
ᵇʸ ᴬˡᵉᶠᵘᵉⁿᵗᵉˢ
```
# Stefanini Task Manager - Fullstack Challenge
<img src="https://img.shields.io/badge/Java_v21-informational?style=flat-square&logo=openjdk&logoColor=000000&color=ffffff" /> <img src="https://img.shields.io/badge/Spring_Boot_v3.5-informational?style=flat-square&logo=spring&logoColor=6db33f&color=ffffff" /> <img src="https://img.shields.io/badge/Maven-informational?style=flat-square&logo=apachemaven&logoColor=c71a36&color=ffffff" /> <img src="https://img.shields.io/badge/Angular_v20-informational?style=flat-square&logo=angular&logoColor=0F0F11&color=ffffff" /> <img src="https://img.shields.io/badge/Tailwind_CSS_v3.4-informational?style=flat-square&logo=tailwindcss&logoColor=06B6D4&color=ffffff" /> 𖥔 <img src="https://img.shields.io/badge/Docker-informational?style=flat-square&logo=docker&logoColor=2496ed&color=ffffff" /> <img src="https://img.shields.io/badge/CI/CD_GitHub_Actions-informational?style=flat-square&logo=githubactions&logoColor=2088FF&color=ffffff" /> 


<img src="https://img.shields.io/badge/Dev-Alejandro.Fuentes-informational?style=flat-square&logoColor=white&color=cdcdcd" />

<br />

> Solución integral para la gestión de tareas desarrollada para el proceso técnico de Stefanini (Brasilia). Este proyecto demuestra la implementación de arquitecturas modernas, principios de Clean Code y una esteira de CI/CD automatizada.

## 🏛️ Arquitectura del Sistema
### Backend (Java 21 + Spring Boot 3.5)
Se ha implementado una **Arquitectura Hexagonal (Ports & Adapters)** combinada con **DDD (Domain-Driven Design)** para garantizar el desacoplamiento total de la lógica de negocio.
- **Domain Layer:** Entidades puras y contratos de interfaces (Ports).
- **Application Layer:** Servicios de orquestación y casos de uso.
- **Infrastructure Layer:** Adaptadores para SQL Server (JPA), Clientes API (WebClient) y Documentación (OpenAPI).

### Frontend (Angular 20)
Interfaz vanguardista utilizando el motor **Zoneless** y **Signals** para una reactividad de alto rendimiento.
- **Standalone Components:** Eliminación de NgModules para un bundle más ligero.
- **Tailwind CSS:** Diseño responsivo con estética de producto comercial (SaaS).
- **Vite Proxy:** Configuración de comunicación transparente con el backend.

---

## Tecnologías Utilizadas

- **Lenguajes:** Java 21, TypeScript.
- **Frameworks:** Spring Boot 3.5, Angular 20.
- **Persistencia:** MS SQL Server 2022 + Flyway (Migrations).
- **DevOps:** GitHub Actions, Docker, Docker Compose.
- **Calidad:** JUnit 5, Mockito, MockMvc, AssertJ.

---

## Guía de Inicio Rápido

> ☕︎ ˖ Verifique que las dependencias están instaladas en su entorno de trabajo/teste.

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/ale-fuentes-ar/stefanini-task-manager.git
    ```

    ```bash
    cd stefanini-task-manager
    ```

2. Requisitos Previos
    - Docker & Docker Desktop.
    - JDK 21.
    - Node.js 20+.

3. Infraestructura (Docker)
    Levanta la base de datos SQL Server y servicios secundarios:
    ```bash
    docker-compose up -d

    # verificar si en database 'stefaninidg' exist
    # 1- si Exited (0) => ejecuto script sin problemas
    docker ps -a --filter "name=task-sql-init"

    # 2- visualizar logs, si no retorna, no hubo logs de errores
    docker logs task-sql-init

    # 3- Listar los bancos de datos disponibles, como utilice gitbash, uso al inicio dos barras '//opt...'
    docker exec -it task-sql-server //opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P 'Stefanini2025!' -C -Q "SELECT name FROM sys.databases"
    ```

4. Ejecución Backend
    ```bash
    cd backend
    ./mvnw spring-boot:run
    ```
    *API disponible en: `http://localhost:8081` | Swagger: `/swagger-ui/index.html`*

5. Ejecución Frontend
    ```bash
    cd frontend
    npm install
    npm start
    ```
    *App disponible en: `http://localhost:4200`*

---

## Uso de Inteligencia Artificial (IA)
En cumplimiento con los requisitos de Stefanini, se documenta el uso de IA durante el desarrollo:
- **Herramienta:** ChatGPT/Claude (Modelo GPT-4o).
- **Uso:** Asistencia en la definición de la estructura hexagonal, generación de boilerplate para migraciones SQL y configuración del pipeline de GitHub Actions para SQL Server.
- **Validación:** Cada fragmento de código fue revisado, refactorizado para cumplir con **SOLID** y testeado manualmente para asegurar la integridad del sistema.

---

## Metodología de Trabajo
Se utilizó **GitFlow** estrictamente para la gestión de ramas:
- `main`: Versión estable de producción.
- `develop`: Integración continua.
- `feature/*`: Desarrollo de funcionalidades aisladas (CRUD, UI, CI/CD).

---

## Diagramas

- **Arquitectura (Hexagonal / Ports & Adapters):** Diagrama de alto nivel que muestra las capas y adaptadores principales del proyecto.

```mermaid
graph TD
    subgraph Frontend
        A[Angular Frontend]
    end
    subgraph Backend
        direction TB
        C[Controllers / REST API]
        S["Application Services (Use Cases)"]
        D["Domain (Entities & Ports)"]
        I[Infrastructure Adapters]
        R["Repositories (JPA / SQL)"]
    end
    subgraph External
        DB[(SQL Server)]
        F[Flyway Migrations]
        SW["Swagger / OpenAPI"]
    end

    A -->|HTTP JSON| C
    C --> S
    S --> D
    D --> I
    I --> R
    R --> DB
    F --> R
    C -->|Docs| SW

    classDef infra fill:#f9f,stroke:#333,stroke-width:1px;
    class I,R,DB,F infra;
```

- **Flujo de datos (ej. crear una tarea):** Secuencia desde la UI hasta la persistencia y respuesta.

```mermaid
sequenceDiagram
    participant FE as Frontend (Angular)
    participant API as REST Controller
    participant APP as Application Service
    participant DOM as Domain (Entity/Port)
    participant REP as Repository (JPA)
    participant DB as SQL Server

    FE->>API: POST /tasks {taskDto}
    API->>APP: mapToCommand(taskDto)
    APP->>DOM: executeCreate(command)
    DOM->>REP: save(entity)
    REP->>DB: INSERT task
    DB-->>REP: generated id
    REP-->>DOM: entity with id
    DOM-->>APP: domain event / dto
    APP-->>API: response dto (201 Created)
    API-->>FE: 201 Created (Location + body)
```


