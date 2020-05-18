package com.glureau.invisiblespantesting

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan


/**
 * Used to "hide" a part of a Spannable, for example when applying custom tags like <red>.
 * This way, we don't update the underlying CharSequence (immutable) and can have a fluent API.
 */
class InvisibleSpan : ReplacementSpan() {

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ) = 0

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) = Unit
}
