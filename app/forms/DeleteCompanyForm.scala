package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import java.util.UUID



object DeleteCompanyForm {

  /**
   * A play framework form.
   */
  val form = Form(
    mapping(
      "companyName" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )


  case class Data(
    companyName: String)


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
