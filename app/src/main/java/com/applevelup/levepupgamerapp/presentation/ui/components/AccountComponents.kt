package com.applevelup.levepupgamerapp.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.applevelup.levepupgamerapp.presentation.ui.theme.PrimaryPurple
import com.applevelup.levepupgamerapp.presentation.viewmodel.AccountViewModel
import com.applevelup.levepupgamerapp.presentation.viewmodel.AccountUiState

@Composable
fun AccountForm(
    state: AccountUiState,
    viewModel: AccountViewModel
) {
    var newPassVisible by remember { mutableStateOf(false) }
    var confPassVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = state.fullName,
        onValueChange = viewModel::onFullNameChange,
        label = { Text("Nombre Completo") },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.DarkGray.copy(alpha = 0.3f),
            unfocusedContainerColor = Color.DarkGray.copy(alpha = 0.3f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedIndicatorColor = PrimaryPurple,
            unfocusedIndicatorColor = Color.Gray
        ),
        isError = state.errors["name"] != null
    )
    if (state.errors["name"] != null) Text(state.errors["name"]!!, color = Color.Red, fontSize = 12.sp)
    Spacer(Modifier.height(16.dp))

    OutlinedTextField(
        value = state.email,
        onValueChange = viewModel::onEmailChange,
        label = { Text("Correo Electrónico") },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.DarkGray.copy(alpha = 0.3f),
            unfocusedContainerColor = Color.DarkGray.copy(alpha = 0.3f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedIndicatorColor = PrimaryPurple,
            unfocusedIndicatorColor = Color.Gray
        ),
        isError = state.errors["email"] != null
    )
    if (state.errors["email"] != null) Text(state.errors["email"]!!, color = Color.Red, fontSize = 12.sp)
    Spacer(Modifier.height(32.dp))

    OutlinedTextField(
        value = state.newPassword,
        onValueChange = viewModel::onNewPasswordChange,
        label = { Text("Nueva Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (newPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val icon = if (newPassVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = { newPassVisible = !newPassVisible }) {
                Icon(icon, contentDescription = null, tint = Color.Gray)
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.DarkGray.copy(alpha = 0.3f),
            unfocusedContainerColor = Color.DarkGray.copy(alpha = 0.3f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedIndicatorColor = PrimaryPurple,
            unfocusedIndicatorColor = Color.Gray
        ),
        isError = state.errors["password"] != null
    )
    if (state.errors["password"] != null) Text(state.errors["password"]!!, color = Color.Red, fontSize = 12.sp)
    Spacer(Modifier.height(16.dp))

    OutlinedTextField(
        value = state.confirmPassword,
        onValueChange = viewModel::onConfirmPasswordChange,
        label = { Text("Confirmar Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (confPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val icon = if (confPassVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = { confPassVisible = !confPassVisible }) {
                Icon(icon, contentDescription = null, tint = Color.Gray)
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.DarkGray.copy(alpha = 0.3f),
            unfocusedContainerColor = Color.DarkGray.copy(alpha = 0.3f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedIndicatorColor = PrimaryPurple,
            unfocusedIndicatorColor = Color.Gray
        ),
        isError = state.errors["confirm"] != null
    )
    if (state.errors["confirm"] != null) Text(state.errors["confirm"]!!, color = Color.Red, fontSize = 12.sp)
    Spacer(Modifier.height(32.dp))

    Button(
        onClick = viewModel::saveChanges,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
    ) {
        Text("Guardar Cambios", color = Color.White, fontSize = 18.sp)
    }

    state.successMessage?.let {
        Spacer(Modifier.height(12.dp))
        Text(it, color = Color.Green, fontSize = 14.sp)
    }
}
