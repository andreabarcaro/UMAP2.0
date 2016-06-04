package controllers

import java.io.File
import javax.inject.Inject

import org.apache.commons.mail.EmailAttachment
import play.api.Environment
import play.api.libs.mailer._
import play.api.mvc.{Action, Controller}

class ApplicationScala @Inject()(mailer: MailerClient, environment: Environment) extends Controller {

  def send = Action {
    val cid = "465"
    val email = Email(
      "Simple email",
      "Mister FROM <filippo.todescato@gmail.com>",
      Seq("Miss TO <tode.92@hotmail.it>"),
      // attachments = Seq(
      //   AttachmentFile("favicon.png", new File(environment.classLoader.getResource("public/images/favicon.png").getPath), contentId = Some(cid)),
      //   AttachmentData("data.txt", "data".getBytes, "text/plain", Some("Simple data"), Some(EmailAttachment.INLINE))
      // ),
      bodyText = Some("A text message"),
      bodyHtml = Some(s"""<html><body><p>An <b>html</b> message with cid <img src="cid:$cid"></p></body></html>""")
    )
    val id = mailer.send(email)
    Ok(s"Email $id sent!")
  }

  def sendWithCustomMailer = Action {
    val mailer = new SMTPMailer(SMTPConfiguration("typesafe.org", 1234))
    val id = mailer.send(Email("Simple email", "Mister FROM <filippo.todescato@gmail.com>"))
    Ok(s"Email $id sent!")
  }
}