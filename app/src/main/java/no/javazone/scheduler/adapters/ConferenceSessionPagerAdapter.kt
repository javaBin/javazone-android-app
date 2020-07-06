package no.javazone.scheduler.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ConferenceSessionPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragmentsCreator: Map<Int, () -> Fragment> = mapOf()

    override fun getItemCount(): Int = fragmentsCreator.size

    override fun createFragment(position: Int): Fragment {
        return fragmentsCreator[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}