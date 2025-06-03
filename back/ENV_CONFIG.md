# Environment Variables Configuration Guide

## Overview
This application supports configuration through environment variables, which is particularly useful for sensitive information like passwords and security settings.

## Supported Environment Variables

### Database Configuration
```
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/mdd_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_password
```

### Security Configuration
```
APP_SECURITY_MIN_PASSWORD_LENGTH=8
APP_SECURITY_PASSWORD_CHECK_ENABLED=true
```

### JWT Configuration
```
JWT_SECRET=your_secret_key
JWT_EXPIRATION=86400000
```

## How to Use

### In Development
Use the provided PowerShell script `run-with-env.ps1` which sets environment variables before running the application.

### In Production
Set environment variables at the system level or use a proper secrets management solution.

## Spring's Property Resolution Order
Spring Boot applications resolve properties in this order:
1. Command line arguments
2. Java System properties
3. OS environment variables
4. Application properties files

This means environment variables will override values in the application.properties files.
