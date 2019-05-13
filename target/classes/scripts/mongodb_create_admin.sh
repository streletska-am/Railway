#!/usr/bin/env sh

# mongo --port 27017
use admin
db.createUser({user: "root",pwd: "root",roles: [ { role: "userAdminAnyDatabase", db: "admin" }, "readWriteAnyDatabase" ]})
