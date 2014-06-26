package com.gu.aws.dynamodb

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import java.nio.ByteBuffer

trait AttributeValues {
  def S(str: String) = new AttributeValue().withS(str)
  def N(number: Long) = new AttributeValue().withN(number.toString)
  def N(number: Double) = new AttributeValue().withN(number.toString)
  def B(bytes: ByteBuffer) = new AttributeValue().withB(bytes)
}

object AttributeValues extends AttributeValues
