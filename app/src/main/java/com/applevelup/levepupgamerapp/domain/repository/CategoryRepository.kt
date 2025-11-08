package com.applevelup.levepupgamerapp.domain.repository

import com.applevelup.levepupgamerapp.domain.model.CategoryInfo

interface CategoryRepository {
    fun getAllCategories(): List<CategoryInfo>
}
