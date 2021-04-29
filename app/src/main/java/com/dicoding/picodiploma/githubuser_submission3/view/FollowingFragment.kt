package com.dicoding.picodiploma.githubuser_submission3.view

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.githubuser_submission3.adapter.FollowingAdapter
import com.dicoding.picodiploma.githubuser_submission3.databinding.FragmentFollowingBinding
import com.dicoding.picodiploma.githubuser_submission3.model.FavoriteUser
import com.dicoding.picodiploma.githubuser_submission3.model.User
import com.dicoding.picodiploma.githubuser_submission3.viewModel.FollowingViewModel


class FollowingFragment : Fragment() {

    private lateinit var adapterFollowing: FollowingAdapter
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterFollowing = FollowingAdapter()
        adapterFollowing.notifyDataSetChanged()

        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.adapter = adapterFollowing

        showLoading(true)
        showFollowingViewModel()
        configModel(adapterFollowing)
    }

    private fun configModel(adapterFollowing: FollowingAdapter) {
        followingViewModel.getListFollowing().observe(viewLifecycleOwner, { userItemFollowing ->
            if (userItemFollowing != null) {
                adapterFollowing.setDataFollower(userItemFollowing)
                showLoading(false)
            }
        })
    }

    private fun showFollowingViewModel() {
        val checkFavoriteFrom =
            activity?.intent?.getBooleanExtra(DetailActivity.EXTRA_FROM_FAVORITE, false)
        if (checkFavoriteFrom == true) {
            val userFollower =
                activity?.intent?.getParcelableExtra<Parcelable>(DetailActivity.EXTRA_DETAIL) as FavoriteUser
            followingViewModel = FollowingViewModel()
            followingViewModel.setDataFollowing(
                userFollower.username.toString(),
                requireActivity().applicationContext
            )
            showLoading(true)
        } else {
            val userFollower =
                activity?.intent?.getParcelableExtra<Parcelable>(DetailActivity.EXTRA_DETAIL) as User
            followingViewModel = FollowingViewModel()
            followingViewModel.setDataFollowing(
                userFollower.username.toString(),
                requireActivity().applicationContext
            )
            showLoading(true)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarFollowing.visibility = View.VISIBLE
            binding.tvProgressBarFollowing.visibility = View.VISIBLE
        } else {
            binding.progressBarFollowing.visibility = View.GONE
            binding.tvProgressBarFollowing.visibility = View.GONE
        }
    }
}

