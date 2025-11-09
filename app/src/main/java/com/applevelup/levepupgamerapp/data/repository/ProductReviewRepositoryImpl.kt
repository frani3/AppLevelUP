package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.data.local.seed.LocalSeedData
import com.applevelup.levepupgamerapp.domain.model.ProductReview
import com.applevelup.levepupgamerapp.domain.repository.ProductReviewRepository

class ProductReviewRepositoryImpl : ProductReviewRepository {
    override suspend fun getReviewsForProduct(productId: Int): List<ProductReview> {
        return LocalSeedData.productReviews[productId].orEmpty()
    }
}
