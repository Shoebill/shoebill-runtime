/**
 * Copyright (C) 2011 JoJLlmAn
 * Copyright (C) 2012 MK124
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

package net.gtaun.shoebill.object.impl;

import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.object.Vehicle;
import net.gtaun.shoebill.object.VehicleParam;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author JoJLlmAn, MK124
 */
public class VehicleParamImpl implements VehicleParam {
    private Vehicle vehicle;
    private int engine, lights, alarm, doors, bonnet, boot, objective;


    VehicleParamImpl(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("vehicle", vehicle).append("engine", engine).append("lights", lights)
                .append("alarm", alarm).append("doors", doors).append("bonnet", bonnet)
                .append("boot", boot).append("objective", objective).toString();
    }

    @Override
    public Vehicle getVehicle() {
        return vehicle;
    }

    @Override
    public int getEngine() {
        if (!vehicle.isDestroyed()) SampNativeFunction.getVehicleParamsEx(vehicle.getId(), this);
        return engine;
    }

    @Override
    public void setEngine(int engine) {
        if (vehicle.isDestroyed()) return;

        SampNativeFunction.getVehicleParamsEx(vehicle.getId(), this);
        this.engine = engine;

        set(engine, lights, alarm, doors, bonnet, boot, objective);
    }

    @Override
    public int getLights() {
        if (!vehicle.isDestroyed()) SampNativeFunction.getVehicleParamsEx(vehicle.getId(), this);
        return lights;
    }

    @Override
    public void setLights(int lights) {
        if (vehicle.isDestroyed()) return;

        SampNativeFunction.getVehicleParamsEx(vehicle.getId(), this);
        set(engine, lights, alarm, doors, bonnet, boot, objective);
    }

    @Override
    public int getAlarm() {
        if (!vehicle.isDestroyed()) SampNativeFunction.getVehicleParamsEx(vehicle.getId(), this);
        return alarm;
    }

    @Override
    public void setAlarm(int alarm) {
        if (vehicle.isDestroyed()) return;

        SampNativeFunction.getVehicleParamsEx(vehicle.getId(), this);
        set(engine, lights, alarm, doors, bonnet, boot, objective);
    }

    @Override
    public int getDoors() {
        if (!vehicle.isDestroyed()) SampNativeFunction.getVehicleParamsEx(vehicle.getId(), this);
        return doors;
    }

    @Override
    public void setDoors(int doors) {
        if (vehicle.isDestroyed()) return;

        SampNativeFunction.getVehicleParamsEx(vehicle.getId(), this);
        set(engine, lights, alarm, doors, bonnet, boot, objective);
    }

    @Override
    public int getBonnet() {
        if (!vehicle.isDestroyed()) SampNativeFunction.getVehicleParamsEx(vehicle.getId(), this);
        return bonnet;
    }

    @Override
    public void setBonnet(int bonnet) {
        if (vehicle.isDestroyed()) return;

        SampNativeFunction.getVehicleParamsEx(vehicle.getId(), this);
        set(engine, lights, alarm, doors, bonnet, boot, objective);
    }

    @Override
    public int getBoot() {
        if (!vehicle.isDestroyed()) SampNativeFunction.getVehicleParamsEx(vehicle.getId(), this);
        return boot;
    }

    @Override
    public void setBoot(int boot) {
        if (vehicle.isDestroyed()) return;

        SampNativeFunction.getVehicleParamsEx(vehicle.getId(), this);
        set(engine, lights, alarm, doors, bonnet, boot, objective);
    }

    @Override
    public int getObjective() {
        if (!vehicle.isDestroyed()) SampNativeFunction.getVehicleParamsEx(vehicle.getId(), this);
        return objective;
    }

    @Override
    public void setObjective(int objective) {
        if (vehicle.isDestroyed()) return;

        SampNativeFunction.getVehicleParamsEx(vehicle.getId(), this);
        set(engine, lights, alarm, doors, bonnet, boot, objective);
    }

    @Override
    public void set(int engine, int lights, int alarm, int doors, int bonnet, int boot, int objective) {
        if (vehicle.isDestroyed()) return;

        SampNativeFunction.setVehicleParamsEx(vehicle.getId(), engine, lights, alarm, doors, bonnet, boot, objective);
        SampNativeFunction.getVehicleParamsEx(vehicle.getId(), this);
    }
}
