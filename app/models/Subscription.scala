package models

import com.conekta.Customer
import models.daos.SubscriptionDAO
import utils.ConektaCurrencyMatcher
import models.daos.UserDAOImpl

case class Subscription(
  id: String,
  status: String,
  user: String,
  planID: String){
  
  def destroy() = {
    
//    val customerId = user.conektaUserId.getOrElse(throw new RuntimeException("Can't."))
    val customer = Customer.find(customerId)
    customer.subscription.get.cancel()
    
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
