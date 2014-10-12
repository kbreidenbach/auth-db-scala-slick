package org.baseservices.persistence.dao

import java.util.UUID

import org.baseservices.persistence.entity.AuthUser
import org.baseservices.test.PersistenceFixture
import org.mockito.ArgumentCaptor
import org.mockito.Matchers._

import org.mockito.Mockito.verify

import scala.slick.jdbc.{ResultSetHoldability, ResultSetConcurrency, ResultSetType}

/**
 * Date: 10/11/14
 * Time: 11:34 PM
 * Copyright 2014 Kevin E. Breidenbach
 * @author Kevin E. Breidenbach
 */
class AuthUserDAOTest extends PersistenceFixture {

  test("find user by ID") {
    val uuid = UUID.randomUUID()
    val user = AuthUser(uuid, null, null)
    val foundUser = AuthUserDAO.get(user)
    val bytesCaptor = ArgumentCaptor.forClass(classOf[Array[Byte]])

    verify(mockSession).prepareStatement(anyString, any(classOf[ResultSetType]),
      any(classOf[ResultSetConcurrency]), any(classOf[ResultSetHoldability]))

    verify(mockPreparedStatement).setBytes(anyInt(), bytesCaptor.capture())

    assertResult(toBytes(uuid))(bytesCaptor.getValue)
  }

  test("find user by Email & Password")  {
    val email = "test.email@dummy.com"
    val password = "canttouchthis"
    val user = AuthUser(null, email, password)
    val foundUser = AuthUserDAO.get(user)
    val bytesCaptor = ArgumentCaptor.forClass(classOf[Array[Byte]])

    verify(mockSession).prepareStatement(statementCaptor.capture(), any(classOf[ResultSetType]),
      any(classOf[ResultSetConcurrency]), any(classOf[ResultSetHoldability]))

    val statement = statementCaptor.getValue

    assertResult(true)(statement.contains(email) && statement.contains(password))
  }

  test("insert user") {
    pending
  }

  test("update user") {
    pending
  }

}
