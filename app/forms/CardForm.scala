package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._

/**
 * The form which handles the sign up process.
 */
object CardForm {

  /**
   * A play framework form.
   */
  val checkoutForm = Form(
    "cardToken" -> nonEmptyText
  )

}
