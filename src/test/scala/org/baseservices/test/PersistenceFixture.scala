package org.baseservices.test

import java.sql.{Connection, ResultSet, PreparedStatement}
import java.util.UUID

import org.mockito.ArgumentCaptor
import org.mockito.Mockito.{when, reset}
import org.mockito.Matchers.{any, anyString}

import org.scalatest.{BeforeAndAfterEach, BeforeAndAfter, FunSuite}
import org.scalatest.mock.MockitoSugar
import scala.slick.jdbc.JdbcBackend.Session
import scala.slick.jdbc.{ResultSetHoldability, ResultSetConcurrency, ResultSetType}

/**
 * Date: 10/11/14
 * Time: 11:37 PM
 * Copyright 2014 Kevin E. Breidenbach
 * @author Kevin E. Breidenbach
 */
trait PersistenceFixture extends FunSuite with AnswerSugar with MockitoSugar with BeforeAndAfter with BeforeAndAfterEach {
  implicit val mockSession = mock[Session]
  val mockConnection = mock[Connection]
  val mockPreparedStatement = mock[PreparedStatement]
  val mockResultSet = mock[ResultSet]
  val statementCaptor = ArgumentCaptor.forClass(classOf[String])

  override def beforeEach() {
    when(mockSession.conn).thenReturn(mockConnection)
    when(mockSession.prepareStatement(anyString(), any(classOf[ResultSetType]),
      any(classOf[ResultSetConcurrency]), any(classOf[ResultSetHoldability]))).thenReturn(mockPreparedStatement)
    when(mockPreparedStatement.getResultSet).thenReturn(mockResultSet)
  }

  override def afterEach() {
    reset(mockSession)
    reset(mockPreparedStatement)
    reset(mockResultSet)
  }

  def toBytes(uuid: UUID) = if(uuid eq null) null else {
    val msb = uuid.getMostSignificantBits
    val lsb = uuid.getLeastSignificantBits
    val buff = new Array[Byte](16)
    var i = 0
    while(i < 8) {
      buff(i) = ((msb >> (8 * (7 - i))) & 255).toByte
      buff(8 + i) = ((lsb >> (8 * (7 - i))) & 255).toByte
      i += 1
    }
    buff
  }

}
