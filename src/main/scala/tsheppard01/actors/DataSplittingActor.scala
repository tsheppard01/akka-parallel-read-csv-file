package tsheppard01.actors

//import java.io.File

//import akka.actor.{Actor, ActorRef, Props}
//import tsheppard01.actors.DataSplittingActor.{AddDataSource, FileSplitIteratorRequest}

//class DataSplittingActor(dataReadingActor: ActorRef) extends Actor {
//
//  private val FILESPLIT_SIZE_BYTES = 1024 * 1024
//
//  override def receive: Receive = {
//    case AddDataSource(pathToFile: String) =>
//      val fileSize = new File(pathToFile, "r").length()
//
//      List
//        .range(0L, fileSize, FILESPLIT_SIZE_BYTES)
//        .sliding(2)
//        .map { pairs =>
//          (pairs.head, pairs.takeRight(1).head)
//        }
//        .toList
//        .foreach { split =>
//          dataReadingActor ! DataReadingActor.ReadFileSplit(pathToFile,
//                                                            split._1,
//                                                            split._2)
//        }
//
//    case FileSplitIteratorRequest =>
//
//  }
//}
//
//object DataSplittingActor {
//  def apply(): Props = Props(new DataSplittingActor())
//
//  final case class AddDataSource(pathToFile: String)
//  final case object FileSplitIteratorRequest
//}
