``` 
script for migrate sql:
mvn clean compile \
-Dflyway.user=postgres \
-Dflyway.password=admin123 \
-Dflyway.url=jdbc:postgresql://localhost:5432/postgres \
flyway:migrate
.
for clean table:
mvn clean compile \
-Dflyway.user=postgres \
-Dflyway.password=admin123 \
-Dflyway.schemas=public,perpustakaan \
-Dflyway.url=jdbc:postgresql://localhost:5432/postgres \
flyway:clean or migrate
```
