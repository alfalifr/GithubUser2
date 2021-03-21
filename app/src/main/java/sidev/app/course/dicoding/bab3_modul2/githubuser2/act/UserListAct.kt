package sidev.app.course.dicoding.bab3_modul2.githubuser2.act

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import sidev.app.course.dicoding.bab3_modul2.githubuser2.R
import sidev.app.course.dicoding.bab3_modul2.githubuser2.frag.UserListFrag
import sidev.lib.android.std.tool.util.`fun`.commitFrag

class UserListAct: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_single_frag)
        commitFrag(UserListFrag(), fragContainerId = R.id.fragment, forceReplace = false)
        setTitle(R.string.user_list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_page, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.set_language -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        return super.onOptionsItemSelected(item)
    }
}