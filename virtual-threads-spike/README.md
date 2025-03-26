# Virtual threads spike

## Jmh

### Build
`./gradlew jmhjar`

### Run

#### Quick
```
java -jar build/libs/virtual-threads-spike-jmh.jar -v normal -wi 1 -w 10 -i 1 -f 1 -r 10 -rff "jmh.quick.$(date --iso-8601=minutes).csv"
```

#### Longer/All
```
java -jar build/libs/virtual-threads-spike-jmh.jar -v normal -bm Throughput -bm AverageTime -rff "jmh.long.$(date --iso-8601=minutes).csv"
```

### Help
```
java -jar build/libs/virtual-threads-spike-jmh.jar -h
```
