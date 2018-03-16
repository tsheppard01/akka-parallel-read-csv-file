package tsheppard01.actors

import akka.actor.{Actor, ActorLogging, Props}

/**
  * Class to receive records
  */
class NextActor extends Actor with ActorLogging {

  /**
    * Handle messages
    */
  override def receive = {
    case NextActor.NextActorMessage(_) =>
      context.become(counter(1))
  }

  /**
    * An alternate state for the actor.  Allows us to save the
    * number of records so far received by the actor.  This method is
    * set as the main message handling message via context.become
    *
    * @param numRecords Number of records received by the actor
    */
  def counter(numRecords: Int): Receive = {
    case NextActor.NextActorMessage(_) =>
      val newTotal = numRecords + 1
      log.info(s"NumRecords: $newTotal")
      context.become(counter(newTotal))
  }
}

/**
  * Companion object
  */
object NextActor {

  /**
    * Method instantiates a NextActor instance vis the Props factory
    * method
    */
  def apply(): Props = Props(new NextActor)

  /**
    * Message containing a record to be processed
    *
    * @param record Record to be processed
    */
  final case class NextActorMessage(record: String)
}
