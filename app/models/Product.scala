package models

case class Product (
  id: Option[Long] = None,
  name: String,
  description: String,
  price: Double
)