package services.articleGetter

import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.ws._

class StackOverFlowGetter(implicit ws: WSClient) extends Getter {
  case class JsonArticle(title: String, link: String)

  def execute(keyword: String): Option[Seq[Article]] = {
    val response = getResponse(getParams(keyword))
    analyzeResponse(response.json)
  }

  protected[this] def getSite(): String = "StackOverFlow"
  protected[this] def getUrl(): String = "https://api.stackexchange.com/2.2/questions"
  protected[this] def getParams(keyword: String): Seq[(String,String)] = {
    Seq(
      "order"  -> "desc",
      "sort"   -> "creation",
      "tagged" -> keyword,
      "site"   -> getStackOverflowSite()
    )
  }

  protected[this] def getStackOverflowSite(): String = "stackoverflow"

  protected[this] def analyzeResponse(response: JsValue): Option[Seq[Article]] = {
    implicit val articleReader = Json.reads[JsonArticle]
    val results = (response \ "items").as[Seq[JsonArticle]]
    if(results.nonEmpty) { Some(convertJsonArticleToArticle(results)) }
    else { None }
  }

  protected[this] def convertJsonArticleToArticle(articles: Seq[JsonArticle]): Seq[Article] = {
    articles.map{ article =>
      makeArticle(
        title = article.title,
        url = article.link,
        contents = None,
        postedAt = None
      )
    }
  }

  protected[this] def convertStringToDateTime(time: String): Option[DateTime] = {
    Some(new DateTime(time.toInt * 1000))
  }
}
