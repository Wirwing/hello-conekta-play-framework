package controllers

import models.User
import com.mohiva.play.silhouette.core.{ LogoutEvent, Environment, Silhouette }
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import scala.concurrent.Future
import javax.inject.Inject
import models.daos.ProductDAO
import forms._
import play.api.Play
import play.api.Logger

/**
 * The basic application controller.
 *
 * @param env The Silhouette environment.
 */
class CardsController @Inject() (implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] {

  def add = SecuredAction.async { implicit request =>

    val key = Play.current.configuration.getString("conekta.api_key").getOrElse("")
    Future.successful(Ok(views.html.cards.add(request.identity, CardForm.checkoutForm, key)))

  }

  def create = SecuredAction.async { implicit request =>

    CardForm.checkoutForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(Redirect(routes.CardsController.add))
      },
      cardToken => {
        Logger.debug(cardToken)

        val user = request.identity
        user.saveCard(cardToken)

        Future.successful(Redirect(routes.ApplicationController.index).flashing(
          "success" -> "Tarjeta añadida"))

      })

  }

}