package com.example.currentweatherdatabinding.citiesRecycler

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class ScrollPagerListener(private val pagerHelper: PagerSnapHelper, private val pagerItemChangeListener: (newPosition: Int) -> Unit) : RecyclerView.OnScrollListener() {
    private var previousPagerPosition = -1

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        // состояние равно 0, когда юзер закончил скролл, и ячейка вернулась на свое положение
        if (newState == 0) {
            val currentPagerPosition = getPagerPosition(recyclerView, pagerHelper)
            if (previousPagerPosition != currentPagerPosition) {
                pagerItemChangeListener(currentPagerPosition)
                previousPagerPosition = currentPagerPosition
            }
        }
    }

    private fun getPagerPosition(recyclerView: RecyclerView, pagerHelper: PagerSnapHelper): Int {
        val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
        val pagerView = pagerHelper.findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
        return layoutManager.getPosition(pagerView)
    }
}