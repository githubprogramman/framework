/*
 * Copyright 2007-2011 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.liftweb 
package record 

import common._
import db.{ConnectionIdentifier, DB}
import util._
import scala.xml._

@deprecated("This was never fully implemented. If you're looking for a SQL implementation of Record, please see Squeryl-Record. If you have any questions, please bring them up on the mailing list.")
trait DBRecord[MyType <: DBRecord[MyType]] extends Record[MyType] {
  self: MyType =>

  /**
   * Was this instance deleted from backing store?
   */
  private var was_deleted_? = false

  /**
   * The meta record (the object that contains the meta result for this type)
   */
  def meta: DBMetaRecord[MyType]

  /**
   * Save the instance and return the instance
   */
  def save(): MyType = {
    runSafe {
      meta.save(this)
    }
    this
  }


 /**
  * Save the instance and return the instance
  */
  override def saveTheRecord(): Box[MyType] = {save(); Full(this)}

  /**
   * Delete the instance from backing store
   */
  def delete_! : Boolean = {
    if (!can_delete_?) false else
    runSafe {
      was_deleted_? = meta.delete_!(this)
      was_deleted_?
    }
  }

  /**
   * Can this model object be deleted?
   */
  def can_delete_? : Boolean =  meta.saved_?(this) && !was_deleted_?

  private var dbConnectionIdentifier: Box[ConnectionIdentifier] = Empty

  def connectionIdentifier = dbConnectionIdentifier openOr calcDbId

  def dbCalculateConnectionIdentifier: PartialFunction[MyType, ConnectionIdentifier] = Map.empty

  private def calcDbId = if (dbCalculateConnectionIdentifier.isDefinedAt(this)) dbCalculateConnectionIdentifier(this)
                         else meta.dbDefaultConnectionIdentifier

  /**
   * Append a function to perform after the commit happens
   * @param func - the function to perform after the commit happens
   */
  def doPostCommit(func: () => Unit) {
    DB.appendPostFunc(connectionIdentifier, func)
  }
}

