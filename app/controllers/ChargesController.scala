package controllers

import models.User
import com.mohiva.play.silhouette.core.{LogoutEvent, Environment, Silhouette}
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import scala.concurrent.Future
import javax.inject.Inject
import models.daos.ProductDAO
import forms._

/**
 * The basic application controller.
 *
 * @param env The Silhouette environment.
 */
class ChargesController @Inject() (implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] {

  def index = SecuredAction.async{ implicit request =>

    val user = request.identity

    val charges = user.charges
    
    Future.successful(Ok(views.html.charges.index(request.identity, charges)))
    
  }

}