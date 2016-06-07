package models.daos.thing

import java.util.UUID

import models.Thing
import models.Measurements

import scala.collection.mutable
import scala.concurrent.Future

import javax.inject.Inject
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global

import reactivemongo.api._

import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection._

/**
 * Give access to the thing object.
 */
class ThingDAOImpl @Inject() (db : DB) extends ThingDAO {

  def collection: JSONCollection = db.collection[JSONCollection]("thing")

  def findByName(thingName: String): Future[Option[Thing]] = {
    collection.find(Json.obj("name" -> thingName)).one[Thing]
  }


  def findAll(): Future[List[Thing]] = {
    collection.find(Json.obj()).cursor[Thing]().collect[List]()
  }

  def findByCompany(companyID: UUID): Future[Option[Thing]] = {
    collection.find(Json.obj("companyID" -> companyID)).one[Thing]
  }

  def findByID(thingID: UUID) : Future[Option[Thing]] = {
    collection.find(Json.obj("thingID" -> thingID)).one[Thing]
  }

  def find(serialNumber: String) : Future[Option[Thing]] = {
    collection.find(Json.obj("serialNumber" -> serialNumber)).one[Thing]
  }

  def save(thing: Thing): Future[Thing] = {
    collection.insert(thing)
    Future.successful(thing)
  }

  def update(thingID: UUID, thing2: Thing): Future[Thing] = {
    collection.update(Json.obj("thingID" -> thingID), thing2)
    Future.successful(thing2)
  }

  def updateMeasurements(thingID: UUID, measurements: Measurements): Future[Thing] = {
    findByID(thingID).flatMap{
      case Some(thing) =>
      val newDatas = thing.datas
      newDatas += measurements
      val thing2 = Thing(
        thingID = thingID,
        name = thing.name,
        serialNumber = thing.serialNumber,
        description = thing.description,
        thingTypeID = thing.thingTypeID,
        companyID = thing.companyID,
        datas = newDatas
      )
      collection.update(Json.obj("thingID" -> thingID), thing2)
      Future.successful(thing2)
      case None =>
      val thingNull = Thing(
        thingID = null,
        name = "",
        serialNumber = null,
        description = "",
        thingTypeID = null,
        companyID = null,
        datas = null
      )
        Future.successful(thingNull)
    }
  }

  def remove(thingID: UUID): Future[List[Thing]] = {
    collection.remove(Json.obj("thingID" -> thingID))
    collection.find(Json.obj()).cursor[Thing]().collect[List]()
  }

  def removeByThingTypeID(thingTypeID: UUID): Future[List[Thing]] = {
    collection.remove(Json.obj("thingTypeID" -> thingTypeID))
    collection.find(Json.obj()).cursor[Thing]().collect[List]()
  }
}
