package tsheppard01.data

import scala.util.Random

/**
  * Class to generated random csv data
  */
class CsvDataGenerator {

  /**
    * Method to create random csv data.  Length of fields is
    * between 0 and maxFieldLength
    *
    * @param numFields Number of fields in generated records
    * @param numRecords Number of record to generate
    * @param maxFieldLength The maximum length of the generated fields
    * @return
    */
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

  /**
    * Method to generate random upper or lower case character
    * with value A-Z
    */
  private def generateRandomCharAToZ(): Char = {
    val high = 122
    val low = 65
    (Random.nextInt(high - low) + low).toChar
  }
}
