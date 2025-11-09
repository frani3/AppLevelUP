package com.applevelup.levepupgamerapp.data.mapper

import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.data.local.entity.UserEntity
import com.applevelup.levepupgamerapp.domain.model.UserProfile

object UserMapper {
	fun toProfile(entity: UserEntity): UserProfile {
		val avatar = entity.avatarRes ?: R.drawable.avatar_placeholder
		return UserProfile(
			name = entity.fullName,
			email = entity.email,
			avatarRes = avatar,
			orderCount = entity.orderCount,
			wishlistCount = entity.wishlistCount,
			couponCount = entity.couponCount
		)
	}
}