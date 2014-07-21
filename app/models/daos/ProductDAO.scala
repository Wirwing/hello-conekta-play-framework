package models.daos

import models.Product
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models.daos.slick.DBConektaTableDefinitions._
import scala.concurrent.Future
import play.Logger

/**
 * Give access to the user object using Slick
 */
object ProductDAO {

  import play.api.Play.current

  def all = {
    DB withSession { implicit session =>
      slickProducts.list
    }
  }

  /**
   * Finds a product by its id.
   *
   * @param id The id of the product to find.
   * @return The found product or None if no product for the given id could be found.
   */
  def find(id: Long) = {
    DB withSession { implicit session =>
      Future.successful(slickProducts.filter(_.id === id).firstOption)
    }
  }
  
  /**
   * Saves a product.
   *
   * @param product The product to save.
   * @return The saved product.
   */
  def save(product: Product) = {
    DB withSession { implicit session =>
      Future.successful {
        slickProducts.filter(_.id === product.id).firstOption match {
          case Some(productFound) => slickProducts.filter(_.id === product.id).update(product)
          case None => slickProducts.insert(product)
        }
        product // We do not change the user => return it
      }
    }
  }
}
