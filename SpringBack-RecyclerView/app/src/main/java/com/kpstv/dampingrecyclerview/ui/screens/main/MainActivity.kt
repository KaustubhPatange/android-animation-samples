package com.kpstv.dampingrecyclerview.ui.screens.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kpstv.dampingrecyclerview.*
import com.kpstv.dampingrecyclerview.ui.adapters.AnimalsAdapter
import com.kpstv.dampingrecyclerview.ui.helpers.SpringScrollHelper
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recyclerView) }
    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        drawBehindSystemUI()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.applyTopInsets()

        recyclerView.apply {
            applyTopInsets()
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = AnimalsAdapter()
            addOnScrollChanged { _, _ ->
                viewModel.setElevatedToolbar(recyclerView.computeVerticalScrollOffset() > 0)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.showElevatedToolbar.collect { enabled ->
                val surfaceColor = getColorAttr(R.attr.colorSurface)
                val toolbarColor: Int = if (enabled) {
                    ColorUtils.setAlphaComponent(surfaceColor, 217)
                } else {
                    surfaceColor
                }

                toolbar.animateElevationChange(if (enabled) 3.dp() else 0f)
                toolbar.animateColorChange(toolbarColor)
            }
        }

        SpringScrollHelper().attachToRecyclerView(recyclerView)
    }
}