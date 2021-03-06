package models.daos.slick

import play.api.db.slick.Config.driver.simple._
import models.Product
import models.Charge
import models.Subscription

object DBConektaTableDefinitions {

  class Products(tag: Tag) extends Table[Product](tag, "product") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def description = column[String]("description")
    def price = column[Double]("price")
    def * = (id, name, description, price) <> (Product.tupled, Product.unapply)
  }

  val slickProducts = TableQuery[Products]

  class Charges(tag: Tag) extends Table[Charge](tag, "charge") {
    def id = column[String]("id", O.PrimaryKey)
    def description = column[String]("description")
    def status = column[String]("status")
    def amount = column[Double]("amount")
    def userID = column[String]("userID")
    def * = (id, description, status, amount, userID) <> ((Charge.apply _).tupled, Charge.unapply)
  }

  val slickCharges = TableQuery[Charges]

  class Subscriptions(tag: Tag) extends Table[Subscription](tag, "subscription") {
    def id = column[String]("id", O.PrimaryKey, O.AutoInc)
    def conektaSubscriptionID = column[String]("conektaSubscriptionID")
    def userID = column[String]("userID")
    def planID = column[String]("planID")
    def * = (id, conektaSubscriptionID, userID, planID) <> ((Subscription.apply _).tupled, Subscription.unapply)
  }

  val slickSubscriptions = TableQuery[Subscriptions]

}