#!/usr/bin/env sh

mongoimport --db railway_system --collection users --type json --file "../json/users.json" --jsonArray
mongoimport --db railway_system --collection stations --type json --file "../json/stations.json" --jsonArray
mongoimport --db railway_system --collection prices --type json --file "../json/prices.json" --jsonArray
mongoimport --db railway_system --collection routes --type json --file "../json/routes.json" --jsonArray
mongoimport --db railway_system --collection trains --type json --file "../json/trains.json" --jsonArray
mongoimport --db railway_system --collection requests --type json --file "../json/requests.json" --jsonArray
