package services.articleGetter

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json._
import play.api.libs.ws._

class QiitaGetter(keyword: String)(implicit ws: WSClient) extends Getter(keyword) {
  case class JsonArticle(title: String, body: String, url: String, updated_at: String)

  def execute(): Option[Seq[Article]] = {
    val response = getResponse(getParams())
    analyzeResponse(response.json)
  }

  protected[this] def getSite(): String = "Qiita"
  protected[this] def getUrl(): String = "https://qiita.com/api/v1/search"
  protected[this] def getParams(): Seq[(String, String)] = {
    Seq(
      "q" -> keyword
    )
  }

  protected[this] def analyzeResponse(response: JsValue): Option[Seq[Article]] = {
    implicit val articleReader = Json.reads[JsonArticle]
    val results = (response).as[Seq[JsonArticle]]
    if(results.nonEmpty) { Some(convertJsonArticleToArticle(results)) }
    else { None }
  }

  protected[this] def convertJsonArticleToArticle(articles: Seq[JsonArticle]): Seq[Article] = {
    articles.map{ article =>
      makeArticle(
        title = article.title,
        url = article.url,
        contents = Some(article.body),
        postedAt = convertStringToDateTime(article.updated_at)
      )
    }
  }

  protected[this] def convertStringToDateTime(time: String): Option[DateTime] = {
    val pattern = """^(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}).*$""".r
    time match {
      case pattern(n) => Some(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(n))
      case _ => None
    }
  }
}
