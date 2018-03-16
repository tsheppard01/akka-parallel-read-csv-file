# akka-parallel-read-csv-file
Shows examples of how to read a CSV variable width file from filesystem in parallel
inside an akka actor system by breaking it into filesplits.

### How it works

The file is split into arbitrary filesplits based on the size of the file which are read independently. Any records 
spanning two filesplits are read within the filesplit containing the beginning of the record.  They are captured by 
reading off the end of a filesplit until a record delimiter is found. Reading of filesplits is completely independent 
so they can be read in parallel.  Data reading actors are passed messages containing filesplit beginning and end points
and take responsiblity for reading all records in the file split.

Repo contains two apps *ParallelCsvFileReading* and *ParallelCsvFileStreaming*

#### ParallelCsvFileReading

FileSplits are read by actors in a single operation, all records are read at once and loaded into
memory as a batch

#### ParallelCsvFileStreaming

FileSplits are streamed from file by actors.  Records are read from file one at a time and only those read are.
Not all records are loaded into memory at the same time, which can result in lower memory usage for large data files.


### Usage
Can run using sbt : `sbt run`

This will present the following options:

``` 
 [1] tsheppard01.GenerateData
 [2] tsheppard01.ParallelCsvFileReading
 [3] tsheppard01.ParallelCsvFileStreaming
  ```
##### GenerateData
Will generate a randomly generated csv data file using values in application.conf
```
app {
  data {
    filepath: "./generatedData.csv"
    numFields: 10
    numRecords: 10000
    maxFieldLength: 12
  }
}
```

##### ParallelCsvFileReading
Actor system showing example of reading data from CSV file in batches.  FilePath is read from applcation.conf.
Outputs a count of number of records so far read by the actor system.

System: 
```DataReadingActor -> NextActor```

##### ParallelCsvFileStreaming
Actor system showing example of streaming data from CSV file in parallel.  FilePath is read from application.conf.
Outputs a count of number of records so far read by the actor system.

System:
```DataStreaming -> NextActor```
