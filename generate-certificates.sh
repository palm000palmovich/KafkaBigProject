#!/bin/bash

# Создаем директорию для SSL
mkdir -p kafka-ssl
cd kafka-ssl

echo "Generating Root CA..."
# Генерируем приватный ключ CA
openssl genrsa -out ca-key.pem 4096

# Генерируем сертификат CA
openssl req -new -x509 -key ca-key.pem -out ca-cert.pem -days 365 \
  -subj "/C=RU/ST=Moscow/L=Moscow/O=OnlineShop/CN=Kafka-Root-CA"

echo "Root CA generated successfully"

# Массив с именами брокеров
BROKERS=("kafka-0" "kafka-1" "kafka-2" "kafka-second-0" "kafka-second-1" "kafka-second-2")

for BROKER in "${BROKERS[@]}"; do
  echo "Generating certificate for $BROKER"

  # Генерируем приватный ключ
  openssl genrsa -out $BROKER-key.pem 4096

  # Создаем запрос на подпись сертификата (CSR)
  openssl req -new -key $BROKER-key.pem -out $BROKER-cert.csr \
    -subj "/C=RU/ST=Moscow/L=Moscow/O=OnlineShop/CN=$BROKER" \
    -addext "subjectAltName=DNS:$BROKER,DNS:localhost,IP:127.0.0.1"

  # Подписываем сертификат с помощью CA
  openssl x509 -req -in $BROKER-cert.csr -CA ca-cert.pem -CAkey ca-key.pem \
    -CAcreateserial -out $BROKER-cert.pem -days 365 -sha256 \
    -extfile <(printf "subjectAltName=DNS:$BROKER,DNS:localhost,IP:127.0.0.1")

  # Создаем keystore для Java приложений
  openssl pkcs12 -export -in $BROKER-cert.pem -inkey $BROKER-key.pem \
    -out $BROKER-keystore.p12 -name $BROKER -password pass:password123

  echo "Certificate for $BROKER generated successfully"
done

# Создаем клиентский сертификат для приложений
echo "Generating client certificate..."
openssl genrsa -out client-key.pem 4096
openssl req -new -key client-key.pem -out client-cert.csr \
  -subj "/C=RU/ST=Moscow/L=Moscow/O=OnlineShop/CN=client-app" \
  -addext "subjectAltName=DNS:client-app,DNS:localhost,IP:127.0.0.1"

openssl x509 -req -in client-cert.csr -CA ca-cert.pem -CAkey ca-key.pem \
  -CAcreateserial -out client-cert.pem -days 365 -sha256 \
  -extfile <(printf "subjectAltName=DNS:client-app,DNS:localhost,IP:127.0.0.1")

openssl pkcs12 -export -in client-cert.pem -inkey client-key.pem \
  -out client-keystore.p12 -name client-app -password pass:password123

# Создаем truststore с корневым CA для Java приложений
echo "Generating truststore..."
keytool -import -trustcacerts -alias ca-root -file ca-cert.pem \
  -keystore client-truststore.jks -storepass password123 -noprompt

# Копируем truststore для каждого брокера
for BROKER in "${BROKERS[@]}"; do
  cp client-truststore.jks $BROKER-truststore.jks
done

echo "All certificates generated successfully in kafka-ssl/ directory"
echo "Generated files:"
ls -la

cd ..