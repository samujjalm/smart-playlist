# Perpetua Assignment

## Pre-requisites
______________________
1. Java 11
3. IntelliJ (or any other IDE of your choice)
4. Application will run at port:8080 - please make sure the port is not in use.


![Architecture diagram](https://i.ibb.co/HP7HkVQ/Perpetua.png)

## Program Execution:
_______________________

1. Extract/Download the contents of the codebase to your local system.
2. Using any command line tool, navigate to the directory where you have extracted/downloaded the codebase.
3. Use the following command to rebuild the application: 'gradle build'
4. Wait till the application is rebuilt
5. The command will create a JAR file inside a folder named 'build' present in the codebase.
6. You can copy paste this command to run the JAR file:  java -jar build/libs/smart-playlist-0.0.1-SNAPSHOT.jar
7. Once the server is started, you can use a get request to get the data
   curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X POST localhost:8080/api/v1/playlists?searchString="love is in air"
   e.g. localhost:8080/api/v1/playlists?searchString=<search>
   All other URL patterns are 404 not found json response

### Short cut to execute
1. Execute `gradle bootRun` from inside of root folder of extracted zipped file


## Design Choices and Challenges Faced:
______________________

The service contains two APIs
1) First one allows creating a new playlist based on a search term
2) Second API allows to fetch next song based on lyrics of the current song for a given playlist

Service maintains a cache of played songs against a playlist
Service also maintains a cache of Track ID and lyrics to avoid remote calls

These caches can be moved to an external cache like Redis.

One of the challenge faced was designing an logic to fetch Random words from current lyrics. 
The current track lyrics needed cleansing due presence of unwanted texts and characters

## Response Structure Explained
_______________________


1. The response will be returned with correct response code (can be checked Postman)
2. A sample successfull API response looks like this.
   {
   "trackId": 48058449,
   "title": "Nobody Could Care Less About Your Private Lives",
   "artist": "McCarthy",
   "lyrics": "The well-to-do family, the rich\nIn a life they talk about themselves\nAll the time but if they do\nIt's because they can afford to\nNobody could care less about your private life\n\n...\n\n******* This Lyrics is NOT for Commercial use *******\n(1409622230991)",
   "playListId": "cab347f6-8642-4bd9-a9ac-c67986ba81c8"
   }

## Swagger and Testing
______________________
    1. To run tests execute: grable build
    3. Access swagger url here: localhost:8080/api-doc.html


## Important files
______________________
1. PlaylistController: A rest controller which takes the requests and directs them to underlying code.
2. PlaylistService: Contains application logic. Runs orchestration with MusixMatch Service
3. application.properties: Contains configuration like api baseurl, app id and measurement units
4. build.gradle: Gradle file containing dependencies
5. ExceptionControllerAdvice: handles REST API exception responses

## Added Implementation:
______________________
1. Swagger API documentation
2. The API URL, KEY, number of random words are configurable in application.properties.


### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.6/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.6/gradle-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.5.6/reference/htmlsingle/#boot-features-developing-web-applications)


