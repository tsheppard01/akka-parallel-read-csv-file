package tsheppard01

import java.io.{BufferedWriter, File, FileWriter}

import akka.actor.ActorSystem
import akka.routing.FromConfig
import tsheppard01.actors.{DataReadingActor, DataStreamingActor, NextActor}
import tsheppard01.data.CsvDataGenerator
import tsheppard01.filesplitters.CsvFileSplitRecordReader

object ParallelCsvFileReading {

  def main(args: Array[String]): Unit = {

    val dataGenerator = new CsvDataGenerator()
    val csvData = dataGenerator.generateData(10, 10000, 10)

    val reader = new CsvFileSplitRecordReader()

    val pathToFile = "/Users/terences/generatedCsvData.csv"
    val bufferedWriter =
      new BufferedWriter(
        new FileWriter(
          new File(pathToFile)
        )
      )
    bufferedWriter.write(csvData)
    bufferedWriter.close()

    val actorSystem = ActorSystem("readCsv")

    val nextActorRef = actorSystem.actorOf(
      NextActor(),
      name = "NextActor"
    )

    val dataReadingActorRef = actorSystem.actorOf(
      FromConfig.props(DataReadingActor(reader)),
      name = "DataReader"
    )

    val fileSplits = getSplits(pathToFile, 20)
    fileSplits.foreach{ split =>
      dataReadingActorRef ! DataReadingActor.ReadFileSplit(pathToFile, split._1, split._2)
    }
  }

  def getSplits(pathToFile: String, numSplit: Int): Seq[(Long, Long)] = {
    val file = new File(pathToFile)
    val fileSize: Long = file.length()

    val splitSize: Long = Math.floor(fileSize/numSplit).toLong

    List.range(0L, fileSize, splitSize)
      .sliding(2).map{ pairs =>
        (pairs.head, pairs.takeRight(1).head)
      }.toList
  }
}
