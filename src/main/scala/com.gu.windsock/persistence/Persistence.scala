package com.gu.windsock.persistence

import com.gu.windsock.model.{Notice, NoticeRecord}

trait Persistence {

  def list: Seq[NoticeRecord]
  def get(id: String): Option[NoticeRecord]
  def add(notice: Notice): NoticeRecord
  def update(id: String, notice: Notice): Option[NoticeRecord]
  def delete(id: String): Unit
}
