package controllers

import models.User
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

/**
 * The basic application controller.
 *
 * @param env The Silhouette environment.
 */
class PlansController @Inject() (implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] {

  /**
   * Handles the index action.
   *
   * @return The result to display.
   */
  def index = SecuredAction.async { implicit request =>
    val plans = Plan.all
    Future.successful(Ok(views.html.plans.index(request.identity, plans)))
  }

  def add = SecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.plans.add(request.identity, PlanForm.form)))
  }

  def create = SecuredAction.async { implicit request =>

    PlanForm.form.bindFromRequest.fold(

      formWithErrors => {
        Future.successful(BadRequest(views.html.plans.add(request.identity, PlanForm.form)))
      },
      plan => {

        val conektaAmount = ConektaCurrencyMatcher.convertToConektaAmount(plan._3)
        val planMap = Map("name" -> plan._1, "trial_period_days" -> plan._2, "amount" -> conektaAmount, "currency" -> "MXN")

        Future.successful(Redirect(routes.PlansController.index))
        
      })

  }

}