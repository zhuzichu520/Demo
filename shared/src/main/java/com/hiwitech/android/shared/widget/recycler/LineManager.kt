package com.hiwitech.android.shared.widget.recycler

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class LineManager {

    interface Factory {
        fun create(recyclerView: RecyclerView): ItemDecoration
    }

    companion object {

        @JvmStatic
        fun both(dividerSize: Float): Factory {
            return object : Factory {
                override fun create(recyclerView: RecyclerView): ItemDecoration {
                    return DividerLine(
                        recyclerView.context,
                        dividerSize,
                        DividerLine.LineDrawMode.BOTH
                    )
                }
            }
        }

        @JvmStatic
        fun horizontal(dividerSize: Float): Factory {
            return object : Factory {
                override fun create(recyclerView: RecyclerView): ItemDecoration {
                    return DividerLine(
                        recyclerView.context,
                        dividerSize,
                        DividerLine.LineDrawMode.HORIZONTAL
                    )
                }
            }
        }

        @JvmStatic
        fun vertical(dividerSize: Float): Factory {
            return object : Factory {
                override fun create(recyclerView: RecyclerView): ItemDecoration {
                    return DividerLine(
                        recyclerView.context,
                        dividerSize,
                        DividerLine.LineDrawMode.VERTICAL
                    )
                }
            }
        }

        @JvmStatic
        fun gridSpacing(spanCount: Int, spacing: Float): Factory {
            return object : Factory {
                override fun create(recyclerView: RecyclerView): ItemDecoration {
                    return GridSpacingDecoration(
                        spanCount,
                        spacing,
                        false
                    )
                }
            }
        }

    }
}