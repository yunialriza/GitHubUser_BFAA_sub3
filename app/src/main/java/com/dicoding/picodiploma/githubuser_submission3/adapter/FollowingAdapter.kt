package com.dicoding.picodiploma.githubuser_submission3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.githubuser_submission3.R
import com.dicoding.picodiploma.githubuser_submission3.databinding.ItemFollowingBinding
import com.dicoding.picodiploma.githubuser_submission3.model.Following

class FollowingAdapter: RecyclerView.Adapter<FollowingAdapter.RecyclerViewHolder>() {
    inner class RecyclerViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemFollowingBinding.bind(itemView)
        fun bind(following: Following) {
            binding.tvItemId.text = following.id
            binding.tvItemType.text = following.type

            Glide.with(itemView.context)
                .load(following.avatar)
                .apply(RequestOptions().override(100,100))
                .into(binding.ivItemAvatarFollower)
            binding.tvItemUsernameFollower.text = following.username
        }
    }

    private val listDataFollowing = ArrayList<Following>()

    fun setDataFollower(items: ArrayList<Following>){
        listDataFollowing.clear()
        listDataFollowing.addAll(items)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewHolder {
        val mView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_following, parent, false)
        return RecyclerViewHolder(mView)
    }

    override fun onBindViewHolder(holder:RecyclerViewHolder, position: Int) {
        holder.bind(listDataFollowing[position])
    }

    override fun getItemCount(): Int = listDataFollowing.size
}