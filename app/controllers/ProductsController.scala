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

}