package com.gu.aws.dynamodb

import com.amazonaws.services.dynamodbv2.model._
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient

import collection.convert.decorateAll._

trait DynamoDBTable extends AttributeValues {
  def client: AmazonDynamoDBAsyncClient
  def tableName: String

  // TODO: make actually async

  def get(key: Map[String, AttributeValue]): Option[Map[String, AttributeValue]] = Option(client.getItem(
    new GetItemRequest().withTableName(tableName).withKey(key.asJava)).getItem) map (_.asScala.toMap)

  def query(keyConditions: Map[String, Condition]): Seq[Map[String, AttributeValue]] = client.query(
    new QueryRequest().withTableName(tableName).withKeyConditions(keyConditions.asJava))
      .getItems.asScala.toSeq map (_.asScala.toMap)

  def scan: Seq[Map[String, AttributeValue]] = client.scan(
    new ScanRequest().withTableName(tableName))
      .getItems.asScala map (_.asScala.toMap)

  def put(item: Map[String, AttributeValue]): Unit = client.putItem(
    new PutItemRequest().withTableName(tableName).withItem(item.asJava))

  def delete(key: Map[String, AttributeValue]): Unit = client.deleteItem(
    new DeleteItemRequest().withTableName(tableName).withKey(key.asJava))
}
