package org.baseservices.persistence.dao

import java.sql.Timestamp
import java.util.{Date, Calendar, UUID}

import org.baseservices.persistence.entity.AuthUser
import org.baseservices.test.PersistenceFixture
import org.mockito.ArgumentCaptor
import org.mockito.Matchers._

import org.mockito.Mockito.{when, verify}

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
    assertResult(toBytes(uuid))(bytesCaptor.getValue)
    assertResult(true)(result.isLeft)

    val foundUser = result.left.get
    assertResult(uuid)(foundUser.get.uuid)
    assertResult(email)(foundUser.get.email)
    assertResult(password)(foundUser.get.password)
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
    assertResult(uuid)(foundUser.get.uuid)
    assertResult(email)(foundUser.get.email)
    assertResult(password)(foundUser.get.password)
    assertResult(time)(foundUser.get.createdAt.get)
  }

  test("find user with insuffient information") {
    var user = AuthUser(null, email, null)
    val result = AuthUserDAO.get(user)

    assertResult(true)(result.isRight)
  }

  test("insert user") {
    val user = AuthUser(uuid, email, password)
    val result = AuthUserDAO.insert(user)
    val sqlCaptor = ArgumentCaptor.forClass(classOf[String])

    assertResult(true)(result.isLeft)
  }

  test("update user") {
    prepareMocks()
    val user = AuthUser(uuid, email, password)
    val result = AuthUserDAO.update(user)
    val sqlCaptor = ArgumentCaptor.forClass(classOf[String])

    assertResult(true)(result.isLeft)
  }

}
