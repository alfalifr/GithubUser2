package sidev.app.course.dicoding.bab3_modul2.githubuser2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data about user that shown in RecyclerView
 */
@Parcelize
data class User(
    val username: String,
    val avatar: String, //remote url
): Parcelable