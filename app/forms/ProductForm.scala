package forms

import models.Product
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._ 

/**
 * The form which handles the sign up process.
 */
object ProductForm {

  /**
   * A play framework form.
   */
  val form = Form(
    mapping(
      "id" -> optional(longNumber),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "price" -> of[Double]
    )(Product.apply)(Product.unapply)
  )

}
