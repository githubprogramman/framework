/*
 * Copyright 2018 WorldWide Conferencing, LLC
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
package mongodb
package codecs

import net.liftweb.json.JString

import org.bson.codecs._
import org.bson.{BsonReader, BsonWriter}

/**
 * A Codec for JString instances.
 *
 * @since 3.3.1
 */
class JStringCodec extends Codec[JString] {
  override def decode(reader: BsonReader, decoderContext: DecoderContext): JString = {
    JString(reader.readString())
  }

  override def encode(writer: BsonWriter, value: JString, encoderContext: EncoderContext): Unit = {
    writer.writeString(value.values)
  }

  override def getEncoderClass(): Class[JString] = {
    classOf[JString]
  }
}