/**
 * Copyright (C) 2011-2012 MK124
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

import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.object.Timer;
import net.gtaun.util.event.EventManager;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MK124
 */
public class TimerImpl implements Timer {
    private final EventManager rootEventManager;

    private TimerCallback callback;

    private int interval, count;

    private boolean running;
    private int counting, factualInterval;

    private boolean destroyed;


    public TimerImpl(EventManager eventManager, int interval, int count, TimerCallback callback) {
        this.rootEventManager = eventManager;
        this.interval = interval;
        this.count = count;
        this.callback = callback;

        destroyed = false;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void destroy() {
        DestroyEvent destroyEvent = new DestroyEvent(this);
        rootEventManager.dispatchEvent(destroyEvent, this);

        destroyed = true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("interval", interval).append("count", count).append("running", running).toString();
    }

    @Override
    public int getInterval() {
        return interval;
    }

    @Override
    public void setInterval(int ms) {
        this.interval = ms;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void start() {
        if (running) return;

        counting = count;
        factualInterval = 0;
        running = true;

        if (callback != null) {
            try {
                callback.onStart();
            } catch (Throwable ignored) {}
        }
    }

    @Override
    public void stop() {
        if (!running) return;

        running = false;
        if (callback != null) {
            try {
                callback.onStop();
            } catch (Throwable ignored) {}
        }
    }

    @Override
    public void setCallback(TimerCallback callback) {
        this.callback = callback;
    }

    public void tick(int factualInt) {
        if (!running) return;

        factualInterval += factualInt;
        if (factualInterval < interval) return;

        if (count > 0) counting--;
        if (callback != null) {
            try {
                callback.onTick(factualInterval);
            } catch (Throwable ignored) {}
        }

        factualInterval = 0;
        if (count > 0 && counting == 0) stop();
    }
}
