package com.kebstudios.farmrunhelper.ui

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class NumberPicker : LinearLayout {

    private val text: TextView = TextView(context)
    private var value = 0

    private var min = 0
    private var max = 0

    private val incrementer = object : Runnable {
        override fun run() {
            increment()
            text.postDelayed(this, 100)
        }
    }

    private val decrementer = object : Runnable {
        override fun run() {
            decrement()
            text.postDelayed(this, 100)
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context,attributeSet, defStyleAttr)

    init {

        text.minEms = 4
        text.maxEms = 4
        text.text = "$value"
        text.gravity = Gravity.CENTER

        text.measure(0,0)

        val button = Button(context, null)
        button.text = "-"
        button.layoutParams = LinearLayout.LayoutParams((text.measuredHeight * 2.3).toInt(), (text.measuredHeight * 2.3).toInt())
        button.setOnTouchListener(TouchListener(decrementer, this::decrement))

        addView(button)

        addView(text)

        val button2 = Button(context, null)
        button2.text = "+"
        button2.layoutParams = LinearLayout.LayoutParams((text.measuredHeight * 2.3).toInt(), (text.measuredHeight * 2.3).toInt())
        button2.setOnTouchListener(TouchListener(incrementer, this::increment))
        addView(button2)

    }

    fun getValue(): Int {
        return value
    }

    fun setValue(value: Int) {
        this.value = Math.min(Math.max(value, min), max)
        text.text = "$value"
    }

    fun setMin(value: Int) {
        min = value
        setValue(Math.max(min, this.value))
    }

    fun setMax(value: Int) {
        max = value
        setValue(Math.min(max, this.value))
    }

    private fun increment() {
        value = Math.min(value + 1, max)
        text.text = "$value"
    }

    private fun decrement() {
        value = Math.max(value - 1, min)
        text.text = "$value"
    }

    private inner class TouchListener(private val runnable: Runnable,  val action: () -> Unit) : OnTouchListener {

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            if (event.action == MotionEvent.ACTION_DOWN) {
                action()
                v.parent.requestDisallowInterceptTouchEvent(true)
                postDelayed(runnable, 500)
            } else if (event.action == MotionEvent.ACTION_UP) {
                removeCallbacks(runnable)
            }

            return true
        }

    }

}