package com.kebstudios.farmrunhelper.farm_runs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TabAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

    private val mFragments = mutableListOf<Fragment>()
    private val mFragmentTitleList = mutableListOf<String>()

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragments.add(fragment)
        mFragmentTitleList.add(title)
    }

    fun getFragment(title: String): Fragment {
        return mFragments[mFragmentTitleList.indexOf(title)]
    }

}