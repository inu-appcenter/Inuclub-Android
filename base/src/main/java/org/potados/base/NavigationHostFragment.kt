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

package org.potados.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController

/**
 * A base Fragment that will be a holder of each page of bottom navigation.
 */
class NavigationHostFragment : Fragment() {
    private var layoutRes: Int = -1
    private var toolbarId: Int = -1
    private var navHostId: Int = -1
    private var tabItemId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parseArguments()
    }

    private fun parseArguments() {
        arguments?.let {
            layoutRes = it.getInt(KEY_LAYOUT, -1)
            toolbarId = it.getInt(KEY_TOOLBAR, -1)
            navHostId = it.getInt(KEY_NAV_HOST, -1)
            tabItemId = it.getInt(KEY_TAB_ITEM, -1)
        } ?: return
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onStart() {
        super.onStart()

        // It doesn't work inside onCreate.
        setUpToolbarAndNavController()
    }

    private fun setUpToolbarAndNavController() = getToolbar()?.setupWithNavController(getNavController())

    private fun getToolbar(): Toolbar? = if (toolbarId > 0) view?.findViewById(toolbarId) else null

    private fun getNavController(): NavController = requireActivity().findNavController(navHostId)

    fun onBackPressed(): Boolean {
        return getNavController().navigateUp()
    }

    fun popToRoot() {
        getNavController().let {
            it.popBackStack(it.graph.startDestination, false)
        }
    }

    fun handleDeepLink(intent: Intent) = requireActivity().findNavController(navHostId).handleDeepLink(intent)

    fun getTabItemId(): Int {
        return tabItemId
    }

    data class Arguments(
        val layoutRes: Int,
        val toolbarId: Int,
        val navHostId: Int,
        val tabItemId: Int,
    )

    companion object {
        private const val KEY_LAYOUT = "layout_key"
        private const val KEY_TOOLBAR = "toolbar_key"
        private const val KEY_NAV_HOST = "nav_host_key"
        private const val KEY_TAB_ITEM = "tab_item_key"

        fun createArguments(
            layoutRes: Int,
            toolbarId: Int,
            navHostId: Int,
            tabItemId: Int
        ) =
            Arguments(
                layoutRes,
                toolbarId,
                navHostId,
                tabItemId
            )

        fun newInstance(args: Arguments) =
            NavigationHostFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_LAYOUT, args.layoutRes)
                    putInt(KEY_TOOLBAR, args.toolbarId)
                    putInt(KEY_NAV_HOST, args.navHostId)
                    putInt(KEY_TAB_ITEM, args.tabItemId)
                }
            }
    }
}