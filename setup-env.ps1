# ============================================
# GoCart - NeonDB Environment Setup Script
# ============================================
# This script sets environment variables for NeonDB PostgreSQL connection
# Run this before running: mvn spring-boot:run
#
# Usage: .\setup-env.ps1

Write-Host "╔════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║         GoCart - NeonDB Environment Configuration         ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# ============================================
# NeonDB Configuration - UPDATE THESE VALUES
# ============================================

# Get your connection string from https://neon.tech
# Format: postgresql://user:password@ep-xxx.neon.tech/dbname

$DATABASE_URL = "postgresql://user:password@ep-xxx.neon.tech/dbname"
$DB_USERNAME = "your_username"
$DB_PASSWORD = "your_password"
$SERVER_PORT = "8080"

# ============================================
# Set Environment Variables
# ============================================

$env:DATABASE_URL = $DATABASE_URL
$env:DB_USERNAME = $DB_USERNAME
$env:DB_PASSWORD = $DB_PASSWORD
$env:SERVER_PORT = $SERVER_PORT

Write-Host "✓ Environment variables configured:" -ForegroundColor Green
Write-Host ""
Write-Host "  DATABASE_URL  : $DATABASE_URL" -ForegroundColor Yellow
Write-Host "  DB_USERNAME   : $DB_USERNAME" -ForegroundColor Yellow
Write-Host "  DB_PASSWORD   : $DB_PASSWORD" -ForegroundColor Yellow
Write-Host "  SERVER_PORT   : $SERVER_PORT" -ForegroundColor Yellow
Write-Host ""

# ============================================
# Verify Variables Are Set
# ============================================

Write-Host "Verifying environment variables..." -ForegroundColor Cyan
Write-Host ""

if ($env:DATABASE_URL) {
    Write-Host "✓ DATABASE_URL is set" -ForegroundColor Green
} else {
    Write-Host "✗ DATABASE_URL is NOT set" -ForegroundColor Red
}

if ($env:DB_USERNAME) {
    Write-Host "✓ DB_USERNAME is set" -ForegroundColor Green
} else {
    Write-Host "✗ DB_USERNAME is NOT set" -ForegroundColor Red
}

if ($env:DB_PASSWORD) {
    Write-Host "✓ DB_PASSWORD is set" -ForegroundColor Green
} else {
    Write-Host "✗ DB_PASSWORD is NOT set" -ForegroundColor Red
}

if ($env:SERVER_PORT) {
    Write-Host "✓ SERVER_PORT is set" -ForegroundColor Green
} else {
    Write-Host "✗ SERVER_PORT is NOT set" -ForegroundColor Red
}

Write-Host ""
Write-Host "╔════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║  Ready to run: mvn spring-boot:run                        ║" -ForegroundColor Cyan
Write-Host "║  Or build and run: mvn clean package && java -jar ...     ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# ============================================
# Optional: Automatically run Maven
# ============================================
# Uncomment the line below to automatically run Maven after setting variables
# mvn spring-boot:run

# Or prompt user to run Maven
Write-Host "Would you like to run Maven now? (y/n)" -ForegroundColor Yellow
$response = Read-Host

if ($response -eq 'y' -or $response -eq 'Y') {
    Write-Host ""
    Write-Host "Starting Maven build and run..." -ForegroundColor Cyan
    Write-Host ""
    mvn spring-boot:run
} else {
    Write-Host ""
    Write-Host "Setup complete! Run the following command when ready:" -ForegroundColor Green
    Write-Host "mvn spring-boot:run" -ForegroundColor White
    Write-Host ""
}
