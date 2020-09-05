package com.antarikshc.decirclebar

import android.content.Context
import android.util.AttributeSet
import android.view.View

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


    fun init(context: Context, attrs: AttributeSet? = null) {
    }

}