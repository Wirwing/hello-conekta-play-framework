package models

import com.mohiva.play.silhouette.core.{ LoginInfo, Identity }
import java.util.UUID

import com.conekta.Customer

/**
 * The user object.
 *
 * @param userID The unique ID of the user.
 * @param loginInfo The linked login info.
 * @param firstName Maybe the first name of the authenticated user.
 * @param lastName Maybe the last name of the authenticated user.
 * @param fullName Maybe the full name of the authenticated user.
 * @param email Maybe the email of the authenticated provider.
 * @param avatarURL Maybe the avatar URL of the authenticated provider.
 */
case class User(
  userID: UUID,
  loginInfo: LoginInfo,
  firstName: Option[String],
  lastName: Option[String],
  fullName: Option[String],
  email: Option[String],
  avatarURL: Option[String],
  conektaUserId: Option[String]) extends Identity {

  def saveCard(cardToken: String) = {

    val customerId = conektaUserId.getOrElse(throw new RuntimeException("Can't."))
    val customer = Customer.find(customerId)

    if (customer.cards.isEmpty) {
      customer.createCard(cardToken)
    } else {
      customer.cards.head.update(Map("token" -> cardToken))
    }

  }

  def hasCard(): Boolean = {

    if (!conektaUserId.isDefined) {
      false
    }

    val customer = Customer.find(conektaUserId.get)
    !customer.cards.isEmpty

  }

}