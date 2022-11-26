# DWT - Probono - Selenium Testing
This project was created as a skeleton for DWT - Probono testing automation.
It doesn't contain any references to DWT project yet.

## Available Browsers
_(This is Boni García's list of available drivers)_
* CHROME
* CHROMIUM
* EDGE
* FIREFOX
* IEXPLORER
* OPERA
* SAFARI

## Running

### Using Maven

#### With almost no browser logging (default)
```shell
mvn test -Dbrowser={BROWSER, see above}
```

#### Verbose
```shell
mvn test -Dbrowser={BROWSER} -verbose=true
```

#### With a Selenium Hub
```shell
mvn test -Dbrowser={BROWSER} -Dhub.url={HUB_URL}
```
E.g.:
```shell
mvn test -Dbrowser=chrome -Dhub.url=http://127.0.0.1:4444
```

### Using the packaged jar
_(See section [Package into jar file](#package) below)_
```shell
java -jar dwt_probono_selenium.jar -Dbrowser={BROWSER}
```

<a id="package"></a>
## Package into jar file

### Without tests
```shell
mvn package -DskipTests=true
```

### With tests
```shell
mvn package -Dbrowser={BROWSER}
```
