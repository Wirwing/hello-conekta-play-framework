package models

case class Charge (
  id: String,
  description: String,
  status: String,
  amount: Double
)

object Charge{

  def generateCharge(user: User, product: Product) : Charge = {

    val chargeData = Map("amount" -> product.price.toInt, "description" -> product.description, 
                          "card" -> user.conektaUserId, "currency" -> "mxn")
    val remoteCharge = com.conekta.Charge.create(chargeData)

    if (remoteCharge.status != "paid"){
      throw new RuntimeException("Charge couldn't be paid.")  
    }

    Charge(id = remoteCharge.id, status = remoteCharge.status, description = product.description, amount = product.price)

  }

}
