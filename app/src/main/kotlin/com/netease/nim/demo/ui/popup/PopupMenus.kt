package com.netease.nim.demo.ui.popup

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.recyclical.ViewHolder
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.hiwitech.android.shared.databinding.recycler.setLineManager
import com.hiwitech.android.shared.widget.decoration.SuperOffsetDecoration
import com.netease.nim.demo.R
import com.netease.nim.demo.base.PopupWindowBase


/**
 * desc
 * author: 朱子楚
 * time: 2020/5/28 3:50 PM
 * since: v 1.0.0
 */
class PopupMenus(
    context: Context,
    val list: List<ItemMenu>,
    val onClickMenu: ItemMenu.() -> Unit
) : PopupWindowBase(context) {

    private lateinit var recycler: RecyclerView

    override fun setLayoutId(): Int = R.layout.popup_menus

    override fun onCreateView(view: View): View {
        width = WRAP_CONTENT
        height = WRAP_CONTENT
        popupGravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        setBackgroundColor(android.R.color.transparent)
        return super.onCreateView(view)
    }

    override fun onViewCreated(contentView: View) {
        super.onViewCreated(contentView)
        recycler = findViewById(R.id.recycler)
        setLineManager(recycler, null, null, null, SuperOffsetDecoration.SHOW_DIVIDER_MIDDLE)
    }

    override fun initView() {
        recycler.setup {
            withDataSource(
                dataSourceTypedOf(list)
            )
            withItem<ItemMenu, MenuViewHolder>(R.layout.item_popup_menu) {
                onBind(::MenuViewHolder) { _, item ->
                    title.setText(item.titleId)
                }
                onClick {
                    onClickMenu.invoke(this.item)
                    dismiss()
                }
            }
        }


    }

    data class ItemMenu(
        val type: Int,
        @StringRes val titleId: Int
    )

    class MenuViewHolder(itemView: View) : ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
    }

    override fun onCreateShowAnimation(): Animation? {
        return getTranslateVerticalAnimation(1f, 0f, 300)
    }


    override fun onCreateDismissAnimation(): Animation? {
        return getTranslateVerticalAnimation(0f, 1f, 300)
    }

}