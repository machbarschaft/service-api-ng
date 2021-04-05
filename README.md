![GitHub actions workflow](https://github.com/machbarschaft/service-api-ng/actions/workflows/actions.yml/badge.svg)

# Colivery Service API

## Prerequisites
Please check if you have [Docker](https://docs.docker.com/engine/install/), [Docker Compose](https://docs.docker.com/compose/install/) and [Java 11](https://adoptopenjdk.net/installation.html) or same other version installed. [PGAdmin](https://www.pgadmin.org) is optional but quite helpful.
## Get started

1. Checkout the current version from GitHub
2. Create yourself a project at [Firebase Console](https://console.firebase.google.com), which is free for authentication
3. In Firebase Console click on the cogwheel (left at the top) and select 'Project settings' (Projekteinstellungen) and within the screen on the tab 'Service accounts' (Dienstkonten).
4. Create yourself a new private key and store this key in the root directory of your project (the one you checked out previously) in a file named 'adminsdk.json'
5. Move to the tab 'General' (Allgemein) and copy the 'Project ID' (Projekt-ID) and 'Web API Key' (Web-API-Schlüssel)
6. Create a file 'application-dev.properties' in the root of your project and replace data as follows:
```
firebase.project.id              =Replace with your 'Project ID'
google_maps_api_key              =Replace with your 'Web API Key'

firebase.credentials.path        =./adminsdk.json
colivery.security.allowedOrigins =http://localhost:4200
liquibase.contexts               =baseline non-prd sta
# logging.level.root              =DEBUG
```
7. Start the database, which is a dockerized version of PostgreSQL by running `docker-compose up -d` - assuming that you have Docker and Docker Compose installed.
8. Start the application by running these commands
```bash
# Builds the application with gradle
./gradlew clean assemble
# Runs the generated jar
java -jar -Dspring.profiles.active=dev,localdb,google_maps_api build/libs/service-api-ng-0.0.1-SNAPSHOT.jar
```
9. Test if everything is up and running by opening this URL [http://localhost:8080/v2/api-docs](http://localhost:8080/v2/api-docs) in a browser
10. A connection to the database could be established via host `localhost`, port `5432` and the database name `colivery`

## Enable Firebase Authentication


`Get started` (Los gehts)
`Sign-in methods` ()
`Sign-in providers` (Anbieter für Anmeldungen)
`Email/Password` (E-Mail-Adresse/Passwort)
`Enable` (Aktivieren)
`Save` (Speichern)
`Users` (Benutzer)
`Add user` (Nutzer hinzufügen)

`Email / Password` (E-Mail / Passwort)

`User UID` (Nutzer-UID)