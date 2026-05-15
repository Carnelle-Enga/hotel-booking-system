# hotel-booking-system

Système de réservation d’hôtel basé sur une architecture microservices avec Spring Boot, Spring Cloud Gateway, Consul, OpenFeign, Resilience4j et Docker.

Le projet est entièrement local, reproductible avec une seule commande et conçu comme projet vitrine pour démontrer une architecture microservices moderne prête pour un entretien technique ou une soutenance.

---

# Architecture du projet

```text
                        +----------------------+
                        |    Gateway Service   |
                        |      Port 8080       |
                        +----------+-----------+
                                   |
             ---------------------------------------------
             |                                           |
             v                                           v

+----------------------+                 +----------------------+
|    Room Service      |                 | Reservation Service  |
|      Port 8081       | <-------------> |      Port 8082       |
| CRUD des chambres    |   OpenFeign     | Gestion réservations |
+----------+-----------+                 +----------+-----------+
           |                                          |
           v                                          v

+----------------------+                 +----------------------+
|      rooms_db        |                 |   reservations_db    |
|     PostgreSQL       |                 |     PostgreSQL       |
+----------------------+                 +----------------------+

                                   |
                                   v

                        +----------------------+
                        | Notification Service |
                        |      Port 8083       |
                        +----------------------+

                                   |
                                   v

                        +----------------------+
                        |        Consul        |
                        |      Port 8500       |
                        +----------------------+
```

---

# Fonctionnalités

## Gestion des chambres

* Création de chambres
* Modification de chambres
* Suppression de chambres
* Consultation des chambres disponibles
* Validation des données

## Gestion des réservations

* Création de réservation
* Vérification de disponibilité en temps réel
* Annulation de réservation
* Gestion des statuts :

    * EN_ATTENTE
    * CONFIRMEE
    * ANNULEE

## Résilience

* Circuit Breaker avec fallback
* Retry automatique
* Gestion propre des erreurs inter-services

## Monitoring

* Actuator
* Endpoint Prometheus
* Logs corrélés avec traceId

## Documentation API

* Swagger/OpenAPI centralisé via la Gateway

---

# Stack technique

## Backend

* Java 21
* Spring Boot
* Spring Data JPA
* Spring Validation
* Spring Cloud Gateway
* Spring Cloud Consul Discovery
* OpenFeign
* Resilience4j
* Flyway

## Base de données

* PostgreSQL 15

## Infrastructure

* Docker
* Docker Compose

## Monitoring

* Spring Boot Actuator
* Prometheus
* Micrometer Tracing

## Tests

* JUnit 5
* Mockito
* H2 Database
* Testcontainers

---

# Structure du projet

```text
hotel-booking-system/
│
├── gateway-service/
├── room-service/
├── reservation-service/
├── notification-service/
│
├── docker-compose.yml
├── README.md
└── postman/
```

---

# Microservices

## room-service

Service responsable de la gestion des chambres.

### Responsabilités

* CRUD Room
* Validation métier
* Disponibilité des chambres

### Base de données

* room_service_db

### Endpoints principaux

| Méthode | Endpoint    | Description           |
| ------- | ----------- | --------------------- |
| POST    | /rooms      | Créer une chambre     |
| GET     | /rooms/{id} | Obtenir une chambre   |
| GET     | /rooms      | Lister les chambres   |
| PUT     | /rooms/{id} | Modifier une chambre  |
| DELETE  | /rooms/{id} | Supprimer une chambre |

---

## reservation-service

Service responsable des réservations.

### Responsabilités

* CRUD Reservation
* Vérification de disponibilité
* Communication avec room-service
* Circuit Breaker

### Base de données

* reservations_service_db

### Endpoints principaux

| Méthode | Endpoint           | Description              |
| ------- | ------------------ | ------------------------ |
| POST    | /reservations      | Créer une réservation    |
| GET     | /reservations/{id} | Obtenir une réservation  |
| GET     | /reservations      | Lister les réservations  |
| PUT     | /reservations/{id} | Modifier une réservation |
| DELETE  | /reservations/{id} | Annuler une réservation  |

---

## notification-service

Service léger simulant l’envoi d’emails.

### Responsabilités

* Écoute des événements
* Logs de notifications
* Simulation d’envoi d’email

---

## gateway-service

Point d’entrée unique du système.

### Responsabilités

* Routage dynamique
* Service Discovery
* Centralisation Swagger

---

# Modèle de données

## Room

```text
id
numero
type (SIMPLE / DOUBLE / SUITE)
prix_par_nuit
disponible
```

## Reservation

```text
id
date_debut
date_fin
statut (EN_ATTENTE / CONFIRMEE / ANNULEE)
room_id
client_email
```

---

# Prérequis

## Windows

* Installer Java 21
* Installer Maven 3.9+
* Installer Docker Desktop

## Linux

```bash
sudo apt update
sudo apt install docker.io docker-compose openjdk-21-jdk maven
```

## macOS

```bash
brew install openjdk@21 maven docker
```

---

# Installation du projet

## 1. Cloner le dépôt

```bash
git clone https://github.com/votre-utilisateur/hotel-booking-system.git
```

```bash
cd hotel-booking-system
```

---

## 2. Construire les services

```bash
mvn clean package
```

---

## 3. Lancer toute la stack

```bash
docker-compose up --build
```

---

# Services disponibles

| Service              | URL                                                                            |
| -------------------- | ------------------------------------------------------------------------------ |
| Gateway              | [http://localhost:8080](http://localhost:8080)                                 |
| Room Service         | [http://localhost:8081](http://localhost:8081)                                 |
| Reservation Service  | [http://localhost:8082](http://localhost:8082)                                 |
| Notification Service | [http://localhost:8083](http://localhost:8083)                                 |
| Consul               | [http://localhost:8500](http://localhost:8500)                                 |
| Swagger UI           | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) |

---

# Service Discovery avec Consul

Le projet utilise Consul pour :

* L’enregistrement automatique des services
* La découverte dynamique
* Le load balancing
* La surveillance de santé

Chaque service s’enregistre automatiquement au démarrage.

---

# Communication inter-services

La communication entre services est réalisée avec OpenFeign.

Exemple :

```java
@FeignClient(name = "room-service")
public interface RoomClient {

    @GetMapping("/rooms/{id}")
    RoomResponse getRoomById(@PathVariable Long id);
}
```

---

# Résilience avec Resilience4j

Le projet utilise Resilience4j pour :

* Circuit Breaker
* Retry
* Fallback

## Exemple de comportement

Si room-service devient indisponible :

* reservation-service ne plante pas
* la réservation passe automatiquement au statut EN_ATTENTE
* les erreurs sont loggées proprement

---

# Monitoring

## Actuator

```bash
curl http://localhost:8081/actuator/health
```

## Prometheus

```bash
curl http://localhost:8081/actuator/prometheus
```

---

# Logs corrélés

Chaque requête possède un `traceId` unique propagé entre les services.

Exemple :

```text
[traceId=45ab78d9]
```

Cela facilite énormément le debugging dans une architecture distribuée.

---

# Docker

## Docker Compose

Le projet peut être démarré avec une seule commande :

```bash
docker-compose up
```

Tous les éléments sont lancés automatiquement :

* Gateway
* Room Service
* Reservation Service
* Notification Service
* PostgreSQL
* Consul

---

# Swagger / OpenAPI

Documentation centralisée disponible sur :

```text
http://localhost:8080/swagger-ui.html
```

---

# Tests

## Exécuter les tests

```bash
mvn test
```

## Types de tests

* Tests unitaires
* Tests d’intégration
* Validation des contrôleurs
* Tests de communication inter-services

---

# Choix techniques

| Technologie    | Justification                       |
| -------------- | ----------------------------------- |
| Spring Boot    | Rapidité de développement           |
| PostgreSQL     | Base relationnelle robuste          |
| Flyway         | Versionnement propre des migrations |
| OpenFeign      | Communication déclarative           |
| Consul         | Découverte de services              |
| Resilience4j   | Tolérance aux pannes                |
| Docker Compose | Reproductibilité locale             |

---

# Objectifs pédagogiques

Ce projet permet de maîtriser :

* Architecture microservices
* Communication inter-services
* Service Discovery
* API Gateway
* Résilience
* Monitoring
* Dockerisation
* Observabilité
* Bonnes pratiques REST

---

# Commandes utiles

## Voir les logs

```bash
docker-compose logs -f
```

## Arrêter les conteneurs

```bash
docker-compose down
```

## Reconstruire les images

```bash
docker-compose up --build
```

---

# Auteur

Projet réalisé dans le cadre d’une formation avancée sur les architectures microservices avec Spring Boot.
