#!/bin/bash

set -e

# === Config ===
DOMAIN="localhost"
KEYSTORE_PASSWORD="predictech"
KEYSTORE_NAME="keystore.p12"
KEY_ALIAS="predictech"

# === Generate TLS cert with mkcert ===
echo "📜 Generating certificates for $DOMAIN using mkcert..."

# Ensure mkcert CA is installed
mkcert -install

# Generate localhost cert and key
mkcert -cert-file "${DOMAIN}.pem" -key-file "${DOMAIN}-key.pem" "$DOMAIN" 127.0.0.1 ::1

# === Convert to PKCS12 ===
echo "🔒 Converting to PKCS12 keystore for Spring Boot..."

openssl pkcs12 -export \
  -in "${DOMAIN}.pem" \
  -inkey "${DOMAIN}-key.pem" \
  -out "$KEYSTORE_NAME" \
  -name "$KEY_ALIAS" \
  -password "pass:$KEYSTORE_PASSWORD"

echo "✅ Keystore created: $KEYSTORE_NAME"
echo "   Password: $KEYSTORE_PASSWORD"