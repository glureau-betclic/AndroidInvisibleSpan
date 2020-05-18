package com.glureau.invisiblespantesting

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Spannable
import android.text.style.ReplacementSpan
import android.text.style.UpdateAppearance


enum class SpannableMatchingMode {
    FIRST, // Matches the first occurence only
    LAST, // Matches the last occurrence only
    ALL // Matches all the occurrences
}

fun Spannable.applySpanOn(
    target: String,
    spanFactory: () -> UpdateAppearance,
    matchingMode: SpannableMatchingMode = SpannableMatchingMode.ALL
) = apply {
    val indexes = this.indexesOf(target, matchingMode)
    indexes.forEach { startIndex ->
        val endIndex = startIndex + target.length
        setSpan(spanFactory(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

fun Spannable.applySpanBetweenMarkersXml(
    marker: String,
    spanFactory: () -> UpdateAppearance,
    matchingMode: SpannableMatchingMode = SpannableMatchingMode.ALL,
    hideMarkers: Boolean = true
) = applySpanBetweenMarkers("<$marker>", "</$marker>", spanFactory, matchingMode, hideMarkers)

fun Spannable.applySpanBetweenMarkers(
    startMarker: String,
    endMarker: String,
    spanFactory: () -> UpdateAppearance,
    matchingMode: SpannableMatchingMode = SpannableMatchingMode.ALL,
    hideMarkers: Boolean = true
) = apply {
    val startIndexes = this.indexesOf(startMarker, matchingMode)
    val endIndexes = this.indexesOf(endMarker, matchingMode)

    // Build list of all pair start/end
    val finalIndexes: List<Pair<Int, Int>> = startIndexes
        .mapNotNull { startIndex ->
            endIndexes.firstOrNull { it > startIndex }?.let { endIndex -> startIndex to endIndex }
        }
    finalIndexes.forEach { (startIndex, endIndex) ->
        // If a couple start/end exist, apply the span and then remove the range
        setSpan(
            spanFactory(),
            startIndex + startMarker.length,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        if (hideMarkers) {
            setSpan(
                InvisibleSpan(),
                startIndex,
                startIndex + startMarker.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                InvisibleSpan(),
                endIndex,
                endIndex + endMarker.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}

fun CharSequence.indexesOf(token: String, matchingMode: SpannableMatchingMode): List<Int> =
    when (matchingMode) {
        SpannableMatchingMode.FIRST -> {
            val indexOf = toString().indexOf(token, ignoreCase = true)
            if (indexOf >= 0) listOf(indexOf) else emptyList()
        }
        SpannableMatchingMode.LAST -> {
            val indexOf = toString().lastIndexOf(token, ignoreCase = true)
            if (indexOf >= 0) listOf(indexOf) else emptyList()
        }
        SpannableMatchingMode.ALL -> allIndexesOf(token)
    }

private fun CharSequence.allIndexesOf(token: String): List<Int> {
    var index = -1
    val indexes = mutableListOf<Int>()
    while (true) {
        index = toString().indexOf(token, index + 1, ignoreCase = true)
        if (index >= 0) {
            indexes.add(index)
        } else {
            return indexes
        }
    }
}