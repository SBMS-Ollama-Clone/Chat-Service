# Mirocservice with for Chat Ollama LLM Project

## Chat Service

This service is responsible for handling chat of users and save in the `chat-service` DB.

[//]: # ([![Build Status]&#40;https://travis-ci.com/SBMS-Ollama-Clone/Chat-Service.svg?branch=main&#41;]&#40;https://travis-ci.com/SBMS-Ollama-Clone/Chat-Service&#41;)
[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/dashboard?id=SBMS-Ollama-Clone_Chat-Service)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=SBMS-Ollama-Clone_Chat-Service&metric=alert_status)](https://sonarcloud.io/dashboard?id=SBMS-Ollama-Clone_Chat-Service)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=SBMS-Ollama-Clone_Chat-Service&metric=bugs)](https://sonarcloud.io/dashboard?id=SBMS-Ollama-Clone_Chat-Service)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=SBMS-Ollama-Clone_Chat-Service&metric=coverage)](https://sonarcloud.io/dashboard?id=SBMS-Ollama-Clone_Chat-Service)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=SBMS-Ollama-Clone_Chat-Service&metric=ncloc)](https://sonarcloud.io/dashboard?id=SBMS-Ollama-Clone_Chat-Service)


### Getting Started

1. To build and run sample applications you need to have Maven, JDK22 and Docker. However, the simplest way to start
   with it is through any IDE like Intellij or Eclipse.
2. First, you have to run require utility services on Docker container, go to `DevOps-Deployment` folder and run the
   following command:
    ```bash
    docker-compose up -d
    ```
3. Then you can compile your application with Maven `mvn clean install package` command.
4. Or you can run directly from your IDE by running the `AuthServiceApplication.java` file.

### Architecture

Our sample microservices-based system consists of the following modules:

- **Gateway Service** - the main entry point into the system. This is where all incoming requests are routed to the
  appropriate microservice. Port 8880.
- **Config Server** - responsible for keeping the configuration of all services in the system. Port 8888.
- **Auth Service** - responsible for user authentication and authorization. Port 8888.
- **Chat Service** - responsible for handling chat of users and save in the `chat-service` DB. Port 9990.
- **Content Service** - responsible for handling chat contents and save in the `content-service` DB. Port 9991.
- **Setting Service** - responsible for handling user settings and save in the `setting-service` DB. Port 9992.
- **Mail Service** - responsible for sending email to the user when they register. Port 9995.
- **Notification Service** - responsible for sending notification to the user when they CRUD on chat. Port 9996.
- **Hashicorp Consul Discovery Service** - responsible for service discovery and registration. Port 8500. (Using Docker)

### Author

[![Email](https://img.shields.io/badge/Email-Kimleang-blue?style=flat&logo=gmail)](mailto:kimleang.srd@gmail.com)<br/>
[![Website](https://img.shields.io/badge/Website-Kimleang-blue?style=flat&logo=google-chrome)](https://kkimleang.com)<br/>
[![Github](https://img.shields.io/badge/Github-Kimleang-blue?style=flat&logo=github)](https://github.com/KimleangSama)<br/>
[![Google Scholar](https://img.shields.io/badge/Google%20Scholar-Kimleang-blue?style=flat&logo=google-scholar)](https://scholar.google.com/citations?user=j67umTIAAAAJ&hl=en&oi=ao)<br/>
