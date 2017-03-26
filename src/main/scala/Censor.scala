import scala.io.Source

/**
  * Created by Sijmen on 26-3-2017.
  */
trait Censor {

  val badwords: Map[String, String] = {
    val source = Source.fromResource("badwords.txt")
    val lines = source.getLines()

    (Map[String, String]() /: lines) {
      (m, t) => {
        val split = t.split('=')
        m + (split(0) -> split(1))
      }
    }
  }

  def censoredText(words: Array[String]) : Array[String] =
    words.map(word => badwords.getOrElse(word, word))

}

class TextReader(words: Array[String]) extends Censor {

  def this(text: String) = this(text.split(' '))

  def wordCount(): Int = {
    (0 /: words) {(sum, _) => sum + 1}
  }
  def censoredText() : String = {
    ("" /: censoredText(words)){(total, word) => total + word + " "}
  }

}

object TextReaderTest {
  def main(args: Array[String]): Unit = {
    val reader1 = new TextReader("Shoot Sijmen Huizenga")
    val reader2 = new TextReader(Array("I", "Am","Pucky", "ShootingStar"))
    println(reader1.wordCount())
    println(reader2.wordCount())
    println(reader1.censoredText())
    println(reader2.censoredText())
  }
}
