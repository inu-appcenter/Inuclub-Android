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

package org.potados.network.repository

import android.net.ConnectivityManager
import org.potados.network.helper.NetworkHelper
import org.potados.network.util.PublicLiveEvent
import java.util.*
import kotlin.concurrent.schedule

class DeviceStatusRepository(private val manager: ConnectivityManager) {

    private val networkChangeEvent = PublicLiveEvent<Boolean>()

    fun initialize() {
        startObservingNetworkState()
    }

    private fun startObservingNetworkState() {
        val onOnline = { postAfterSomeTime(true) }
        val onOffline = { networkChangeEvent.postValue(false) }

        NetworkHelper.onNetworkChange(manager, onOnline, onOffline)
    }

    private fun postAfterSomeTime(value: Boolean) {
        // Don't know why but after network turned available and isOnline() returns true,
        // the network doesn't work for a while.
        // So we need to wait until it become 'really' available.
        Timer().schedule(500) {
            networkChangeEvent.postValue(value)
        }
    }

    fun isOnline(): Boolean {
        return NetworkHelper.isOnline(manager)
    }

    fun networkStateChangeEvent(): PublicLiveEvent<Boolean> = networkChangeEvent
}