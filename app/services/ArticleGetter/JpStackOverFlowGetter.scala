package services.articleGetter

import play.api.libs.ws._

class JpStackOverFlowGetter(keyword: String)(implicit ws: WSClient) extends StackOverFlowGetter(keyword) {
  override protected[this] def getSite(): String = "JpStackOverFlow"
  override protected[this] def getStackOverflowSite(): String = "ja.stackoverflow"
}
