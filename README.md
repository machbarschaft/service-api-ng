# Colivery Service API

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

**Running the application from command line**
8. St
