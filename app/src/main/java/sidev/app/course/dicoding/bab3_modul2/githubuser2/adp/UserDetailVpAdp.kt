package sidev.app.course.dicoding.bab3_modul2.githubuser2.adp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import sidev.app.course.dicoding.bab3_modul2.githubuser2.frag.UserListFrag
import sidev.app.course.dicoding.bab3_modul2.githubuser2.model.User
import sidev.app.course.dicoding.bab3_modul2.githubuser2.util.Const

class UserDetailVpAdp(act: FragmentActivity, val user: User): FragmentStateAdapter(act) {
    private val fragClassList = listOf(
        UserListFrag::class.java,
        UserListFrag::class.java,
    )
    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = fragClassList.size

    /**
     * Provide a new Fragment associated with the specified position.
     *
     *
     * The adapter will be responsible for the Fragment lifecycle:
     *
     *  * The Fragment will be used to display an item.
     *  * The Fragment will be destroyed when it gets too far from the viewport, and its state
     * will be saved. When the item is close to the viewport again, a new Fragment will be
     * requested, and a previously saved state will be used to initialize it.
     *
     * @see ViewPager2.setOffscreenPageLimit
     */
    override fun createFragment(position: Int): Fragment = fragClassList[position].newInstance().apply {
        arguments= Const.UserListType.values()[position].getArgs(user)
    }
}