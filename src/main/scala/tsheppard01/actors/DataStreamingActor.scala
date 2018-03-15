package tsheppard01.actors

import akka.actor.{Actor, ActorRef, Props}
import tsheppard01.actors.DataStreamingActor.StreamFileSplit
import tsheppard01.filesplitters.FileSplitRecordIteratorProvider

class DataStreamingActor(nextActor: ActorRef, recordIteratorProvider: FileSplitRecordIteratorProvider) extends Actor {

  override def receive: Receive = {
    case StreamFileSplit(filePath, startPosition, endPosition) =>
      val iterator = recordIteratorProvider.getRecordIterator(filePath, startPosition, endPosition)
      iterator.foreach(record =>
        nextActor ! NextActor.NextActorMessage(record)
      )
  }
}

object DataStreamingActor {
  def apply(nextActor: ActorRef, recordIteratorProvider: FileSplitRecordIteratorProvider): Props =
    Props(new DataStreamingActor(nextActor, recordIteratorProvider))

  final case class StreamFileSplit(filePath: String, startPosition: Long, endPosition: Long)

  final case object NextRecordRequest

}