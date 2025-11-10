package com.applevelup.levepupgamerapp.domain.exceptions

class EmailAlreadyInUseException(email: String) : IllegalStateException("El correo $email ya está registrado")
