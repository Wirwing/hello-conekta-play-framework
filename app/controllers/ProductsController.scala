package controllers

import models.User
import models.Charge
import models.Product
import com.mohiva.play.silhouette.core.{LogoutEvent, Environment, Silhouette}
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import scala.concurrent.Future
import javax.inject.Inject
import models.daos.ProductDAO
import models.daos.ChargeDAO
import forms._
import play.api.Play
import play.api.Logger

/**
 * The basic application controller.
 *
 * @param env The Silhouette environment.
 */
class ProductsController @Inject() (implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] {

  /**
   * Handles the index action.
   *
   * @return The result to display.
   */
  def index = SecuredAction.async { implicit request =>

    val products = ProductDAO.all

    Future.successful(Ok(views.html.products.index(request.identity, products)))
  }

  def add = SecuredAction.async{ implicit request =>
    Future.successful(Ok(views.html.products.add(request.identity, ProductForm.form)))
  }

  def create = SecuredAction.async{ implicit request =>

    ProductForm.form.bindFromRequest.fold(

      formWithErrors => {
        Future.successful(BadRequest(views.html.products.add(request.identity, ProductForm.form)))
      },
      product => {
        ProductDAO.save(product)
        Future.successful(Redirect(routes.ProductsController.index))
      })
    
  }

  def show(id: Long) = SecuredAction.async{ implicit request =>

    ProductDAO.find(id).map{ product =>
      Future.successful(Ok(views.html.products.show(request.identity, product)))
    }.getOrElse(Future.successful(NotFound("Producto no encontrado!")))

  }

  def checkout(id: Long) = SecuredAction.async{ implicit request =>

    ProductDAO.find(id).map{ product =>

      val key = Play.current.configuration.getString("conekta.api_key").getOrElse("")

      Future.successful(Ok(views.html.products.checkout(request.identity, product, ProductForm.checkoutForm, key)))
    }.getOrElse(Future.successful(NotFound("Producto no encontrado!")))

  }

  def buy(id: Long) = SecuredAction.async{ implicit request =>

    ProductForm.checkoutForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(Redirect(routes.ProductsController.checkout(id)))
      },
      cardToken => {
        Logger.debug(cardToken)

        val product = ProductDAO.find(id).get

        val user = request.identity

        user.saveCard(cardToken)

        val charge = Charge.generateCharge(user, product)
        ChargeDAO.save(charge)

        Future.successful(Redirect(routes.ProductsController.index))
      }
    )

  }

}