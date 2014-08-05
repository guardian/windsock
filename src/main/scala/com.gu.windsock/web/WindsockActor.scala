package com.gu.windsock.web

import akka.actor.Actor
import spray.routing._
import spray.routing.authentication.{BasicAuth, UserPass}
import spray.http._
import MediaTypes._
import StatusCodes._

import com.typesafe.config.ConfigFactory

import org.joda.time.DateTime

import com.gu.windsock.util.UniqueId
import com.gu.windsock.model._
import com.gu.windsock.persistence.{Persistence, DynamoDBPersistence}

import com.gu.argo._

import scala.concurrent.ExecutionContext.Implicits.global



// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class WindsockActor extends Actor with WindsockRouter {

  val config = com.typesafe.config.ConfigFactory.load()

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  val baseUri = config.getString("windsock.base-uri")

  val tableName = config.getString("windsock.dynamodb.notice-table-name")
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
  val baseUri: String


  // TODO: extract auth-related stuff, read from updateable file
  def extractUser(userPass: UserPass): String = userPass.user

  def requireAuth =
    authenticate(BasicAuth(realm = "Admin credentials", createUser = extractUser _))


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
    path("admin") {
      get {
        requireAuth { username =>
          getFromFile("public/admin.html")
        }
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
            complete {
              val links = List(Link("notices", absUrl("/api/notices")))
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
              requireAuth { username =>
                entity(as[Notice]) { notice =>
                  complete {
                    val newRecord = store.add(notice)
                    recordToEntity(newRecord)
                  }
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
              requireAuth { username =>
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
              }
            } ~
            delete {
              requireAuth { username =>
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

}
