package controllers

import models.User
import models.Subscription
import com.mohiva.play.silhouette.core.{ LogoutEvent, Environment, Silhouette }
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import scala.concurrent.Future
import javax.inject.Inject
import models.daos.ProductDAO
import models.daos.ChargeDAO
import forms._
import play.api.Play
import play.api.Logger
import com.conekta.Plan
import utils.ConektaCurrencyMatcher
import models.daos.SubscriptionDAO

/**
 * The basic application controller.
 *
 * @param env The Silhouette environment.
 */
class SubscriptionsController @Inject() (implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] {

  def delete(id: String) = SecuredAction.async { implicit request =>

    SubscriptionDAO.find(id).map { subscription =>
      
      subscription.destroy()
      
      Future.successful(Redirect(routes.ApplicationController.index).flashing(
      "success" -> "Suscripción cancelada"))
      
    }.getOrElse(Future.successful(NotFound))
    
  }

}