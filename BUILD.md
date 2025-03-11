

## Environment Variables
Ensure the following environment variables are set or mentioned \
keys are manually replaced with correct values.

Backend - `src/main/resources/application.yml` \
Frontend - `src/main/web/src/constants.js`

Note `GOOGLE_APPLICATION_CREDENTIALS` should be defined as environment variable.

### Backend
```env
PHOTO_LIB_DB_URL=jdbc:postgresql://<DB_HOST>:<DB_PORT>/<DB_NAME>
PHOTO_LIB_DB_USER=<DB_USERNAME>
PHOTO_LIB_DB_PASS=<DB_PASSWORD>
PHOTO_LIB_GOOGLE_CLIENT_ID=<GOOGLE_CLIENT_ID>
PHOTO_LIB_GOOGLE_CLIENT_SECRET=<GOOGLE_CLIENT_SECRET>

GOOGLE_APPLICATION_CREDENTIALS=/path/to/credentials.json
```

### Frontend
```env
REACT_APP_API_URL=http://<BACKEND_HOST>:<BACKEND_PORT>
REACT_APP_GOOGLE_CLIENT_ID=<GOOGLE_CLIENT_ID>
```

## Build

### Backend

```shell
sudo mvn -f pom.xml clean package -DskipTests
java -jar target/*.jar &
```

### Frontend

```shell
TBA
```
