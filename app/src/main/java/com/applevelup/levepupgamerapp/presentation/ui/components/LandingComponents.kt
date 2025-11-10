package com.applevelup.levepupgamerapp.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.domain.model.Category
import com.applevelup.levepupgamerapp.domain.model.ProductSummary
import com.applevelup.levepupgamerapp.domain.model.Promotion
import com.applevelup.levepupgamerapp.presentation.navigation.Destinations
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple

@Composable
fun CartActionButton(
    count: Int,
    onClick: () -> Unit,
    tint: Color = Color.White
) {
    IconButton(onClick = onClick) {
        BadgedBox(
            badge = {
                if (count > 0) {
                    Badge(containerColor = PrimaryPurple) {
                        val label = if (count > 99) "99+" else count.toString()
                        Text(
                            text = label,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Carrito",
                tint = tint
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPageTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchActive: Boolean,
    onSearchActiveChange: (Boolean) -> Unit,
    notificationCount: Int,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    val searchShape by animateDpAsState(
        targetValue = if (searchActive) 18.dp else 26.dp,
        label = "searchShape"
    )
    val searchHeight by animateDpAsState(
        targetValue = if (searchActive) 56.dp else 48.dp,
        label = "searchHeight"
    )
    val searchElevation by animateDpAsState(
        targetValue = if (searchActive) 8.dp else 2.dp,
        label = "searchElevation"
    )

    LaunchedEffect(searchActive) {
        if (searchActive) {
            focusRequester.requestFocus()
        } else {
            focusManager.clearFocus()
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF050506),
        contentColor = Color.White,
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val baseModifier = Modifier
                    .weight(1f)
                    .height(searchHeight)

                Surface(
                    modifier = baseModifier,
                    color = Color(0xFF111118),
                    shape = RoundedCornerShape(searchShape),
                    tonalElevation = searchElevation,
                    shadowElevation = searchElevation
                ) {
                    val textFieldModifier = if (searchActive) {
                        Modifier
                            .fillMaxSize()
                            .focusRequester(focusRequester)
                    } else {
                        Modifier
                            .fillMaxSize()
                            .focusRequester(focusRequester)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) { onSearchActiveChange(true) }
                    }

                    TextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = textFieldModifier,
                        placeholder = {
                            Text(
                                text = "Buscar productos...",
                                color = Color.White.copy(alpha = 0.7f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Buscar",
                                tint = Color.White.copy(alpha = 0.85f)
                            )
                        },
                        trailingIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { /* TODO voice search */ }) {
                                    Icon(
                                        imageVector = Icons.Filled.Mic,
                                        contentDescription = "Búsqueda por voz",
                                        tint = Color.White.copy(alpha = 0.8f)
                                    )
                                }
                                AnimatedVisibility(visible = searchActive) {
                                    IconButton(onClick = {
                                        onSearchQueryChange("")
                                        onSearchActiveChange(false)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = "Cerrar búsqueda",
                                            tint = Color.White.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        },
                        singleLine = true,
                        readOnly = !searchActive,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            focusManager.clearFocus()
                        }),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = PrimaryPurple,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(searchShape)
                    )
                }

                BadgedBox(
                    badge = {
                        if (notificationCount > 0) {
                            Badge(containerColor = PrimaryPurple) {
                                Text(
                                    text = notificationCount.coerceAtMost(99).toString(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                ) {
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            imageVector = if (notificationCount > 0) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                            contentDescription = "Notificaciones",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchSuggestionPanel(
    query: String,
    recentItems: List<String>,
    trendingItems: List<String>,
    onSuggestionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val normalizedQuery = remember(query) { query.trim() }
    val filteredTrending = remember(normalizedQuery, trendingItems) {
        if (normalizedQuery.isBlank()) trendingItems
        else trendingItems.filter { it.contains(normalizedQuery, ignoreCase = true) }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        color = Color(0xFF0F0F14).copy(alpha = 0.98f),
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 6.dp,
        shadowElevation = 12.dp
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            if (normalizedQuery.isBlank()) {
                SuggestionSection(
                    title = "Búsquedas recientes",
                    icon = Icons.Outlined.History,
                    items = recentItems,
                    onSuggestionClick = onSuggestionClick
                )
                Spacer(modifier = Modifier.height(12.dp))
                SuggestionSection(
                    title = "Tendencias LevelUp",
                    icon = Icons.Outlined.TrendingUp,
                    items = trendingItems,
                    onSuggestionClick = onSuggestionClick
                )
            } else {
                SuggestionSection(
                    title = "Sugerencias",
                    icon = Icons.Outlined.Search,
                    items = if (filteredTrending.isEmpty()) listOf("Ver resultados para \"$normalizedQuery\"") else filteredTrending,
                    onSuggestionClick = onSuggestionClick,
                    highlightQuery = normalizedQuery
                )
            }
        }
    }
}

@Composable
private fun SuggestionSection(
    title: String,
    icon: ImageVector,
    items: List<String>,
    onSuggestionClick: (String) -> Unit,
    highlightQuery: String? = null
) {
    if (items.isEmpty()) return

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryPurple, modifier = Modifier.size(18.dp))
            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }

        items.forEach { suggestion ->
            SuggestionRow(
                icon = icon,
                text = suggestion,
                highlightQuery = highlightQuery,
                onClick = { onSuggestionClick(suggestion) }
            )
        }
    }
}

@Composable
private fun SuggestionRow(
    icon: ImageVector,
    text: String,
    highlightQuery: String?,
    onClick: () -> Unit
) {
    val annotatedText: AnnotatedString = remember(text, highlightQuery) {
        if (highlightQuery.isNullOrBlank()) {
            AnnotatedString(text)
        } else {
            val index = text.indexOf(highlightQuery, ignoreCase = true)
            if (index < 0) {
                AnnotatedString(text)
            } else {
                buildAnnotatedString {
                    append(text.substring(0, index))
                    pushStyle(SpanStyle(color = PrimaryPurple, fontWeight = FontWeight.SemiBold))
                    append(text.substring(index, index + highlightQuery.length))
                    pop()
                    append(text.substring(index + highlightQuery.length))
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(icon, contentDescription = null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
        Text(
            text = annotatedText,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun FeaturedCarousel(
    promotions: List<Promotion>,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    if (promotions.isEmpty()) return

    val safeIndex = currentPage.coerceIn(0, promotions.lastIndex)
    val promotion = promotions[safeIndex]

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 8.dp,
        shadowElevation = 12.dp
    ) {
        Box {
            Image(
                painter = painterResource(id = promotion.imageRes),
                contentDescription = promotion.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.15f), Color.Black.copy(alpha = 0.85f))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = promotion.title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = promotion.subtitle,
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                promotions.forEachIndexed { index, _ ->
                    val isSelected = index == safeIndex
                    val width by animateDpAsState(
                        targetValue = if (isSelected) 22.dp else 8.dp,
                        label = "carouselDotWidth"
                    )
                    val alpha by animateFloatAsState(
                        targetValue = if (isSelected) 1f else 0.3f,
                        label = "carouselDotAlpha"
                    )

                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .width(width)
                            .clip(RoundedCornerShape(4.dp))
                            .background(PrimaryPurple.copy(alpha = alpha))
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        if (actionLabel != null && onActionClick != null) {
            Text(
                text = actionLabel,
                color = PrimaryPurple,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clickable(onClick = onActionClick)
            )
        }
    }
}

@Composable
fun CategoryShortcuts(
    categories: List<Category>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    if (categories.isEmpty()) return

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            Column(
                modifier = Modifier
                    .width(86.dp)
                    .clickable {
                        navController.navigate(Destinations.ProductList.create(category.name))
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF16161F))
                        .border(width = 1.dp, color = PrimaryPurple.copy(alpha = 0.6f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = category.iconRes),
                        contentDescription = category.name,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = category.name,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun ProductShowcaseRow(
    products: List<ProductSummary>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    if (products.isEmpty()) return

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(products) { product ->
            Card(
                modifier = Modifier
                    .width(176.dp)
                    .height(236.dp)
                    .clickable { navController.navigate(Destinations.ProductDetail.create(product.id)) },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF14141C)),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        tonalElevation = 0.dp,
                        color = Color(0xFF1E1E27)
                    ) {
                        Image(
                            painter = painterResource(id = product.imageRes),
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Text(
                        text = product.name,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = product.price,
                        color = PrimaryPurple,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    AnimatedVisibility(visible = product.id % 2 == 0, enter = fadeIn(), exit = fadeOut()) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = PrimaryPurple.copy(alpha = 0.18f)
                        ) {
                            Text(
                                text = "Envío gratis",
                                color = PrimaryPurple,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
