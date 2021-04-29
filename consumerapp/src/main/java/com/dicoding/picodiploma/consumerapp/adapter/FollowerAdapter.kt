package com.dicoding.picodiploma.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.consumerapp.R
import com.dicoding.picodiploma.consumerapp.databinding.ItemFollowersBinding
import com.dicoding.picodiploma.consumerapp.model.Follower

class FollowerAdapter: RecyclerView.Adapter<FollowerAdapter.RecyclerViewHolder>(){
    private val listData = ArrayList<Follower>()

    fun setDataFollower(items: ArrayList<Follower>){
        listData.clear()
        listData.addAll(items)
        notifyDataSetChanged()
    }
    inner class RecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemFollowersBinding.bind(itemView)
        fun bind(follower: Follower){
            binding.tvItemId.text = follower.id
            binding.tvItemType.text = follower.type

            Glide.with(itemView.context)
                .load(follower.avatar)
                .apply(RequestOptions().override(100,100))
                .into(binding.ivItemAvatarFollower)
            binding.tvItemUsernameFollower.text = follower.username
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewHolder {
        val mView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_followers, parent, false)
        return RecyclerViewHolder(mView)
    }

    override fun onBindViewHolder(holder:RecyclerViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

}