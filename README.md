# 🏋️ FitVerse Microservices

A modern, full-stack fitness application built with **Java Spring Boot microservices architecture**. This project demonstrates enterprise-grade patterns including service discovery, API gateway, centralized configuration, and AI-powered features.

> **Note:** Frontend UI was generated using AI assistance.

---

## 🏗️ Architecture Overview

```
┌─────────────────┐
│   Frontend      │ (React/JavaScript)
└────────┬────────┘
         │
    ┌────▼────────┐
    │   Gateway   │ (Spring Cloud Gateway)
    └────┬────────┘
         │
    ┌────▼─────────┐
    │   Eureka     │ (Service Discovery)
    └──────────────┘
         │
    ┌────┴──────────────────────┐
    │              |             │
┌───▼──────┐  ┌────▼────────┐  ┌───▼──────┐
│  User    │  │  Activity   │  │   AI     │
│ Service  │  │  Service    │  │ Service  │
└──────────┘  └─────────────┘  └──────────┘
```

---

## 🚀 Services

| Service | Description | Port |
|---------|-------------|------|
| **Config Server** | Centralized configuration management | 8888 |
| **Eureka Server** | Service discovery and registration | 8761 |
| **API Gateway** | Single entry point, routing & load balancing | 8080 |
| **User Service** | User management and authentication | 8081 |
| **Activity Service** | Workout tracking and activity management | 8082 |
| **AI Service** | ML-powered fitness recommendations | 8083 |
| **Frontend** | React-based user interface | 5173 |

---

## 🛠️ Tech Stack

### Backend
- **Java 17+** with Spring Boot 3.x
- **Spring Cloud** (Gateway, Config, Eureka)
- **RabbitMQ 4.x** - Message broker
- **Keycloak** - Identity and access management

### Frontend
- **JavaScript/React** - Modern UI framework
- **HTML/CSS** - Styling and markup

### Infrastructure
- **Docker** - Containerization
- **Maven** - Build and dependency management

---

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker Desktop
- Node.js 16+ (for frontend)
- Git

---

## ⚙️ Installation & Setup

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/Surendra1341/FitVerse-Microservice.git
cd FitVerse-Microservice
```

### 2️⃣ Start Required Infrastructure

**RabbitMQ 4.x** (Message Broker)
```bash
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4-management
```
- **AMQP Port:** `5672`
- **Management UI:** http://localhost:15672 (guest/guest)

**Keycloak** (Authentication)
```bash
docker run -p 127.0.0.1:8181:8080 \
  -e KC_BOOTSTRAP_ADMIN_USERNAME=admin \
  -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:26.4.0 start-dev
```
- **Admin Console:** http://localhost:8181 (admin/admin)

### 3️⃣ Build All Services

```bash
mvn clean install
```

### 4️⃣ Start Services (In Order)

```bash
# 1. Config Server
cd configserver && mvn spring-boot:run

# 2. Eureka Server
cd eureka && mvn spring-boot:run

# 3. API Gateway
cd gateway && mvn spring-boot:run

# 4. Microservices (in parallel)
cd userservice && mvn spring-boot:run
cd activityservice && mvn spring-boot:run
cd aiservice && mvn spring-boot:run
```

### 5️⃣ Start Frontend

```bash
cd fitness-app-frontend
npm install
npm start
```

Access the app at: **http://localhost:3000**

---

## 🎯 Key Features

- 🔐 **Secure Authentication** - Keycloak integration
- 📊 **Activity Tracking** - Log workouts and monitor progress
- 🤖 **AI Recommendations** - Personalized fitness suggestions
- 🔄 **Event-Driven** - Asynchronous messaging with RabbitMQ
- 🎨 **Modern UI** - Responsive React frontend
- 📡 **Service Discovery** - Automatic service registration
- ⚡ **API Gateway** - Unified API entry point

---

## 📚 API Documentation

Once services are running, check:
- **Eureka Dashboard:** http://localhost:8761
- **RabbitMQ Management:** http://localhost:15672
- **Keycloak Console:** http://localhost:8181
- **Gateway Routes:** http://localhost:8080

---

## 🧪 Testing

Run tests for all services:

```bash
mvn test
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

---

## 📝 License

This project is available under the MIT License - see [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Surendra**
- GitHub: [@Surendra1341](https://github.com/Surendra1341)
- Project: [FitVerse-Microservice](https://github.com/Surendra1341/FitVerse-Microservice)

---

## 🙏 Acknowledgments

- Backend architecture inspired by Spring Cloud best practices
- Frontend UI generated with AI assistance

---

<div align="center">
  <sub>Built with ❤️ using Spring Boot and React</sub>
</div>
