package models.daos

import models.Charge
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models.daos.slick.DBConektaTableDefinitions._
import scala.concurrent.Future
import play.Logger

/**
 * Give access to the user object using Slick
 */
object ChargeDAO {

  import play.api.Play.current

  /**
   * Finds a charge by its id.
   *
   * @param id The id of the charge to find.
   * @return The found charge or None if no charge for the given id could be found.
   */
  def find(id: String) = {
    DB withSession { implicit session =>
      slickCharges.filter(_.id === id).firstOption
    }
  }
  
  /**
   * Finds a charge by its id.
   *
   * @param id The id of the charge to find.
   * @return The found charge or None if no charge for the given id could be found.
   */
  def findAllForUserId(userID: String) = {
    DB withSession { implicit session =>
      slickCharges.filter(_.userID === userID).list
    }
  }

  /**
   * Saves a charge.
   *
   * @param charge The charge to save.
   * @return The saved charge.
   */
  def save(charge: Charge) = {
    DB withSession { implicit session =>
      Future.successful {
        slickCharges.filter(_.id === charge.id).firstOption match {
          case Some(chargeFound) => slickCharges.filter(_.id === charge.id).update(charge)
          case None => slickCharges.insert(charge)
        }
        charge // We do not change the entity => return it
      }
    }
  }
}
