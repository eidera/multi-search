package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{MessagesApi, I18nSupport}

import javax.inject.Inject
import scala.concurrent.Future

import play.api.mvc._
import play.api.libs.ws._

import services.articleGetter.TargetedSiteInfos
import services.articleGetter.{MultiSiteArticleGetter, Article}

case class SearchForm(q: String, site: List[String]) {
}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (val messagesApi: MessagesApi) (implicit ws: WSClient) extends Controller with I18nSupport {
  val form = Form(mapping(
                "q" -> text,
                "site" -> list(text)
              )(SearchForm.apply)(SearchForm.unapply))

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready.", form, getTargetedSiteInfos(None)))
  }

  def search = Action { implicit request =>
    val searchForm = form.bindFromRequest.get
    val keyword = searchForm.q
    val infos = getTargetedSiteInfos(Some(searchForm))
    Ok(views.html.index(
          "Search Results : " + keyword,
          form,
          infos,
          Some(keyword),
          getArticles(keyword, infos)
        ))
  }

  private[this] def getArticles(keyword: String, infos: TargetedSiteInfos): Option[Seq[Article]] = {
    val getter = new MultiSiteArticleGetter
    getter.execute(keyword, infos)
  }

  private[this] def getTargetedSiteInfos(searchForm: Option[SearchForm]): TargetedSiteInfos = {
    searchForm match {
      case Some(n) => new TargetedSiteInfos(n.site.map{ i => i.toInt })
      case None    => new TargetedSiteInfos()
    }
  }
}
