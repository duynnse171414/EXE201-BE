# =========================
# 1️⃣ Build Stage
# =========================
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Copy toàn bộ project vào container
COPY . .

# Cấp quyền thực thi cho mvnw (nếu có)
RUN chmod +x mvnw

# Build file jar, bỏ qua test
RUN ./mvnw clean package -DskipTests


# =========================
# 2️⃣ Run Stage
# =========================
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy file jar từ build stage
COPY --from=builder /app/target/*.jar app.jar

# Render tự truyền biến PORT → Spring Boot nhận qua --server.port=$PORT
CMD ["sh", "-c", "java -jar app.jar --server.port=$PORT"]
