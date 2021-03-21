package sidev.app.course.dicoding.bab3_modul2.githubuser2.viewmodel

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import org.jetbrains.anko.runOnUiThread
import org.json.JSONObject
import sidev.app.course.dicoding.bab3_modul2.githubuser2.model.User
import sidev.app.course.dicoding.bab3_modul2.githubuser2.model.UserDetail
import sidev.app.course.dicoding.bab3_modul2.githubuser2.util.Const
import sidev.app.course.dicoding.bab3_modul2.githubuser2.util.Util
import sidev.app.course.dicoding.bab3_modul2.githubuser2.util.Util.Json.getStringOrNull
import sidev.lib.`val`.SuppressLiteral

class UserDetailViewModel(private val ctx: Context): ViewModel() {
    companion object {
        fun getInstance(owner: ViewModelStoreOwner, ctx: Context): UserDetailViewModel = ViewModelProvider(
            owner,
            object: ViewModelProvider.Factory {
                @Suppress(SuppressLiteral.UNCHECKED_CAST)
                override fun <T : ViewModel?> create(modelClass: Class<T>): T = UserDetailViewModel(ctx) as T
            }
        ).get(UserDetailViewModel::class.java)
    }

    private val _data = MutableLiveData<UserDetail>()
    val data: LiveData<UserDetail>
        get()= _data
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
            _data.postValue(_data.value)
            runningJob= null
        }
    }

    fun downloadData(uname: String){
        cancelJob()
        doOnPreAsyncTask()
        runningJob = Util.httpGet(ctx, Const.getUserUrl(uname)) { _, content ->
            JSONObject(content).apply {
                val user = UserDetail(
                    User(
                        getString(Const.KEY_USERNAME),
                        getString(Const.KEY_AVATAR),
                    ),
                    getStringOrNull(Const.KEY_NAME),
                    getStringOrNull(Const.KEY_COMPANY),
                    getStringOrNull(Const.KEY_LOCATION),
                    getInt(Const.KEY_REPO),
                    getInt(Const.KEY_FOLLOWER),
                    getInt(Const.KEY_FOLLOWING),
                )
                _data.postValue(user)
            }
        }
    }
}