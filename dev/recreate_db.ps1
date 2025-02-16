
docker rm -vf photo-library-db
docker volume rm photo-library_pg-dev-data

docker compose -f ./docker-compose-dev.yml up -d
