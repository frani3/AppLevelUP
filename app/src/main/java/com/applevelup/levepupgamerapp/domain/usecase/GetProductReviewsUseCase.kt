package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.ProductReview
import com.applevelup.levepupgamerapp.domain.repository.ProductReviewRepository

class GetProductReviewsUseCase(private val repository: ProductReviewRepository) {
    suspend operator fun invoke(productId: Int): List<ProductReview> {
        return repository.getReviewsForProduct(productId)
    }
}
