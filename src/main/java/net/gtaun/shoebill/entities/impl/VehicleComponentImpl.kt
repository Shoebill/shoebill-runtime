/**
 * Copyright (C) 2011 MK124
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
import net.gtaun.shoebill.constant.VehicleComponentSlot
import net.gtaun.shoebill.entities.Vehicle
import net.gtaun.shoebill.entities.VehicleComponent
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

/**
 * @author MK124
 * @author JoJLlmAn
 * @author Marvin Haschker
 */
class VehicleComponentImpl internal constructor(override val vehicle: Vehicle) : VehicleComponent {

    private val components = IntArray(VehicleComponentSlot.values().size)

    init {
        update()
    }

    override fun toString(): String = ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
            .append("vehicle", vehicle)
            .append("components", components)
            .toString()

    override fun add(componentId: Int) {
        if (vehicle.isDestroyed) return

        SampNativeFunction.addVehicleComponent(vehicle.id, componentId)

        val slot = SampNativeFunction.getVehicleComponentType(componentId)
        components[slot] = SampNativeFunction.getVehicleComponentInSlot(vehicle.id, slot)
    }

    override fun remove(componentId: Int) {
        if (vehicle.isDestroyed) return

        SampNativeFunction.removeVehicleComponent(vehicle.id, componentId)

        val slot = SampNativeFunction.getVehicleComponentType(componentId)
        components[slot] = SampNativeFunction.getVehicleComponentInSlot(vehicle.id, slot)
    }

    override fun remove(slot: VehicleComponentSlot) {
        if (vehicle.isDestroyed) return

        val componentId = components[slot.value]
        SampNativeFunction.removeVehicleComponent(vehicle.id, componentId)
    }

    override fun get(slot: VehicleComponentSlot): Int {
        if (vehicle.isDestroyed) return 0
        return SampNativeFunction.getVehicleComponentInSlot(vehicle.id, slot.value)
    }

    override fun toArray(): IntArray {
        val data = IntArray(components.size)
        System.arraycopy(components, 0, data, 0, components.size)
        return data
    }

    internal fun update() {
        components.indices.forEachIndexed { index, i ->
            components[index] = SampNativeFunction.getVehicleComponentInSlot(vehicle.id, i) }
    }
}
