/*
 * Copyright 2018 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.wire

import com.squareup.wire.protos.kotlin.map.Mappy
import com.squareup.wire.protos.kotlin.map.Thing
import okio.ByteString
import okio.ByteString.Companion.decodeHex
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameter
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class KotlinMapTest {

  @Parameter(0) lateinit var name: String
  @Parameter(1) lateinit var adapter: ProtoAdapter<Mappy>

  @Test fun serialize() {
    assertThat(ByteString.of(*adapter.encode(THREE))).isEqualTo(BYTES)

    assertThat(adapter.encode(EMPTY)).hasSize(0)
  }

  @Test fun deserialize() {
    assertThat(adapter.decode(BYTES)).isEqualTo(THREE)

    val empty = adapter.decode(ByteArray(0))
    assertThat(empty.things).isNotNull()
  }

  companion object {
    private val BYTES =
        "0a0c0a036f6e6512050a034f6e650a0c0a0374776f12050a0354776f0a100a05746872656512070a055468726565".decodeHex()
    private val EMPTY = Mappy(things = emptyMap())
    private val THREE = Mappy(
        things = mapOf(
            "one" to Thing("One"),
            "two" to Thing("Two"),
            "three" to Thing("Three")
        )
    )

    @JvmStatic
    @Parameters(name = "{0}")
    fun parameters(): List<Array<Any>> {
      return listOf(
          arrayOf("Generated", Mappy.ADAPTER)

          // TODO(egorand): Add support for runtime adapters
          // arrayOf("Runtime", RuntimeMessageAdapter.create(Mappy::class.java))
      )
    }
  }
}
