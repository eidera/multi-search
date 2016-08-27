package services.articleGetter

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json._
import play.api.libs.ws._

class TeratailGetter(keyword: String)(implicit ws: WSClient) extends Getter(keyword) {
  //case class JsonArticle(id: Int, title: String, modified: String)
  case class JsonArticle(title: String, modified: String)

  def execute(): Option[Seq[Article]] = {
    val response = getResponse(getParams())
    analyzeResponse(response.json)
  }

  protected[this] def getSite(): String = "Teratail"
  protected[this] def getUrl(): String = {
    s"https://teratail.com/api/v1/tags/${keyword}/questions"
  }
  protected[this] def getParams(): Seq[(String, String)] = {
    Seq[(String, String)]()
  }

  protected[this] def analyzeResponse(response: JsValue): Option[Seq[Article]] = {
    implicit val articleReader = Json.reads[JsonArticle]
    val target = (response \ "questions")
    target match {
      case _: JsDefined => {
        val results = target.as[Seq[JsonArticle]]
        if(results.nonEmpty) { Some(convertJsonArticleToArticle(results)) }
        else { None }
      }
      case _: JsUndefined => None
      case _ => None
    }
  }

  protected[this] def convertJsonArticleToArticle(articles: Seq[JsonArticle]): Seq[Article] = {
    articles.map{ article =>
      makeArticle(
        title = article.title,
        //url = makeUrl(article.id),
        url = "hoge",
        contents = None,
        postedAt = convertStringToDateTime(article.modified)
      )
    }
  }

  protected[this] def makeUrl(id: Int): String = {
    s"https://teratail.com/questions/${id.toString}"
  }

  protected[this] def convertStringToDateTime(time: String): Option[DateTime] = {
    val pattern = """^(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}).*$""".r
    time match {
      case pattern(n) => Some(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(n))
      case _ => None
    }
  }
}
