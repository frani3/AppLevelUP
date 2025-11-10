package com.applevelup.levepupgamerapp.presentation.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.applevelup.levepupgamerapp.utils.RunUtils

class RunVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val components = RunUtils.parseInput(text.text)
        val body = components.body
        val checkDigit = components.checkDigit
        val formattedBody = RunUtils.formatNumberPart(body)
        val showHyphen = (components.hasHyphen || checkDigit.isNotEmpty()) && formattedBody.isNotEmpty()

        if (formattedBody.isEmpty() && checkDigit.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val display = buildString {
            append(formattedBody)
            if (showHyphen) append('-')
            if (checkDigit.isNotEmpty()) append(checkDigit.uppercase())
        }

        val bodyLength = body.length
        val rawHyphenCount = if (components.hasHyphen || checkDigit.isNotEmpty()) 1 else 0
        val hasCheckDigit = checkDigit.isNotEmpty()
        val rawLength = bodyLength + rawHyphenCount + if (hasCheckDigit) 1 else 0
        val displayDigitsLength = formattedBody.length
        val displayHyphenLength = if (showHyphen) 1 else 0
        val displayLength = display.length

        val bodyOriginalToTransformed = IntArray(bodyLength + 1)
        var transformedIndex = 0
        for (i in 0 until bodyLength) {
            bodyOriginalToTransformed[i] = transformedIndex
            transformedIndex += 1
            val remaining = bodyLength - i - 1
            if (remaining > 0 && remaining % 3 == 0) {
                transformedIndex += 1
            }
        }
        if (bodyLength >= 0) {
            bodyOriginalToTransformed[bodyLength] = transformedIndex
        }

        val displayToBodyOriginal = IntArray(displayDigitsLength + 1)
        transformedIndex = 0
        for (i in 0 until bodyLength) {
            displayToBodyOriginal[transformedIndex] = i
            transformedIndex += 1
            val remaining = bodyLength - i - 1
            if (remaining > 0 && remaining % 3 == 0) {
                displayToBodyOriginal[transformedIndex] = i + 1
                transformedIndex += 1
            }
        }
        if (displayDigitsLength >= 0) {
            displayToBodyOriginal[displayDigitsLength] = bodyLength
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (rawLength == 0) return 0
                val clamped = offset.coerceIn(0, rawLength)
                return when {
                    clamped <= bodyLength -> bodyOriginalToTransformed[clamped]
                    rawHyphenCount == 0 -> displayDigitsLength
                    clamped <= bodyLength + rawHyphenCount -> displayDigitsLength + if (showHyphen) clamped - bodyLength else 0
                    hasCheckDigit && clamped > bodyLength + rawHyphenCount -> displayDigitsLength + displayHyphenLength + 1
                    else -> displayDigitsLength + displayHyphenLength
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (displayLength == 0) return 0
                val clamped = offset.coerceIn(0, displayLength)
                return when {
                    clamped <= displayDigitsLength -> displayToBodyOriginal[clamped]
                    !showHyphen -> bodyLength
                    clamped <= displayDigitsLength + displayHyphenLength -> bodyLength + (clamped - displayDigitsLength)
                    hasCheckDigit && clamped > displayDigitsLength + displayHyphenLength -> bodyLength + rawHyphenCount + 1
                    else -> bodyLength + rawHyphenCount
                }
            }
        }

        return TransformedText(AnnotatedString(display), offsetMapping)
    }
}
