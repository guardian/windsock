package com.gu.windsock.util

import java.security.{SecureRandom, MessageDigest}

object UniqueId {
  val generator = SecureRandom.getInstance("SHA1PRNG")

  def sha1 = MessageDigest.getInstance("SHA-1")

  def next: String = {
    val randomNum = generator.nextInt.toString
    sha1.digest(randomNum.getBytes).map("%02X".format(_)).mkString
  }
}
