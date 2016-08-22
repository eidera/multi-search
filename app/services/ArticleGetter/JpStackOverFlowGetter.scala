package services.articleGetter

import play.api.libs.ws._

class JpStackOverFlowGetter(implicit ws: WSClient) extends StackOverFlowGetter {
  override protected[this] def getSite(): String = "JpStackOverFlow"
  override protected[this] def getStackOverflowSite(): String = "ja.stackoverflow"
}
