package com.applevelup.levepupgamerapp.domain.sync

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile

interface LegacyUserSyncer {
    suspend fun replaceWith(profile: LevelUpUserProfile)
    suspend fun clear()
}
