package com.applevelup.levepupgamerapp.domain.repository

import com.applevelup.levepupgamerapp.domain.model.ProductReview

interface ProductReviewRepository {
    suspend fun getReviewsForProduct(productId: Int): List<ProductReview>
}
