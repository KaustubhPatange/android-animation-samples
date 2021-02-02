package com.kpstv.dampingrecyclerview

import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

// Created by Kaustubh Patange at 02-02-2021

/**
 * Usage: ```SpringScrollHelper().attachToRecyclerView(recyclerView)```
 */
class SpringScrollHelper {
    /**
     * Adds a snappy spring effect to recyclerView.
     *
     * Passing null will remove it.
     */
    fun attachToRecyclerView(recyclerView: RecyclerView?) {
        if (recyclerView != null) {
            this.recyclerView = recyclerView
            setup()
        } else {
            remove()
        }
    }

    private var recyclerView: RecyclerView? = null
    private var flingVx: Int = 0
    private var flingVy: Int = 0

    private fun setup() {
        recyclerView?.onFlingListener = recyclerViewFlingListener
        recyclerView?.addOnScrollListener(recyclerViewScrollListener)
    }

    private fun remove() {
        recyclerView?.onFlingListener = null
        recyclerView?.removeOnScrollListener(recyclerViewScrollListener)
        recyclerView = null
    }

    private val recyclerViewFlingListener: RecyclerView.OnFlingListener = object: RecyclerView.OnFlingListener() {
        override fun onFling(velocityX: Int, velocityY: Int): Boolean {
            flingVx = velocityX
            flingVy = velocityY
            return false
        }
    }

    private val recyclerViewScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                var velocity = 1f
                var currentOffset = 0
                var totalOffset = 0
                var property: DynamicAnimation.ViewProperty = DynamicAnimation.SCALE_Y
                if (flingVy != 0) {
                    currentOffset = recyclerView.computeVerticalScrollOffset()
                    totalOffset = recyclerView.computeVerticalScrollRange() - recyclerView.measuredHeight
                    velocity = (abs(flingVy) /recyclerView.measuredHeight).toFloat()
                    property = DynamicAnimation.SCALE_Y
                    if (flingVy > 0) {
                        recyclerView.pivotY = recyclerView.measuredHeight.toFloat()
                    } else {
                        recyclerView.pivotY = 0f
                    }
                } else if (flingVx != 0) {
                    currentOffset = recyclerView.computeHorizontalScrollOffset()
                    totalOffset = recyclerView.computeHorizontalScrollRange() - recyclerView.measuredWidth
                    velocity = (abs(flingVx) /recyclerView.measuredWidth).toFloat()
                    property = DynamicAnimation.SCALE_X
                    if (flingVx > 0) {
                        recyclerView.pivotX = recyclerView.measuredWidth.toFloat()
                    } else {
                        recyclerView.pivotX = 0f
                    }
                }
                if (currentOffset == totalOffset || currentOffset == 0) {
                    val anim = SpringAnimation(recyclerView, property, 1f)
                    // You can set other animation parameters like stiffness or damping ratio, I'm leaving it default.
                    anim.setStartVelocity(velocity.coerceAtMost(2f)).start()
                }
            }
        }
    }
}