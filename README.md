# takehome
API that receives a list of country codes as inputs and retrieve the countries that are in the same continents as the given countries.
This API utilizes [Trevorblades endpoint](https://countries.trevorblades.com/graphql) to acquire the information needed.
[Annexare project](https://annexare.github.io/Countries/) is used as data source for the countries as described in [Trevorblades repository](https://github.com/trevorblades/countries)  

<hr>

* [Spring Boot 3.0.4](https://start.spring.io/)
* [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

# Endpoint to find countries in the same continent that the given countries

## [Swagger](http://localhost:8080/swagger-ui/index.html) 

## Example: Input countries -> BV (Bouvet Island) and TF (French Southern Territories)

### POST request to localhost:8080/api/countries/same-continent

### Input
The codes or names of the countries (case-insensitive).
As an example, a possible input is BV (Bouvet Island) and TF (French Southern Territories)

```json
{
  "countries": ["BV", "TF"] 
}
```

Alternatively, the input can be also the name of the countries or a mix of names and country codes (case-insensitive):

```json
{
  "countries": ["bouvet island", "French southern territories", "bV"] 
}
```

### Output
The codes of the countries in the same continent of the input countries. 
As BV (Bouvet Island) and TF (French southern territories) are in Antarctica the result will be all the countries 
in this continent except for the input countries.

```json
{
  "message": "There are 3 countries in the same continents as the input countries bouvet island,French southern territories,bV",
  "countriesInput": [
    "bouvet island",
    "French southern territories",
    "bV"
  ],
  "countriesOutput": [
    {
      "code": "AQ",
      "name": "Antarctica",
      "continent": {
        "code": "AN",
        "name": "Antarctica"
      }
    },
    {
      "code": "GS",
      "name": "South Georgia and the South Sandwich Islands",
      "continent": {
        "code": "AN",
        "name": "Antarctica"
      }
    },
    {
      "code": "HM",
      "name": "Heard Island and McDonald Islands",
      "continent": {
        "code": "AN",
        "name": "Antarctica"
      }
    }
  ]
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