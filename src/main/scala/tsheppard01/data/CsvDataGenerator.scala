package tsheppard01.data

import scala.util.Random

class CsvDataGenerator {

  def generateData(numFields: Int,
                   numRecords: Int,
                   maxFieldLength: Int): String =
    List
      .range(1, numRecords + 1)
      .map { _ =>
        List
          .range(1, numFields + 1)
          .map { fieldNum =>
            val field =
              List
                .range(1, Random.nextInt(maxFieldLength) + 1)
                .map { _ =>
                  generateRandomCharAToZ()
                }
                .mkString("")
            val separator =
              if (fieldNum == numFields)
                "\n"
              else
                ","
            field ++ separator
          }
          .mkString("")
      }
      .mkString("")

  private def generateRandomCharAToZ(): Char = {
    val high = 122
    val low = 65
    (Random.nextInt(high - low) + low).toChar
  }
}
