package tsheppard01.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import tsheppard01.actors.DataReadingActor.ReadFileSplit
import tsheppard01.filesplitters.CsvFileSplitRecordReader

/**
  * Actor for reading csv records from a filesplit.
  *
  * @param nextActor Actor to forward records to
  * @param reader A reader to read all records in a file split
  */
class DataReadingActor(nextActor: ActorRef, reader: CsvFileSplitRecordReader)
    extends Actor
    with ActorLogging {

  /**
    * Handle messages
    */
  override def receive = {
    case ReadFileSplit(pathToFile, startByteOffset, endByteOffset) =>
      val records = reader.readRecordsInFileSplit(pathToFile,
                                                  startByteOffset,
                                                  endByteOffset)
      records.foreach { record =>
        nextActor ! NextActor.NextActorMessage(record)
      }
  }
}

/**
  * Companion object
  */
object DataReadingActor {

  /**
    * Method instantiates a DataReadingActor via Props
    *
    * @param nextActor The actor to forward records to
    * @param reader A reader to read all records in a file split
    */
  def apply(nextActor: ActorRef, reader: CsvFileSplitRecordReader): Props =
    Props(new DataReadingActor(nextActor, reader))

  /**
    * Message containing information on the file split to read records from
    *
    * @param pathToFile Path to the data file
    * @param startByteOffset ByteOffset in the file to start reading
    * @param endByteOffset ByteOffset in the file to read to
    */
  final case class ReadFileSplit(pathToFile: String,
                                 startByteOffset: Long,
                                 endByteOffset: Long)
}
