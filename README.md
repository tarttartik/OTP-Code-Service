# OTP-Code-Service
Otp Code Service Spring Boot project for Master's degree course at MEPhI

Project structure:
  - controllers: API controllers include an authentication controller, a common controller for handling user requests concerning OTP codes and a separate admin controller
  - DTO's: stores models for data transfering, such as requests models
  - entities: stores models for project's key entities - OTP code, OTP config and user - and two enums relating to them
  - notification: services for user notification via email, sms, telegram and saving codes to files
  - repositories: JpaRepositories for integration with PostgreSQL DB
  - security: stores files that concern security logic - authentication filter and security configuration
  - services: includes a service for handling  authentication, separate services for JWT tokens' and users managing logic and a service for managing OTP codes
  - CodesLifespanValidator: serves for expired codes identification
  - OtpCodeService: the main file

The project uses Maven as the build tool.
