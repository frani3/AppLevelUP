package com.applevelup.levepupgamerapp.domain.repository

import com.applevelup.levepupgamerapp.domain.model.CategoryInfo

interface CategoryRepository {
    suspend fun getAllCategories(): List<CategoryInfo>
}
