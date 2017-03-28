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
import scala.collection.immutable
import scala.concurrent.duration.Duration
import scala.xml.XML

class PageScraper extends Actor {

  var result : ScrapeUrlResult = _

  override def receive = {
    case (_ : Url) if result != null =>
      sender ! Status.Failure(new IllegalStateException("already called"))
    case (req : ScrapeUrl) =>
      try{
        val page = Source.fromURL(req.url.path).mkString
        var urls : List[Url] = findUrls(page)

        implicit val timeout = Timeout(20, TimeUnit.SECONDS)

        val linkfutures: List[Future[ScrapeLinkResult]] = urls.map(link => (context.actorOf(Props[UrlSizeLoader]) ? link.path).mapTo[ScrapeLinkResult])

        val linkResults: List[ScrapeLinkResult] = linkfutures.map(lf => Await.result(lf, timeout.duration))

        result = ScrapeUrlResult(req.url, page.length, linkResults)

        sender ! result
      }catch{
        case e: Exception => sender ! Status.Failure(e)
      }
    case x =>
      throw new IllegalArgumentException(s"Request $x not acceptable")
  }

  def findUrls(page : String) : List[Url] = {
      "href=\"([^\"]*)\"".r
        .findAllIn(page)
        .matchData.map(f => Url(f.group(1)).pretify)
        .filter(p => p != null)
        .toList
        .distinct
  }
}

class UrlSizeLoader extends Actor {
  override def receive = {
    case url : String => sender ! ScrapeLinkResult(Url(url), Source.fromURL(url).mkString.length)
  }
}

case class Url(path : String){
  def pretify : Url = {
    if(this.path == null || this.path == "#" || this.path.startsWith("/"))
      return null
    Url(path.split('?')(0))
  }
}
case class ScrapeUrl(url : Url)
case class ScrapeLinkResult(url: Url, pageSize : Integer)
case class ScrapeUrlResult(url: Url, pageSize : Integer, links : List[ScrapeLinkResult])

object SizerMain {

  private val system = ActorSystem("System")

  def main(args: Array[String]): Unit = {
    runTest()
    system.terminate()
    Await.ready(system.whenTerminated, Duration(60, TimeUnit.SECONDS))
  }

  def runTest() {
    val urls : List[Url] = List(
      Url("https://sijmen.it"),
      Url("http://example.com/"),
      Url("http://webscraper.io/test-sites/e-commerce/allinone"),
      Url("http://webscraper.io/test-sites/tables"),
      Url("http://testing-ground.scraping.pro/invalid")
//      Url("https://github.com"),
//      Url("https://www.google.nl")
    )

    implicit val timeout = Timeout(60, TimeUnit.SECONDS)

    for(url <- urls){
      val actor = system.actorOf(Props[PageScraper])
      val fut : Future[ScrapeUrlResult] = (actor ? ScrapeUrl(url)).mapTo[ScrapeUrlResult]

      fut onComplete {
        case Success(resp : ScrapeUrlResult) =>
          println(resp.url + " : " + resp.pageSize + "\n" +
            ("" /: resp.links){
              (total, link) => total + "\t: " + link.url + " : " + link.pageSize + "\n"
            }
          )
        case Failure(t : Throwable) =>
          System.err.println("failed to load " + url + " : " + t.getMessage)
      }
      actor ! PoisonPill
    }
  }
}