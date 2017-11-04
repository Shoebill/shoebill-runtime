/**
 * Copyright (C) 2011-2012 MK124

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

import net.gtaun.shoebill.entities.Timer
import net.gtaun.shoebill.entities.TimerCallback
import net.gtaun.shoebill.event.destroyable.DestroyEvent
import net.gtaun.util.event.EventManager

/**
 * @author MK124
 * @author Marvin Haschker
 */
class TimerImpl(private val rootEventManager: EventManager, override var interval: Int, override var count: Int,
                override var callback: TimerCallback) : Timer() {

    override var isRunning: Boolean = false
        private set

    private var counting: Int = 0
    private var factualInterval: Int = 0

    override var isDestroyed: Boolean = false
        private set

    override fun destroy() {
        if(isDestroyed) return

        val destroyEvent = DestroyEvent(this)
        rootEventManager.dispatchEvent(destroyEvent, this)

        isDestroyed = true
    }

    override fun start() {
        if (isRunning) return

        counting = count
        factualInterval = 0
        isRunning = true

        callback.onStart()
    }

    override fun stop() {
        if (!isRunning) return

        isRunning = false
        callback.onStop()
    }

    fun tick(factualInt: Int) {
        if (!isRunning) return

        factualInterval += factualInt
        if (factualInterval < interval) return

        if (count > 0) counting--
        try {
            callback.onTick(factualInterval)
        } catch (e: Throwable) { }

        elapsedCounts += 1
        factualInterval = 0
        if (count > 0 && counting == 0) stop()
    }
}
