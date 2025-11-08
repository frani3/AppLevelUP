package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.CategoryInfo
import com.applevelup.levepupgamerapp.domain.repository.CategoryRepository

class GetCategoriesUseCase(private val repo: CategoryRepository) {
    operator fun invoke(): List<CategoryInfo> = repo.getAllCategories()
}
