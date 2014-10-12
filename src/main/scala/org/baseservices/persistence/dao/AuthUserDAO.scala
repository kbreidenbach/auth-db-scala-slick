package org.baseservices.persistence.dao

import java.sql.SQLException

import org.baseservices.persistence.entity.{AuthUsers, AuthUser}
import org.baseservices.persistence.exceptions.{EntityNotWritable, RecordNotFound}

import scala.slick.driver.JdbcDriver.simple._

/**
 * Date: 10/11/14
 * Time: 9:36 PM
 * Copyright 2014 Kevin E. Breidenbach
 * @author Kevin E. Breidenbach
 */
trait AuthUserDAO {
  val users = TableQuery[AuthUsers]
  def get(user: AuthUser)(implicit session: Session): Either[Option[AuthUser], Error]
  def insert(user: AuthUser)(implicit session: Session): Either[AuthUser, Error]
  def update(user: AuthUser)(implicit session: Session): Either[AuthUser, Error]
}

object AuthUserDAO extends AuthUserDAO {
  override def get(user: AuthUser)(implicit session: Session): Either[Option[AuthUser], Error] = {
    try {
      val query = for {
        foundUsers <- users if (user match {
        case AuthUser(uuid, null, null, _) =>
          foundUsers.uuid === uuid
        case AuthUser(null, email, password, _) =>
          if (email == null || password == null) throw RecordNotFound("insufficient information to find Auth User")
          foundUsers.email === email && foundUsers.password === password
      })
      } yield foundUsers

      Left(query.firstOption)
    } catch {
      case e: RecordNotFound => Right(e)
    }
  }

  override def insert(user: AuthUser)(implicit session: Session): Either[AuthUser, Error] = {
    try {
      users += user
      Left(user)
    } catch {
      case e: SQLException => Right(EntityNotWritable(e.getMessage))
    }
  }

  override def update(user: AuthUser)(implicit session: Session): Either[AuthUser, Error] = {
    val query = for {foundUsers <- users if foundUsers.uuid === user.uuid} yield foundUsers
    query.firstOption match {
      case Some(foundUser) =>
        val newEmail = if (user.email.isEmpty) foundUser.email else user.email
        val newPassword = if (user.password.isEmpty) foundUser.password else user.password
        try {
          val updatedUser = foundUser.copy(email = newEmail, password = newPassword)
          query.update(updatedUser)
          Left(updatedUser)
        } catch {
          case e: SQLException => Right(EntityNotWritable(e.getMessage))
        }
      case _ => Right(RecordNotFound("User not found"))
    }
  }
}