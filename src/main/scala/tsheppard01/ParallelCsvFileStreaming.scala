package tsheppard01

import java.io.{BufferedWriter, File, FileWriter}

import akka.actor.ActorSystem
import akka.routing.FromConfig
import tsheppard01.ParallelCsvFileReading.getSplits
import tsheppard01.actors.{DataStreamingActor, NextActor}
import tsheppard01.data.CsvDataGenerator
import tsheppard01.filesplitters.{CsvFileSplitRecordIteratorProvider, CsvFileSplitRecordReader}

object ParallelCsvFileStreaming {

  def main(args: Array[String]): Unit = {
    val dataGenerator = new CsvDataGenerator()
    val csvData = dataGenerator.generateData(10, 10000000, 10)

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
