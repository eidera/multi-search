package services.articleGetter

import org.joda.time.DateTime
import scala.concurrent.Await
import scala.concurrent.duration._
import play.api.libs.ws._

abstract class Getter(keyword: String)(implicit ws: WSClient) {
  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  val TimeOut = 10000 // 10[sec]
  val ContentsLength = 500

  def execute(): Option[Seq[Article]]

  def makeArticle(title: String, url: String, contents: Option[String], postedAt: Option[DateTime]): Article = {
    new Article(
      site = getSite(),
      title = title,
      contents = convertContents(contents),
      url = url,
      postedAt = postedAt
    )
  }

  protected[this] def getTimeout(): Int = { TimeOut }
  protected[this] def getSite(): String
  protected[this] def getUrl(): String
  protected[this] def getParams(): Seq[(String,String)]

  protected[this] def getResponse(params: Seq[(String, String)]): WSResponse = {
    val w = ws.url(getUrl()).withQueryString(params:_*).get()
    Await.result(w, Duration(getTimeout(), MILLISECONDS))
  }

  protected[this] def convertContents(contents: Option[String]): Option[String] = {
    contents match {
      case Some(n) => Some(deleteHtmlTag(n).take(ContentsLength) + "…")
      case None => None
    }
  }

  protected[this] def deleteHtmlTag(contents: String): String = {
    val tag = """<(\"[^\"]*\"|'[^']*'|[^'\">])*>"""
    contents.replaceAll(tag, "")
  }
}
