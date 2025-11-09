package com.applevelup.levepupgamerapp.data.mapper

import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.data.local.entity.UserEntity
import com.applevelup.levepupgamerapp.domain.model.User
import com.applevelup.levepupgamerapp.domain.model.UserProfile

object UserMapper {
	fun toProfile(entity: UserEntity): UserProfile {
		val avatar = entity.avatarRes ?: R.drawable.avatar_placeholder
		return UserProfile(
			name = entity.fullName,
			email = entity.email,
			avatarRes = avatar,
			photoUri = entity.photoUri,
			orderCount = entity.orderCount,
			wishlistCount = entity.wishlistCount,
			couponCount = entity.couponCount
		)
	}

	fun toUser(entity: UserEntity): User = User(
		id = entity.id,
		fullName = entity.fullName,
		email = entity.email,
		isSuperAdmin = entity.isSuperAdmin,
		avatarRes = entity.avatarRes
	)
}