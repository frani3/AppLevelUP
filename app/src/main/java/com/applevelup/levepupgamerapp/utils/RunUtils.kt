package com.applevelup.levepupgamerapp.utils

data class RunComponents(
    val body: String,
    val checkDigit: String,
    val hasHyphen: Boolean
)

object RunUtils {
    const val RUN_BODY_MIN_LENGTH = 7
    const val RUN_BODY_MAX_LENGTH = 8

    fun parseInput(raw: String): RunComponents {
        if (raw.isEmpty()) {
            return RunComponents(body = "", checkDigit = "", hasHyphen = false)
        }

        val upper = raw.uppercase()
        val explicitHyphen = upper.contains('-')

        val bodyBuilder = StringBuilder()
        var checkDigit: Char? = null
        var seenHyphen = false

        for (char in upper) {
            when {
                char == '-' -> {
                    seenHyphen = true
                }

                char.isDigit() -> {
                    if (!seenHyphen) {
                        if (bodyBuilder.length < RUN_BODY_MAX_LENGTH) {
                            bodyBuilder.append(char)
                        } else if (checkDigit == null) {
                            checkDigit = char
                        }
                    } else if (checkDigit == null) {
                        checkDigit = char
                    }
                }

                char == 'K' -> {
                    if (!seenHyphen) {
                        if (bodyBuilder.length >= RUN_BODY_MIN_LENGTH && checkDigit == null) {
                            checkDigit = 'K'
                        }
                    } else if (checkDigit == null) {
                        checkDigit = 'K'
                    }
                }
            }
        }

        val body = bodyBuilder.toString()
        val normalizedCheck = checkDigit?.let { ch ->
            if (isCheckDigitValid(ch)) ch.uppercaseChar().toString() else null
        } ?: ""

        val showHyphen = explicitHyphen || normalizedCheck.isNotEmpty()

        return RunComponents(
            body = body,
            checkDigit = normalizedCheck,
            hasHyphen = showHyphen && body.isNotEmpty()
        )
    }

    fun composeRawInput(body: String, checkDigit: String, hasHyphen: Boolean): String {
        if (body.isEmpty()) return ""
        val normalizedCheck = checkDigit.uppercase()
        val showHyphen = hasHyphen || normalizedCheck.isNotEmpty()
        return buildString {
            append(body)
            if (showHyphen) append('-')
            if (normalizedCheck.isNotEmpty()) append(normalizedCheck)
        }
    }

    fun formatNumberPart(digits: String): String {
        if (digits.isEmpty()) return ""
        val builder = StringBuilder()
        digits.forEachIndexed { index, char ->
            builder.append(char)
            val remaining = digits.length - index - 1
            if (remaining > 0 && remaining % 3 == 0) {
                builder.append('.')
            }
        }
        return builder.toString()
    }

    fun formatForDisplay(body: String, checkDigit: String, hasHyphen: Boolean): String {
        if (body.isEmpty()) return ""
        val formattedBody = formatNumberPart(body)
        val normalizedCheck = checkDigit.uppercase()
        val showHyphen = hasHyphen || normalizedCheck.isNotEmpty()
        return buildString {
            append(formattedBody)
            if (showHyphen) append('-')
            if (normalizedCheck.isNotEmpty()) append(normalizedCheck)
        }
    }

    fun calculateCheckDigit(numberPart: String): String {
        if (numberPart.isBlank()) return ""
        var multiplier = 2
        var sum = 0
        for (digitChar in numberPart.reversed()) {
            val digit = digitChar.digitToIntOrNull() ?: return ""
            sum += digit * multiplier
            multiplier = if (multiplier == 7) 2 else multiplier + 1
        }
        val remainder = 11 - (sum % 11)
        return when (remainder) {
            11 -> "0"
            10 -> "K"
            else -> remainder.toString()
        }
    }

    fun buildFullRun(body: String, checkDigit: String): String? {
        val normalizedBody = body.filter(Char::isDigit)
        val normalizedCheck = checkDigit.uppercase().takeIf(::isCheckDigitValid) ?: return null
        if (!isBodyLengthValid(normalizedBody)) return null
        return "$normalizedBody-$normalizedCheck"
    }

    fun isBodyLengthValid(body: String): Boolean {
        return body.length in RUN_BODY_MIN_LENGTH..RUN_BODY_MAX_LENGTH
    }

    private fun isCheckDigitValid(value: Char): Boolean {
        return value.isDigit() || value.equals('K', ignoreCase = true)
    }

    fun isCheckDigitValid(value: String): Boolean {
        return value.length == 1 && isCheckDigitValid(value[0])
    }
}
