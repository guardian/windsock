package com.gu.argo

case class EntityResponse[T](
  uri: Option[String] = None,
  data: Option[T],
  links: Option[List[Link]] = None
)

case class EmbeddedEntity[T](
  uri: String,
  data: Option[T],
  links: Option[List[Link]] = None
)

// TODO: or URI?
case class Link(rel: String, href: String)
