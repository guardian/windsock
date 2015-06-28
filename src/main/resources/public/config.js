System.config({
  "transpiler": "traceur",
  "paths": {
    "windsock/*": "*.js",
    "*": "*.js",
    "github:*": "jspm_packages/github/*.js"
  }
});

System.config({
  "map": {
    "reqwest": "github:ded/reqwest@1.1.5",
    "traceur": "github:jmcriffey/bower-traceur@0.0.88",
    "traceur-runtime": "github:jmcriffey/bower-traceur-runtime@0.0.88"
  }
});

