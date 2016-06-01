package controllers.admin

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services.AvatarService
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import forms.user._
import models.User
import models.Company
import models.services._
import models.daos.user._
import models.daos.password._
import models.daos.company._
import play.api.i18n.{ MessagesApi, Messages }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.Future


class UserController @Inject() (
  val messagesApi: MessagesApi,
  val env: Environment[User, JWTAuthenticator],
  userService: UserService,
  userDao: UserDAO,
  companyDao: CompanyDAO,
  passwordInfoDao: PasswordInfoDAO,
  authInfoRepository: AuthInfoRepository,
  avatarService: AvatarService,
  passwordHasher: PasswordHasher)
  extends Silhouette[User, JWTAuthenticator] {



  def showUsers = SecuredAction.async{ implicit request =>
   val users = userDao.findByIDCompany(request.identity.company)
   users.flatMap{
    users =>
     Future.successful(Ok(Json.toJson(users)))
   }
  }

  //lato fronted bisogna fare la ricerca findByID solo sugli utenti della stessa company
  def showUsersByName(userName: String) = Action.async{ implicit request =>
    val users = userDao.findByName(userName)
    users.flatMap{
     users =>
      Future.successful(Ok(Json.toJson(users)))
    }
  }

  //lato fronted bisogna fare la ricerca findByID solo sugli utenti della stessa company
  def showUsersBySurname(userSurname: String) = Action.async{ implicit request =>
    val users = userDao.findBySurname(userSurname)
    users.flatMap{
     users =>
      Future.successful(Ok(Json.toJson(users)))
    }
  }

  //lato fronted bisogna fare la ricerca findByID solo sugli utenti della stessa company
 def showUserDetails(userID: UUID) = Action.async{ implicit request =>
      val user = userDao.findByID(userID)
        user.flatMap{
         user =>
        Future.successful(Ok(Json.toJson(user)))
        }
    }

    //lato fronted bisogna fare la ricerca findByID solo sugli utenti della stessa company
    def delete(userID: UUID) = Action.async{ implicit request =>
      userDao.findByID(userID).flatMap{
          case None => Future.successful(BadRequest(Json.obj("message" -> "User non trovato")))
          case Some (user) =>
          val loginInfo = LoginInfo(CredentialsProvider.ID, user.email)
            for{
              user <- userDao.remove(userID)
              authInfo <- passwordInfoDao.remove(loginInfo)
            }yield {
                Ok(Json.obj("ok" -> "ok"))
              }
            }
     }

     //lato fronted bisogna fare aggiungere la company dell'admin (nascosta nella form)
    def updateUser(userID: UUID) = Action.async(parse.json) { implicit request =>
      request.body.validate[EditUser.Data].map { data =>
        userDao.findByID(userID).flatMap {
          case None => Future.successful(BadRequest(Json.obj("message" -> Messages("user.notComplete"))))
          case Some(user) =>
          val loginInfo = LoginInfo(CredentialsProvider.ID, user.email)
          val loginInfoNew = LoginInfo(CredentialsProvider.ID, data.email)
          passwordInfoDao.find(loginInfo).flatMap{
            case None =>
              Future.successful(BadRequest(Json.obj("message" -> Messages("mail.notExists"))))
            case Some(psw) =>
              val authInfo = psw
              for{
                authInfo <- passwordInfoDao.updateNewLoginInfo(loginInfo, loginInfoNew, authInfo)
              }yield {
                Ok(Json.obj("token" -> "ok"))
               }
          }
            val companyInfo = companyDao.findByIDUser(userID)
            val user2 = User(
              userID = user.userID,
              name = data.name,
              surname = data.surname,
              loginInfo = loginInfoNew,
              email = data.email,
              company = user.company,
              role = data.role
            )
            for {
              user <- userDao.update(userID,user2)
              authenticator <- env.authenticatorService.create(loginInfo)
              token <- env.authenticatorService.init(authenticator)
            } yield {
              Ok(Json.obj("token" -> "ok"))
            }
        }
      }.recoverTotal {
        case error =>
          Future.successful(Unauthorized(Json.obj("message" -> Messages("invalid.data"))))
    }
}

  def addUser(companyID: UUID) = Action.async(parse.json) { implicit request =>
    request.body.validate[SignUp.Data].map { data =>
      val loginInfo = LoginInfo(CredentialsProvider.ID, data.email)
      userDao.find(loginInfo).flatMap {
        case Some(user) =>
          Future.successful(BadRequest(Json.obj("message" -> Messages("user.exists"))))
            case None =>
            val authInfo = passwordHasher.hash(data.password)
            val user = User(
              userID = UUID.randomUUID(),
              name = data.name,
              surname = data.surname,
              loginInfo = loginInfo,
              email = data.email,
              company = companyID,
              role = data.role
            )
            for {
              user <- userDao.save(user)
              authInfo <- authInfoRepository.add(loginInfo, authInfo)
              authenticator <- env.authenticatorService.create(loginInfo)
              token <- env.authenticatorService.init(authenticator)
            } yield {
              Ok(Json.obj("token" -> "ok"))
            }
      }
  }.recoverTotal {
      case error =>
        Future.successful(Unauthorized(Json.obj("message" -> Messages("invalid.data"))))
    }
  }
}