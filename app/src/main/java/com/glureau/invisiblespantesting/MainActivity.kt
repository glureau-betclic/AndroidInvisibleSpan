package com.glureau.invisiblespantesting

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var originalText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        originalText = textView.text as String
        val originalTextLength = originalText.length

        start_seekbar.max = originalTextLength - 1
        start_seekbar.progress = 0
        start_seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val end = end_seekbar.progress
                if (end <= progress) {
                    end_seekbar.progress = progress + 1
                }
                updateText()
            }
        })

        end_seekbar.max = originalTextLength
        end_seekbar.progress = 30
        end_seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val start = start_seekbar.progress
                if (progress <= start) {
                    start_seekbar.progress = progress - 1
                }
                updateText()
            }
        })

        end_seekbar.progress = 26
        start_seekbar.progress = 22

        toggle_invisible_span.setOnCheckedChangeListener { _, _ ->
            updateText()
        }

        width_seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeAdapter() {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val factor = progress.toFloat() / seekBar.max.toFloat()
                guideline.setGuidelinePercent(0.5f + factor * 0.5f)
            }
        })
    }

    private fun updateText() {
        val start = start_seekbar.progress
        val end = end_seekbar.progress
        val useInvisibleSpan = toggle_invisible_span.isChecked
        if (useInvisibleSpan) {
            textView.text = SpannableString.valueOf(
                originalText.substring(0, start) +
                        "<b>" +
                        originalText.substring(start, end) +
                        "</b>" +
                        originalText.substring(end)
            )
                .applySpanBetweenMarkersXml("b", { StyleSpan(Typeface.BOLD_ITALIC) })
        } else {
            textView.text = SpannableString.valueOf(originalText)
                .applySpanOn(
                    originalText.substring(start, end),
                    { StyleSpan(Typeface.BOLD_ITALIC) })
        }

        selection.text = "<b>" + originalText.substring(start, end) + "</b>"
    }
}

open class OnSeekBarChangeAdapter : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
    }

}