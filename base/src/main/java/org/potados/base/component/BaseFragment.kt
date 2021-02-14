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

package org.potados.base.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import org.potados.base.extension.observe
import org.potados.network.NetworkObserver

/**
 * A base Fragment that:
 * - provides shorthanded view creating
 * - listens for network changes
 */
abstract class BaseFragment<T: ViewDataBinding> :
    Fragment(),
    BindingOwner<T>,
    NetworkChangeObserver {

    /****************************************************************
     * BindingOwner
     ****************************************************************/

    override var binding: T? = null

    /****************************************************************
     * Fragment
     ****************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeNetworkStateChange(savedInstanceState == null)
    }

    /**
     * See [onCreateBinding].
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = onCreateBinding(BindingCreator(this, inflater, container))
        .also { binding = it }
        .root

    /**
     * User can choose to override [onCreateView] or this method [onCreateBinding].
     * This method helps creating view easily.
     */
    abstract fun onCreateBinding(create: BindingCreator): T

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

    /**
     * This wraps view creating code.
     * Invoke a BindingCreator instance to create a ViewDataBinding instance.
     */
    inner class BindingCreator(
        val fragment: Fragment,
        val inflater: LayoutInflater,
        val container: ViewGroup?
    ) {
        inline operator fun <reified ReifiedT: ViewDataBinding> invoke(also: ReifiedT.() -> Unit = {}) =
            createView(also)

        inline fun <reified ReifiedT: ViewDataBinding> createView(also: ReifiedT.() -> Unit = {}): T {
            val inflateMethod = ReifiedT::class.java.getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )

            @Suppress("UNCHECKED_CAST")
            return (inflateMethod.invoke(null, inflater, container, false) as ReifiedT)
                .apply { lifecycleOwner = fragment }
                .apply { also(this) } as? T
                ?: throw Exception("Type mismatch! Please check the generic parameters passed to the BindingCreator and BaseFragment. They must be equal.")
        }
    }

    /****************************************************************
     * NetworkChangeObserver
     ****************************************************************/

    protected fun isOnline() = NetworkObserver.isOnline

    private fun observeNetworkStateChange(isThisFirstTimeCreated: Boolean) {
        if (isThisFirstTimeCreated) {
            onNetworkStateChange(isOnline())
        }

        observe(NetworkObserver.networkChangeEvent) {
            it?.let(::onNetworkStateChange)
        }
    }

    override fun onNetworkStateChange(available: Boolean) {
        // Make your implementation here.
    }
}