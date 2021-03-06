package com.hiwitech.android.shared.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.hiwitech.android.shared.ext.toStringByResId

class DefaultIntFragmentPagerAdapter(
    fm: FragmentManager,
    private val list: List<Fragment>,
    private val titles: List<Int>? = null
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = list[position]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? {
        return if (titles == null)
            super.getPageTitle(position)
        else
            titles[position].toStringByResId()
    }

}

class DefaultIntFragmentStatePagerAdapter(
    fm: FragmentManager,
    private val list: List<Fragment>,
    private val titles: List<Int>? = null
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = list[position]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? {
        return if (titles == null)
            super.getPageTitle(position)
        else
            titles[position].toStringByResId()
    }

}

class DefaultStringFragmentPagerAdapter(
    fm: FragmentManager,
    private val list: List<Fragment>,
    private val titles: List<String>? = null
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = list[position]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? {
        return if (titles == null)
            super.getPageTitle(position)
        else
            titles[position]
    }

}

class DefaultStringFragmentStatePagerAdapter(
    fm: FragmentManager,
    private val list: List<Fragment>,
    private val titles: List<String>? = null
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = list[position]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? {
        return if (titles == null)
            super.getPageTitle(position)
        else
            titles[position]
    }

}