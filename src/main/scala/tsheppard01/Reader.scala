package tsheppard01

import java.io.{BufferedInputStream, RandomAccessFile}
import java.nio.channels.Channels
import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils

import scala.annotation.tailrec
import scala.collection.convert.WrapAsScala.asScalaIterator

trait FileReader {
  def readRecords(pathToFile: String, startPosition: Long, endPosition: Long): Seq[String]
}

class CsvFileReader() {

  def readRecords(pathToFile: String, startPosition: Long, endPosition: Long): Seq[String] = {

    val randomAccessFile = new RandomAccessFile(pathToFile, "r")
    randomAccessFile.seek(startPosition)
    val inputStream: BufferedInputStream = new BufferedInputStream(Channels.newInputStream(randomAccessFile.getChannel))

    val lineIterator: Iterator[String] = asScalaIterator(IOUtils.lineIterator(inputStream, StandardCharsets.UTF_8))
    val firstPartialLine = lineIterator.next()

    readLines(lineIterator, firstPartialLine.length + startPosition, endPosition)
  }

  private def readLines(lineIterator: Iterator[String], offset: Long, endPosition: Long): Seq[String] = {

    @tailrec
    def readNextLine(lineIterator: Iterator[String], offset: Long, endPosition: Long, results: Seq[String]): Seq[String] = {
      val line = lineIterator.next()
      val newOffset = offset + line.length

      if(newOffset > endPosition || !lineIterator.hasNext)
        results ++ Seq(line)
      else
        readNextLine(lineIterator, newOffset, endPosition, results ++ Seq(line))
    }

    readNextLine(lineIterator, offset, endPosition, Seq.empty[String])
  }
}
