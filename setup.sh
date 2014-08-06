#!/bin/bash

cd public

npm install &&
./node_modules/.bin/bower install &&
./node_modules/.bin/jspm install
