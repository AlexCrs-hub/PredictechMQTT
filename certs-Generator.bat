@echo off
setlocal enabledelayedexpansion

:: === Config ===
set DOMAIN=localhost
set KEYSTORE_PASSWORD=predictech
set KEYSTORE_NAME=keystore.p12
set KEY_ALIAS=predictech

:: === Check mkcert ===
where mkcert >nul 2>nul
if errorlevel 1 (
    echo ❌ mkcert not found. Please install with: choco install mkcert
    exit /b 1
)

:: === Check openssl ===
where openssl >nul 2>nul
if errorlevel 1 (
    echo ❌ openssl not found. Please install with: choco install openssl
    exit /b 1
)

echo 📜 Generating certificate for %DOMAIN%...

:: === Generate CA and certs ===
mkcert -install
mkcert -cert-file %DOMAIN%.pem -key-file %DOMAIN%-key.pem %DOMAIN% 127.0.0.1 ::1

:: === Convert to PKCS12 keystore ===
echo 🔒 Creating PKCS12 keystore for Spring Boot...

openssl pkcs12 -export ^
  -in %DOMAIN%.pem ^
  -inkey %DOMAIN%-key.pem ^
  -out %KEYSTORE_NAME% ^
  -name %KEY_ALIAS% ^
  -passout pass:%KEYSTORE_PASSWORD%

if exist %KEYSTORE_NAME% (
    echo ✅ Keystore created: %KEYSTORE_NAME%
    echo    Password: %KEYSTORE_PASSWORD%
) else (
    echo ❌ Failed to create keystore.
    exit /b 1
)