package com.applevelup.levepupgamerapp.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.domain.model.CardType
import com.applevelup.levepupgamerapp.presentation.ui.screens.getTextFieldColors
import com.applevelup.levepupgamerapp.presentation.viewmodel.AddPaymentUiState

@Composable
fun PaymentForm(
    state: AddPaymentUiState,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onExpiryChange: (String) -> Unit,
    onCvvChange: (String) -> Unit
) {
    OutlinedTextField(
        value = state.cardholderName,
        onValueChange = onNameChange,
        label = { Text("Nombre del Titular") },
        modifier = Modifier.fillMaxWidth(),
        colors = paymentTextFieldColors(),
        singleLine = true
    )
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(
        value = state.cardNumber,
        onValueChange = onNumberChange,
        label = { Text("Número de la Tarjeta") },
        modifier = Modifier.fillMaxWidth(),
        colors = paymentTextFieldColors(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = CardNumberVisualTransformation(),
        trailingIcon = { CardTypeIcon(state.cardNumber) },
        singleLine = true
    )
    Spacer(Modifier.height(16.dp))
    Row(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = state.expiryDate,
            onValueChange = onExpiryChange,
            label = { Text("Expira (MM/AA)") },
            modifier = Modifier.weight(1f),
            colors = paymentTextFieldColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = ExpiryDateVisualTransformation(),
            singleLine = true
        )
        Spacer(Modifier.width(16.dp))
        OutlinedTextField(
            value = state.cvv,
            onValueChange = onCvvChange,
            label = { Text("CVV") },
            modifier = Modifier.weight(1f),
            colors = paymentTextFieldColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
    }
}

@Composable
fun CardTypeIcon(cardNumber: String) {
    val cardType = when {
        cardNumber.startsWith("4") -> CardType.VISA
        cardNumber.startsWith("5") -> CardType.MASTERCARD
        else -> CardType.OTHER
    }

    val iconRes = when (cardType) {
        CardType.VISA -> R.drawable.ic_visa
        CardType.MASTERCARD -> R.drawable.ic_mastercard
        else -> null
    }

    if (iconRes != null) {
        Image(painter = painterResource(iconRes), contentDescription = cardType.name, modifier = Modifier.height(24.dp))
    } else {
        Icon(Icons.Default.CreditCard, contentDescription = "Tarjeta", tint = Color.Gray)
    }
}

private class CardNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.take(16)
        val spaced = trimmed.chunked(4).joinToString(" ")
        val offset = object : OffsetMapping {
            override fun originalToTransformed(offset: Int) = offset + offset / 4
            override fun transformedToOriginal(offset: Int) = offset - offset / 5
        }
        return TransformedText(AnnotatedString(spaced), offset)
    }
}

private class ExpiryDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = text.text.take(4).let {
            if (it.length >= 2) it.substring(0, 2) + "/" + it.drop(2) else it
        }
        val offset = object : OffsetMapping {
            override fun originalToTransformed(offset: Int) = if (offset > 1) offset + 1 else offset
            override fun transformedToOriginal(offset: Int) = if (offset > 2) offset - 1 else offset
        }
        return TransformedText(AnnotatedString(formatted), offset)
    }
}
@Composable
fun paymentTextFieldColors(): TextFieldColors {
    val backgroundColor = Color.DarkGray.copy(alpha = 0.3f)
    val textColor = Color.White
    return TextFieldDefaults.colors(
        focusedContainerColor = backgroundColor,
        unfocusedContainerColor = backgroundColor,
        disabledContainerColor = backgroundColor,
        focusedIndicatorColor = Color.Gray,
        unfocusedIndicatorColor = Color.Gray,
        focusedLabelColor = textColor,
        unfocusedLabelColor = textColor,
        focusedTextColor = textColor,
        unfocusedTextColor = textColor
    )
}
