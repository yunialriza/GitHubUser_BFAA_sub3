package com.dicoding.picodiploma.githubuser_submission3.view

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.githubuser_submission3.adapter.FollowerAdapter
import com.dicoding.picodiploma.githubuser_submission3.databinding.FragmentFollowersBinding
import com.dicoding.picodiploma.githubuser_submission3.model.FavoriteUser
import com.dicoding.picodiploma.githubuser_submission3.model.User
import com.dicoding.picodiploma.githubuser_submission3.viewModel.FollowerViewModel

class FollowersFragment : Fragment() {
    private lateinit var adapterFollower: FollowerAdapter
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var followerViewModel: FollowerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterFollower= FollowerAdapter()
        adapterFollower.notifyDataSetChanged()

        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowers.adapter = adapterFollower

        showLoading(true)
        showFollowerViewModel()
        configHomeViewModel(adapterFollower)
    }

    private fun configHomeViewModel(adapterFollower: FollowerAdapter) {
        followerViewModel.getListFollower().observe(viewLifecycleOwner, {
            userItem -> if (userItem != null){
                adapterFollower.setDataFollower(userItem)
                showLoading(false)
        }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarFollowers.visibility = View.VISIBLE
            binding.tvProgressBarFollowers.visibility = View.VISIBLE
        } else {
            binding.progressBarFollowers.visibility = View.GONE
            binding.tvProgressBarFollowers.visibility = View.GONE
        }
    }

    private fun showFollowerViewModel() {
        val checkFavoriteFrom = activity?.intent?.getBooleanExtra(DetailActivity.EXTRA_FROM_FAVORITE, false)
        if (checkFavoriteFrom == true) {
            val userFollower = activity?.intent?.getParcelableExtra<Parcelable>(DetailActivity.EXTRA_DETAIL) as FavoriteUser
            followerViewModel = FollowerViewModel()
            followerViewModel.setDataFollower(userFollower.username.toString(), requireActivity().applicationContext)
            showLoading(true)
        }
        else {
            val userFollower = activity?.intent?.getParcelableExtra<Parcelable>(DetailActivity.EXTRA_DETAIL) as User
            followerViewModel = FollowerViewModel()
            followerViewModel.setDataFollower(userFollower.username.toString(), requireActivity().applicationContext)
            showLoading(true)
        }
    }
}