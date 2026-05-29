# 🏢 4bitLabs Employee Management System

A full-stack Employee Management System built with **React + Vite** (frontend) and **Spring Boot** (backend), containerized with Docker.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [Clone the Repository](#1-clone-the-repository)
  - [Configure Environment](#2-configure-environment)
  - [Run with Docker](#3-run-with-docker)
- [Default Admin Credentials](#default-admin-credentials)
- [How to Use](#how-to-use)
- [API Overview](#api-overview)
- [Role & Permissions](#roles--permissions)
- [Troubleshooting](#troubleshooting)

---

## Overview

This system allows a company to manage its employees across four roles:

| Role | Responsibilities |
|------|-----------------|
| **Admin** | Creates and manages Trainers, Analysts, Counsellors |
| **Analyst** | Creates and manages Batches, assigns Trainers |
| **Counsellor** | Creates and manages Students, assigns them to Batches |
| **Trainer** | Tracks Batch Progress, uploads session documents |

---

## Tech Stack

**Frontend**
- React 19 + Vite
- Tailwind CSS 4
- React Router v7
- Axios (API communication)
- React Hot Toast (notifications)

**Backend**
- Java 17 + Spring Boot 3.5
- Spring Security + JWT Authentication
- Spring Data JPA + Hibernate
- MySQL 8.0
- Cloudinary (file/document uploads)

**DevOps**
- Docker + Docker Compose

---

## Project Structure

```
4bitLabs-Employee-Management-System/
├── frontend/                  # React + Vite app
│   ├── src/
│   │   ├── components/        # Reusable UI components
│   │   ├── pages/             # Role-based dashboards
│   │   ├── hooks/             # Custom React hooks
│   │   ├── services/          # Axios API calls
│   │   └── utils/             # Helpers (notify, storage)
│   ├── Dockerfile
│   └── .env                   # Frontend env config
│
├── backend/                   # Spring Boot app
│   ├── src/main/java/
│   │   └── com/fourbitlabs/employee_management_system/
│   │       ├── controller/    # REST API endpoints
│   │       ├── service/       # Business logic
│   │       ├── entity/        # JPA entities
│   │       ├── dto/           # Request/Response DTOs
│   │       ├── security/      # JWT filter & config
│   │       └── config/        # CORS, Cloudinary, DataInitializer
│   ├── src/main/resources/
│   │   ├── application.properties          # Base config
│   │   ├── application-dev.properties      # Dev overrides
│   │   └── application-prod.properties     # Prod overrides
│   └── Dockerfile
│
└── compose.yml                # Docker Compose (runs everything)
```

---

## Prerequisites

Make sure you have these installed before starting:

| Tool | Version | Check |
|------|---------|-------|
| Docker | 20+ | `docker --version` |
| Docker Compose | 2+ | `docker compose version` |
| Git | any | `git --version` |

> ✅ You do **NOT** need Java, Node.js, or MySQL installed locally. Docker handles everything.

---

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/vaibhavCodes18/4bitLabs-Employee-Management-System.git
cd 4bitLabs-Employee-Management-System
```

### 2. Configure Environment

The `compose.yml` file already has default values for local development. Open it and update these fields with your actual credentials:

```yaml
# backend environment in compose.yml
DB_PASSWORD: yourpassword           # Choose any MySQL password
CLOUDINARY_CLOUD_NAME: your_name    # From cloudinary.com dashboard
CLOUDINARY_API_KEY: your_key
CLOUDINARY_API_SECRET: your_secret
JWT_SECRET: "your-base64-encoded-secret-min-32-chars"
```

> 💡 **How to generate a JWT secret:**
> ```bash
> echo -n "your-random-phrase-here-make-it-long" | base64 -w 0
> ```
> Paste the output as your `JWT_SECRET` value.

Also make sure `frontend/.env` has:
```env
VITE_API_URL=http://localhost:8082/api
```

### 3. Run with Docker

```bash
# Start all services (MySQL + Backend + Frontend)
docker compose up --build
```

Wait for all three services to be ready. You'll see:

```
✅ 4bitlabs-mysql    → MySQL ready on port 3306
✅ 4bitlabs-backend  → Spring Boot started on port 8082
✅ 4bitlabs-frontend → Vite dev server ready on port 5173
```

Then open your browser at:
```
http://localhost:5173
```

---

## Default Admin Credentials

On first startup, the system automatically creates a default admin account:

| Field | Value |
|-------|-------|
| Email | `admin@4bitlabs.com` |
| Password | `Admin@123` |

> ⚠️ Change this password after your first login in a production environment.

---

## How to Use

### Step 1 — Login as Admin
Go to `http://localhost:5173` → Login with admin credentials → You'll land on the Admin Dashboard.

### Step 2 — Create Employees
From the Admin Dashboard:
- Go to **Trainers** → Add Trainer
- Go to **Analysts** → Add Analyst
- Go to **Counsellors** → Add Counsellor

Each new employee gets an email + password they can use to log in.

### Step 3 — Create Batches (as Analyst)
Login as an Analyst → Go to **Batches** → Create a Batch → Assign a Trainer.

### Step 4 — Add Students (as Counsellor)
Login as a Counsellor → Go to **Students** → Add Student → Assign to a Batch.

### Step 5 — Track Progress (as Trainer)
Login as a Trainer → Go to **Batch Progress** → Add session notes and upload documents.

---

## API Overview

Base URL: `http://localhost:8082/api`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/auth/login` | Login | ❌ |
| POST | `/auth/refresh` | Refresh token | ❌ |
| POST | `/auth/logout` | Logout | ❌ |
| GET | `/admin/trainers` | List trainers | ✅ ADMIN |
| POST | `/admin/trainers` | Create trainer | ✅ ADMIN |
| PUT | `/admin/trainers/{id}` | Update trainer | ✅ ADMIN |
| DELETE | `/admin/trainers/{id}` | Delete trainer | ✅ ADMIN |
| GET | `/analyst/batches` | List batches | ✅ ANALYST |
| POST | `/analyst/batches` | Create batch | ✅ ANALYST |
| GET | `/counsellor/students` | List students | ✅ COUNSELLOR |
| POST | `/counsellor/students` | Create student | ✅ COUNSELLOR |
| POST | `/assignments` | Assign student to batch | ✅ |
| POST | `/trainer/batch-progress` | Add batch progress | ✅ TRAINER |

> Full API docs available at: `http://localhost:8082/swagger-ui.html`

---

## Roles & Permissions

```
Admin
 ├── Create / Edit / Delete Trainers
 ├── Create / Edit / Delete Analysts
 └── Create / Edit / Delete Counsellors

Analyst
 ├── Create / Edit / Delete Batches
 └── View Trainers (for assignment)

Counsellor
 ├── Create / Edit / Delete Students
 ├── Assign Students to Batches
 └── Transfer Students between Batches

Trainer
 ├── View assigned Batches
 └── Add / Edit / Delete Batch Progress entries
```

---

## Troubleshooting

### Port 3306 already in use
```bash
sudo systemctl stop mysql    # Stop local MySQL
sudo fuser -k 3306/tcp       # Force kill if needed
docker compose up
```

### Backend fails to start (DataSource error)
Make sure `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` are set correctly in `compose.yml` and match the `db` service credentials.

### Login returns 500 error
- Check `JWT_SECRET` is a valid Base64 string (min 32 chars when decoded)
- Generate one with: `echo -n "your-long-phrase" | base64 -w 0`

### Frontend shows blank / can't reach API
- Make sure `frontend/.env` has `VITE_API_URL=http://localhost:8082/api`
- Check backend is running: `docker logs 4bitlabs-backend --tail 20`

### Stop all containers
```bash
docker compose down        # Stop containers (keeps data)
docker compose down -v     # Stop + delete all data (fresh start)
```

---

## Production Deployment

This project is deployed on:
- **Frontend** → [Vercel](https://vercel.com)
- **Backend** → [Railway](https://railway.app)
- **Database** → Railway MySQL
- **File Storage** → [Cloudinary](https://cloudinary.com)

For production, use `application-prod.properties` which uses `ddl-auto=validate` and secure cookie settings.

---

## License

This project is built for **4bitLabs** internal use.
