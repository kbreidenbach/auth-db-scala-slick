package org.baseservices.persistence.exceptions

/**
 * Date: 10/11/14
 * Time: 11:13 PM
 * Copyright 2014 Kevin E. Breidenbach
 * @author Kevin E. Breidenbach
 */
case class EntityNotWritable(error: String) extends Error(error)
case class RecordNotFound(error: String) extends Error(error)
