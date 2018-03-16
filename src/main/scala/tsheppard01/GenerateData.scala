package tsheppard01

import java.io.{BufferedWriter, File, FileWriter}

import com.typesafe.config.ConfigFactory
import tsheppard01.data.CsvDataGenerator

/**
  * App generates random CSV data and save to a file.
  * Parameters are set in applciation.conf.
  */
object GenerateData {

  def main(args: Array[String]): Unit ={

    val config = ConfigFactory.load()

    val filePath = config.getString("app.data.filepath")
    val numRecords = config.getInt("app.data.numRecords")
    val numFields = config.getInt("app.data.numFields")
    val maxFieldLength = config.getInt("app.data.maxFieldLength")

    val dataGenerator = new CsvDataGenerator()
    val csvData = dataGenerator.generateData(numFields, numRecords, maxFieldLength)

    val bufferedWriter =
      new BufferedWriter(
        new FileWriter(
          new File(filePath)
        )
      )
    bufferedWriter.write(csvData)
    bufferedWriter.close()
  }
}
