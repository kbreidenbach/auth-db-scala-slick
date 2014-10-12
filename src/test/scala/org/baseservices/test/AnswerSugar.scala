package org.baseservices.test

import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer

/**
 * Date: 10/11/14
 * Time: 11:40 PM
 * Copyright 2014 Kevin E. Breidenbach
 * @author Kevin E. Breidenbach
 */
trait AnswerSugar {

  implicit def toAnswer[T](f: () => T): Answer[T] = new Answer[T] {
    override def answer(invocation: InvocationOnMock): T = f()
  }

  implicit def toAnswerWithArguments[T](f: (InvocationOnMock) => T): Answer[T] = new Answer[T] {
    override def answer(invocation: InvocationOnMock): T = f(invocation)
  }

}