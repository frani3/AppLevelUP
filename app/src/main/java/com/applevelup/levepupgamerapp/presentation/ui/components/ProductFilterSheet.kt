package com.applevelup.levepupgamerapp.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.applevelup.levepupgamerapp.domain.model.ProductFilters
import com.applevelup.levepupgamerapp.domain.model.ProductSortOption
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple
import com.applevelup.levepupgamerapp.presentation.ui.theme.TopBarAndDrawerColor
import com.applevelup.levepupgamerapp.utils.PriceUtils
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProductFilterSheet(
    currentFilters: ProductFilters,
    availablePriceRange: ClosedFloatingPointRange<Double>,
    onApply: (ProductFilters) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit,
    availableCategories: List<String> = emptyList()
) {
    val priceRange = normalizeRange(availablePriceRange)
    val scrollState = rememberScrollState()
    var sliderValues by remember(currentFilters, priceRange) {
        val initialMin = (currentFilters.minPrice?.toFloat() ?: priceRange.start)
            .coerceIn(priceRange.start, priceRange.endInclusive)
        val initialMax = (currentFilters.maxPrice?.toFloat() ?: priceRange.endInclusive)
            .coerceIn(priceRange.start, priceRange.endInclusive)
        mutableStateOf(min(initialMin, initialMax)..max(initialMin, initialMax))
    }
    var onSaleOnly by remember(currentFilters) { mutableStateOf(currentFilters.onSaleOnly) }
    var minRating by remember(currentFilters) { mutableStateOf(currentFilters.minRating ?: 0f) }
    var sortOption by remember(currentFilters) { mutableStateOf(currentFilters.sortOption) }
    val normalizedCategories = remember(availableCategories) {
        availableCategories.distinct().sorted()
    }
    var allCategoriesSelected by remember(currentFilters, normalizedCategories) {
        mutableStateOf(currentFilters.categories.isEmpty() || currentFilters.categories.containsAll(normalizedCategories))
    }
    var selectedCategories by remember(currentFilters, normalizedCategories) {
        mutableStateOf(currentFilters.categories.intersect(normalizedCategories.toSet()))
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = TopBarAndDrawerColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Filtros",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            if (normalizedCategories.isNotEmpty()) {
                Text("Categorías", color = Color.LightGray, fontWeight = FontWeight.SemiBold)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FilterChip(
                        selected = allCategoriesSelected,
                        onClick = {
                            allCategoriesSelected = true
                            selectedCategories = emptySet()
                        },
                        label = { Text("Todas", color = Color.White) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryPurple,
                            selectedLabelColor = Color.White,
                            containerColor = Color.DarkGray.copy(alpha = 0.5f)
                        )
                    )

                    normalizedCategories.forEach { category ->
                        val isSelected = !allCategoriesSelected && selectedCategories.contains(category)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                val next = selectedCategories.toMutableSet().also { set ->
                                    if (set.contains(category)) {
                                        set.remove(category)
                                    } else {
                                        set.add(category)
                                    }
                                }
                                if (next.isEmpty()) {
                                    allCategoriesSelected = true
                                    selectedCategories = emptySet()
                                } else if (next.containsAll(normalizedCategories)) {
                                    allCategoriesSelected = true
                                    selectedCategories = emptySet()
                                } else {
                                    allCategoriesSelected = false
                                    selectedCategories = next
                                }
                            },
                            label = { Text(category, color = Color.White) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryPurple,
                                selectedLabelColor = Color.White,
                                containerColor = Color.DarkGray.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            }

            Text("Rango de precios", color = Color.LightGray, fontWeight = FontWeight.SemiBold)
            RangeSlider(
                value = sliderValues,
                onValueChange = { sliderValues = it.coerceWithin(priceRange) },
                valueRange = priceRange,
                colors = SliderDefaults.colors(
                    thumbColor = PrimaryPurple,
                    activeTrackColor = PrimaryPurple,
                    inactiveTrackColor = Color.Gray.copy(alpha = 0.4f)
                )
            )
            PriceHintRow(
                minValue = sliderValues.start.toDouble(),
                maxValue = sliderValues.endInclusive.toDouble()
            )

            FilterToggle(
                title = "Mostrar solo en oferta",
                checked = onSaleOnly,
                onCheckedChange = { onSaleOnly = it }
            )

            RatingSelector(minRating = minRating, onSelect = { minRating = it })

            SortSelector(current = sortOption, onSelected = { sortOption = it })

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = {
                        onReset()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Text("Restablecer")
                }

                Button(
                    onClick = {
                        val minPriceValue = sliderValues.start.toDouble()
                        val maxPriceValue = sliderValues.endInclusive.toDouble()
                        val sanitizedMin = if (abs(minPriceValue - priceRange.start.toDouble()) < 1.0) null else minPriceValue
                        val sanitizedMax = if (abs(maxPriceValue - priceRange.endInclusive.toDouble()) < 1.0) null else maxPriceValue
                        val ratingValue = if (minRating <= 0f) null else minRating
                        val categorySelection = when {
                            allCategoriesSelected -> emptySet()
                            selectedCategories.isEmpty() -> emptySet()
                            selectedCategories.containsAll(normalizedCategories) -> emptySet()
                            else -> selectedCategories
                        }
                        onApply(
                            currentFilters.copy(
                                categories = categorySelection,
                                minPrice = sanitizedMin,
                                maxPrice = sanitizedMax,
                                minRating = ratingValue,
                                onSaleOnly = onSaleOnly,
                                sortOption = sortOption
                            )
                        )
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                    Text("Aplicar")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun PriceHintRow(minValue: Double, maxValue: Double) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(PriceUtils.formatPriceCLP(minValue), color = Color.LightGray)
        Text(PriceUtils.formatPriceCLP(maxValue), color = Color.LightGray)
    }
}

@Composable
private fun FilterToggle(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(value = checked, onValueChange = onCheckedChange),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
        Switch(
            checked = checked,
            onCheckedChange = null,
            colors = SwitchDefaults.colors(
                checkedThumbColor = PrimaryPurple,
                checkedTrackColor = PrimaryPurple.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
private fun RatingSelector(minRating: Float, onSelect: (Float) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Calificación mínima", color = Color.LightGray, fontWeight = FontWeight.SemiBold)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val options = listOf(0f to "Todas", 3f to "3★+", 4f to "4★+", 4.5f to "4.5★+")
            options.forEach { (value, label) ->
                FilterChip(
                    selected = minRating == value,
                    onClick = { onSelect(value) },
                    label = { Text(label, color = Color.White) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryPurple,
                        selectedLabelColor = Color.White,
                        containerColor = Color.DarkGray.copy(alpha = 0.5f)
                    )
                )
            }
        }
    }
}

@Composable
private fun SortSelector(current: ProductSortOption, onSelected: (ProductSortOption) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Ordenar por", color = Color.LightGray, fontWeight = FontWeight.SemiBold)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SortChip(option = ProductSortOption.RELEVANCE, current = current, label = "Relevancia", onSelected = onSelected)
            SortChip(option = ProductSortOption.PRICE_ASC, current = current, label = "Precio (↑)", onSelected = onSelected)
            SortChip(option = ProductSortOption.PRICE_DESC, current = current, label = "Precio (↓)", onSelected = onSelected)
            SortChip(option = ProductSortOption.RATING_DESC, current = current, label = "Mejor valoración", onSelected = onSelected)
        }
    }
}

@Composable
private fun SortChip(
    option: ProductSortOption,
    current: ProductSortOption,
    label: String,
    onSelected: (ProductSortOption) -> Unit
) {
    FilterChip(
        selected = option == current,
        onClick = { onSelected(option) },
        label = { Text(label, color = Color.White) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = PrimaryPurple,
            selectedLabelColor = Color.White,
            containerColor = Color.DarkGray.copy(alpha = 0.5f)
        )
    )
}

private fun normalizeRange(range: ClosedFloatingPointRange<Double>): ClosedFloatingPointRange<Float> {
    val start = range.start.toFloat()
    val end = range.endInclusive.toFloat()
    return if (end <= start) {
        start..(start + 1f)
    } else {
        start..end
    }
}

private fun ClosedFloatingPointRange<Float>.coerceWithin(limit: ClosedFloatingPointRange<Float>): ClosedFloatingPointRange<Float> {
    val coercedStart = start.coerceIn(limit.start, limit.endInclusive)
    val coercedEnd = endInclusive.coerceIn(limit.start, limit.endInclusive)
    val orderedStart = min(coercedStart, coercedEnd)
    val orderedEnd = max(coercedStart, coercedEnd)
    return orderedStart..orderedEnd
}
