package tsheppard01.actors

import akka.actor.{Actor, ActorLogging, Props}
import tsheppard01.actors.DataReadingActor.ReadFileSplit
import tsheppard01.filesplitters.CsvFileSplitRecordReader

class DataReadingActor(reader: CsvFileSplitRecordReader) extends Actor with ActorLogging {
  override def receive = {
    case ReadFileSplit(pathToFile, startPosition, endPosition) =>
      reader.readRecordsInFileSplit(pathToFile, startPosition, endPosition)
  }
}

object DataReadingActor{
  def apply(reader: CsvFileSplitRecordReader): Props =
    Props(new DataReadingActor(reader))

  final case class ReadFileSplit(pathToFile: String, startPosition: Long, endPosition: Long)
}
