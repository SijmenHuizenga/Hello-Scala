/**
  * Created by Sijmen on 28-3-2017.
  */

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorSystem, PoisonPill, Props, Status}

import scala.util.{Failure, Success, Try}
import scala.io.Source
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class PageScraper extends Actor {
  override def receive = {
    case (url : Url) =>
      try{
        val page = Source.fromURL(url.url).mkString
        sender ! UrlLength(url, page.length)
      }catch{
        case e: Exception => sender ! Status.Failure(e)
      }
    case x =>
      throw new IllegalArgumentException(s"Request $x not acceptable")
  }
}

class LinkFinder

case class Url(url : String)
case class UrlLength(url : Url, time : Integer)

object SizerMain {

  private val system = ActorSystem("System")

  def main(args: Array[String]): Unit = {
    runTest()
    system.terminate()
    Await.ready(system.whenTerminated, Duration(10, TimeUnit.SECONDS))
  }

  def runTest() {
    val urls : List[Url] = List(
      Url("https://sijmen.it"),
      Url("https://facebook.com"),
      Url("https://github.com"),
      Url("https://twitter.com")
    )

    implicit val timeout = Timeout(20, TimeUnit.SECONDS)

    for(url <- urls){
      val actor = system.actorOf(Props[PageScraper])
      val fut : Future[UrlLength] = (actor ? url).mapTo[UrlLength]

      fut onComplete {
        case Success(resp : UrlLength) =>
          println(resp.url + " : " + resp.time)
        case Failure(t : Throwable) =>
          System.err.println("failed to load " + url + " : " + t.getMessage)
      }
      actor ! PoisonPill
    }
  }
}