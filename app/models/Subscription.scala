package models

import com.conekta.Customer
import models.daos.SubscriptionDAO
import utils.ConektaCurrencyMatcher
import models.daos.UserDAOImpl
import models.daos.UserDAO
import java.util.UUID
import scala.concurrent.Await
import scala.concurrent.duration._

case class Subscription(
  id: String,
  status: String,
  userId: String,
  planID: String){
  
  def destroy() = {
    
    val customerId = user.conektaUserId.getOrElse(throw new RuntimeException("Can't."))
    val customer = Customer.find(customerId)
    customer.subscription.get.cancel()
    
    SubscriptionDAO.delete(this)
    
  }
  
  private def user : User = {
    val userDao = new UserDAOImpl
    Await.result(userDao.find(UUID.fromString(userId)), 10 seconds).get
  }
  
}

object Subscription {

  def generate(user: User, planId: String) = {

    val customerId = user.conektaUserId.getOrElse(throw new RuntimeException("Can't."))
    val customer = Customer.find(customerId)
    val remoteSubscription = customer.createSubscription(Map("plan" -> planId))

    val subscription = Subscription(remoteSubscription.id, remoteSubscription.status, user.userID.toString, planId)
    SubscriptionDAO.save(subscription)
    
  }

}
