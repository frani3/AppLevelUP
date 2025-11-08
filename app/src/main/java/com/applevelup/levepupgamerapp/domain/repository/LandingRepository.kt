package com.applevelup.levepupgamerapp.domain.repository

import com.applevelup.levepupgamerapp.domain.model.*

interface LandingRepository {
    fun getPromotions(): List<Promotion>
    fun getCategories(): List<Category>
    fun getFeaturedProducts(): List<ProductSummary>
    fun getNewProducts(): List<ProductSummary>
}
