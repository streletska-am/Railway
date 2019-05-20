#!/usr/bin/env sh

mongoimport --host localhost --port 47019 --db railway_system --collection users --type json --file "../json/users.json" --jsonArray
mongoimport --host localhost --port 47019 --db railway_system --collection stations --type json --file "../json/stations.json" --jsonArray
mongoimport --host localhost --port 47019 --db railway_system --collection prices --type json --file "../json/prices.json" --jsonArray
mongoimport --host localhost --port 47019 --db railway_system --collection routes --type json --file "../json/routes.json" --jsonArray
mongoimport --host localhost --port 47019 --db railway_system --collection trains --type json --file "../json/trains.json" --jsonArray
mongoimport --host localhost --port 47019 --db railway_system --collection requests --type json --file "../json/requests.json" --jsonArray
