#!/bin/bash

echo "Converting P12 certificates to JKS format..."

cd kafka-ssl

BROKERS=("kafka-0" "kafka-1" "kafka-2" "kafka-second-0" "kafka-second-1" "kafka-second-2")

for BROKER in "${BROKERS[@]}"; do
  echo "Converting certificate for $BROKER"

  # Конвертируем P12 в JKS для keystore
  keytool -importkeystore \
    -srckeystore $BROKER-keystore.p12 \
    -srcstoretype PKCS12 \
    -srcstorepass password123 \
    -destkeystore $BROKER-keystore.jks \
    -deststoretype JKS \
    -deststorepass password123 \
    -noprompt

  # Копируем truststore (он уже в JKS формате)
  cp client-truststore.jks $BROKER-truststore.jks

  echo "Certificate for $BROKER converted successfully"
done

# Конвертируем клиентский сертификат
echo "Converting client certificate..."
keytool -importkeystore \
  -srckeystore client-keystore.p12 \
  -srcstoretype PKCS12 \
  -srcstorepass password123 \
  -destkeystore client-keystore.jks \
  -deststoretype JKS \
  -deststorepass password123 \
  -noprompt

echo "All certificates converted to JKS format"
cd ..