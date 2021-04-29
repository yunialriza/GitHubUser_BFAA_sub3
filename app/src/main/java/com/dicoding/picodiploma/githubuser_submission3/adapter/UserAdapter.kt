package com.dicoding.picodiploma.githubuser_submission3.adapter


import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.githubuser_submission3.R
import com.dicoding.picodiploma.githubuser_submission3.databinding.ItemUserBinding
import com.dicoding.picodiploma.githubuser_submission3.model.User
import com.dicoding.picodiploma.githubuser_submission3.view.DetailActivity
import com.dicoding.picodiploma.githubuser_submission3.view.DetailActivity.Companion.REQUEST_FAV_ADD


class UserAdapter : RecyclerView.Adapter<UserAdapter.DataViewHolder>(){
    private val listDataUser = ArrayList<User>()

    fun setData(items: ArrayList<User>){
        listDataUser.clear()
        listDataUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val mView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return DataViewHolder(mView)
    }

//    mendistribusikan viewHolder dengan data

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(listDataUser[position])
        val data = listDataUser[position]
        holder.itemView.setOnClickListener {
            val userIntent = User(
                data.user,
                data.username,
                data.avatar,
                data.company,
                data.location,
                data.repository,
                data.followers,
                data.following
            )
            val mDetail = Intent(it.context, DetailActivity::class.java)
            mDetail.putExtra(DetailActivity.EXTRA_DETAIL, userIntent)
            mDetail.putExtra(DetailActivity.EXTRA_FROM_FAVORITE, false)
//            it.context.startActivity(mDetail)
            (it.context as Activity).startActivityForResult(mDetail, REQUEST_FAV_ADD)
        }
        }

//    ukuran set Data
    override fun getItemCount(): Int = listDataUser.size

    inner class DataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserBinding.bind(itemView)
        fun bind(user: User) {
            Glide.with(itemView.context)
                .load(user.avatar)
                .apply(RequestOptions().override(100,100))
                .into(binding.itemAvatar)

            if (user.user?.isEmpty() == true || user.user?.equals("null") == true) {
                binding.tvItemName.visibility = View.INVISIBLE
            } else {
            binding.tvItemName.text = user.user
            }
            
            binding.tvItemUsername.text = user.username
            if (user.company?.isEmpty() == true || user.company?.equals("null") == true){
                binding.tvItemCompany.visibility = View.INVISIBLE
            } else {
            binding.tvItemCompany.text = user.company
            }
        }
    }

}


