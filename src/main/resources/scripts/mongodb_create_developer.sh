#!/usr/bin/env sh

# mongo --port 27017 -u "root" -p "root" --authenticationDatabase "admin"
use railway_system
db.createUser({user: "developer",pwd: "12345678",roles: [ { role: "readWrite", db: "railway_system" } ]})
