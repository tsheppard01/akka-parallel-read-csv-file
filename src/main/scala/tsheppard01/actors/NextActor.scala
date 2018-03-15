package tsheppard01.actors

import akka.actor.{Actor, ActorLogging, Props}

class NextActor extends Actor with ActorLogging {

  override def receive = {
    case NextActor.NextActorMessage(_) =>
      context.become(counter(1))
  }

  def counter(numRecords: Int): Receive = {
    case NextActor.NextActorMessage(_) =>
      val newTotal = numRecords + 1
      log.info(s"NumRecords: $newTotal")
      context.become(counter(newTotal))
  }
}

object NextActor{
  def apply(): Props = Props(new NextActor)

  final case class NextActorMessage(record: String)
}
