package tsheppard01

import java.io.{BufferedWriter, File, FileWriter}

import scala.util.Random

class CsvFileGenerator {

  def generateCsvFile(): String = {

    val numFields = 10
    val maxFieldLength = 100
    val numRecords = 100000

    val csvData =
      List.range(1,numRecords + 1)
      .map{ _ =>
        List.range(1,numFields + 1)
          .map { fieldNum =>
            val field =
              List.range(1, Random.nextInt(maxFieldLength) + 1)
                .map(_ => generateRandomCharAToZ())
            val separator =
              if(fieldNum == numFields)
                "\n"
              else
                ","
            field ++ separator
          }
      }

    val csvString = csvData.mkString("")

    println("generated csv string")
    val pathToFile = "/Users/terences/generatedCsvData.csv"
    val outputFile = new File(pathToFile)
    val bufferedWriter = new BufferedWriter(new FileWriter(outputFile))
    bufferedWriter.write(csvString)
    bufferedWriter.close()

    pathToFile
  }

  private def generateRandomCharAToZ(): Char = {
    val high = 122
    val low = 65
    (Random.nextInt(high - low) + low).toChar
  }
}
