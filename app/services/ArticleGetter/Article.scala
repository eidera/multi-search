package services.articleGetter

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class Article(site: String, title: String, url: String, contents: Option[String], postedAt: Option[DateTime]) {
  def getSite(): String = { site }
  def getTitle(): String = { title }
  def getUrl(): String = { url }
  def getContents(): Option[String] = { contents }
  def getPostedAt(): Option[DateTime] = { postedAt }
  def getPostedString(pattern: String = "yyyy-MM-dd HH:mm:ss"): String = {
    postedAt match {
      case Some(n) => DateTimeFormat.forPattern(pattern).print(n)
      case None => ""
    }
  }
}
