package com.gu.windsock.model

import org.joda.time.DateTime

// sealed trait NoticeType
// case class Announcement() extends NoticeType
// case class Warning() extends NoticeType
// case class Critical() extends NoticeType

// TODO: title vs description? severity?
case class Notice(
  text: String,
  // `type`: NoticeType,
  `type`: String,
  author: String
)

case class NoticeRecord(
  id: String,
  created: DateTime,
  lastModified: DateTime,
  notice: Notice
)
