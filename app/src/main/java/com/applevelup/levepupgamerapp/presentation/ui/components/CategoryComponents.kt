package com.applevelup.levepupgamerapp.presentation.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.applevelup.levepupgamerapp.domain.model.CategoryInfo
import com.applevelup.levepupgamerapp.presentation.ui.theme.CardBackgroundColor
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple

@Composable
fun CategoryCard(category: CategoryInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .aspectRatio(1f)
            .border(2.dp, PrimaryPurple, MaterialTheme.shapes.large),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(category.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                Icon(category.icon, contentDescription = category.name, tint = PrimaryPurple)
            }
            Spacer(Modifier.weight(1f))
            Column {
                category.sampleProducts.forEach { productName ->
                    Text("• $productName", color = Color.Gray, fontSize = 14.sp, lineHeight = 20.sp)
                }
            }
        }
    }
}
