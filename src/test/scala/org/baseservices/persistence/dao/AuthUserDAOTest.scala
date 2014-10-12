package org.baseservices.persistence.dao

import java.sql.{SQLException, Timestamp}
import java.util.{Date, Calendar, UUID}

import org.baseservices.persistence.entity.AuthUser
import org.baseservices.test.PersistenceFixture
import org.mockito.ArgumentCaptor
import org.mockito.Matchers.{any, anyInt, anyString}
import org.mockito.Mockito.{when, verify}

import org.hamcrest.MatcherAssert._
import org.hamcrest.Matchers._

import scala.slick.jdbc.{ResultSetHoldability, ResultSetConcurrency, ResultSetType}

/**
 * Date: 10/11/14
 * Time: 11:34 PM
 * Copyright 2014 Kevin E. Breidenbach
 * @author Kevin E. Breidenbach
 */
class AuthUserDAOTest extends PersistenceFixture {
  val email = "test.email@dummy.com"
  val password = "canttouchthis"
  val uuid = UUID.randomUUID()
  val time = new Date(Calendar.getInstance().getTimeInMillis)


  private def prepareMocks() {
    when(mockPreparedStatement.execute()).thenReturn(true)
    when(mockResultSet.getBytes(1)).thenReturn(toBytes(uuid))
    when(mockResultSet.getString(2)).thenReturn(email)
    when(mockResultSet.getString(3)).thenReturn(password)
    when(mockResultSet.getTimestamp(4)).thenReturn(new Timestamp(time.getTime))
    when(mockResultSet.wasNull()).thenReturn(false)
    when(mockResultSet.next()).thenReturn(true).thenReturn(false).thenReturn(false)
  }

  test("find user by ID") {
    prepareMocks()

    val user = AuthUser(uuid, null, null)
    val result = AuthUserDAO.get(user)
    val bytesCaptor = ArgumentCaptor.forClass(classOf[Array[Byte]])

    verify(mockSession).prepareStatement(anyString, any(classOf[ResultSetType]),
      any(classOf[ResultSetConcurrency]), any(classOf[ResultSetHoldability]))

    verify(mockPreparedStatement).setBytes(anyInt(), bytesCaptor.capture())

    assertThat(bytesCaptor.getValue, is(toBytes(uuid)))
    assertThat(result.isLeft, is(true))

    val foundUser = result.left.get

    assertThat(foundUser.get.uuid, is(uuid))
    assertThat(foundUser.get.email, is(email))
    assertThat(foundUser.get.password, is(password))

    assertResult(time)(foundUser.get.createdAt.get)
  }

  test("find user by Email & Password")  {
    prepareMocks()

    val user = AuthUser(null, email, password)
    val result = AuthUserDAO.get(user)

    verify(mockSession).prepareStatement(statementCaptor.capture(), any(classOf[ResultSetType]),
      any(classOf[ResultSetConcurrency]), any(classOf[ResultSetHoldability]))

    val statement = statementCaptor.getValue

    assertResult(true)(statement.contains(email) && statement.contains(password))
    assertResult(true)(result.isLeft)

    val foundUser = result.left.get
    assertThat(foundUser.get.uuid, is(uuid))
    assertThat(foundUser.get.email, is(email))
    assertThat(foundUser.get.password, is(password))
    assertResult(time)(foundUser.get.createdAt.get)
  }

  test("find user with insuffient information") {
    val user = AuthUser(null, email, null)
    val result = AuthUserDAO.get(user)

    assertThat(result.isRight, is(true))
  }

  test("insert user") {
    val user = AuthUser(uuid, email, password)
    val result = AuthUserDAO.insert(user)

    assertThat(result.isLeft, is(true))
  }

  test("update user") {
    prepareMocks()
    val user = AuthUser(uuid, email, password)
    val result = AuthUserDAO.update(user)

    assertThat(result.isLeft, is(true))
  }

  test("update user not found") {
    prepareMocks()
    when(mockResultSet.next()).thenReturn(false)
    val user = AuthUser(uuid, email, password)
    val result = AuthUserDAO.update(user)

    assertThat(result.isRight, is(true))
  }

  test("update user sql error") {
    prepareMocks()
    val errorMessage = "SQL Error"
    when(mockPreparedStatement.execute()).thenThrow(new SQLException(errorMessage))
    val user = AuthUser(uuid, email, password)
    val result = AuthUserDAO.update(user)

    assertThat(result.isRight, is(true))
  }
}
