package com.antarikshc.decirclebar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.*

class DecircleSlider : View {

    companion object {
        private const val MAX_PROGRESS = 100
        private const val DEGREE_CONVERSION = MAX_PROGRESS / 360F
    }

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

    private var progress = 0
    private val progressInDegree = progress / DEGREE_CONVERSION


    // Center points of Views
    private var centerX = 0
    private var centerY = 0

    private var diameter = 0


    // Angles
    // we define picker and slider position by angle
    private var initAngle = PI / 2 - (progressInDegree * PI) / 180

    // Progress angle
    private var angle = initAngle


    // Backdrop props
    private var backdropWidth = 10F
    private var backdropPaint = Paint()


    // Picker props
    private var pickerX = 0
    private var pickerY = 0
    private var pickerWidth = 50F
    private var pickerPaint = Paint()

    private var maxWidth = max(backdropWidth, pickerWidth)


    fun init(context: Context, attrs: AttributeSet? = null) {
        backdropPaint = backdropPaint.apply {
            color = Color.GRAY
            style = Paint.Style.STROKE
            strokeWidth = backdropWidth
        }

        pickerPaint = pickerPaint.apply {
            color = Color.RED
            style = Paint.Style.FILL
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

        // Total height or width is diameter
        // Minus the max stroke/picker width
        diameter = min - maxWidth.toInt()

        // Calc picker distance
        pickerX = centerX + ((diameter / 2F) * cos(angle)).toInt()
        pickerY = centerY - ((diameter / 2F) * sin(angle)).toInt()

        super.onSizeChanged(w, h, oldw, oldh)
    }


    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {

            // Draw backdrop
            drawCircle(centerX.toFloat(), centerY.toFloat(), diameter / 2F, backdropPaint)

            // Draw Picker
            drawCircle(pickerX.toFloat(), pickerY.toFloat(), pickerWidth / 2F, pickerPaint)

        }
    }

}