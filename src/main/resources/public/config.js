System.config({
  "paths": {
    "windsock:*": "*.js",
    "npm:*": "jspm_packages/npm/*.js",
    "github:*": "jspm_packages/github/*.js",
    "*": "*.js"
  }
});

System.config({
  "map": {
    "reqwest": "github:ded/reqwest@^1.1.2"
  }
});

System.config({
  "versions": {
    "github:ded/reqwest": "1.1.2"
  }
});

