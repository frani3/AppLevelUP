package com.applevelup.levepupgamerapp.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.applevelup.levepupgamerapp.presentation.navigation.Destinations
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple

enum class MainDestination { Home, Categories, Cart, Favorites, Profile }

data class BottomNavItem(
    val destination: MainDestination,
    val label: String,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
    val isCentral: Boolean = false,
    val badgeCount: Int = 0
)

fun mapRouteToMainDestination(route: String?): MainDestination = when (route) {
    Destinations.Categories.route -> MainDestination.Categories
    Destinations.Cart.route -> MainDestination.Cart
    Destinations.Favorites.route -> MainDestination.Favorites
    Destinations.Profile.route -> MainDestination.Profile
    else -> MainDestination.Home
}

fun NavController.navigateToMainDestination(destination: MainDestination) {
    val targetRoute = when (destination) {
        MainDestination.Home -> Destinations.Landing.route
        MainDestination.Categories -> Destinations.Categories.route
        MainDestination.Cart -> Destinations.Cart.route
        MainDestination.Favorites -> Destinations.Favorites.route
        MainDestination.Profile -> Destinations.Profile.route
    }

    navigate(targetRoute) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun LevelUpBottomNavigation(
    selectedDestination: MainDestination,
    onDestinationSelected: (MainDestination) -> Unit,
    cartBadgeCount: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF0B0B0D)
) {
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val navItems = remember(cartBadgeCount) {
        listOf(
            BottomNavItem(MainDestination.Home, "Inicio", Icons.Outlined.Home, Icons.Filled.Home),
            BottomNavItem(MainDestination.Categories, "Categorías", Icons.Outlined.Category, Icons.Filled.Category),
            BottomNavItem(
                destination = MainDestination.Cart,
                label = "Carrito",
                outlinedIcon = Icons.Outlined.ShoppingCart,
                filledIcon = Icons.Filled.ShoppingCart,
                isCentral = true,
                badgeCount = cartBadgeCount
            ),
            BottomNavItem(MainDestination.Favorites, "Favoritos", Icons.Outlined.Favorite, Icons.Filled.Favorite),
            BottomNavItem(MainDestination.Profile, "Perfil", Icons.Outlined.Person, Icons.Filled.Person)
        )
    }

    Surface(modifier = modifier, color = backgroundColor) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(108.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                navItems.forEach { item ->
                    val isSelected = selectedDestination == item.destination
                    val interactionSource = remember(item.destination) { MutableInteractionSource() }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) { onDestinationSelected(item.destination) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            val spacerHeight = if (item.isCentral) 6.dp else 8.dp

                            if (item.isCentral) {
                                val circleSize = if (isSelected) 70.dp else 64.dp
                                val circleColor = if (isSelected) PrimaryPurple else PrimaryPurple.copy(alpha = 0.22f)
                                val iconModifier = Modifier.size(if (isSelected) 26.dp else 24.dp)

                                Surface(
                                    modifier = Modifier.size(circleSize),
                                    shape = androidx.compose.foundation.shape.CircleShape,
                                    color = circleColor,
                                    tonalElevation = if (isSelected) 8.dp else 4.dp,
                                    shadowElevation = if (isSelected) 8.dp else 4.dp
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        if (item.badgeCount > 0) {
                                            BadgedBox(
                                                badge = {
                                                    Badge(containerColor = Color.White, contentColor = PrimaryPurple) {
                                                        Text(
                                                            text = item.badgeCount.coerceAtMost(99).toString(),
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = item.filledIcon,
                                                    contentDescription = item.label,
                                                    tint = Color.White,
                                                    modifier = iconModifier
                                                )
                                            }
                                        } else {
                                            Icon(
                                                imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                                                contentDescription = item.label,
                                                tint = Color.White,
                                                modifier = iconModifier
                                            )
                                        }
                                    }
                                }
                            } else {
                                val iconModifier = Modifier.size(if (isSelected) 26.dp else 24.dp)
                                if (item.badgeCount > 0) {
                                    BadgedBox(
                                        badge = {
                                            Badge(containerColor = PrimaryPurple) {
                                                Text(
                                                    text = item.badgeCount.coerceAtMost(99).toString(),
                                                    color = Color.White,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                                            contentDescription = item.label,
                                            tint = if (isSelected) PrimaryPurple else Color.White.copy(alpha = 0.7f),
                                            modifier = iconModifier
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                                        contentDescription = item.label,
                                        tint = if (isSelected) PrimaryPurple else Color.White.copy(alpha = 0.7f),
                                        modifier = iconModifier
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(spacerHeight))
                            Text(
                                text = item.label,
                                color = if (isSelected) PrimaryPurple else Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(spacerHeight))
                            Box(
                                modifier = Modifier
                                    .height(3.dp)
                                    .width(30.dp)
                                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(50))
                                    .background(if (isSelected) PrimaryPurple else Color.Transparent)
                            )
                        }
                    }
                }
            }
            if (bottomInset > 0.dp) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(bottomInset)
                        .background(backgroundColor)
                )
            }
        }
    }
}
