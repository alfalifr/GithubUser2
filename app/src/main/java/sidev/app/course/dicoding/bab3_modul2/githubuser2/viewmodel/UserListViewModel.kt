package sidev.app.course.dicoding.bab3_modul2.githubuser2.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.android.volley.VolleyError
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import org.jetbrains.anko.runOnUiThread
import org.json.JSONArray
import org.json.JSONObject
import sidev.app.course.dicoding.bab3_modul2.githubuser2.model.User
import sidev.app.course.dicoding.bab3_modul2.githubuser2.model.UserDetail
import sidev.app.course.dicoding.bab3_modul2.githubuser2.util.Const
import sidev.app.course.dicoding.bab3_modul2.githubuser2.util.Util
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.std.tool.util.`fun`.loge
import java.lang.IllegalStateException

class UserListViewModel(private val ctx: Context): ViewModel() {
    companion object {
        fun getInstance(owner: ViewModelStoreOwner, ctx: Context): UserListViewModel = ViewModelProvider(
            owner,
            object: ViewModelProvider.Factory {
                @Suppress(SuppressLiteral.UNCHECKED_CAST)
                override fun <T : ViewModel?> create(modelClass: Class<T>): T = UserListViewModel(ctx) as T
            }
        ).get(UserListViewModel::class.java)
    }

    private val _dataList = MutableLiveData<MutableList<User>>()
    val dataList: LiveData<out List<User>>
        get()= _dataList
    private var runningJob: Job?= null

    /**
     * Executed before any async task in `this` runs.
     */
    private var onPreAsyncTask: (() -> Unit)?= null
    fun onPreAsyncTask(f: (() -> Unit)?){
        onPreAsyncTask= f
    }
    private fun doOnPreAsyncTask(){
        onPreAsyncTask?.also { ctx.runOnUiThread { it() } }
    }

    private fun cancelJob(){
        runningJob?.also {
            it.cancel()
            _dataList.postValue(_dataList.value)
            runningJob= null
        }
    }

    private fun downloadUserList(url: String){
        cancelJob()
        doOnPreAsyncTask()
        runningJob = Util.httpGet(ctx, url, ::checkNetworkException) { _, content ->
            val list= mutableListOf<User>()
            //loge("VM url= $url content= '$content'")
            //loge("content.substring(8500 - 10, 8500 + 100)= ${content.substring(8500 - 10, 8500 + 100)}")
            val arr= JSONArray(content.trim())
            for(i in 0 until arr.length()){
                val obj= arr.getJSONObject(i)
                list += User(
                    obj.getString(Const.KEY_USERNAME),
                    obj.getString(Const.KEY_AVATAR),
                )
            }
            _dataList.postValue(list)
        }
    }

    fun downloadInitDataList() = downloadUserList(Const.getUserListUrl(20))
    fun getUserFollower(uname: String) = downloadUserList(Const.getUserFollowerListUrl(uname))
    fun getUserFollowing(uname: String) = downloadUserList(Const.getUserFollowingListUrl(uname))

    fun searchUser(uname: String){
        cancelJob()
        doOnPreAsyncTask()
        runningJob = Util.httpGet(ctx, Const.getUserUrl(uname), ::checkNetworkException) { _, content ->
            JSONObject(content).apply {
                val user= User(
                    getString(Const.KEY_USERNAME),
                    getString(Const.KEY_AVATAR),
                )
                _dataList.postValue(mutableListOf(user))
            }
        }
    }

    private fun checkNetworkException(code: Int, e: VolleyError){
        if(code == 404){
            _dataList.postValue(mutableListOf())
        } else {
            loge("NETWORK ERROR!!!", e)
            throw IllegalStateException("Something wrong happened is connection", e)
        }
    }
}