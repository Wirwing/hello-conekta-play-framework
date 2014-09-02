package models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

import scala.concurrent.Await
import scala.concurrent.duration._

import com.conekta.Customer

import models.daos.SubscriptionDAO
import models.daos.slick.UserDAOSlick

case class Subscription(
  id: String,
  conektaSubscriptionId: String,
  userID: String,
  planID: String) {

  def resume() = {
    remoteSubscription.resume
  }
  
  def pause() = {
    remoteSubscription.pause
  }
  
  def cancel() = {
    remoteSubscription.cancel()
    SubscriptionDAO.delete(this)
  }
  
  def status = {
    remoteSubscription.status
  }

  def statusDescription(): String = {

    val remote = remoteSubscription()

    val status = remote.status match {
      case "in_trial" => "En periodo de prueba"
      case "active" => "Activa"
      case "past_due" => "Vencida"
      case "paused" => "En pausa"
      case "canceled" => "Cancelada"
      case "pending_pause" => "Pendiente de pausa"
      case "pending_cancelation" => "Pendiente de cancelación"
      case _ => "Desconocido"

    }

    status

  }

  def nextBillingDate(): Option[String] = {

    val remote = remoteSubscription()
    val epochInt = remote.billingCycledEnd

    val date = epochInt match {
      case Some(epoch) => {
        val sdf = new SimpleDateFormat("dd/MM/yyyy");
        Some(sdf.format(new Date(epoch.toLong * 1000)))
      }
      case None => None
    }

    date

  }

  def startBillingDate(): String = {

    val remote = remoteSubscription()
    val epochInt = remote.billingCycleStart

    val sdf = new SimpleDateFormat("dd/MM/yyyy");
    sdf.format(new Date(epochInt.toLong * 1000));

  }

  private def user: User = {
    val userDao = new UserDAOSlick
    Await.result(userDao.find(UUID.fromString(userID)), 10 seconds).get
  }

  private def remoteSubscription(): com.conekta.Subscription = {
    val customerId = user.conektaUserId.getOrElse(throw new RuntimeException("Can't."))
    val customer = Customer.find(customerId)
    customer.subscription.get
  }

}

object Subscription {

  def generate(user: User, planId: String) = {

    val customerId = user.conektaUserId.getOrElse(throw new RuntimeException("Can't."))
    val customer = Customer.find(customerId)
    val remoteSubscription = customer.createSubscription(Map("plan" -> planId))

    val subscription = Subscription(id = null, conektaSubscriptionId = remoteSubscription.id,
      userID = user.userID.toString, planID = planId)
    SubscriptionDAO.save(subscription)

  }

}
