package org.baseservices.persistence.entity

import java.util.{Calendar, Date, UUID}

import org.baseservices.persistence.mappers.{DateMapper, UUIDMapper}

import scala.slick.driver.JdbcDriver.simple._

/**
 * Date: 10/11/14
 * Time: 8:33 PM
 * Copyright 2014 Kevin E. Breidenbach
 * @author Kevin E. Breidenbach
 */

case class AuthUser(uuid: UUID, email: String, password: String, createdAt: Option[Date] = None)

class AuthUsers(tag: Tag) extends Table[AuthUser](tag, "auth_user") {
  implicit val uuidRoleColumnType = UUIDMapper.uuidRoleColumnType
  implicit val dateColumnType = DateMapper.dateColumnType

  def uuid = column[UUID]("uuid", O.PrimaryKey)
  def email = column[String]("email", O.NotNull)
  def password = column[String]("password", O.NotNull)
  def created = column[Option[Date]]("created_at", O.NotNull, O.Default(Some(Calendar.getInstance().getTime)))
  def * = (uuid, email, password, created)  <> (AuthUser.tupled, AuthUser.unapply)
}
