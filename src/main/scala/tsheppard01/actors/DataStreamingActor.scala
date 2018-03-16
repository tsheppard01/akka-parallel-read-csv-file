package tsheppard01.actors

import akka.actor.{Actor, ActorRef, Props}
import tsheppard01.actors.DataStreamingActor.StreamFileSplit
import tsheppard01.filesplitters.FileSplitRecordIteratorProvider

/**
  * Actor for streaming csv records from a filesplit.
  *
  * @param nextActor Actor to forward records to
  * @param recordIteratorProvider Class to provide iterator containing records in the file split
  */
class DataStreamingActor(
    nextActor: ActorRef,
    recordIteratorProvider: FileSplitRecordIteratorProvider)
    extends Actor {

  /**
    * Handle messages
    */
  override def receive: Receive = {
    case StreamFileSplit(filePath, startByteOffset, endByteOffset) =>
      val iterator = recordIteratorProvider.getRecordIterator(filePath,
                                                              startByteOffset,
                                                              endByteOffset)
      iterator.foreach(record => nextActor ! NextActor.NextActorMessage(record))
  }
}

/**
  * Companion object
  */
object DataStreamingActor {

  /**
    * Method instantiates a DataStreamingActor via Props
    *
    * @param nextActor The actor to forward records to
    * @param recordIteratorProvider Class to provide iterator containing records in the file split
    */
  def apply(nextActor: ActorRef,
            recordIteratorProvider: FileSplitRecordIteratorProvider): Props =
    Props(new DataStreamingActor(nextActor, recordIteratorProvider))

  /**
    * Message containing information on the file split to read records from
    *
    * @param pathToFile Path to the data file
    * @param startByteOffset ByteOffset in the file to start reading
    * @param endByteOffset ByteOffset in the file to read to
    */
  final case class StreamFileSplit(pathToFile: String,
                                   startByteOffset: Long,
                                   endByteOffset: Long)
}
