package com.gu.windsock.web

import spray.http._
import spray.routing._
import spray.http.HttpHeaders._
import spray.http.HttpMethods._

trait CORSDirective { this: HttpService =>

  private val respondWithAccessControlAllowAllOrigins: Directive0 = {
    respondWithHeaders(
      HttpHeaders.`Access-Control-Allow-Origin`(AllOrigins),
      HttpHeaders.`Access-Control-Allow-Credentials`(true)
    )
  }

  private def respondWithAccessControlAllowMethodsAndHeaders(route: Route): Route = {
    headerValueByName("Access-Control-Request-Headers") { headers =>
      respondWithHeaders(
        `Access-Control-Allow-Methods`(GET, POST, PUT, DELETE, OPTIONS),
        `Access-Control-Allow-Headers`(headers)
      ) { route }
    }
  }

  val withAllowOriginHeader = respondWithAccessControlAllowAllOrigins
  def withAllowMethodsHeader(route: Route) = respondWithAccessControlAllowMethodsAndHeaders(route)

}
