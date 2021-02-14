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

package org.inu.club.injection

import org.inu.club.common.navigation.Navigator
import org.inu.club.config.Config
import org.inu.club.retrofit.ClubNetworkService
import org.koin.dsl.module
import org.potados.base.infrastructure.retrofit.RetrofitFactory

val myModules = module {

    /** Navigator */
    single {
        Navigator(
            context = get()
        )
    }

    /** Network Service */
    single {
        RetrofitFactory.createNetworkService(
            networkInterface = ClubNetworkService::class.java,
            context = get(),
            baseUrl = Config.baseUrl
        )
    }
}