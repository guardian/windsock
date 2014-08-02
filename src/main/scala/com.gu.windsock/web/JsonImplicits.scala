package com.gu.windsock.web


import spray.httpx.marshalling._
import spray.json._

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

import com.gu.windsock.model._

import com.gu.argo._


object JsonImplicits extends DefaultJsonProtocol {
  implicit object DateTimeJsonFormat extends RootJsonFormat[DateTime] {

    val fmt = ISODateTimeFormat.dateTime()

    def write(dt: DateTime) = JsString(fmt.print(dt))

    def read(value: JsValue) = value match {
      case JsString(dtstring) => fmt.parseDateTime(dtstring)
      case _ => deserializationError("DateTime expected")
    }
  }

  // TODO: God can we automate some of this?
  implicit val impNotice = jsonFormat3(Notice)
  implicit val impNoticeRecord = jsonFormat4(NoticeRecord)
  implicit val impLink = jsonFormat2(Link)
  implicit val impEntityResponseNoticeRecord = jsonFormat3(EntityResponse[NoticeRecord])
  implicit val impEmbeddedNoticeRecord = jsonFormat3(EmbeddedEntity[NoticeRecord])
  implicit val impEntityResponseListNotice = jsonFormat3(EntityResponse[List[EmbeddedEntity[NoticeRecord]]])
  implicit val impEntityResponseString = jsonFormat3(EntityResponse[String])
  implicit val impEmbeddedEntityString = jsonFormat3(EmbeddedEntity[String])
  implicit val impEmbeddedEntityListNotices = jsonFormat3(EmbeddedEntity[List[EmbeddedEntity[NoticeRecord]]])
  implicit val impIndex = jsonFormat2(Index)
  implicit val impEntityResponseIndex = jsonFormat3(EntityResponse[Index])
}

