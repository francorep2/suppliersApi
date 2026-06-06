# 🚀 Suppliers TGS API

Backend API para gestión de proveedores, autenticación JWT y sincronización de datos, desarrollado con **Spring Boot + PostgreSQL + Docker**.

---

## 🧠 Tech Stack

![Java](https://img.shields.io/badge/Java-17-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED)
![JWT](https://img.shields.io/badge/Auth-JWT-orange)

---

## ⚙️ Requisitos

- Java 17
- Maven 3.8+
- Docker & Docker Compose
- Git

---

## 🐳 Docker Compose (Base de datos)

version: '3.9'

services:

  postgres:
    image: postgres:16
    container_name: suppliers-db
    restart: always

    env_file:
      - .env

    environment:
      POSTGRES_DB: ${DB_NAME}
      POSTGRES_USER: ${DB_USER}
      POSTGRES_PASSWORD: ${DB_PASSWORD}

    ports:
      - "5432:5432"

    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:

---

## 🐳 Docker Commands

### Levantar base de datos
docker-compose up -d

### Bajar base de datos
docker-compose down

### Reset completo (borra datos)
docker-compose down -v

---

## 🧪 Variables de entorno (.env)

DB_URL= 
DB_USER= 
DB_NAME= 
DB_PASSWORD=YOUR_PASSWORD

JWT_SECRET=
JWT_EXPIRATION=3600000

PORT=8080

ENCRYPTION_KEY= 
ALGORITHM=AES

FRONT_URL= 

---

## ▶️ Backend (Spring Boot)

mvn spring-boot:run

mvn clean install
mvn spring-boot:run

---

## 🌐 Base URL

http://localhost:8080

---

## 🔐 Auth

Authorization: Bearer <token>

---

## 💻 VS Code Extensions

- Extension Pack for Java
- Spring Boot Extension Pack
- Lombok Annotations Support
- Docker
- GitLens
- PostgreSQL
- REST Client

---

## 📦 Dependencias

Spring Boot Web
Spring Boot Security
Spring Data JPA
PostgreSQL Driver
JWT (jjwt + nimbus)
Lombok
dotenv-java

---

## 🧠 Notas

Arquitectura REST stateless con JWT
AuthenticationPrincipal para usuario logueado
JPA + PostgreSQL
Variables de entorno con .env

---

## 🚀 Comandos rápidos

docker-compose up -d
docker-compose down
docker-compose down -v
mvn spring-boot:run

---

## 📌 Autor

Franco Repetto
