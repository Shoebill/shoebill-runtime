/**
 * Copyright (C) 2012 MK124

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.gtaun.shoebill

import net.gtaun.shoebill.util.config.YamlConfiguration
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

import java.io.InputStream

/**
 * @author MK124
 * @author Marvin Haschker
 */
class ShoebillVersionImpl(`in`: InputStream) : ShoebillVersion {

    override val name: String
    override val version: String
    override val supportedVersion: String
    override val buildNumber: Int
    override val buildDate: String
    override val targetApi: String

    init {
        val config = YamlConfiguration()
        config.setDefault("name", "Unknown")
        config.setDefault("version", "Unknown")
        config.setDefault("support", "Unknown")
        config.setDefault("buildNumber", 0)
        config.setDefault("buildDate", "Unknown")
        config.setDefault("targetApi", "Unknown")

        config.read(`in`)

        name = config.getString("name")
        version = config.getString("version")
        supportedVersion = config.getString("support")
        buildNumber = config.getInt("buildNumber")
        buildDate = config.getString("buildDate")
        targetApi = config.getString("targetApi")
    }

    override fun toString(): String = ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE)
}
