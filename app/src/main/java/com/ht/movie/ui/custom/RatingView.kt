package com.ht.movie.ui.custom

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.ht117.app.R
import kotlin.math.roundToInt

class RatingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var score: Float = 0F
    private var angle: Float = 0F
        set(value) {
            field = value
            invalidate()
        }

    var maxSize: Float = Math.min(width, height).toFloat()

    private var animator = ObjectAnimator.ofFloat(this, "angle", 0F, 0F).apply {
        duration = DURATION
    }

    private val basePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.resources.getColor(R.color.base_rating)
        style = Paint.Style.FILL
    }

    private val upVoteBasePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.resources.getColor(R.color.up_vote_base)
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = DEF_TXT_SIZE
    }

    private val upVoteMainPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.resources.getColor(R.color.up_vote_main)
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = DEF_TXT_SIZE
    }

    private val downVoteBasePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.resources.getColor(R.color.down_vote_base)
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = DEF_TXT_SIZE
    }

    private val downVoteMainPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.resources.getColor(R.color.down_vote_main)
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = DEF_TXT_SIZE
    }

    private val textPaint = TextPaint().apply {
        textAlign = Paint.Align.CENTER
        textSize = DEF_TXT_SIZE
        color = Color.WHITE
    }

    private val percentPaint = TextPaint().apply {
        textAlign = Paint.Align.CENTER
        color = Color.WHITE
    }

    fun setScore(value: Float, isAnimate: Boolean) {
        if (value < 0F || value > 10F)
            return
        score = value
        if (isAnimate) {
            animator.setFloatValues(0F, (value / 10F) * 360)
            animator.start()
        } else {
            angle = value / 10F * 360
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            drawArc(basePaint.strokeWidth , basePaint.strokeWidth, maxSize - basePaint.strokeWidth * 2, maxSize - basePaint.strokeWidth * 2, 0F, 360F, false, basePaint)

            val rate = maxSize / 8
            if (score < 5) {
                drawArc(rate, rate, maxSize - rate, maxSize - rate, 0F, CIRCLE, false, downVoteBasePaint)
                drawArc(rate, rate, maxSize - rate, maxSize - rate, -90F, angle, false, downVoteMainPaint)
            } else {
                drawArc(rate, rate, maxSize - rate, maxSize - rate, 0F, CIRCLE, false, upVoteBasePaint)
                drawArc(rate, rate, maxSize - rate, maxSize - rate, -90F, angle, false, upVoteMainPaint)
            }

            val text = (angle / CIRCLE * 100).roundToInt().toString()
            val bound = Rect()
            textPaint.getTextBounds("1", 0, 1, bound)
            val textWidth = textPaint.measureText("99")

            drawText(text, maxSize / 2, (maxSize + bound.height()) / 2, textPaint)
            drawText("%",maxSize / 2 + textWidth / 2 + 8F, maxSize / 2 - bound.height() / 2, percentPaint )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        maxSize = Math.min(w, h).toFloat()
        val rate = maxSize / 8
        val maxBound = Rect(0, 0, (maxSize - rate).roundToInt(), (maxSize - rate).roundToInt())
        var curWidth = textPaint.measureText("99 %")
        while (curWidth < (maxBound.width() - rate * 3)) {
            textPaint.textSize += 1
            curWidth = textPaint.measureText("99 %")
        }
        percentPaint.textSize = textPaint.textSize / 2
    }

    companion object {
        const val DEF_TXT_SIZE = 12F
        const val DURATION = 1500L
        const val CIRCLE = 360F
    }
}