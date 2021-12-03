# Barter Exchange
Barter is a system of exchange in which participants in a transaction directly exchange goods or services for other goods or services without using a medium of exchange, such as money - [Wikipedia](https://en.wikipedia.org/wiki/Barter).

### Why Barter Exchange?

This project has been designed and implemented for a Master's Thesis. It allows registered users for barter exchange, i.e., adding ads with products intended to exchange and respond to these ads.

### Tech
- Java 8 (Amazon Corretto 8)
- Spring
- Firebase
- Postgres
- Apache Tomcat 8.5.73
- Docker
- ES6
- Webpack
- Angular

### Simplified static model representing the business logic
![static model](https://raw.githubusercontent.com/gpiecyk/barter-exchange/main/Server/src/main/resources/images/StaticModel.png)

### Database Schema
![database schema](https://raw.githubusercontent.com/gpiecyk/barter-exchange/main/Server/src/main/resources/images/DatabaseSchema.png)

# Getting Started

### Configuration:

#### Email Notifications
The system has email notifications built-in. The current implementation has been tested with Gmail. To add a new email address, `email.properties` has to be changed. Additionally, if Gmail is used, [less secure app access](https://support.google.com/accounts/answer/6010255) should be turned on. Otherwise, Google will block access.

#### Push Notifications (Firebase)
[Firebase Realtime Database](https://firebase.google.com/products/realtime-database) is used to provide realtime push notifications.
To set it up:
1. Create a Firebase project.
2. In the project settings, go to Service Accounts and generate new private key.
3. The generated file should replace current `Barter Exchange-f7xxxxxxxx.json`.
4. Add necessary changes in `FirebaseUtil.java` and `index.js`.

## Build
#### Requirements:
- Amazon Corretto 8 JDK
- Maven
- Node.js

#### Web
In `./Web/src/main/webapp` run the following commands:
```
npm run install-modules
npm run build
```
#### Server
On the root `pom.xml` run:
```
mvn clean install
```

## Deployment
Start docker environments (in `./Docker`):
```
docker-compose up -d
```
Database schema migration is executed automatically via Docker volume `docker-entrypoint-initdb.d`.

Web and Server are automatically deployed to Tomcat in Docker. The application should be running under:
```
http://localhost:8080/app/app/#/
```
