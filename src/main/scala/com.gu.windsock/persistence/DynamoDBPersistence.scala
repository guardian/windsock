package com.gu.windsock.persistence

import com.gu.aws.dynamodb.{DynamoDBTable, AttributeValues}
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue

import com.amazonaws.regions.{Region, Regions}
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}

import com.gu.windsock.model.{NoticeRecord, Notice}
import com.gu.windsock.util.UniqueId

class DynamoDBPersistence(noticeTableName: String)
  extends Persistence
  with AttributeValues {

  private val dynamodb = new DynamoDBTable {
    val client = new AmazonDynamoDBAsyncClient
    // TODO: configurable
    client.setRegion(Region.getRegion(Regions.EU_WEST_1))

    val tableName = noticeTableName
  }

  val dateFormat = ISODateTimeFormat.dateTimeNoMillis

  private def itemToNoticeRecord(item: Map[String, AttributeValue]) = {
    NoticeRecord(
      item("Id").getS,
      dateFormat.parseDateTime(item("DateCreated").getS),
      dateFormat.parseDateTime(item("LastModified").getS),
      Notice(
        item("Notice.Text").getS,
        item("Notice.Type").getS,
        item("Notice.Author").getS
      )
    )
  }

  private def noticeRecordToItem(record: NoticeRecord): Map[String, AttributeValue] = {
    Map(
      "Id"            -> S(record.id),
      "DateCreated"   -> S(dateFormat.print(record.created)),
      "LastModified"  -> S(dateFormat.print(record.lastModified)),
      "Notice.Text"   -> S(record.notice.text),
      "Notice.Type"   -> S(record.notice.`type`),
      "Notice.Author" -> S(record.notice.author)
    )
  }

  private def byId(id: String) = Map("Id" -> S(id))

  // Order by lastModified, most recent first
  def list: Seq[NoticeRecord] = dynamodb.scan map (itemToNoticeRecord) sortWith (_.lastModified isAfter _.lastModified)

  def get(id: String): Option[NoticeRecord] =
    dynamodb.get(byId(id)) map (itemToNoticeRecord)

  def add(notice: Notice): NoticeRecord = {
    val id = UniqueId.next
    val now = new DateTime
    val newRecord = NoticeRecord(id, now, now, notice)

    dynamodb.put(noticeRecordToItem(newRecord))

    newRecord
  }

  def update(id: String, notice: Notice): Option[NoticeRecord] = get(id) map { existingRecord =>
    val updated = existingRecord.copy(lastModified = new DateTime, notice = notice)
    dynamodb.put(noticeRecordToItem(updated))
    updated
  }

  def delete(id: String): Unit = dynamodb.delete(byId(id))
}
