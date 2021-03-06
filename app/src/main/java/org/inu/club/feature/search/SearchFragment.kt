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

package org.inu.club.feature.search

import android.os.Bundle
import android.view.View
import org.inu.club.R
import org.inu.club.databinding.SearchFragmentBinding
import org.potados.base.component.BaseFragment
import org.potados.base.extension.setupToolbarForNavigation

class SearchFragment : BaseFragment<SearchFragmentBinding>() {

    override fun onCreateBinding(create: BindingCreator) = create<SearchFragmentBinding> {
        // Do some...
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbarForNavigation(R.id.toolbar)
    }
}