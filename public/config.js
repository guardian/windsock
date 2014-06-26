System.config({
  "paths": {
    "*": "*.js",
    "npm:*": "jspm_packages/npm/*.js",
    "github:*": "jspm_packages/github/*.js"
  }
});

System.config({
  "map": {
    "github:ded/reqwest": "github:ded/reqwest@^1.1.1"
  }
});

System.config({
  "versions": {
    "github:ded/reqwest": "1.1.1"
  }
});

