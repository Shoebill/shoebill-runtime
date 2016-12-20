/**
 * Copyright (C) 2011 JoJLlmAn
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

package net.gtaun.shoebill.entities.impl

import net.gtaun.shoebill.SampNativeFunction
import net.gtaun.shoebill.entities.Vehicle
import net.gtaun.shoebill.entities.VehicleParam

/**
 * @author JoJLlmAn, MK124
 * @author MK124
 * @author Marvin Haschker
 */
internal class VehicleParamImpl(override val vehicle: Vehicle) : VehicleParam() {
    override var engine: Int = 0
        get() {
            SampNativeFunction.getVehicleParamsEx(vehicle.id, this)
            return field
        }
        set(value) {
            set(engine, lights, alarm, doors, bonnet, boot, objective)
            field = value
        }

    override var lights: Int = 0
        get() {
            SampNativeFunction.getVehicleParamsEx(vehicle.id, this)
            return field
        }
        set(value) {
            set(engine, lights, alarm, doors, bonnet, boot, objective)
            field = value
        }

    override var alarm: Int = 0
        get() {
            SampNativeFunction.getVehicleParamsEx(vehicle.id, this)
            return field
        }
        set(value) {
            set(engine, lights, alarm, doors, bonnet, boot, objective)
            field = value
        }

    override var doors: Int = 0
        get() {
            SampNativeFunction.getVehicleParamsEx(vehicle.id, this)
            return field
        }
        set(value) {
            set(engine, lights, alarm, doors, bonnet, boot, objective)
            field = value
        }

    override var bonnet: Int = 0
        get() {
            SampNativeFunction.getVehicleParamsEx(vehicle.id, this)
            return field
        }
        set(value) {
            set(engine, lights, alarm, doors, bonnet, boot, objective)
            field = value
        }

    override var boot: Int = 0
        get() {
            SampNativeFunction.getVehicleParamsEx(vehicle.id, this)
            return field
        }
        set(value) {
            set(engine, lights, alarm, doors, bonnet, boot, objective)
            field = value
        }

    override var objective: Int = 0
        get() {
            SampNativeFunction.getVehicleParamsEx(vehicle.id, this)
            return field
        }
        set(value) {
            set(engine, lights, alarm, doors, bonnet, boot, objective)
            field = value
        }

    override fun set(engine: Int, lights: Int, alarm: Int, doors: Int, bonnet: Int, boot: Int, objective: Int) {
        if (vehicle.isDestroyed) return

        SampNativeFunction.setVehicleParamsEx(vehicle.id, engine, lights, alarm, doors, bonnet, boot, objective)
        SampNativeFunction.getVehicleParamsEx(vehicle.id, this)
    }
}
