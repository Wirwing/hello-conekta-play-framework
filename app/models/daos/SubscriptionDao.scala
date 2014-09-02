package models.daos

import models.Subscription
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models.daos.slick.DBConektaTableDefinitions._
import scala.concurrent.Future
import play.Logger

/**
 * Give access to the user object using Slick
 */
object SubscriptionDAO {

  import play.api.Play.current

  /**
   * Finds a subscription by its id.
   *
   * @param id The id of the subscription to find.
   * @return The found subscription or None if no subscription for the given id could be found.
   */
  def find(id: String) = {
    DB withSession { implicit session =>
      slickSubscriptions.filter(_.id === id).firstOption
    }
  }
  
  /**
   * Finds a subscription by its user.
   *
   * @param id The id of the subscription to find.
   * @return The found subscription or None if no subscription for the given id could be found.
   */
  def findByUserId(userID: String) = {
    DB withSession { implicit session =>
      slickSubscriptions.filter(_.userID === userID).firstOption
    }
  }

  /**
   * Saves a subscription.
   *
   * @param subscription The subscription to save.
   * @return The saved subscription.
   */
  def save(subscription: Subscription) = {
    DB withSession { implicit session =>
      Future.successful {
        slickSubscriptions.filter(_.id === subscription.id).firstOption match {
          case Some(subscriptionFound) => slickSubscriptions.filter(_.id === subscription.id).update(subscription)
          case None => slickSubscriptions.insert(subscription)
        }
        subscription // We do not change the entity => return it
      }
    }
  }
  
  /**
   * Deletes a subscription.
   *
   * @param subscription The subscription to delete.
   * @return The deleted subscription.
   */
  def delete(subscription: Subscription) = {
    DB withSession { implicit session =>
      Future.successful {
        slickSubscriptions.filter(_.id === subscription.id).delete
        subscription // We do not change the entity => return it
      }
    }
  }
  
}
