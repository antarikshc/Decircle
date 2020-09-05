package com.antarikshc.decirclebar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class DecircleSlider : View {

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    /**   Props   */

    // Center points of Views
    private var centerX = 0
    private var centerY = 0

    // Paints
    private var backdropPaint = Paint()

    fun init(context: Context, attrs: AttributeSet? = null) {
        backdropPaint = backdropPaint.apply {
            color = Color.GRAY
            style = Paint.Style.STROKE
            strokeWidth = 10F
        }
    }

    /**
     * Called every time View size changes
     * Initial call with old values 0
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        // Take min to keep content centered. Ignore the diff between W and H
        val min = min(w, h)

        // Get all sides of View
        val left = (w - min) / 2
        val top = (h - min) / 2
        val right = left + min
        val bottom = top + min

        // Get center points of View
        centerX = right / 2 + (w - right) / 2
        centerY = bottom / 2 + (h - bottom) / 2

        super.onSizeChanged(w, h, oldw, oldh)
    }


    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {

            drawCircle(centerX.toFloat(), centerY.toFloat(), 300F, backdropPaint)

        }
    }


}