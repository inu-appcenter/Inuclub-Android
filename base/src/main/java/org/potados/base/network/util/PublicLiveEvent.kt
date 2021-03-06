/**
 * This file is part of INU Club.
 *
 * Copyright (C) 2021 INU Global App Center <potados99@gmail.com>
 *
 * INU Club is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * INU Club is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.potados.base.network.util

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Similar to [androidx.lifecycle.LiveData].
 * However, it does not trigger observer when starting observing:
 * It only fires event when new value is set.
 */
class PublicLiveEvent<T> : MediatorLiveData<T?>() {
    private val observers = ConcurrentHashMap<LifecycleOwner, MutableSet<ObserverWrapper<in T?>>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        val wrapper = ObserverWrapper(observer)

        if (observers[owner] == null) {
            observers[owner] = Collections.newSetFromMap(ConcurrentHashMap())
        }

        observers[owner]?.add(wrapper)

        super.observe(owner, wrapper)
    }

    override fun removeObservers(owner: LifecycleOwner) {
        observers.remove(owner)

        super.removeObservers(owner)
    }

    override fun removeObserver(observer: Observer<in T?>) {
        observers.forEach {
            val wrappers = it.value as MutableSet<*>

            if (wrappers.remove(observer)) {
                if (wrappers.isEmpty()) {
                    observers.remove(it.key)
                }

                return@forEach
            }
        }

        super.removeObserver(observer)
    }

    @MainThread
    override fun setValue(t: T?) {
        observers.forEach { it.value.forEach { wrapper -> wrapper.newValue() } }

        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

    private class ObserverWrapper<T>(private val observer: Observer<in T?>) : Observer<T?> {

        private val pending = AtomicBoolean(false)

        override fun onChanged(t: T?) {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }

        fun newValue() {
            pending.set(true)
        }
    }
}