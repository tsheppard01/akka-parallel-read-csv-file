package tsheppard01

import java.io.File

import akka.actor.{ActorSystem, Props}
import akka.routing.RoundRobinPool
import tsheppard01.actors.DataReadingActor

object ParallelCsvFileReading {

  def main(args: Array[String]): Unit = {

    val csvFileGenerator = new CsvFileGenerator()
    val pathToData = csvFileGenerator.generateCsvFile()

    val actorSystem = ActorSystem("Parallel_reading_of_csv_file")

    val dataReadingActorRef = actorSystem.actorOf(
      RoundRobinPool(5).props(DataReadingActor())
    )

    getSplits(pathToData, 1000).foreach{ split =>
      dataReadingActorRef ! DataReadingActor.ReadFileSplit(pathToData, split._1, split._2)
    }
  }

  def getSplits(pathToFile: String, numSplit: Int): Seq[(Long, Long)] = {
    val file = new File(pathToFile)
    val fileSize: Long = file.length()

    val splitSize: Long = Math.floor(fileSize/numSplit).toLong

    (List.range(0L, fileSize, splitSize) ++ Seq(fileSize - 1))
      .sliding(2).map{ pairs =>
        (pairs.head, pairs.takeRight(1).head)
      }.toSeq
  }
}
