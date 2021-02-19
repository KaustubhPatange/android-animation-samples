package com.kpstv.dampingrecyclerview

import android.animation.ValueAnimator
import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView

fun Activity.drawBehindSystemUI() {
    val flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

    if (Build.VERSION.SDK_INT >= 23) {
        window.decorView.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        window.decorView.systemUiVisibility = flags
    }
}

fun View.applyTopInsets() {
    setOnApplyWindowInsetsListener { view, insets ->
        view.updatePadding(top = insets.systemWindowInsetTop + view.paddingTop)
        insets
    }
}

fun Int.dp() = Resources.getSystem().displayMetrics.density * this

@ColorInt
fun Activity.getColorAttr(@AttrRes resId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}

fun View.animateColorChange(@ColorInt toColor: Int) : ValueAnimator {
    val before = (background as? ColorDrawable)?.color ?: Color.TRANSPARENT
    return ValueAnimator.ofArgb(before, toColor).apply {
        duration = 250
        addUpdateListener {
            setBackgroundColor(it.animatedValue as Int)
        }
        start()
    }
}

fun View.animateElevationChange(afterElevation: Float): ValueAnimator {
    return ValueAnimator.ofFloat(elevation, afterElevation).apply {
        duration = 250
        addUpdateListener {
            elevation = it.animatedValue as Float
        }
        start()
    }
}

fun RecyclerView.addOnScrollChanged(block: (dx: Int, dy: Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            block.invoke(dx, dy)
        }
    })
}