package bazooka.server.util

import java.io._
import javax.xml.parsers._

import org.apache.xml.serialize._
import org.xml.sax._

object XmlFormatter {

  def isXml(text: String) = {
    try {
      text.indexOf("<?xml") == 0
    }
    catch {
      case ex => false
    }
  }

  def format(unformattedXml: String) = {
    try {
      val document = parseXmlFile(unformattedXml)

      val format = new OutputFormat(document)
      format.setLineWidth(65)
      format.setIndenting(true)
      format.setIndent(2)

      val out = new StringWriter
      val serializer = new XMLSerializer(out, format)

      serializer.serialize(document)
      out.toString
    }
    catch {
      case ex => unformattedXml
    }
  }

  private def parseXmlFile(in: String) = {
    val builder = DocumentBuilderFactory.newInstance.newDocumentBuilder
    val source = new InputSource(new StringReader(in))
    builder.parse(source)
  }
}