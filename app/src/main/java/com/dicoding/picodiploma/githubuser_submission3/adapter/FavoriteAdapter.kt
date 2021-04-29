@file:Suppress("DEPRECATION")

package com.dicoding.picodiploma.githubuser_submission3.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.githubuser_submission3.CustomOnItemClickListener
import com.dicoding.picodiploma.githubuser_submission3.R
import com.dicoding.picodiploma.githubuser_submission3.databinding.ItemUserBinding
import com.dicoding.picodiploma.githubuser_submission3.model.FavoriteUser
import com.dicoding.picodiploma.githubuser_submission3.view.DetailActivity

class FavoriteAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listFavorite = ArrayList<FavoriteUser>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    fun addItem(favoriteUser: FavoriteUser) {
        this.listFavorite.add(favoriteUser)
        notifyItemInserted(this.listFavorite.size - 1)
    }

    fun removeItem(position: Int) {
        this.listFavorite.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavorite.size)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteAdapter.FavoriteViewHolder {
        val mViewFav =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return FavoriteViewHolder(mViewFav)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = this.listFavorite.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserBinding.bind(itemView)
        fun bind(favoriteUser: FavoriteUser) {
            Glide.with(itemView.context)
                .load(favoriteUser.avatar)
                .apply(RequestOptions().override(100, 100))
                .into(binding.itemAvatar)

            binding.tvItemName.text = favoriteUser.user

            binding.tvItemUsername.text = favoriteUser.username
            if (favoriteUser.company?.isEmpty() == true || favoriteUser.company.equals("null")) {
                binding.tvItemCompany.visibility = View.GONE
            } else {
                binding.tvItemCompany.text = favoriteUser.company
            }
            binding.cvUser.setOnClickListener(
                CustomOnItemClickListener(adapterPosition,
                    object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(view: View, position: Int) {
                            val intent = Intent(activity, DetailActivity::class.java)
                            intent.putExtra(DetailActivity.EXTRA_POSITION, position)
                            intent.putExtra(DetailActivity.EXTRA_DETAIL, favoriteUser)
                            intent.putExtra(DetailActivity.EXTRA_FROM_FAVORITE, true)

//                        activity.startActivityForResult(intent, DetailActivity.REQUEST_FAV_DELETE)
                            activity.startActivity(intent)

                        }
                    })
            )
        }
    }

}