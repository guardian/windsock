package com.gu.windsock.web

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._

// import com.gu.windsock.persistence.Persistence
import com.gu.windsock.persistence._
import com.gu.argo._

// object MockPersistence extends Persistence {
//   def add(notice: com.gu.windsock.model.Notice): com.gu.windsock.model.NoticeRecord =
// }

class MyServiceSpec extends Specification with Specs2RouteTest with WindsockRouter {
  def actorRefFactory = system

  // TODO: create and use separate test table, or mock the store
  val tableName = "Notices"
  val store = new DynamoDBPersistence(tableName)
  // val store = MockPersistence

  "Windsock API" should {

    "return an index HTML page for GET requests to the root path" in {
      Get() ~> root ~> check {
        responseAs[String] must contain("<title>Windsock</title>")
      }
    }
  }

  "Windsock API" should {

    import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller
    import JsonImplicits._

    "return a greeting for GET requests to the API root" in {
      Get("/api") ~> root ~> check {
        responseAs[EntityResponse[Index]].data.get.title must contain("Windsock API")
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> root ~> check {
        handled must beFalse
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(root) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }
  }
}
