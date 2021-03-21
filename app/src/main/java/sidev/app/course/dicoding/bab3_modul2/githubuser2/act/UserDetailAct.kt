package sidev.app.course.dicoding.bab3_modul2.githubuser2.act

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import sidev.app.course.dicoding.bab3_modul2.githubuser2.R
import sidev.app.course.dicoding.bab3_modul2.githubuser2.adp.UserDetailVpAdp
import sidev.app.course.dicoding.bab3_modul2.githubuser2.databinding.PageDetailBinding
import sidev.app.course.dicoding.bab3_modul2.githubuser2.model.User
import sidev.app.course.dicoding.bab3_modul2.githubuser2.util.Const
import sidev.app.course.dicoding.bab3_modul2.githubuser2.util.Util
import sidev.app.course.dicoding.bab3_modul2.githubuser2.viewmodel.UserDetailViewModel
import sidev.lib.android.std.tool.util._ResUtil
import sidev.lib.android.std.tool.util._ViewUtil
import sidev.lib.android.std.tool.util.`fun`.loge

class UserDetailAct: AppCompatActivity() {
    private lateinit var data: User
    private lateinit var binding: PageDetailBinding
    private val vm: UserDetailViewModel by lazy {
        UserDetailViewModel.getInstance(this, this)
    }
    private val vpAdp: UserDetailVpAdp by lazy {
        UserDetailVpAdp(this, data)
    }
    private val vpTitles by lazy {
        Const.UserListType.values().map { it.titleRes }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= PageDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.user_detail)

        // It's obvious to use !! since `data` should not be null.
        data= intent.getParcelableExtra(Const.DATA)!!
        loge("USerDetail user= $data")

        binding.apply {
            val icPadding= _ViewUtil.dpToPx(7f, this@UserDetailAct)

            tvCompany.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_company, 0, 0, 0)
            tvLocation.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_location_, 0, 0, 0)
            tvFollower.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_follower, 0, 0, 0)
            tvRepository.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_repo, 0, 0, 0)

            tvCompany.compoundDrawablePadding = icPadding.toInt()
            tvLocation.compoundDrawablePadding = icPadding.toInt()
            tvFollower.compoundDrawablePadding = icPadding.toInt()
            tvRepository.compoundDrawablePadding = icPadding.toInt()

            val value = TypedValue()
            theme.resolveAttribute(R.attr.colorOnPrimary, value, true)
            val colorRes= value.data

            loge("colorRes= $colorRes")

            Util.setDrawableTint(tvCompany, colorRes)
            Util.setDrawableTint(tvLocation, colorRes)
            Util.setDrawableTint(tvFollower, colorRes)
            Util.setDrawableTint(tvRepository, colorRes)

            vp.adapter = vpAdp
            TabLayoutMediator(tabs, vp) { tab, pos ->
                tab.text = getString(vpTitles[pos])
            }.attach()
        }

        vm.apply {
            data.observe(this@UserDetailAct){
                if(it != null){
                    binding.apply {
                        tvUname.text= it.user.username
                        Glide.with(this@UserDetailAct)
                            .load(it.user.avatar)
                            .into(ivProfile)
                        tvName.textOrNull(it.name)
                        tvCompany.textOrNull(it.company)
                        tvLocation.textOrNull(it.location)
                        tvFollower.text= resources.getQuantityString(R.plurals.followers, it.follower, it.follower)
                        tvFollowing.text= resources.getQuantityString(R.plurals.followings, it.following, it.following)
                        tvRepository.text= resources.getQuantityString(R.plurals.repositories, it.repository, it.repository)
                    }
                    showLoading(false)
                }
            }
            onPreAsyncTask {
                showLoading()
            }
            downloadData(this@UserDetailAct.data.username)
        }
    }

    private fun showLoading(show: Boolean = true){
        binding.apply {
            if(show){
                vgContent.visibility= View.GONE
                pb.visibility= View.VISIBLE
            } else {
                vgContent.visibility= View.VISIBLE
                pb.visibility= View.GONE
            }
        }
    }

    /**
     * If [txt] is null, then `this` will be [View.GONE].
     */
    private fun TextView.textOrNull(txt: String?){
        text= txt
        visibility= if(txt != null) View.VISIBLE else View.GONE
    }
}