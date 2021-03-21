package sidev.app.course.dicoding.bab3_modul2.githubuser2.adp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import sidev.app.course.dicoding.bab3_modul2.githubuser2.R
import sidev.app.course.dicoding.bab3_modul2.githubuser2.databinding.ItemUserBinding
import sidev.app.course.dicoding.bab3_modul2.githubuser2.model.User

class UserAdp: RecyclerView.Adapter<UserAdp.UserViewHolder>() {
    var dataList: List<User>? = null
        set(v){
            field= v
            notifyDataSetChanged()
        }

    private var onItemClick: ((User) -> Unit)?= null
    fun onItemClick(f: ((User) -> Unit)?){
        onItemClick= f
    }

    inner class UserViewHolder(val binding: ItemUserBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var data: User

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(position: Int){
            // It's obvious that this `UserAdp` only calls this method if the `dataList` is not empty (and of course not null too).
            // So, using !! is safe here.
            data= dataList!![position]

            Glide.with(itemView.context)
                .load(data.avatar)
                .into(binding.ivProfile)
            binding.tvUname.text= data.username
        }
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        override fun onClick(v: View?) {
            onItemClick?.invoke(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder = UserViewHolder(
        ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = dataList?.size ?: 0
}