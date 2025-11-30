package com.applevelup.levepupgamerapp.domain.usecase.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpProduct
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpProductRepository

class FetchProductDetailUseCase(private val repository: LevelUpProductRepository) {

    suspend operator fun invoke(code: String): LevelUpResult<LevelUpProduct> {
        return repository.fetchProductDetail(code)
    }
}
