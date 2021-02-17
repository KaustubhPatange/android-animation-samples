package com.kpstv.fabreveal

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.addListener
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FabReveal(
    private val fab: FloatingActionButton,
    private val toContainer: View
) {
    private lateinit var translation: ObjectAnimator
    private lateinit var alphaFab: ObjectAnimator
    private lateinit var alphaContainer: ObjectAnimator
    private lateinit var scaleFab: ObjectAnimator

    fun start(duration: Long = 200, transaction: () -> Unit = {}, onEnd: () -> Unit = {}) {
        val parent = fab.parent as ViewGroup
        val cx = fab.marginEnd + fab.width / 2f - parent.width / 2f
        val cy = fab.marginBottom + fab.height / 2f - parent.height / 2f

        translation = ObjectAnimator.ofPropertyValuesHolder(
            fab,
            PropertyValuesHolder.ofFloat(View.TRANSLATION_X, cx),
            PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, cy)
        )
        alphaFab = ObjectAnimator.ofFloat(fab, View.ALPHA, 0f)
        alphaContainer = ObjectAnimator.ofFloat(toContainer, View.ALPHA, 0f, 1f)
        scaleFab = ObjectAnimator.ofPropertyValuesHolder(
            fab,
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 10f),
            PropertyValuesHolder.ofFloat(View.SCALE_X, 10f)
        )

        val first = AnimatorSet().apply {
            setDuration(duration)
            interpolator = FastOutSlowInInterpolator()
            play(translation)

            addListener(onEnd = {
                transaction()
            })
        }

        val second = AnimatorSet().apply {
            setDuration(duration)
            playTogether(alphaFab, scaleFab, alphaContainer)
        }

        AnimatorSet().apply {
            playSequentially(first, second)
            addListener(onEnd = {
                fab.visibility = View.GONE
                onEnd()
            })
        }.start()
    }

    fun reverse(duration: Long = 150, onEnd: () -> Unit = {}) {
        reverseInternal(duration = duration, onEnd = onEnd)
    }

    /**
     * Resets Fab & Container view position back to normal. Useful if you want to show
     * Fab in another fragment as well.
     */
    fun reset() {
        reverseInternal(true)
    }

    private fun reverseInternal(silent: Boolean = false, duration: Long = 150, onEnd: () -> Unit = {}) {
        if (!silent) {
            fab.visibility = View.VISIBLE
        } else {
            fab.visibility = View.GONE
        }

        translation.addListener(onEnd = {
            onEnd()
            translation.removeAllListeners()
        })
        scaleFab.addListener(onEnd = {
            scaleFab.removeAllListeners()
            translation.reverse()
        })

        scaleFab.setDuration(duration).reverse()
        alphaFab.setDuration(duration).reverse()
        alphaContainer.setDuration(duration).start()
    }
}