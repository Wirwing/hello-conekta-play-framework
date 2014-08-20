package models

case class Charge (
  id: String,
  description: String,
  status: String,
  amount: Double
)

object Charge{

  def generateCharge(user: User, product: Product) : Charge = {

    //Use customer id as card id, to make charge with customer's default card.
    val cardId = user.conektaUserId.get

    //Add two trailing zeros to match conekta amount format
    val conektaAmount = product.price.toInt * 100

    val chargeData = Map("amount" -> conektaAmount, "description" -> product.description, 
                          "card" -> cardId, "currency" -> "mxn")
    val remoteCharge = com.conekta.Charge.create(chargeData)

    if (remoteCharge.status != "paid"){
      throw new RuntimeException("Charge couldn't be paid.")  
    }

    Charge(id = remoteCharge.id, status = remoteCharge.status, description = product.description, amount = product.price)

  }

}
