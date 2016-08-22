package services.articleGetter

import play.api.libs.ws._

class MultiSiteArticleGetter(implicit ws: WSClient) {
  def execute(keyword: String): Option[Seq[Article]] = {
    if(keyword.isEmpty) { return None }

    val results = getGetters.map{ getter =>
      getter.execute(keyword)
    }.filter(n => n.nonEmpty).map(n => n.get).foldLeft(Seq[Article]())((z, n) => z ++ n)

    if(results.nonEmpty) {
      Some(results)
    } else {
      None
    }
  }

  private[this] def getGetters(): Seq[Getter] = {
    Seq(
      new QiitaGetter,
      new JpStackOverFlowGetter,
      new StackOverFlowGetter
    )
  }
}
