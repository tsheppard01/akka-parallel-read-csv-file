package tsheppard01.filesplitters

import java.io.{BufferedInputStream, RandomAccessFile}
import java.nio.channels.Channels
import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.apache.commons.io.input.BoundedInputStream

import scala.collection.convert.WrapAsScala.asScalaIterator

/**
  * Trait for reading records from a portion of a file
  */
trait FileSplitRecordReader {

  /**
    * Method to read the records in a portion of a file
    *
    * @param pathToFile Path to the file to read
    * @param startByteOffset Offset from the start of the file to start reading from
    * @param endByteOffset Offset from the start of the file to read until
    * @return Seq of records
    */
  def readRecordsInFileSplit(pathToFile: String,
                             startByteOffset: Long,
                             endByteOffset: Long): Seq[String]
}

/**
  * Class for reading csv records from a portion of a file
  */
class CsvFileSplitRecordReader() extends FileSplitRecordReader {

  /**
    * Method to read the records in a portion of a file.
    * Reads from the beginning of the first full record in the fileSplit until
    * the end of the last record that begins in the fileSplit
    * ie reads from startByteOffset + X to endByteOffset + Y
    *
    * @param pathToFile Path to the file to read
    * @param startByteOffset Offset from the start of the file to start reading from
    * @param endByteOffset Offset from the start of the file to read until
    * @return Seq of all records in split
    */
  def readRecordsInFileSplit(pathToFile: String,
                             startByteOffset: Long,
                             endByteOffset: Long): Seq[String] = {

    val lastRecordOffset = getNextRecordOffset(pathToFile, endByteOffset)

    val randomAccessFile = new RandomAccessFile(pathToFile, "r")
    randomAccessFile.seek(startByteOffset)

    val fileSplitSize = endByteOffset - startByteOffset + lastRecordOffset
    val inputStream =
      new BufferedInputStream(
        new BoundedInputStream(
          Channels.newInputStream(randomAccessFile.getChannel),
          fileSplitSize
        )
      )

    val lineIterator: Iterator[String] = asScalaIterator(
      IOUtils.lineIterator(inputStream, StandardCharsets.UTF_8))
    // Read the first partial line
    if(startByteOffset != 0)
      lineIterator.next()

    lineIterator.toList
  }

  /**
    * Method to return the number of bytes between byteOffset
    * and the next record end point
    *
    * @param pathToFile Path to the csv data file
    * @param byteOffset The point in file to start reading from
    */
  private def getNextRecordOffset(pathToFile: String, byteOffset: Long): Long = {
    val raf = new RandomAccessFile(pathToFile, "r")
    raf.seek(byteOffset)
    val tmpIterator =
      asScalaIterator(
        IOUtils.lineIterator(
          new BufferedInputStream(Channels.newInputStream(raf.getChannel)),
          StandardCharsets.UTF_8)
      )
    tmpIterator.next().length
  }
}
