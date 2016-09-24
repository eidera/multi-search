package services.articleGetter

import play.api.libs.ws._

class MultiSiteArticleGetter(implicit ws: WSClient) {
  def execute(keyword: String, targetedSiteInfos: TargetedSiteInfos): Option[Seq[Article]] = {
    if(keyword.isEmpty) { return None }

    val results = getGetters(keyword, targetedSiteInfos).map{ getter =>
      getter.execute()
    }.filter(n => n.nonEmpty).map(n => n.get).foldLeft(Seq[Article]())((z, n) => z ++ n)

    if(results.nonEmpty) {
      Some(results)
    } else {
      None
    }
  }

  private[this] def getGetters(keyword: String, infos: TargetedSiteInfos): Seq[Getter] = {
    infos.getTargetSiteNumbers.map { i =>
      TargetedSiteInfos.SITES(i) match {
        case "Qiita" => new QiitaGetter(keyword)
        case "StackoverFlow" => new StackOverFlowGetter(keyword)
        case "JpStackoverFlow" => new JpStackOverFlowGetter(keyword)
        case "Teratail" => new TeratailGetter(keyword)
      }
    }
  }
}
