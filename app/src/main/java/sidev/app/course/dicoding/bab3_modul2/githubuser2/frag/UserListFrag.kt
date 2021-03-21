package sidev.app.course.dicoding.bab3_modul2.githubuser2.frag

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sidev.app.course.dicoding.bab3_modul2.githubuser2.R
import sidev.app.course.dicoding.bab3_modul2.githubuser2.act.UserDetailAct
import sidev.app.course.dicoding.bab3_modul2.githubuser2.adp.UserAdp
import sidev.app.course.dicoding.bab3_modul2.githubuser2.databinding.PageListBinding
import sidev.app.course.dicoding.bab3_modul2.githubuser2.util.Const
import sidev.app.course.dicoding.bab3_modul2.githubuser2.viewmodel.UserListViewModel
import sidev.lib.android.std.tool.util.`fun`.startAct
import sidev.lib.exception.IllegalArgExc

class UserListFrag: Fragment(), TextWatcher {
    private var args: UserListFragArgs?= null
    private lateinit var binding: PageListBinding
    private lateinit var adp: UserAdp
    private val vm: UserListViewModel by lazy {
        UserListViewModel.getInstance(this, requireContext(), args?.owner == null)
    }
    private var runningSearchJob: Job?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = arguments?.let { UserListFragArgs.fromBundle(it) }
        adp= UserAdp().apply {
            onItemClick {
                startAct<UserDetailAct>(Const.DATA to it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PageListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rv.apply {
                adapter= adp
                layoutManager= LinearLayoutManager(requireContext())
            }
            inputLayout.hint = getString(R.string.search_uname)
        }

        vm.dataList.observe(this) {
            adp.dataList= it
            showNoData(adp.itemCount == 0)
            showLoading(false)
        }
        vm.onPreAsyncTask {
            showNoData(false)
            showLoading()
        }

        args?.apply {
            owner?.also {
                when(type){
                    Const.UserListType.FOLLOWER.ordinal -> vm.getUserFollower(it)
                    Const.UserListType.FOLLOWING.ordinal -> vm.getUserFollowing(it)
                    else -> throw IllegalArgExc(
                        paramExcepted = arrayOf("getArguments().getType()"),
                        detailMsg = "`getArguments().getType()` ($type) has value outside range of Const.UserListType"
                    )
                }
                binding.rv.isNestedScrollingEnabled = false
            }
        }
    }

    private fun showNoData(show: Boolean = true){
        binding.apply {
            if(show){
                rv.visibility = View.GONE
                tvNoData.visibility = View.VISIBLE
            } else {
                rv.visibility = View.VISIBLE
                tvNoData.visibility = View.GONE
            }
        }
    }

    private fun showLoading(show: Boolean = true){
        binding.apply {
            if(show){
                rv.visibility = View.GONE
                pb.visibility = View.VISIBLE
            } else {
                rv.visibility = View.VISIBLE
                pb.visibility = View.GONE
            }
        }
    }

    private fun showSearchBar(show: Boolean = true){
        binding.apply {
            if(show){
                inputLayout.visibility = View.VISIBLE
                etUname.addTextChangedListener(this@UserListFrag)
            } else {
                inputLayout.visibility = View.GONE
                etUname.removeTextChangedListener(this@UserListFrag)
            }
        }
    }

    /**
     * Called when all saved state has been restored into the view hierarchy
     * of the fragment.  This can be used to do initialization based on saved
     * state that you are letting the view hierarchy track itself, such as
     * whether check box widgets are currently checked.  This is called
     * after [.onActivityCreated] and before
     * [.onStart].
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        showNoData(false)
        showSearchBar(args?.owner == null)
    }

    /*
    =================
    TextWatcher
    =================
     */

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Do nothing
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Do nothing
    }

    override fun afterTextChanged(s: Editable?) {
        cancelSearchJob()
        runningSearchJob = GlobalScope.launch {
            delay(700)
            s?.toString()?.also {
                if(it.isNotBlank())
                    vm.searchUser(it)
                else
                    vm.downloadInitDataList()
            }
            runningSearchJob= null
        }
    }

    private fun cancelSearchJob(){
        runningSearchJob?.also {
            it.cancel()
            runningSearchJob= null
        }
    }
}