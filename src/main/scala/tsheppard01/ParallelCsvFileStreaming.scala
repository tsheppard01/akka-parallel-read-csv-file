package tsheppard01

import akka.actor.ActorSystem
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory
import tsheppard01.ParallelCsvFileReading.getSplits
import tsheppard01.actors.{DataStreamingActor, NextActor}
import tsheppard01.filesplitters.CsvFileSplitRecordIteratorProvider

/**
  * App to show streaming records from a local csv file in parallel.
  * Each DataStreamingActor reads a portion of the csv file.  Records
  * are streamed from the file only as they are required.  A data
  * file should exist at location given in conf.
  *
  * Actors:
  *   DataStreamingActor -> NextActor
  *
  */
object ParallelCsvFileStreaming {

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()
    val pathToFile = config.getString("app.data.filepath")

    val recordIteratorProvider = new CsvFileSplitRecordIteratorProvider()
    val actorSystem = ActorSystem("streamCsv")

    val nextActorRef = actorSystem.actorOf(
      NextActor(),
      name = "NextActor"
    )

    val dataStreamingActorRef = actorSystem.actorOf(
      FromConfig.props(DataStreamingActor(nextActorRef, recordIteratorProvider)),
      name = "DataStreamer"
    )

    val fileSplits = getSplits(pathToFile, 20)
    fileSplits.foreach{ split =>
      dataStreamingActorRef ! DataStreamingActor.StreamFileSplit(pathToFile, split._1, split._2)
    }
  }
}
