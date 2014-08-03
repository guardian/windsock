package com.gu.windsock.web

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import StatusCodes._

import org.joda.time.DateTime

import com.gu.windsock.util.UniqueId
import com.gu.windsock.model._
import com.gu.windsock.persistence.{Persistence, DynamoDBPersistence}

import com.gu.argo._


// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class WindsockActor extends Actor with WindsockRouter {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // TODO: Make it a preference
  val tableName = "Notices"
  val store = new DynamoDBPersistence(tableName)

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(root)
}


case class Index(title: String, notices: EmbeddedEntity[List[EmbeddedEntity[NoticeRecord]]])

trait WindsockRouter
    extends HttpService
    with CORSDirective {

  import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
  import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller
  import JsonImplicits._

  val store: Persistence

  val baseUri = "http://localhost:8080"
  def absUrl(path: String) = s"$baseUri$path"

  def recordToEntity(notice: NoticeRecord) =
    EmbeddedEntity(absUrl(s"/api/notices/${notice.id}"), Some(notice))

  val root = withAllowOriginHeader {
    options {
      withAllowMethodsHeader {
        complete(StatusCodes.OK)
      }
    } ~
    path("") {
      get {
        getFromFile("public/index.html")
      }
    } ~
    pathPrefix("") {
      get {
        getFromDirectory("public")
      }
    } ~
    pathPrefix("api") {
      respondWithMediaType(`application/json`) {
        pathEnd {
          get {
            val links = List(Link("notices", absUrl("/api/notices")))
            complete {
              val notices = store.list.map(recordToEntity).toList
              // TODO: return collection response with total etc
              val data = Index("Windsock API", EmbeddedEntity(absUrl("/api/notices"), Some(notices)))
              EntityResponse(data = Some(data), links = Some(links))
            }
          }
        } ~
        pathPrefix("notices") {
          pathEnd {
            get {
              complete {
                val entities = store.list.map(recordToEntity).toList
                // TODO: return collection response with total etc
                EntityResponse(data = Some(entities))
              }
            } ~
            post {
              entity(as[Notice]) { notice =>
                complete {
                  val newRecord = store.add(notice)
                  EmbeddedEntity(s"/api/notices/${newRecord.id}", Some(newRecord))
                }
              }
            }
          } ~
          path(Segment) { noticeId =>
            get {
              complete {
                store.get(noticeId) match {
                  case Some(notice) => {
                    EntityResponse(data = Some(notice))
                  }
                  case None => NotFound
                }
              }
            } ~
            put {
              entity(as[Notice]) { notice =>
                complete {
                  store.update(noticeId, notice) match {
                    case Some(updatedRecord) => {
                      EntityResponse(data = Some(updatedRecord))
                    }
                    case None => NotFound
                  }
                }
              }
            } ~
            delete {
              complete {
                store.delete(noticeId)
                NoContent
              }
            }
          }
        }
      }
    }
  }

}
