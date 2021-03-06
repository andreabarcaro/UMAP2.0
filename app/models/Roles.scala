package models

import com.mohiva.play.silhouette.api.Authorization
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import play.api.libs.json.Json
import scala.concurrent.Future
import play.api.mvc.Request
import play.api.Logger._

import play.api.i18n._
import play.api.mvc.RequestHeader

/**
 * Check for authorization
 */
 case class WithServices(role: String) extends Authorization[User, CookieAuthenticator] {
   def isAuthorized[B](user: User, authenticator: CookieAuthenticator)(implicit r: Request[B], m: Messages)  = {

    Future.successful(user.role == role)
  }
 }/*
 object WithServices {
   def isAuthorized(user: User, role: String): Boolean =
     role == user.role
 }*/
