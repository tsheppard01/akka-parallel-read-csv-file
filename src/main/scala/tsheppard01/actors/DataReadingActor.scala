package tsheppard01.actors

import akka.actor.{Actor, Props}
import tsheppard01.CsvFileReader
import tsheppard01.actors.DataReadingActor.ReadFileSplit

class DataReadingActor(reader: CsvFileReader) extends Actor {
  override def receive = {
    case ReadFileSplit(pathToFile, startPosition, endPosition) =>
      val records = reader.readRecords(pathToFile, startPosition, endPosition)
      println(s"Num records: ${records.length}")
  }
}

object DataReadingActor{
  def apply(): Props = {
    val csvFileReader = new CsvFileReader()
    Props(new DataReadingActor(csvFileReader))

  }

  final case class ReadFileSplit(pathToFile: String, startPosition: Long, endPosition: Long)
}
