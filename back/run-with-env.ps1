#!/usr/bin/env pwsh
# PowerShell script to run the application with environment variables

# Set custom environment variables
$env:APP_SECURITY_MIN_PASSWORD_LENGTH = "10"
$env:APP_SECURITY_PASSWORD_CHECK_ENABLED = "true"

# Database credentials from environment (safer alternative)
# $env:SPRING_DATASOURCE_USERNAME = "your_db_username"
# $env:SPRING_DATASOURCE_PASSWORD = "your_db_password"

# More secure way to handle JWT secret (would be better to use a secret manager in production)
# $env:JWT_SECRET = "your-secure-jwt-secret-key-here"

Write-Host "Starting application with custom environment variables..."
Write-Host "Min Password Length: $env:APP_SECURITY_MIN_PASSWORD_LENGTH"
Write-Host "Password Check Enabled: $env:APP_SECURITY_PASSWORD_CHECK_ENABLED"

# Run the application with Maven
./mvnw spring-boot:run
