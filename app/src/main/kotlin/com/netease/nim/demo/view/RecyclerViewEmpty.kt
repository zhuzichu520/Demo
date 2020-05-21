package com.netease.nim.demo.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewEmpty : RecyclerView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    var emptyView: View? = null
        set(value) {
            if (field != null) {
                return
            }
            field = value
            (this@RecyclerViewEmpty.rootView as ViewGroup).addView(value)
            field?.visibility = View.GONE
        }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(adapterDataObserver)
        adapterDataObserver.onChanged()
    }

    private val adapterDataObserver by lazy {
        object : AdapterDataObserver() {
            override fun onChanged() {
                if (adapter?.itemCount ?: 0 == 0 && emptyView != null) {
                    emptyView?.visibility = View.VISIBLE
                    this@RecyclerViewEmpty.visibility = View.GONE
                } else {
                    emptyView?.visibility = View.GONE
                    this@RecyclerViewEmpty.visibility = View.VISIBLE
                }
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                onChanged()
            }
        }
    }
}