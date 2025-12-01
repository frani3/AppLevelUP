package com.applevelup.levepupgamerapp.data.repository.mobile

import com.applevelup.levepupgamerapp.data.mapper.LevelUpCartMapper
import com.applevelup.levepupgamerapp.data.network.LevelUpMobileApi
import com.applevelup.levepupgamerapp.data.network.dto.CartItemRequestDto
import com.applevelup.levepupgamerapp.data.network.dto.UpdateCartRequestDto
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpCart
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpCartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LevelUpCartRepositoryImpl(
    private val api: LevelUpMobileApi,
    private val mapper: LevelUpCartMapper
) : LevelUpCartRepository {

    private val cachedCart = MutableStateFlow(LevelUpCart())

    override fun observeCart(): Flow<LevelUpCart> = cachedCart.asStateFlow()

    override suspend fun refreshCart(): LevelUpResult<LevelUpCart> {
        return runCatching {
            val dto = api.getCart()
            val cart = mapper.fromDto(dto)
            cachedCart.emit(cart)
            LevelUpResult.Success(cart)
        }.getOrElse { LevelUpResult.Failure(it) }
    }

    override suspend fun addItem(productCode: String, quantity: Int): LevelUpResult<LevelUpCart> {
        return updateCartItems { currentItems ->
            val existing = currentItems.find { it.codigo == productCode }
            if (existing != null) {
                currentItems.map {
                    if (it.codigo == productCode) it.copy(cantidad = it.cantidad + quantity) else it
                }
            } else {
                currentItems + CartItemRequestDto(codigo = productCode, cantidad = quantity)
            }
        }
    }

    override suspend fun updateItemQuantity(productCode: String, quantity: Int): LevelUpResult<LevelUpCart> {
        return updateCartItems { currentItems ->
            if (quantity <= 0) {
                currentItems.filter { it.codigo != productCode }
            } else {
                currentItems.map {
                    if (it.codigo == productCode) it.copy(cantidad = quantity) else it
                }
            }
        }
    }

    override suspend fun removeItem(productCode: String): LevelUpResult<LevelUpCart> {
        return updateCartItems { currentItems ->
            currentItems.filter { it.codigo != productCode }
        }
    }

    override suspend fun clearCart(): LevelUpResult<Unit> {
        return runCatching {
            api.clearCart()
            cachedCart.emit(LevelUpCart())
            LevelUpResult.Success(Unit)
        }.getOrElse { LevelUpResult.Failure(it) }
    }

    private suspend fun updateCartItems(
        transform: (List<CartItemRequestDto>) -> List<CartItemRequestDto>
    ): LevelUpResult<LevelUpCart> {
        return runCatching {
            val currentItems = cachedCart.value.items.map {
                CartItemRequestDto(codigo = it.productCode, cantidad = it.quantity)
            }
            val newItems = transform(currentItems)
            val dto = api.updateCart(UpdateCartRequestDto(items = newItems))
            val cart = mapper.fromDto(dto)
            cachedCart.emit(cart)
            LevelUpResult.Success(cart)
        }.getOrElse { LevelUpResult.Failure(it) }
    }
}
