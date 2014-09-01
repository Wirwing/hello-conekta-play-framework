package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._

/**
 * The form which handles the sign up process.
 */
object PlanForm {

  val defaultNumber = default(number(min = 0), 0)

  /**
   * A play framework form.
   */
  val form = Form(
    tuple(
      "name" -> nonEmptyText,
      "trialPeriodDays" -> defaultNumber,
      "amount" -> defaultNumber))

}
