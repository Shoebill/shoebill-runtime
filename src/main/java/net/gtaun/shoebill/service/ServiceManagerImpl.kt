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

package net.gtaun.shoebill.service

import net.gtaun.shoebill.event.service.ServiceRegisterEvent
import net.gtaun.shoebill.event.service.ServiceUnregisterEvent
import net.gtaun.shoebill.resource.Resource
import net.gtaun.util.event.EventManager
import java.util.HashMap

/**
 * @author MK124
 * @author Marvin Haschker
 */
class ServiceManagerImpl(private val rootEventManager: EventManager) : ServiceManager {
    private val services: MutableMap<Class<out Service>, ServiceEntry> = HashMap<Class<out Service>, ServiceEntry>()

    private fun registerServiceEntry(entry: ServiceEntry) {
        val type = entry.type

        val prevEntry = services[type]
        services.put(type, entry)

        if (prevEntry != null) rootEventManager.dispatchEvent(ServiceUnregisterEvent(prevEntry), this)
        rootEventManager.dispatchEvent(ServiceRegisterEvent(entry, prevEntry), this)
    }

    private fun unregisterServiceEntry(entry: ServiceEntry) {
        val type = entry.type
        services.remove(type)
        rootEventManager.dispatchEvent(ServiceUnregisterEvent(entry), this)
    }

    override fun <T : Service> registerService(resource: Resource, type: Class<T>, service: T) {
        val entry = ServiceEntryImpl(resource, type, service)
        registerServiceEntry(entry)
    }

    override fun <T : Service> unregisterService(resource: Resource, type: Class<T>) {
        val entry = services[type] ?: return
        if (entry.provider !== resource) return
        unregisterServiceEntry(entry)
    }

    override fun <T : Service> getService(type: Class<T>): T? {
        val entry = services[type] ?: return null
        return type.cast(entry.service)
    }

    override fun <T : Service> isServiceAvailable(type: Class<T>): Boolean = services.containsKey(type)

    override val serviceEntries: Collection<ServiceEntry>
        get() = services.values

    override fun unregisterAllServices(resource: Resource) {
        services.values.apply { remove(find { it.provider === resource }) }
    }

    override fun <T : Service> getServiceEntry(type: Class<T>): ServiceEntry? = services[type]

    private inner class ServiceEntryImpl<T: Service> internal constructor(override val provider: Resource,
                                                                          type: Class<T>, service: T) : ServiceEntry {
        override val type: Class<out Service> = type
        override val service: Service = service
    }
}
