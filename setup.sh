#!/bin/bash

cd src/main/resources/public

npm install &&
./node_modules/.bin/bower install &&
./node_modules/.bin/jspm install
