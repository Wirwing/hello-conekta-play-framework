package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._

/**
 * The form which handles the sign up process.
 */
object PlanForm {

  /**
   * A play framework form.
   */
  val form = Form(
    tuple(
      "name" -> nonEmptyText,
      "trialPeriodDays" -> nonEmptyText,
      "amount" -> nonEmptyText))

}
