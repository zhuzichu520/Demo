package com.hiwitech.android.shared.databinding.button

import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.button.MaterialButtonToggleGroup

@BindingAdapter("checkedButton")
fun checkedButton(toggleGroup: MaterialButtonToggleGroup, id: Int?) {
    id?.let {
        toggleGroup.check(id)
    }
}

@InverseBindingAdapter(attribute = "checkedButton", event = "onCheckedButtonListener")
fun getCheckedButton(toggleGroup: MaterialButtonToggleGroup): Int {
    return toggleGroup.checkedButtonId
}

@BindingAdapter("onCheckedButtonListener")
fun setOnCheckedButtonListener(
    toggleGroup: MaterialButtonToggleGroup,
    bindingListener: InverseBindingListener?
) {
    bindingListener?.let {
        toggleGroup.addOnButtonCheckedListener { _, _, isChecked ->
            if (isChecked)
                bindingListener.onChange()
        }
    }
}

@BindingAdapter(value = ["radioIndex"], requireAll = false)
fun bindRadioGroup(radioGroup: RadioGroup, index: Int?) {
    index?.let { it ->
        val list = radioGroup.children.filter { view ->
            view is RadioButton
        }.toMutableList()
        (list[it] as RadioButton).isChecked = true
    }
}

@InverseBindingAdapter(attribute = "radioIndex", event = "onRadioButtonListener")
fun getRadioIndex(radioGroup: RadioGroup): Int {
    var index = 0
    val list = radioGroup.children.filter { view ->
        view is RadioButton
    }.toMutableList()
    list.forEachIndexed { i, view ->
        if ((view as RadioButton).isChecked) {
            index = i
            return@forEachIndexed
        }
    }
    return index
}

@BindingAdapter("onRadioButtonListener")
fun setOnRadioButtonListener(
    radioGroup: RadioGroup,
    bindingListener: InverseBindingListener?
) {
    bindingListener?.let {
        radioGroup.setOnCheckedChangeListener { _, _ ->
            it.onChange()
        }
    }
}