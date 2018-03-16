package tsheppard01

import java.io.File

import akka.actor.ActorSystem
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory
import tsheppard01.actors.{DataReadingActor, NextActor}
import tsheppard01.filesplitters.CsvFileSplitRecordReader

/**
  * App to show reading records from a local csv file in parallel.
  * Each DataStreamingActor reads a portion of the csv file.  Records
  * are read from the file in batches at a time where a batch is an
  * entire file split.  A data file should exist at location given in conf.
  *
  * Actors:
  *   DataReadingActor -> NextActor
  *
  */
object ParallelCsvFileReading {

  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load()
    val pathToFile = config.getString("app.data.filepath")
    val reader = new CsvFileSplitRecordReader()

    val actorSystem = ActorSystem("readCsv")

    val nextActorRef = actorSystem.actorOf(
      NextActor(),
      name = "NextActor"
    )

    val dataReadingActorRef = actorSystem.actorOf(
      FromConfig.props(DataReadingActor(nextActorRef, reader)),
      name = "DataReader"
    )

    val fileSplits = getSplits(pathToFile, 20)
    fileSplits.foreach{ split =>
      dataReadingActorRef ! DataReadingActor.ReadFileSplit(pathToFile, split._1, split._2)
    }
  }

  /**
    * Method to calculate file split start and end byte offset points in file
    *
    * @param pathToFile Path to data file
    * @param numSplits Number of file splits to generate
    * @return
    */
  def getSplits(pathToFile: String, numSplits: Int): Seq[(Long, Long)] = {
    val file = new File(pathToFile)
    val fileSize: Long = file.length()

    val splitSize: Long = Math.floor(fileSize/numSplits).toLong

    List.range(0L, fileSize, splitSize)
      .sliding(2).map{ pairs =>
        (pairs.head, pairs.takeRight(1).head)
      }.toList
  }
}
