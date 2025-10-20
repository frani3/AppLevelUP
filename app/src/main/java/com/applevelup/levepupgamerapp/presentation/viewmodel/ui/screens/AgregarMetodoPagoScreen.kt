package com.applevelup.levepupgamerapp.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.presentation.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarMetodoPagoScreen(navController: NavController) {

    var cardholderName by rememberSaveable { mutableStateOf("") }
    var cardNumber by rememberSaveable { mutableStateOf("") }
    var expiryDate by rememberSaveable { mutableStateOf("") }
    var cvv by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agregar Tarjeta", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TopBarAndDrawerColor,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = Color.White)
                    }
                }
            )
        },
        containerColor = PureBlackBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = cardholderName,
                    onValueChange = { cardholderName = it },
                    label = { Text("Nombre del Titular") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = getTextFieldColors(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { if (it.length <= 16) cardNumber = it.filter { char -> char.isDigit() } },
                    label = { Text("Número de la Tarjeta") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = getTextFieldColors(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = CardNumberVisualTransformation(),
                    trailingIcon = { CardTypeIcon(cardNumber) },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = expiryDate,
                        onValueChange = { if (it.length <= 4) expiryDate = it.filter { char -> char.isDigit() } },
                        label = { Text("Expira (MM/AA)") },
                        modifier = Modifier.weight(1f),
                        colors = getTextFieldColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = ExpiryDateVisualTransformation(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { if (it.length <= 3) cvv = it.filter { char -> char.isDigit() } },
                        label = { Text("CVV") },
                        modifier = Modifier.weight(1f),
                        colors = getTextFieldColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        // TODO: Lógica para validar y guardar la tarjeta
                        // viewModel.addPaymentMethod(...)
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text(text = "Guardar Tarjeta", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}

// --- COMPONENTES Y LÓGICA DE FORMATO ---

@Composable
private fun CardTypeIcon(cardNumber: String) {
    val cardType = when {
        cardNumber.startsWith("4") -> CardType.VISA
        cardNumber.startsWith("5") -> CardType.MASTERCARD
        else -> CardType.OTHER
    }

    val iconRes = when(cardType) {
        CardType.VISA -> R.drawable.ic_visa
        CardType.MASTERCARD -> R.drawable.ic_mastercard
        else -> null
    }

    if (iconRes != null) {
        Image(painter = painterResource(id = iconRes), contentDescription = cardType.name, modifier = Modifier.height(24.dp))
    } else {
        Icon(Icons.Default.CreditCard, contentDescription = "Tarjeta", tint = Color.Gray)
    }
}

private class CardNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 4 == 3 && i != 15) out += " "
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset + 1
                if (offset <= 11) return offset + 2
                if (offset <= 16) return offset + 3
                return 19
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 9) return offset - 1
                if (offset <= 14) return offset - 2
                if (offset <= 19) return offset - 3
                return 16
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

private class ExpiryDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val out = if (text.text.length >= 2) {
            text.text.substring(0, 2) + "/" + text.text.substring(2)
        } else {
            text.text
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return if (offset >= 2) offset + 1 else offset
            }
            override fun transformedToOriginal(offset: Int): Int {
                return if (offset >= 3) offset - 1 else offset
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

// --- FUNCIÓN DE AYUDA AÑADIDA ---
@Composable
private fun getTextFieldColors(): TextFieldColors {
    val textFieldBackgroundColor = Color.DarkGray.copy(alpha = 0.3f)
    val lightTextColor = Color.LightGray
    return TextFieldDefaults.colors(
        focusedContainerColor = textFieldBackgroundColor,
        unfocusedContainerColor = textFieldBackgroundColor,
        disabledContainerColor = textFieldBackgroundColor,
        focusedIndicatorColor = PrimaryPurple,
        unfocusedIndicatorColor = Color.Gray,
        focusedLabelColor = lightTextColor,
        unfocusedLabelColor = lightTextColor,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedTrailingIconColor = lightTextColor,
        unfocusedTrailingIconColor = lightTextColor
    )
}

