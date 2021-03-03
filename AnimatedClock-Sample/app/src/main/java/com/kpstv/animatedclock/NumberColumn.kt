package com.kpstv.animatedclock

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.withTranslation
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

class NumberColumn @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttr: Int = 0
) : View(context, attrs, defAttr) {

    private fun Int.dp() = this * resources.displayMetrics.density

    private val verticalOffsetDuration = 700L
    private val circleOffsetDuration = 500L
    private var maxDigit = 9
    private val digitHeight = 24.dp()
    private val bar = RectF()
    private val barPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#FF03DAC5")
        setShadowLayer(12f, 0f, 0f, Color.GRAY)
    }
    private val normalTextPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.DKGRAY
        textSize = 16.dp()
    }
    private val boldTextPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.DKGRAY
        textSize = 16.dp()
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val textBounds = Rect()

    private val circlePaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#d2e7d6")
    }

    private val verticalOffsetAnimator = ValueAnimator.ofFloat()
    private val circleOffsetAnimator = ValueAnimator.ofFloat()
    private val overshootAnimator = ValueAnimator.ofFloat()
    private var verticalOffset = 0f

    private val fixedTopHeight = digitHeight * 10
    private val circle = RectF()

    init {
        context.withStyledAttributes(attrs, R.styleable.NumberColumn, defAttr, 0) {
            maxDigit = getInt(R.styleable.NumberColumn_maxDigit, 9)
            barPaint.color = getColor(R.styleable.NumberColumn_columnColor, barPaint.color)
            normalTextPaint.color = getColor(R.styleable.NumberColumn_digitColor, normalTextPaint.color)
            boldTextPaint.color = getColor(R.styleable.NumberColumn_digitColor, boldTextPaint.color)
            circlePaint.color = getColor(R.styleable.NumberColumn_circleColor, circlePaint.color)
        }
    }

    private var lastDigit: Int = 0
    fun animateToDigit(digit: Int) {
        if (digit > maxDigit) throw IllegalStateException("digit cannot be greater than max digit")
        if (digit == lastDigit) return
        lastDigit = digit

        overshootAnimator.apply {
            setFloatValues(digitHeight, 0f)
            interpolator = OvershootInterpolator(1.3f)
            addUpdateListener {
                circle.top = -(it.animatedValue as Float)
                invalidate()
            }
        }

        verticalOffsetAnimator.cancel()
        verticalOffsetAnimator.apply {
            setFloatValues(verticalOffset, digitHeight * digit)
            interpolator = if (digit == 0) FastOutSlowInInterpolator() else OvershootInterpolator()
            duration = verticalOffsetDuration
            addUpdateListener {
                verticalOffset = it.animatedValue as Float
                invalidate()
            }
        }.start()

        if (digit == 0) return // Don't animate for 0
        circleOffsetAnimator.cancel()
        circleOffsetAnimator.apply {
            setFloatValues(verticalOffset, digitHeight * digit)
            interpolator = OvershootInterpolator()
            duration = circleOffsetDuration
            addUpdateListener {
                val clampFraction = it.animatedFraction.coerceAtMost(1f)
                val topOffset = digitHeight * clampFraction
                circle.top = -topOffset
            }
            doOnEnd {
                overshootAnimator.cancel()
                overshootAnimator.start()
            }
        }.start()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val middlePoint = width / 2 - digitHeight / 2
        bar.set(middlePoint, 0f, digitHeight + middlePoint, digitHeight * (maxDigit + 1))
        circle.set(middlePoint, 0f, digitHeight + middlePoint, digitHeight)
        normalTextPaint.getTextBounds(maxDigit.toString(), 0, maxDigit.toString().length, textBounds)
    }

    override fun draw(canvas: Canvas?) {
        if (canvas == null) return
        super.draw(canvas)
        canvas.withTranslation(y = fixedTopHeight - verticalOffset) {
            canvas.drawRoundRect(bar, digitHeight / 2, digitHeight / 2, barPaint)
        }
        canvas.withTranslation(y = fixedTopHeight) {
            canvas.drawRoundRect(circle, digitHeight / 2f, digitHeight / 2f, circlePaint)
        }
        canvas.withTranslation(y = fixedTopHeight - verticalOffset) {
            for(i in 0..maxDigit) {
                canvas.drawText((i).toString(), bar.left + digitHeight / 2f - textBounds.width() / 2f - 3f, (digitHeight * (i + 1)) - 17f,
                if (lastDigit % (maxDigit + 1) == i)
                    boldTextPaint
                else
                    normalTextPaint
                ) // TODO: Fix this hack
            }
        }
    }
}