# ⚙️ Employee Management System — Backend

A robust RESTful backend API built for the **4bitLabs** Employee Management System. This Spring Boot application provides secure, role-based employee, student, batch, and progress management backed by MySQL and documented with Swagger/OpenAPI.

---

## Tech Stack

| Technology | Purpose |
|---|---|
| **Java 17** | Core language |
| **Spring Boot 3.5** | Application framework |
| **Spring Security + JWT** | Authentication & authorization |
| **Spring Data JPA** | ORM and database access |
| **MySQL 8.0+** | Relational database |
| **SpringDoc OpenAPI 2.0+** | Automated API documentation (Swagger UI) |
| **Cloudinary** | Cloud storage for file/document uploads |
| **Maven** | Build tool |
| **java-dotenv** | Environment variable management |

---

## Architecture

The project follows a standard **layered architecture** with clear separation of concerns:

```
src/main/java/com/fourbitlabs/employee_management_system/
├── config/               # App configuration (CORS, OpenAPI/Swagger)
├── controller/           # REST API controllers
│   ├── AdminController        # Admin registration + Trainer/Analyst/Counsellor CRUD
│   ├── AnalystController      # Batch CRUD
│   ├── AssignmentController   # Student ↔ Batch assignments & transfers
│   ├── AuthController         # Login, token refresh, logout
│   ├── CounsellorController   # Student CRUD
│   ├── HealthController       # Health check endpoint
│   └── TrainerController      # Batch progress management
├── dto/
│   ├── request/          # Incoming request DTOs
│   └── response/         # Outgoing response DTOs
├── entity/               # JPA entity classes
│   ├── User                   # Base user (name, email, password, phone, role, status)
│   ├── TrainerProfile         # Trainer-specific (specialization, experience, qualification)
│   ├── AnalystProfile         # Analyst-specific (department)
│   ├── CounsellorProfile      # Counsellor-specific (department)
│   ├── Student                # Student (linked to counsellor)
│   ├── Batch                  # Batch (linked to trainer & analyst)
│   ├── Assignment             # Student ↔ Batch mapping
│   ├── BatchProgress          # Progress tracking per batch
│   └── RefreshToken           # JWT refresh token storage
├── enums/                # Role, UserStatus, StudentStatus, BatchStatus, AssignmentStatus
├── exception/            # Custom exceptions (ResourceNotFound, DuplicateResource)
├── repository/           # Spring Data JPA repositories
├── response/             # Standardized API response wrapper
├── security/             # JWT filter, security config, user details
└── service/
    ├── interfaces/       # Service contracts
    └── impl/             # Service implementations
```

---

## Entity Relationships

```
User (1) ──── (1) TrainerProfile / AnalystProfile / CounsellorProfile
User (1) ──── (*) RefreshToken                (token revocation & audit)
Batch (*) ──── (1) TrainerProfile              (trainer_id FK)
Batch (*) ──── (1) AnalystProfile              (analyst_id FK)
Student (*) ── (1) CounsellorProfile           (counsellor_id FK)
Assignment ─── Student + Batch                 (many-to-many join entity)
BatchProgress ─ Batch + Trainer                (progress tracking per batch)
```

---

## Project Flow

```
                    ┌──────────────────────────────────┐
                    │         ADMIN registers           │
                    │    POST /api/admin/register        │
                    └──────────────┬───────────────────┘
                                   │
              ┌────────────────────┼────────────────────┐
              │                    │                     │
              ▼                    ▼                     ▼
   ┌──────────────────┐  ┌────────────────┐  ┌──────────────────┐
   │ Create Trainer    │  │ Create Analyst │  │ Create Counsellor│
   │ POST /admin/      │  │ POST /admin/   │  │ POST /admin/     │
   │     trainers      │  │    analysts    │  │   counsellors    │
   └────────┬─────────┘  └───────┬────────┘  └────────┬─────────┘
            │                    │                     │
            │                    ▼                     ▼
            │         ┌──────────────────┐   ┌──────────────────┐
            │         │  Create Batch    │   │  Create Student  │
            │         │ POST /analyst/   │   │ POST /counsellor/│
            │         │    batches       │   │    students      │
            │         └───────┬──────────┘   └────────┬─────────┘
            │                 │                       │
            │                 └────────┬──────────────┘
            │                          ▼
            │               ┌──────────────────┐
            │               │ Assign Student   │
            │               │ to Batch         │
            │               │ POST /assignments│
            │               └───────┬──────────┘
            │                       │
            ▼                       ▼
   ┌──────────────────┐   ┌──────────────────┐
   │  Add Batch       │   │  Transfer Batch  │
   │  Progress        │   │  PUT /assignments│
   │ POST /trainer/   │   │     /transfer    │
   │ batch-progress   │   └──────────────────┘
   └──────────────────┘
```

### Authentication Flow

```
1. POST /api/auth/login        → Returns JWT access token + refresh token (HttpOnly cookie)
2. Protected endpoints         → Send access token in Authorization: Bearer <token>
3. POST /api/auth/refresh      → Rotate expired access token using refresh cookie
4. POST /api/auth/logout       → Revoke refresh token + clear cookie
```

---

## API Endpoints

### 🔐 Authentication (`/api/auth`)

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| `POST` | `/api/auth/login` | Login and receive JWT tokens | Public |
| `POST` | `/api/auth/refresh` | Refresh access token via cookie | Public |
| `POST` | `/api/auth/logout` | Revoke refresh token and clear cookie | Public |

### 👑 Admin (`/api/admin`) — Requires `ADMIN` role

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/admin/register` | Register a new admin (Public, one-time) |
| `POST` | `/api/admin/trainers` | Create a trainer |
| `GET` | `/api/admin/trainers` | Get all trainers |
| `GET` | `/api/admin/trainers/{id}` | Get trainer by user ID |
| `PUT` | `/api/admin/trainers/{id}` | Update trainer |
| `DELETE` | `/api/admin/trainers/{id}` | Hard-delete trainer (Cascades to progress & refresh tokens) |
| `POST` | `/api/admin/analysts` | Create an analyst |
| `GET` | `/api/admin/analysts` | Get all analysts |
| `GET` | `/api/admin/analysts/{id}` | Get analyst by user ID |
| `PUT` | `/api/admin/analysts/{id}` | Update analyst |
| `DELETE` | `/api/admin/analysts/{id}` | Hard-delete analyst (Cascades to refresh tokens) |
| `POST` | `/api/admin/counsellors` | Create a counsellor |
| `GET` | `/api/admin/counsellors` | Get all counsellors |
| `GET` | `/api/admin/counsellors/{id}` | Get counsellor by user ID |
| `PUT` | `/api/admin/counsellors/{id}` | Update counsellor |
| `DELETE` | `/api/admin/counsellors/{id}` | Hard-delete counsellor (Cascades to students & assignments) |

### 🎓 Counsellor (`/api/counsellor`) — Requires `COUNSELLOR` or `ADMIN` role

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/counsellor/students` | Create a student |
| `GET` | `/api/counsellor/students` | Get all students |
| `GET` | `/api/counsellor/students/{id}` | Get student by ID |
| `PUT` | `/api/counsellor/students/{id}` | Update student |
| `DELETE` | `/api/counsellor/students/{id}` | Hard-delete student (Cascades to assignments) |

### 📊 Analyst (`/api/analyst`) — Requires `ANALYST` or `ADMIN` role

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/analyst/batches` | Create a batch |
| `GET` | `/api/analyst/batches` | Get all batches |
| `GET` | `/api/analyst/batches/{id}` | Get batch by ID |
| `PUT` | `/api/analyst/batches/{id}` | Update batch (can reassign trainer/analyst) |
| `DELETE` | `/api/analyst/batches/{id}` | Hard-delete batch (Cascades to assignments, progress, & Cloudinary) |

### 🧑‍🏫 Trainer (`/api/trainer`) — Requires `TRAINER` or `ADMIN` role

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/trainer/batch-progress` | Add batch progress entry with Cloudinary upload |
| `GET` | `/api/trainer/batch-progress/{batchId}` | Get batch progress by batch ID |
| `DELETE` | `/api/trainer/batch-progress/{id}` | Delete batch progress and its Cloudinary file |

### 📋 Assignments (`/api/assignments`) — Requires `ADMIN` or `ANALYST` role

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/assignments` | Assign student to a batch |
| `PUT` | `/api/assignments/transfer` | Transfer student to another batch |
| `GET` | `/api/assignments/batch/{batchId}` | Get all students in a batch |

### ❤️ Health Check

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| `GET` | `/api/` | Health check — returns welcome message | Public |

---

## API Documentation (Swagger UI)

Once the application is running, access the interactive API documentation at:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI Spec (JSON)**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

The Swagger UI includes **JWT Bearer authentication** support — click the 🔒 **Authorize** button and paste your access token to test protected endpoints directly from the browser.

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8.0+

### Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/vaibhavCodes18/4bitLabs-Employee-Management-System-Backend.git
   cd backend
   ```

2. **Create the MySQL database:**
   ```sql
   CREATE DATABASE ems;
   ```

3. **Configure environment variables** (create `.env` or `application-local.properties`):
   ```properties
   DB_URL=jdbc:mysql://localhost:3306/ems
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   JWT_SECRET=your_base64_encoded_secret_key
   ```

4. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

   The API server will start on `http://localhost:8080`.

5. **Open Swagger UI:**
   Navigate to [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) to explore and test the API.

---

## Key Features

- **JWT Authentication** — Stateless access tokens + rotating refresh tokens stored as HttpOnly cookies
- **Token Revocation** — Refresh tokens are actively purged using custom repository methods when users are permanently removed, alongside automatic invalidation during standard logout procedures.
- **Role-Based Access Control** — Four roles: `ADMIN`, `TRAINER`, `ANALYST`, `COUNSELLOR`, enforced at the security filter level
- **Cloudinary Integration** — Remote file hosting integration using Cloudinary seamlessly handling trainer progress documents (both uploading and hard-deletion to avoid memory leaks)
- **Hard Deletes with Cascading Resolvers** — Hard-deletions cleanly remove associated active tokens, files, batch progress, and assignments manually to strictly prevent SQL Intergrity exceptions (Foreign key violations)
- **Password Hashing Protection** — Cryptographic salting of users preventing plaintext exposures internally using B-Crypt standards
- **Partial Updates** — PUT endpoints only update non-null fields, allowing targeted modifications
- **Swagger/OpenAPI Documentation** — Auto-generated interactive API docs with JWT auth support (Version 2.0.0)
- **Standardized Responses** — All endpoints return a consistent `ApiResponse<T>` wrapper (`status`, `message`, `data`)
- **Auto Timestamps** — `createdAt` and `updatedAt` fields auto-managed via JPA `@PrePersist` and `@PreUpdate` lifecycle callbacks
- **Environment Config** — Sensitive credentials managed through `.env` files using `java-dotenv`
