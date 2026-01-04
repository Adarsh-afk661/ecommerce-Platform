# Quick NeonDB Setup for GoCart
# Replace the values below with your actual NeonDB credentials
# Then run: .\setup-env-quick.ps1

# Get your connection string from https://neon.tech
$env:DATABASE_URL = "postgresql://user:password@ep-xxx.neon.tech/dbname"
$env:DB_USERNAME = "your_username"
$env:DB_PASSWORD = "your_password"
$env:SERVER_PORT = "8080"

Write-Host "✓ Environment variables set" -ForegroundColor Green
Write-Host "✓ DATABASE_URL: $($env:DATABASE_URL)" -ForegroundColor Green
Write-Host "✓ DB_USERNAME: $($env:DB_USERNAME)" -ForegroundColor Green
Write-Host "✓ SERVER_PORT: $($env:SERVER_PORT)" -ForegroundColor Green
Write-Host ""
Write-Host "Ready! Run: mvn spring-boot:run" -ForegroundColor Yellow
