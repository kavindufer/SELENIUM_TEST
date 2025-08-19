# playground-e2e

Sample Selenium 4 + TestNG automation suite targeting the demo application at `http://localhost:3000`.

## Prerequisites
- Java 17+
- Maven 3.8+
- Google Chrome/Firefox installed for local runs

## Starting the demo app
```bash
# assuming the Node demo app is available
node server.js # app serves at http://localhost:3000
```

## Running tests
```bash
mvn clean test -DbaseUrl=http://localhost:3000 -Dbrowser=chrome -Dheadless=true
```

### Run only smoke suite
```bash
mvn clean test -Dsuite=smoke
```

### Override download directory
```bash
mvn clean test -DdownloadsDir=/tmp/downloads
```

### Change parallelism
```bash
mvn clean test -DthreadCount=2
```

### Run a single test class
```bash
mvn -Dtest=Smoke_Login_AdminAccessTest test
```

## Allure report
```bash
mvn allure:serve
```

## Troubleshooting
- Ensure browsers are installed and accessible in PATH.
- For WebDriver manager issues, clear `~/.cache/selenium`.
- Headless runs may behave differently with iFrames; try `-Dheadless=false` when debugging.
- File downloads may require additional permissions on CI environments.
- **CDP Warnings**: Chrome DevTools Protocol warnings have been addressed by disabling DevTools features in Chrome options to maintain compatibility with Selenium 4.20.0.
