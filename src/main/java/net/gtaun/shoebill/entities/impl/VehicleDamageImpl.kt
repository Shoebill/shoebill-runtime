/**
 * Copyright (C) 2011 JoJLlmAn

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

package net.gtaun.shoebill.entities.impl

import net.gtaun.shoebill.SampNativeFunction
import net.gtaun.shoebill.entities.Vehicle
import net.gtaun.shoebill.entities.VehicleDamage
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

/**
 * @author JoJLlmAn
 * @author Marvin Haschker
 */
class VehicleDamageImpl(override val vehicle: Vehicle) : VehicleDamage {
    override var panels: Int = 0
        set(panels) {
            if (vehicle.isDestroyed) return
            set(panels, doors, lights, tires)
        }

    override var doors: Int = 0
        set(doors) {
            if (vehicle.isDestroyed) return
            set(panels, doors, lights, tires)
        }

    override var lights: Int = 0
        set(lights) {
            if (vehicle.isDestroyed) return
            set(panels, doors, lights, tires)
        }

    override var tires: Int = 0
        set(tires) {
            if (vehicle.isDestroyed) return
            set(panels, doors, lights, tires)
        }

    override fun toString(): String = ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
            .append("vehicle", vehicle)
            .append("panels", panels)
            .append("doors", doors)
            .append("lights", lights)
            .append("tires", tires)
            .toString()

    override fun set(panels: Int, doors: Int, lights: Int, tires: Int) {
        if (vehicle.isDestroyed) return

        SampNativeFunction.updateVehicleDamageStatus(vehicle.id, panels, doors, lights, tires)
        SampNativeFunction.getVehicleDamageStatus(vehicle.id, this)
    }
}
