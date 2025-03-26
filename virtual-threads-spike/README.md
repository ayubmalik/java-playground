# Virtual threads spike

## Jmh

### Build
`./gradlew jmhjar`

### Run

#### Quick
`java -jar build/libs/virtual-threads-spike-jmh.jar -wi 1 -w 10 -i 1 -f 1 -r 10 -rff "jmh.report.$(date --iso-8601=minutes).csv"

#### Longer/All

### Help
`java -jar build/libs/virtual-threads-spike-jmh.jar -h`
