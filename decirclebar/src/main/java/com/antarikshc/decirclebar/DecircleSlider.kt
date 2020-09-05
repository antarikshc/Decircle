package com.antarikshc.decirclebar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import kotlin.math.*

class DecircleSlider : View {

    companion object {
        private val TAG = DecircleSlider::class.java.simpleName
        private const val MIN_PROGRESS = 0
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

    /**
     * Props
     */

    // Progress
    private var progress = 0
    private var progressInDegree = progress / DEGREE_CONVERSION


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
    private var isPickerSelected = false


    // Arc props
    private var arcWidth = 20F
    private val arcRect = RectF()
    private var arcPaint = Paint()


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

        arcPaint = arcPaint.apply {
            color = Color.GREEN
            style = Paint.Style.STROKE
            strokeWidth = arcWidth
        }
    }


    /**
     * Draw & Size changes
     */

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

        // Set bounds for Arc
        val leftBound = w / 2F - (diameter / 2F)
        val topBound = h / 2F - (diameter / 2F)
        arcRect.set(leftBound, topBound, leftBound + diameter, topBound + diameter)

        super.onSizeChanged(w, h, oldw, oldh)
    }


    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {

            // Draw backdrop
            drawCircle(centerX.toFloat(), centerY.toFloat(), diameter / 2F, backdropPaint)

            // Draw Progress Arc
            drawArc(arcRect, -90F, progressInDegree, false, arcPaint)

            // Draw Picker
            drawCircle(pickerX.toFloat(), pickerY.toFloat(), pickerWidth / 2F, pickerPaint)

        }
    }

    fun setProgress(progress: Int) {
        val currentProgress = this.progress
        val nextProgress = progress

        // Animate from current progress to specified progress
        ValueAnimator.ofInt(currentProgress, nextProgress).apply {
            duration = abs(nextProgress - currentProgress) * 10L
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                updateProgress(it.animatedValue as Int)
            }
            start()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x.toInt()
                val y = event.y.toInt()

                // Detect whether user has selected picker
                if (
                    x < pickerX + pickerWidth && x > pickerX - pickerWidth &&
                    y < pickerY + pickerWidth && y > pickerY - pickerWidth
                ) {
                    isPickerSelected = true

                    updateProgress(x, y)

                    // Prevent any parent touch events Eg: RecyclerView, ScrollView
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isPickerSelected) {
                    val x = event.x.toInt()
                    val y = event.y.toInt()
                    updateProgress(x, y)
                }
            }

            MotionEvent.ACTION_UP -> {
                isPickerSelected = false
                // Allow parent touch events Eg: RecyclerView, ScrollView
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return true
    }

    private fun updateProgress(progress: Int) {
        if (progress < MIN_PROGRESS || progress > MAX_PROGRESS) return

        this.progress = progress
        progressInDegree = progress / DEGREE_CONVERSION

        // Calculate angle
        angle = PI / 2 - (progressInDegree * PI) / 180

        // Calc picker distance
        pickerX = centerX + ((diameter / 2F) * cos(angle)).toInt()
        pickerY = centerY - ((diameter / 2F) * sin(angle)).toInt()

        invalidate()
    }

    private fun updateProgress(x: Int, y: Int) {
        val distanceX = x - centerX
        val distanceY = centerY - y

        val distanceXY = sqrt(distanceX.toDouble().pow(2.0) + distanceY.toDouble().pow(2.0))

        // Reverse
        angle = acos(distanceX / distanceXY)
        if (distanceY < 0) angle = -angle

        // Calc picker distance
        pickerX = centerX + ((diameter / 2F) * cos(angle)).toInt()
        pickerY = centerY - ((diameter / 2F) * sin(angle)).toInt()

        progressInDegree = (90 - (angle * 180) / PI).toFloat()
        // Check bounds
        if (progressInDegree < 0) progressInDegree += 360

        progress = (progressInDegree * DEGREE_CONVERSION).toInt()

        invalidate()
    }
}