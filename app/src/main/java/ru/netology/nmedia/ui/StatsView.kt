package ru.netology.nmedia.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import ru.netology.nmedia.R
import ru.netology.nmedia.util.AndroidUtils
import kotlin.math.min
import kotlin.random.Random

class StatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var radius = 0F
    private var center = PointF(0F, 0F)
    private var oval = RectF(0F, 0F, 0F, 0F)

    private var lineWidth = AndroidUtils.dp(context, 5F).toFloat()
    private var fontSize = AndroidUtils.dp(context, 40F).toFloat()
    private var colors = emptyList<Int>()

    //Уменьшил перекрытие для естественного вида
    private var overlapAngle = 4F

    init {
        context.withStyledAttributes(attrs, R.styleable.StatsView) {
            lineWidth = getDimension(R.styleable.StatsView_lineWidth, lineWidth)
            fontSize = getDimension(R.styleable.StatsView_fontSize, fontSize)
            val resId = getResourceId(R.styleable.StatsView_colors, 0)
            colors = resources.getIntArray(resId).toList()
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = lineWidth
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = fontSize
    }

    var data: List<Float> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    var overlap: Float
        get() = overlapAngle
        set(value) {
            overlapAngle = value
            invalidate()
        }

    private val proportions: List<Float>
        get() {
            if (data.isEmpty()) return emptyList()

            val sum = data.sum()
            if (sum == 0F) return List(data.size) { 1F / data.size }

            return data.map { it / sum }
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2F - lineWidth / 2
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            center.x - radius, center.y - radius,
            center.x + radius, center.y + radius,
        )
    }

    override fun onDraw(canvas: Canvas) {
        if (data.isEmpty()) {
            return
        }

        val proportions = this.proportions
        var startFrom = -90F

        // 1. Рисуем все сегменты
        for (i in proportions.indices) {
            val angle = 360F * proportions[i]
            paint.color = colors.getOrNull(i) ?: randomColor()
            canvas.drawArc(oval, startFrom, angle, false, paint)
            startFrom += angle
        }

        // 2. Перекрытие: первый цвет заходит на последний
        if (proportions.size > 1) {
            val firstColor = colors.getOrNull(0) ?: randomColor()
            paint.color = firstColor
            canvas.drawArc(oval, -90F - overlapAngle, overlapAngle, false, paint)
        }

        canvas.drawText("100.00%", center.x, center.y + textPaint.textSize / 4, textPaint)
    }

    private fun randomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())
}