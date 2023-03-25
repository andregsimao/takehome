# takehome
API to retrieve information about countries' geography. 
 
* [Spring Boot 3.0.4](https://start.spring.io/)
* [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

# Build & Run 

* run spring-boot application
```bash
./gradlew bootRun
```

* build and execute jar
```bash
./gradlew build
java -jar build/libs/takehome-{version}.jar
```

* run tests
```bash
./gradlew test
```

* build OCI image
  Spring Boot added support for buildpacks in version 2.3, so issue the following command in order to build an image ready to run:
```bash
./gradlew bootBuildImage
```

* run the image inside as a container
```bash
docker run -d -p 8080:8080 takehome:{version}
```