# Cloud storage backend
REST-сервис, предоставляющий интерфейс для работы с файлами.

## Описание
При создании приложения были использованы:
- Maven
- MySQL
- Spring Boot
- Spring Data JPA
- Slf4j и logback
- JUnit, Mockito и MockMvc
- Liquibase
- Docker

## Запуск приложения
- выполнить команду mvn clean install
- выполнить команду docker build -t cloud_app .
- скачать [FRONT](./netology-diplom-frontend), добавить в корень файл Docker и вставить в него:
```
  FROM node:10
  WORKDIR /usr/src/app
  COPY package.json ./
  RUN npm install
  COPY . .
  EXPOSE 8081
  CMD ["npm", "run", "serve"]
```
- выполнить docker build -t cloud-front .
- выполнить docker-compose up
- 

