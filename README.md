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

## 🐳 Base de datos (Docker Compose)

Crea un archivo `docker-compose.yml` en la raíz del proyecto:

```yaml
version: "3.8"

services:
  postgres:
    image: postgres:15
    container_name: suppliers_db
    restart: always
    environment:
      POSTGRES_DB: suppliers_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: 353431
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
