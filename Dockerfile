FROM openjdk:17-jdk

ADD build/libs/fastfood-fiap-postech.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
