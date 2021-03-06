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

package org.inu.club.entity

import org.inu.club.entity.base.Entity

/**
 * 소모임.
 */
data class Gatherings(
    val id: Int,
    val host: String,
    val category: GatheringsCategory,
    val title: String,
    val body: String,
    val participationInfo: GatheringsParticipationInfo,
    val numberOfPersonsJoined: Int,
    val numberOfPersonsToInvite: Int,
    val comments: List<Comment>
) : Entity {

    fun canWeJoinHere(): Boolean {
        return !isFull()
    }

    private fun isFull(): Boolean {
        return numberOfPersonsJoined >= numberOfPersonsToInvite
    }
}
