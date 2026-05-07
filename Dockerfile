# ===== Этап 1: сборка =====
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build

# Сначала копируем только pom.xml и mvnw — для кэширования зависимостей.
# Пока pom.xml не меняется, Maven не будет переcкачивать всё заново.
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Копируем исходники и собираем jar
COPY src src
RUN ./mvnw clean package -DskipTests -B

# ===== Этап 2: рантайм =====
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Запускаем не от root — security best practice
RUN addgroup -S app && adduser -S app -G app
USER app

COPY --from=builder /build/target/*.jar app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-XX:+UseSerialGC", "-jar", "app.jar"]