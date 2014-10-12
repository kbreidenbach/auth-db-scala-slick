package org.baseservices.persistence.mappers

import java.util.UUID
import scala.slick.driver.JdbcDriver.simple._
/**
 * Date: 10/12/14
 * Time: 12:06 AM
 * Copyright 2014 Kevin E. Breidenbach
 * @author Kevin E. Breidenbach
 */

object UUIDMapper {
  val uuidRoleColumnType = MappedColumnType.base[UUID, String] (
    { uuid => uuid.toString },
    { string => UUID.fromString(string) }
  )
}

object DateMapper {
  val dateColumnType = MappedColumnType.base[java.util.Date, java.sql.Timestamp](
    { utilDate => new java.sql.Timestamp(utilDate.getTime) },
    { sqlDate => new java.util.Date(sqlDate.getTime) }
  )
}
