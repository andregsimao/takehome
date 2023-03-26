# takehome
API that receives a list of country codes as inputs and retrieve the countries that are in the same continents as the given countries.
This API utilizes [Trevorblades endpoint](https://countries.trevorblades.com/graphql) to acquire the information needed.
[Annexare project](https://annexare.github.io/Countries/) is used as data source for the countries as described in [Trevorblades repository](https://github.com/trevorblades/countries)  

<hr>

* [Spring Boot 3.0.4](https://start.spring.io/)
* [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

# Example

## Finding the countries in the same continent that the countries WF (Wallis and Futuna) and BV (Bouvet Island)

### POST request to localhost:8080/api/countries/same-continent

### Input
The codes or names of the countries (case-insensitive).
As an example, a possible input is WF (Wallis and Futuna) and BV (Bouvet Island)

```json
{
  "codes": ["BV", "WF"] 
}
```

Alternatively, the input can be also the name of the countries (case-insensitive):

```json
{
  "codes": ["Wallis and Futuna", "Bouvet Island"] 
}
```

### Output
The codes of the countries in the same continent of the input countries. 
As WF (Wallis and Futuna) is in Oceania and BV (Bouvet Island) is in Antarctica the result will be all the countries 
in these two continents except for the input countries.

```json
{
  "same-continent-countries": ["AS", "AU", "CK", "FJ", "FM", "GU", "KI", "MH", "MP", "NC", "NF", "NR", "NU", "NZ", "PF", "PG", "PN", "PW", "SB", "TK", "TL", "TO", "TV", "UM", "VU", "WS", "AQ", "GS", "HM", "TF"]
}
```

# Implementation
Instead of hitting the Trevorblades API every time that this Takehome API is called, the idea of this project is doing a pre-processing
phase loading the data needed. Using this approach, this API does not need to call any other endpoint **per request** and 
the response can be acquired quicker, the resources used are lower (cpu and memory), and this API can handle a request rate much higher.      

# Limitations
The pre-processing part does not guarantee that the response is always up-to-date with the most recent continent and country divisions. 
For reducing the possibility of outdated data, this API could reduce the refresh time of the pre-processing step or adding a manual refreshing feature. 
The refresh was configured to be performed each 15 days.

# Build & Run 

## run spring-boot application
```bash
./gradlew bootRun
```

## build and execute jar with JDK 17
```bash
./gradlew build
java -jar build/libs/takehome-{version}.jar
```

## run tests
```bash
./gradlew test
```

## build OCI image  
```bash
docker build --no-cache=true -t takehome .
```

## run the image inside as a container
```bash
docker run -d -p 8080:8080 takehome
```