package sidev.app.course.dicoding.bab3_modul2.githubuser2.adp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import sidev.app.course.dicoding.bab3_modul2.githubuser2.frag.UserListFrag
import sidev.app.course.dicoding.bab3_modul2.githubuser2.model.User
import sidev.app.course.dicoding.bab3_modul2.githubuser2.util.Const

class UserDetailVpAdp(act: FragmentActivity, private val user: User): FragmentStateAdapter(act) {
    private val fragClassList = listOf(
        UserListFrag::class.java,
        UserListFrag::class.java,
    )

    override fun getItemCount(): Int = fragClassList.size

    override fun createFragment(position: Int): Fragment = fragClassList[position].newInstance().apply {
        arguments= Const.UserListType.values()[position].getArgs(user)
    }
}