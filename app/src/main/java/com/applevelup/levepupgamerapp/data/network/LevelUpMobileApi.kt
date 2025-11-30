package com.applevelup.levepupgamerapp.data.network

import com.applevelup.levepupgamerapp.data.network.dto.AuthResponseDto
import com.applevelup.levepupgamerapp.data.network.dto.AddressDto
import com.applevelup.levepupgamerapp.data.network.dto.AddressRequestDto
import com.applevelup.levepupgamerapp.data.network.dto.CategoryDto
import com.applevelup.levepupgamerapp.data.network.dto.CreateProductRequestDto
import com.applevelup.levepupgamerapp.data.network.dto.LevelUpStatsDto
import com.applevelup.levepupgamerapp.data.network.dto.LoginRequestDto
import com.applevelup.levepupgamerapp.data.network.dto.ProductDto
import com.applevelup.levepupgamerapp.data.network.dto.RegisterRequestDto
import com.applevelup.levepupgamerapp.data.network.dto.RegionDto
import com.applevelup.levepupgamerapp.data.network.dto.UserProfileDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface LevelUpMobileApi {

    @POST("/api/v1/auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @POST("/api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @GET("/api/v1/users/me")
    suspend fun getProfile(): UserProfileDto

    @GET("/api/v1/levelup/{run}/stats")
    suspend fun getLevelUpStats(@Path("run") run: String): LevelUpStatsDto

    @GET("/api/v1/products")
    suspend fun getProducts(
        @Query("includeDeleted") includeDeleted: Boolean? = false,
        @Query("category") category: String? = null,
        @Query("query") query: String? = null
    ): List<ProductDto>

    @GET("/api/v1/products/{codigo}")
    suspend fun getProductDetail(@Path("codigo") codigo: String): ProductDto

    @POST("/api/v1/products")
    suspend fun createProduct(@Body request: CreateProductRequestDto): ProductDto

    @GET("/api/v1/categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("/api/v1/regiones")
    suspend fun getRegions(): List<RegionDto>

    @GET("/api/v1/users/{run}/addresses")
    suspend fun getAddresses(@Path("run") run: String): List<AddressDto>

    @POST("/api/v1/users/{run}/addresses")
    suspend fun addAddress(
        @Path("run") run: String,
        @Body request: AddressRequestDto
    ): AddressDto

    @PUT("/api/v1/users/{run}/addresses/{addressId}")
    suspend fun updateAddress(
        @Path("run") run: String,
        @Path("addressId") addressId: String,
        @Body request: AddressRequestDto
    ): AddressDto

    @DELETE("/api/v1/users/{run}/addresses/{addressId}")
    suspend fun deleteAddress(
        @Path("run") run: String,
        @Path("addressId") addressId: String
    ): List<AddressDto>

    @POST("/api/v1/users/{run}/addresses/{addressId}/primary")
    suspend fun setPrimaryAddress(
        @Path("run") run: String,
        @Path("addressId") addressId: String
    ): List<AddressDto>
}
