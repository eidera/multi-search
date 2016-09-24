package services.articleGetter

class TargetSiteInfo(number: Int, name: String, targeted: Boolean) {
  def getNumber(): Int = number
  def getName(): String = name
  def getTargeted(): Boolean = targeted
}

class TargetedSiteInfos(siteNumbers: Seq[Int]) {
  private[this] val infos = makeTargetSiteInfos

  def getLength(): Int = infos.length
  def getInfo(index: Int): TargetSiteInfo = infos(index)
  def getTargetSiteNumbers(): Seq[Int] = {
    infos.filter(info => info.getTargeted).map {info => info.getNumber}
  }

  private[this] def makeTargetSiteInfos: Seq[TargetSiteInfo] = {
    var result: Seq[TargetSiteInfo] = Seq[TargetSiteInfo]()

    TargetedSiteInfos.SITES.foreach { case (number, name) =>
      val targeted = siteNumbers.indexOf(number) match {
                        case -1 => false
                        case _  => true
                      }
      result = result :+ (new TargetSiteInfo(number, name, targeted))
    }
    result
  }
}

object TargetedSiteInfos {
  val SITES = Map(
    1 -> "Qiita",
    2 -> "StackoverFlow",
    3 -> "JpStackoverFlow",
    4 -> "Teratail"
  )
}
