package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class SearchController @Inject() extends Controller {
  //val form = Form(mapping("q" -> text)(SearchForm.apply)(SearchForm.unapply))

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  /*
  def search = Action {
    //val f = form.bindFromRequest.get
    Ok(views.html.search("Search Results"))
  }
  */
}
