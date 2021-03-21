package sidev.app.course.dicoding.bab3_modul2.githubuser2.util

import android.os.Bundle
import sidev.app.course.dicoding.bab3_modul2.githubuser2.R
import sidev.app.course.dicoding.bab3_modul2.githubuser2.model.User

object Const {
    enum class UserListType(val titleRes: Int) {
        //ALL, // For all main user list. Not used
        FOLLOWER(R.string.follower),
        FOLLOWING(R.string.following),
        ;

        /**
         * Creates bundles for fragment to pass.
         */
        fun getArgs(user: User): Bundle = Bundle().apply {
            putString("owner", user.username)
            putInt("type", ordinal)
        }
    }

    const val DATA = "data"
    const val URL_USER_LIST = "https://api.github.com/users"
    const val KEY_USERNAME = "login"
    const val KEY_AVATAR = "avatar_url"
    const val KEY_NAME = "name"
    const val KEY_REPO = "public_repos"
    const val KEY_FOLLOWER = "followers"
    const val KEY_FOLLOWING = "following"
    const val KEY_COMPANY = "company"
    const val KEY_LOCATION = "location"

    fun getUserListUrl(count: Int)= "$URL_USER_LIST?per_page=$count"
    fun getUserUrl(username: String)= "$URL_USER_LIST/$username"
    fun getUserFollowerListUrl(username: String)= "${getUserUrl(username)}/followers"
    fun getUserFollowingListUrl(username: String)= "${getUserUrl(username)}/following"
}