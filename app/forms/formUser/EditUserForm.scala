package forms.formUser

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json

/**
 * The form which handles the sign up process.
 */
object EditUserForm {

  /**
   * A play framework form.
   */
  val form = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText,
      "role"  -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )

  /**
   * The form data.

   */
  case class Data(
    email: String,
    password: String,
    role: String
  )

  /**
  * The companion object.
  */
  object Data {

  /**
   * Converts the [Date] object to Json and vice versa.
   */
  implicit val jsonFormat = Json.format[Data]
  }
}